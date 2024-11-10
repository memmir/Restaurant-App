package com.inn.restaurant.service;

import com.inn.restaurant.wrapper.UserWrapper;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {

    ResponseEntity<String> signUp(Map<String, String> RequestMap);

    ResponseEntity<String> login(Map<String, String> RequestMap);

    ResponseEntity<List<UserWrapper>> getAllUsers();

    ResponseEntity<String> updateUser(Map<String, String> requestMap);

    ResponseEntity<String> checkToken();

    ResponseEntity<String> changePassword(Map<String, String> requestMap);
}
