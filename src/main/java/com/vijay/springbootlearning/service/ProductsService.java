package com.vijay.springbootlearning.service;

import com.vijay.springbootlearning.entity.Products;
import com.vijay.springbootlearning.repository.ProductsRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class ProductsService {
    @Autowired
    private ProductsRepository productsRepository;

    public void setProductsRepository(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Cacheable("products")
    public Products getProducts(Long id) {
        System.out.println("Fetching from Database...");
        return productsRepository.findById(id).orElse(null);
    }
}
