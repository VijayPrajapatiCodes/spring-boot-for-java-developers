package com.vijay.springbootlearning.exception;

import com.vijay.springbootlearning.dto.ProductRequest;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message){
        super(message);
    }

}
