package com.example.shopdienthoai.api.product;



import com.example.shopdienthoai.model.Cart;
import com.example.shopdienthoai.model.Payment;
import com.example.shopdienthoai.model.Product;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductInterface {
    @GET("api/v1/product/get-product")
    Call<ProductData.ProductResponse> getAllProduct();

    @GET("api/v1/product/get-product/{slug}")
    Call<ProductData.DetailProductResponse> getSingleProduct(@Path("slug") String productSlug);

    @GET("api/v1/product/get-product/{slug}")
    Call<Product> getOnlySingleProduct(@Path("slug") String productSlug);

    @GET("api/v1/product/image-product/{pid}")
    Call<ProductData.ImageResponse> getImageProduct(@Path("pid") String productId);

    @GET("api/v1/product/product-category/{slug}")
    Call<ProductData.ProductCategoryResponse> getProductCategory(@Path("slug") String categorySlug);

    @POST("api/v1/product/braintree/payment")
    Call<ProductData.PaymentResponse> processPayment(@Body Payment request) ;
}
