package com.example.shopdienthoai.api.category;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryInterface {
    @GET("api/v1/category/list-category")
    Call<CategoryData.CategoryResponse> getAllCategory();
}
