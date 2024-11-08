package com.inn.restaurant.dao;

import com.inn.restaurant.POJO.User;
import com.inn.restaurant.wrapper.UserWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Integer> {

    User findByEmailId(@Param("email")String email);

    List<UserWrapper> getAllUsers();

    List<String> getAllAdmin();

    @Transactional
    @Modifying // bu iki anotasyonu kullanarak bu metodu kullandığımızda try catch kullanabileceğiz aksi halde kullanamıyoruz.
    Integer updateStatus(@Param("status") String status, @Param("id")Integer id);
}
