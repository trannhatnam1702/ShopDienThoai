package com.example.shopdienthoai.model;

import java.util.List;

public class Payment {
    private String nonce;
    private List<Product> cart;

    public List<Product> getCart() {
        return cart;
    }

    public void setCart(List<Product> cart) {
        this.cart = cart;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

}
