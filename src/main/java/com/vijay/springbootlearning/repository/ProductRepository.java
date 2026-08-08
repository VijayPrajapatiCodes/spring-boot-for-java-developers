package com.vijay.springbootlearning.repository;

import com.vijay.springbootlearning.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository
        extends JpaRepository<Product, Long> {
}