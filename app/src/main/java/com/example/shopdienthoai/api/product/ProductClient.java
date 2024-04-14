package com.example.shopdienthoai.api.product;


import android.content.Context;

import com.example.shopdienthoai.Utils.SharedPreference;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductClient {
    private ProductInterface productInterface;
    SharedPreference sharedPreference;

    public ProductClient(Context context) {

        sharedPreference = new SharedPreference(context);
        String token = sharedPreference.getValueString("token");
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new TokenInterceptor(token)).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://phoneshop-production.up.railway.app/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        productInterface = retrofit.create(ProductInterface.class);
    }

    public ProductInterface getProductInterface() {
        return productInterface;
    }
}
