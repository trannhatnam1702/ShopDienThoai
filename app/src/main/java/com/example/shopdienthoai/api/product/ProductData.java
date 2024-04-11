package com.example.shopdienthoai.api.product;

import com.example.shopdienthoai.model.Category;
import com.example.shopdienthoai.model.Product;

import java.util.List;

public class ProductData {
    public class ProductResponse{
        private boolean success;
        private String message;
        private List<Product> products;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }
    }

    public class DetailProductResponse{
        private boolean success;
        private String message;
        private Product products;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Product getProducts() {
            return products;
        }

        public void setProducts(Product products) {
            this.products = products;
        }
    }

    public class ImageResponse {
        private String data;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
    public class  ProductCategoryResponse{
        private boolean success;
        private Category category;
        private List<Product> products;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public Category getCategory() {
            return category;
        }

        public void setCategory(Category category) {
            this.category = category;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            products = products;
        }
    }

    public  class PaymentResponse {
        private  boolean ok;

        public boolean isOk() {
            return ok;
        }

        public void setOk(boolean ok) {
            this.ok = ok;
        }
    }
}
