package com.inn.restaurant.serviceImpl;

import com.google.common.base.Strings;
import com.inn.restaurant.JWT.CustomerUserDetailsService;
import com.inn.restaurant.JWT.JwtFilter;
import com.inn.restaurant.JWT.JwtUtil;
import com.inn.restaurant.POJO.User;
import com.inn.restaurant.constants.RestaurantConstants;
import com.inn.restaurant.dao.UserDao;
import com.inn.restaurant.service.UserService;
import com.inn.restaurant.utils.EmailUtils;
import com.inn.restaurant.utils.RestaurantUtils;
import com.inn.restaurant.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    EmailUtils emailUtils;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> RequestMap) {
        log.info("Inside signup {}", RequestMap);
        try {
            if (validateSignUpMap(RequestMap)) {
                User user = userDao.findByEmailId(RequestMap.get("email"));

                if (Objects.isNull(user)) { // User objesinin dolu olma durmunu kontrol ediyoruz. eğer boşsa if koşulu sağlanarak işleme devam ediliyor.
                    userDao.save(getUserFromMap(RequestMap)); // mapleme işlemini yaptığımız method u çağırdık.
                    return RestaurantUtils.getResponseEntity("Saved succesfully", HttpStatus.OK);
                } else {
                    return RestaurantUtils.getResponseEntity("Email already exist", HttpStatus.BAD_REQUEST);
                }

            } else {
                return RestaurantUtils.getResponseEntity(RestaurantConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return RestaurantUtils.getResponseEntity(RestaurantConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) { // zorunlu alanların frontend tarafından gelip gelmediğini kontrol eden method.
        if(requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password")){
            return true;
        }
        return false;
    }

    private User getUserFromMap(Map<String, String> requestMap) { // mapleme işlemini yaptığımız ayrı bir method yazıldı.
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

    //Authentication (AuthN): Bir kullanıcının söylediği kişi olup olmadığını doğrulamakla ilgilidir.
    // Authorization (AuthZ): Yetki, ayrıcalık ve bir kullanıcının kimliğini doğruladıktan sonra hangi kaynaklara erişmesine izin verildiğini doğrulamakla ilgilidir.
    @Override
    public ResponseEntity<String> login(Map<String, String> RequestMap) {
        log.info("Inside login");
        try{
            Authentication authentication = authenticationManager.authenticate( // Burda ön yüzden gelen bilgilerle authentication doğrulaması yapıyoruz.
                    new UsernamePasswordAuthenticationToken(RequestMap.get("email"), RequestMap.get("password"))
            );
            if(authentication.isAuthenticated()){ //Bu bölüm, kullanıcının kimlik doğrulamasını yaptıktan sonra hesabının aktif olup olmadığını kontrol eder ve eğer aktifse, kullanıcıya bir JWT token döndürür. Bu token, kullanıcıya güvenli bir oturum sunar ve istemci tarafında kimlik doğrulama amacıyla kullanılabilir.
                if(customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){ //equalsIgnoreCase metodu iki metni, harf büyüklüğüne (case sensitivity) bakmaksızın karşılaştırır.
                    return new ResponseEntity<String>("{\"token\":\""+
                            jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
                                    customerUserDetailsService.getUserDetail().getRole()) + "\"}",
                            HttpStatus.OK);
                }
                else{ // Bu satır ise gönderilen isteğin kimlik doğrulamasından geçemediği durumdaki yaşanacak senaryodur.
                    return new ResponseEntity<String>("{\"message\":\""+"Wait for admin approval."+"\"}", HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception e) {
            log.error("{}",e);
        }
        return new ResponseEntity<String>("{\"message\":\""+"Bad Credentials."+"\"}", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        try{
            if(jwtFilter.isAdmin()){ // Kaydın role ü admin mi değil mi kontrolü yapıyoruz.
                return new ResponseEntity<>(userDao.getAllUsers(), HttpStatus.OK);
            }else{ // Role ü admin değilse yetkili değilsin hatası patlatıyoruz.
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateUser(Map<String, String> requestMap) {
            try{
                if(jwtFilter.isAdmin()){ // kaydın role ünü kontrol ediyoruz.
                    Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
                    if(!optional.isEmpty()){
                        userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                        sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(),userDao.getAllAdmin());
                        return RestaurantUtils.getResponseEntity("Updated Succesfully", HttpStatus.OK);
                    }else{
                        return RestaurantUtils.getResponseEntity("User id does not exist", HttpStatus.OK );
                    }
                }else{
                    return RestaurantUtils.getResponseEntity(RestaurantConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return RestaurantUtils.getResponseEntity(RestaurantConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser()); // listeden current user ı çıkarıyoruz çünkü aynı kişiye sürekli mail atmamak için.

        if(status != null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "ACcount Approved", "USER:- "+ user + " /n is approved ny /nADMIN:-" +jwtFilter.getCurrentUser() , allAdmin);
        }else{
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "ACcount Disabled", "USER:- "+ user + " /n is Disabled ny /nADMIN:-" +jwtFilter.getCurrentUser() , allAdmin);
        }

    }

    @Override
    public ResponseEntity<String> checkToken() {
        return RestaurantUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String,String> requestMap) {
        try{
            User user = userDao.findByEmailId(jwtFilter.getCurrentUser());
            if(user != null){
                if(user.getPassword().equals(requestMap.get("oldPassword"))){
                    user.setPassword(requestMap.get("newPassword"));
                    userDao.save(user);
                    return RestaurantUtils.getResponseEntity("Password changed succesfully", HttpStatus.OK);
                }
                return RestaurantUtils.getResponseEntity("Incorrect old password", HttpStatus.BAD_REQUEST);
            }else{
                return RestaurantUtils.getResponseEntity(RestaurantConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return RestaurantUtils.getResponseEntity(RestaurantConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try{
            User user = userDao.findByEmail(requestMap.get("email"));
            if(!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())){
             emailUtils.forgotMail(user.getEmail(),"Credentals by Cafe Management System", user.getPassword());
            }
            return RestaurantUtils.getResponseEntity("Check your email for Credentials", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return RestaurantUtils.getResponseEntity(RestaurantConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
