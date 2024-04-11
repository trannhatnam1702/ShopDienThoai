package com.example.shopdienthoai.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shopdienthoai.R;
import com.example.shopdienthoai.adapter.ProductAdapter;
import com.example.shopdienthoai.adapter.SearchProductAdapter;
import com.example.shopdienthoai.api.product.ProductClient;
import com.example.shopdienthoai.api.product.ProductData;
import com.example.shopdienthoai.api.product.ProductInterface;
import com.example.shopdienthoai.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    ImageView btnBack;
    RecyclerView recyclerView;
    SearchProductAdapter searchProductAdapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProductAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProductAdapter.getFilter().filter(newText);
                return false;
            }
        });
        getProducts();
        turnBack();
    }

    private void getProducts(){
        recyclerView = findViewById(R.id.recyclerView);
        ProductClient productClient = new ProductClient(SearchActivity.this);
        ProductInterface productInterface = productClient.getProductInterface();
        Call<ProductData.ProductResponse> call = productInterface.getAllProduct();
        call.enqueue(new Callback<ProductData.ProductResponse>() {
            @Override
            public void onResponse(Call<ProductData.ProductResponse> call, Response<ProductData.ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductData.ProductResponse productResponse = response.body();
                    List<Product> productList = productResponse.getProducts();
                    searchProductAdapter = new SearchProductAdapter(SearchActivity.this, productList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                    recyclerView.setAdapter(searchProductAdapter);
                } else {
                    // Handle errors
                    Toast.makeText(SearchActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductData.ProductResponse> call, Throwable t) {
                // Handle failures
                Toast.makeText(SearchActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
            }
        });
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
}