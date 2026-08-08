package com.vijay.springbootlearning.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "products_name",nullable = false,length = 100)
    private String name;
    @Column(nullable = false)
    private double price;
    @Column(length = 50)
    private String category;
    @Column(nullable = false)
    private int stock;
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

}