package com.inn.restaurant.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
//@RequiredArgsConstructor
//@AllArgsConstructor
//@NoArgsConstructor
public class ProductWrapper  {

    Integer id;

    String name;

    String description;

    Integer price;

    String status;

    Integer categoryId;

    String categoryName;

    ProductWrapper(Integer id, String name, String description, Integer price, String status, Integer categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryName = categoryName;

    }

    ProductWrapper(){

    }

    ProductWrapper(Integer id, String name){
        this.id = id;
        this.name = name;
    }


}
