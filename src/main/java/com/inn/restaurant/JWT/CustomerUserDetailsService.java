package com.inn.restaurant.JWT;

import com.inn.restaurant.POJO.User;
import com.inn.restaurant.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;


@Slf4j
@Service
// UserDetailsServiceclass ını spring in kendi kütüphanesinden aldık.
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;

    // Burada kullandığımız User kendi oluşturduğumuz entity.
    private User userDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}", username);
        userDetail = userDao.findByEmailId(username);
        if(!Objects.isNull(userDetail)){
            return new org.springframework.security.core.userdetails.User(userDetail.getEmail(), userDetail.getPassword(),new ArrayList<>());
        }
        else{
            throw new UsernameNotFoundException("User not found");
        }
    }

    public User getUserDetail() {
        return userDetail;
    }

}
