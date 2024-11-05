package com.inn.restaurant.serviceImpl;

import com.inn.restaurant.POJO.User;
import com.inn.restaurant.constants.RestaurantConstants;
import com.inn.restaurant.dao.UserDao;
import com.inn.restaurant.service.UserService;
import com.inn.restaurant.utils.RestaurantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> RequestMap) {
        log.info("Inside signup {}", RequestMap);
        if(validateSignUpMap(RequestMap)) {
            User user = userDao.findByEmailId(RequestMap.get("email"));

            if(Objects.isNull(user)) { // User objesinin dolu olma durmunu kontrol ediyoruz. eğer boşsa if koşulu sağlanarak işleme devam ediliyor.
                userDao.save(getUserFromMap(RequestMap)); // mapleme işlemini yaptığımız method u çağırdık.
                return RestaurantUtils.getResponseEntity("Saved succesfully", HttpStatus.OK);
            }else {
                return RestaurantUtils.getResponseEntity("Email already exist", HttpStatus.BAD_REQUEST);
            }

        }
        else{
            return RestaurantUtils.getResponseEntity(RestaurantConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }

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

}
