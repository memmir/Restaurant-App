package com.inn.restaurant.controllerImpl;

import com.inn.restaurant.constants.RestaurantConstants;
import com.inn.restaurant.controller.UserController;
import com.inn.restaurant.service.UserService;
import com.inn.restaurant.utils.RestaurantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserControllerImpl implements UserController {

    @Autowired
    UserService userService;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {

        try{
            return userService.signUp(requestMap);
        }catch (Exception e){
            e.printStackTrace(); // ifadesi, Java'da bir hata  oluştuğunda Exception bilgilerini ayrıntılı olarak konsola yazdırmak için kullanılır. Bu yöntem, Exception türünü, nedenini ve oluştuğu kod satırlarını gösterir, böylece hata ayıklamak daha kolay hale gelir.
        }
        return RestaurantUtils.getResponseEntity(RestaurantConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
