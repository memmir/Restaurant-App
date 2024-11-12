package com.inn.restaurant.serviceImpl;

import com.inn.restaurant.JWT.JwtFilter;
import com.inn.restaurant.POJO.Category;
import com.inn.restaurant.constants.RestaurantConstants;
import com.inn.restaurant.dao.CategoryDao;
import com.inn.restaurant.service.CategoryService;
import com.inn.restaurant.utils.RestaurantUtils;
import com.inn.restaurant.wrapper.CategoryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<List<CategoryWrapper>> getAllCategory() {
        return null;
    }

    @Override
    public ResponseEntity<String> addCategory(Map<String, String> requestMap) {
        try{

            if(jwtFilter.isAdmin()){ // işlemi yapacak kişinin admin role ünde olmasını istiyoruz.
                if(validateCategoryMap(requestMap, false)){
                    categoryDao.save(getCategoryFromMap(requestMap,false));
                    return RestaurantUtils.getResponseEntity("Category Added Succesfully", HttpStatus.OK);
                }
            }
            else{
                RestaurantUtils.getResponseEntity(RestaurantConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return RestaurantUtils.getResponseEntity(RestaurantConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Bu metodun amacı: daha önce gönderilen parametreler ile aynı isimde category olup olmadığını kontrol ediyoruyz
    // yani updateCategoryde mi kullanılacak yoksa addCategory de mi.
    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id") && validateId){
                return true;
            }else if(!validateId){
                return true;
            }
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String,String> requestMap, Boolean isAdd){
        Category category = new Category();
        if(isAdd){
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }
}
