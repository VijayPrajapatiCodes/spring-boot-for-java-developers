package com.vijay.springbootlearning.repository;

import com.vijay.springbootlearning.entity.Products;
import org.springframework.data.repository.CrudRepository;

public interface ProductsRepository extends CrudRepository<Products, Long> {
}
