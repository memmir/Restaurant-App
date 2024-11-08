package com.inn.restaurant.POJO;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@NamedQuery(name = "User.findByEmailId", query = "select u from User u where u.email=:email") //anotasyonu, JPA (Java Persistence API) kullanılarak tanımlanan bir sabit JPQL sorgusunu temsil eder ve sık kullanılan sorgular için tekrar yazma gereğini ortadan kaldırır.

@NamedQuery(name = "User.getAllUsers", query = "select new com.inn.restaurant.wrapper.UserWrapper(u.id,u.name,u.email,u.contactNumber,u.status) from User u where u.role='user'")

//@DynamicUpdate ve @DynamicInsert,
// Hibernate'de kullanılan ve veri güncelleme ve ekleme işlemlerinde performansı artırmaya yönelik iki anotasyondur.
@Data
@Entity
@DynamicUpdate //Sadece güncellenmiş alanları içeren SQL UPDATE sorgularını oluşturur.
@DynamicInsert //Yeni bir entity eklenirken yalnızca dolu alanları içeren SQL INSERT sorgularını oluşturur.
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L; //ifadesi, Java sınıfında serileştirme (serialization) işlemleri için kullanılan özel bir alan olan serialVersionUID'yi tanımlar.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "contactNumber")
    private String contactNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;
}
