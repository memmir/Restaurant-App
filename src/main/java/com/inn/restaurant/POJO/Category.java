package com.inn.restaurant.POJO;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;


@NamedQuery(name= "Category.getAllCategory",query = "select c from Category c")

//@DynamicUpdate ve @DynamicInsert,
// Hibernate'de kullanılan ve veri güncelleme ve ekleme işlemlerinde performansı artırmaya yönelik iki anotasyondur.
@Data
@Entity
@DynamicUpdate //Sadece güncellenmiş alanları içeren SQL UPDATE sorgularını oluşturur.
@DynamicInsert //Yeni bir entity eklenirken yalnızca dolu alanları içeren SQL INSERT sorgularını oluşturur.
@Table(name = "category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;
}
