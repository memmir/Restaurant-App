package com.inn.restaurant.service;


import com.inn.restaurant.POJO.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService {


    ResponseEntity<List<Category>> getAllCategory(String filterValue);

    ResponseEntity<String> addCategory(Map<String,String> requestMap);

    ResponseEntity<String> updateCategory(Map<String, String> requestMap);
}
