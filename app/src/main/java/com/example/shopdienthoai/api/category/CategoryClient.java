package com.example.shopdienthoai.api.category;

import com.example.shopdienthoai.api.auth.AuthInterface;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryClient {
    private CategoryInterface categoryInterface;

    public CategoryClient() {
        OkHttpClient client = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://phoneshop-production.up.railway.app/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        categoryInterface = retrofit.create(CategoryInterface.class);
    }

    public CategoryInterface getCategoryInterface() {
        return categoryInterface;
    }
}
