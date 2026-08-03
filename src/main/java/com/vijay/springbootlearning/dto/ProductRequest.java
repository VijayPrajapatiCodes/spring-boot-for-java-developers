package com.vijay.springbootlearning.dto;

public class ProductRequest {
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

    private Long id;
    private String name;
    private double price;
    private String category;
    private int stock;
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
