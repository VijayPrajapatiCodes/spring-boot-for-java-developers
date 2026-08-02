package com.vijay.springbootlearning.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/products")
public class ProductController {
    @GetMapping
    public String getProduct(){
        return "All Products";
    }
    @GetMapping("/featured")
    public String getFeaturedProduct(){
        return "Featured Product";
    }
    @PostMapping
    public String createProduct(){
        return "Product Created";
    }
    @PutMapping
    public String updateProduct(){
        return "Product Updated";
    }
    @DeleteMapping
    public String deleteProduct(){
        return "Product Delete";
    }
}
