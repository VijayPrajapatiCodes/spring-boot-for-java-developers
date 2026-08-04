package com.vijay.springbootlearning.Controller;

import com.vijay.springbootlearning.dto.ProductRequest;
import com.vijay.springbootlearning.dto.ProductResponse;
import com.vijay.springbootlearning.entity.Product;
import com.vijay.springbootlearning.exception.ProductNotFoundException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/products")
public class ProductController {
    private final ModelMapper modelMapper;

    public ProductController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @PostMapping("/mapper")
    public ProductResponse createProductWithModel(@RequestBody ProductRequest request){
        Product product=modelMapper.map(request,Product.class);
//        Fake id
        product.setId(101L);
       ProductResponse response =modelMapper.map(product,ProductResponse.class);
       return  response;
    }
    @GetMapping
    public String getProduct(){
        return "All Products";
    }
    @GetMapping("/featured")
    public String getFeaturedProduct(){
        return "Featured Product";
    }
    @PostMapping
    public  ProductRequest createProduct(@Valid @RequestBody ProductRequest product  ){
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
//    @GetMapping("/{id}")
//    public String getProductById(@PathVariable int id){
//        return "Product Id:"+id;
//    }
     @GetMapping("/{id}")
    public ResponseEntity<String>getProductById(@PathVariable int id){
        if(id!=10){
            throw new ProductNotFoundException(
                    "Product is not found id:"+id
            );
        }
        return  ResponseEntity.ok("Product found with id:"+id);
    }

    @PutMapping("/{id}")
    public String updateProduct(@PathVariable int id) {
        return "Updating Product ID: " + id;
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable int id) {
        return "Deleting Product ID: " + id;
    }
    @PostMapping("/with-image")
    public ResponseEntity<String> createProductWithImage(
            @Valid @RequestPart("product") ProductRequest product,
            @RequestPart("image") MultipartFile image) {

        return ResponseEntity.ok(
                "Product Name: " + product.getName()
                        + "\nPrice: " + product.getPrice()
                        + "\nCategory: " + product.getCategory()
                        + "\nStock: " + product.getStock()
                        + "\nImage Name: " + image.getOriginalFilename()
                        + "\nImage Type: " + image.getContentType()
                        + "\nImage Size: " + image.getSize()
        );
    }
}
