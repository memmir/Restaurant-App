package com.inn.restaurant.controllerImpl;

import com.inn.restaurant.constants.RestaurantConstants;
import com.inn.restaurant.controller.CategoryController;
import com.inn.restaurant.service.CategoryService;
import com.inn.restaurant.utils.RestaurantUtils;
import com.inn.restaurant.wrapper.CategoryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryControllerImpl implements CategoryController {


    @Autowired
    CategoryService categoryService;

    @Override
    public ResponseEntity<List<CategoryWrapper>> getAllCategory() {
        try{
            return categoryService.getAllCategory();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<CategoryWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> addCategory(Map<String, String> requestMap) {
        try{
            return categoryService.addCategory(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return RestaurantUtils.getResponseEntity(RestaurantConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
