package com.example.shopdienthoai.api.auth;

import com.example.shopdienthoai.model.User;

public class AuthData {
    public class AuthResponse {
        private boolean success;
        private String message;
        private User User;
        private String token;

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

        public User getUser() {
            return User;
        }

        public void setUser(User user) {
            this.User = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public class ProfileResponse{
        private boolean success;
        private String message;
        private User updatedUser;

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

        public User getUpdatedUser() {
            return updatedUser;
        }

        public void setUpdatedUser(User updatedUser) {
            this.updatedUser = updatedUser;
        }
    }

    public class ForgotPassResponse{
        private boolean success;
        private String message;

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
    }
}
