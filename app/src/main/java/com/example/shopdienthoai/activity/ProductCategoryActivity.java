package com.example.shopdienthoai.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shopdienthoai.R;
import com.example.shopdienthoai.adapter.ProductAdapter;
import com.example.shopdienthoai.api.product.ProductClient;
import com.example.shopdienthoai.api.product.ProductData;
import com.example.shopdienthoai.api.product.ProductInterface;
import com.example.shopdienthoai.model.Category;
import com.example.shopdienthoai.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductCategoryActivity extends AppCompatActivity {

    RecyclerView allProductView;
    ProductAdapter productAdapter;
    ProgressBar progressBar;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);
        allProductView = findViewById(R.id.allProductView);
        progressBar = findViewById(R.id.progressBar);

        getProducts();
        turnBack();
    }
    private void turnBack(){
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getProducts(){
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        String slug = intent.getStringExtra("category_slug");
        System.out.println(slug);
        ProductClient productClient = new ProductClient(ProductCategoryActivity.this);
        ProductInterface productInterface = productClient.getProductInterface();
        Call<ProductData.ProductCategoryResponse> call = productInterface.getProductCategory(slug);
        call.enqueue(new Callback<ProductData.ProductCategoryResponse>() {
            @Override
            public void onResponse(Call<ProductData.ProductCategoryResponse> call, Response<ProductData.ProductCategoryResponse> response) {
                if (response.isSuccessful()) {
                    ProductData.ProductCategoryResponse productResponse = response.body();
                    List<Product> productList = productResponse.getProducts();
                    if (productList != null && !productList.isEmpty()) {
                        productAdapter = new ProductAdapter(ProductCategoryActivity.this, productList);
                        allProductView.setLayoutManager(new GridLayoutManager(ProductCategoryActivity.this, 3));
                        allProductView.setAdapter(productAdapter);
                    }
                    progressBar.setVisibility(View.GONE);
                } else {
                    // Handle errors
                    Toast.makeText(ProductCategoryActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ProductData.ProductCategoryResponse> call, Throwable t) {
                // Handle failures
                Toast.makeText(ProductCategoryActivity.this, "Request Fail", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}