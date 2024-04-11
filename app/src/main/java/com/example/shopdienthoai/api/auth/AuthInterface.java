package com.example.shopdienthoai.api.auth;

import com.example.shopdienthoai.model.NewPass;
import com.example.shopdienthoai.model.Order;
import com.example.shopdienthoai.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AuthInterface {
    @POST("api/v1/auth/register")
    Call<AuthData.AuthResponse> register(@Body User registerRequest);

    @POST("api/v1/auth/login")
    Call<AuthData.AuthResponse> login(@Body User loginRequest);

    @PUT("api/v1/auth/profile")
    Call<AuthData.ProfileResponse> profile(@Body User profileRequest);

    @GET("api/v1/auth/orders")
    Call<List<Order>> orders();

    @POST("api/v1/auth/forgot-password")
    Call<AuthData.ForgotPassResponse> forgotPassword(@Body NewPass passwordRequest);
}
