package com.vijay.springbootlearning.runner;

import com.vijay.springbootlearning.entity.Product;
import com.vijay.springbootlearning.entity.ProductStatus;
import com.vijay.springbootlearning.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {
    private final ProductRepository productRepository;
    public StartupRunner(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Override
    public void run(String... args) {
        Product product = new Product();

        product.setName("Laptop");
        product.setPrice(55000);
        product.setStock(10);
        product.setStatus(ProductStatus.ACTIVE);

        productRepository.save(product);



        System.out.println(
                "CommandLineRunner executed successfully! Vijay"
        );
    }
}
