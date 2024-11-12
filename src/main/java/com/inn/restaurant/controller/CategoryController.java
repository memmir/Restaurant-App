package com.inn.restaurant.controller;

import com.inn.restaurant.wrapper.CategoryWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/category")
public interface CategoryController {

    @GetMapping(name = "/getAll")
    public ResponseEntity<List<CategoryWrapper>> getAllCategory();

    @PostMapping(path= "/add")
    ResponseEntity<String> addCategory(@RequestBody(required = true)Map<String,String> requestMap);
}
