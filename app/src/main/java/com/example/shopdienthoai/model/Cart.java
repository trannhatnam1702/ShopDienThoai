package com.example.shopdienthoai.model;

public class Cart {
    private int id;
    private String productId;
    private String name;
    private String slug;
    private double price;
    private int quantity;
    public Cart() {
    }
    public Cart( String productId, String name, String slug, double price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.slug = slug;
        this.price = price;
        this.quantity = quantity;
    }
    public Cart(int id, String productId, String name, String slug, double price, int quantity) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.slug = slug;
        this.price = price;
        this.quantity = quantity;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
