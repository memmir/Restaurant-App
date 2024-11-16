package com.inn.restaurant.dao;

import com.inn.restaurant.POJO.Product;
import com.inn.restaurant.wrapper.ProductWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends JpaRepository<Product,Integer> {

    List<ProductWrapper> getAllProduct();

    @Transactional
    @Modifying // bu iki anotasyonu kullanarak bu metodu kullandığımızda try catch kullanabileceğiz aksi halde kullanamıyoruz.
    Integer updateProductStatus(@Param("status") String status,@Param("id") Integer id );

    List<ProductWrapper> getProductByCategory(@Param("id") Integer id);

    ProductWrapper getProductById(@Param("id") Integer id);
}
