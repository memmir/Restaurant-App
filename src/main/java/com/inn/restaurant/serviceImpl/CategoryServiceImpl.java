package com.inn.restaurant.serviceImpl;

import com.google.common.base.Strings;
import com.inn.restaurant.JWT.JwtFilter;
import com.inn.restaurant.POJO.Category;
import com.inn.restaurant.constants.RestaurantConstants;
import com.inn.restaurant.dao.CategoryDao;
import com.inn.restaurant.service.CategoryService;
import com.inn.restaurant.utils.RestaurantUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try{
            if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")){
                log.info("Inside if");
                return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(), HttpStatus.OK);
            }
            return new ResponseEntity<>(categoryDao.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR); // Burada diğerlerinden farklı olarak yapılma nedeni RestaurantUtils classında dönüş değeri olarak String tanımlanmış. Bu metodda ise Liste dönüş değeri var.
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
                return RestaurantUtils.getResponseEntity(RestaurantConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
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


    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                 if(validateCategoryMap(requestMap,true)){ // daha önce aynı isimde bir category olup olmadığını kontrol ediyoruz. Olması lazım
                     Optional optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
                     if(!optional.isEmpty()){
                        categoryDao.save(getCategoryFromMap(requestMap,true));
                        return RestaurantUtils.getResponseEntity("Category updated succesfulty", HttpStatus.OK);
                     }else{
                         return RestaurantUtils.getResponseEntity("Category id not exist", HttpStatus.BAD_REQUEST);
                     }
                 }
                 return RestaurantUtils.getResponseEntity(RestaurantConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }else {
                return RestaurantUtils.getResponseEntity(RestaurantConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return RestaurantUtils.getResponseEntity(RestaurantConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
