package com.inn.restaurant.controller;

import com.inn.restaurant.POJO.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/category")
public interface CategoryController {

    @GetMapping(name = "/getAll")
    ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false) String filterValue);

    @PostMapping(path= "/add")
    ResponseEntity<String> addCategory(@RequestBody(required = true)Map<String,String> requestMap);

    @PostMapping(path = "/update")
    ResponseEntity<String> updateCategory(@RequestBody(required = true) Map<String,String> requestMap);
}
