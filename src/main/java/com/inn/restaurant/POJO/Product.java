package com.inn.restaurant.POJO;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@NamedQuery(name = "Product.getAllProduct", query = "select new com.inn.restaurant.wrapper.ProductWrapper(p.id, p.name, p.description, p.price, p.status, p.category.id, p.category.name ) from Product p")

@Data
@Entity
@DynamicUpdate //Sadece güncellenmiş alanları içeren SQL UPDATE sorgularını oluşturur.
@DynamicInsert //Yeni bir entity eklenirken yalnızca dolu alanları içeren SQL INSERT sorgularını oluşturur.
@Table(name = "product")
public class Product implements Serializable {

    private static final Long serialVersionUID = 123456L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_fk", nullable = false)
    private Category category;

    @Column(name = "description")
    private String description;

    @Column(name="price")
    private Integer price;

    @Column(name = "status")
    private String status;


}
