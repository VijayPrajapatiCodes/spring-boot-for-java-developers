package com.vijay.springbootlearning.Controller;

import com.vijay.springbootlearning.dto.ProductRequest;
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
    public  ProductRequest createProduct(@RequestBody ProductRequest product  ){
        return product;
//                + product.getName()
//                + ", Price: " + product.getPrice()
//                + ", Category: " + product.getCategory()
//                + ", Stock: " + product.getStock();
    }
    @PutMapping
    public String updateProduct(){
        return "Product Updated";
    }
    @DeleteMapping
    public String deleteProduct(){
        return "Product Delete";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String name) {

        if (name == null) {
            return "Showing all products";
        }

        return "Searching: " + name;
    }
    @GetMapping("/filter")
    public String filterProduct(@RequestParam String category ,@RequestParam double maxPrize){
        return "Category:"+category+",Max Price"+maxPrize;
    }
    @GetMapping("/{id}")
    public String getProductById(@PathVariable int id){
        return "Product Id:"+id;
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable int id) {
        return "Updating Product ID: " + id;
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable int id) {
        return "Deleting Product ID: " + id;
    }
}
