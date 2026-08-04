package com.vijay.springbootlearning.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductRequest {

    private Long id;
    @NotBlank(message="Product name is required")
    private String name;
    @Positive(message = "Price must be greater than 0")
    private double price;
    @NotBlank(message = "Category is required")
    private String category;
    @PositiveOrZero(message = "Stock cannot be Negative")
    private int stock;
    private ManufacturerRequest manufacturer;

    public ManufacturerRequest manufacturerRequest(){
        return manufacturer;
    }

    public ManufacturerRequest getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(ManufacturerRequest manufacturer) {
        this.manufacturer = manufacturer;
    }

    public ProductRequest(Long id) {
        this.id = id;
    }
    public ProductRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public ProductRequest(String name, double price, String category, int stock) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }



}
