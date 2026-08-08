package com.vijay.springbootlearning.Controller;

import com.vijay.springbootlearning.entity.Products;
import com.vijay.springbootlearning.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductsRepository productsRepository;
    @GetMapping("/{id}")
    public Products getProducts(@PathVariable Long id) {
        return productsRepository.findById(id).orElse(null);
    }
}

