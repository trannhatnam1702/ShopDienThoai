package com.example.shopdienthoai.api.product;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor  implements Interceptor {
    private String token;

    public TokenInterceptor(String token) {
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request authorisedRequest = originalRequest.newBuilder()
                .addHeader("Authorization", token)
                .addHeader("Content-Type", "application/json")
                .build();

        return chain.proceed(authorisedRequest);
    }
}