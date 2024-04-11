package com.example.shopdienthoai.api.category;

import com.example.shopdienthoai.model.Category;
import com.example.shopdienthoai.model.Product;

import java.util.List;

public class CategoryData {
    public class CategoryResponse {
        private boolean success;
        private String message;
        private List<Category> Category;

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

        public List<Category> getCategory() {
            return Category;
        }

        public void setCategory(List<Category> category) {
            Category = category;
        }
    }
}
