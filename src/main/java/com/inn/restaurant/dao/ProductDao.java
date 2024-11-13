package com.inn.restaurant.dao;

import com.inn.restaurant.POJO.Product;
import com.inn.restaurant.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends JpaRepository<Product,Integer> {

    List<ProductWrapper> getAllProduct();
}
