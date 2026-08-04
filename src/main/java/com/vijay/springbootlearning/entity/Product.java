package com.vijay.springbootlearning.entity;

public class Product {
    private long id;
    private String name;
    private double price;
    private String category;
    private int stock;
    public  Product(){

    }
    public void setId(long id) {
        this.id = id;
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

    public Product(long id, double price, String name, String category, int stock) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.category = category;
        this.stock = stock;
    }
}

