package com.inn.restaurant.service;

import com.inn.restaurant.wrapper.CategoryWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService {


    ResponseEntity<List<CategoryWrapper>> getAllCategory();

    ResponseEntity<String> addCategory(Map<String,String> requestMap);
}
