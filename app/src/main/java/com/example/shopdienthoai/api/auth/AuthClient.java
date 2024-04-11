package com.example.shopdienthoai.api.auth;

import android.content.Context;

import com.example.shopdienthoai.Utils.SharedPreference;
import com.example.shopdienthoai.api.product.TokenInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthClient {
    private AuthInterface authInterface;
    SharedPreference sharedPreference;
    public AuthClient(Context context) {
        sharedPreference = new SharedPreference(context);
        String token = sharedPreference.getValueString("token");
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new TokenInterceptor(token)).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://picked-primate-poorly.ngrok-free.app/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authInterface = retrofit.create(AuthInterface.class);
    }

    public AuthInterface getAuthInterface() {
        return authInterface;
    }

}
