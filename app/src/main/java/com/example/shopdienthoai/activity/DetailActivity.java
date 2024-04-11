package com.example.shopdienthoai.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopdienthoai.R;
import com.example.shopdienthoai.api.product.ProductClient;
import com.example.shopdienthoai.api.product.ProductData;
import com.example.shopdienthoai.api.product.ProductInterface;
import com.example.shopdienthoai.dao.CartDAO;
import com.example.shopdienthoai.model.Cart;
import com.example.shopdienthoai.model.Product;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    TextView txtName, txtPrice, txtDescription;
    ImageView productImage, btnBack;
    Button btnBuy;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getProduct();
        turnBack();
    }

    private void addToCart(ProductData.DetailProductResponse productResponse){
        btnBuy = findViewById(R.id.btnCheckout);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = productResponse.getProducts();

                String productId = product.getId();
                String name = product.getName();
                String slug = product.getSlug();
                float price = product.getPrice();
                int quantity = 1;
                Cart cart = new Cart(id, productId, name, slug, price, quantity);
                CartDAO cartDAO = new CartDAO(DetailActivity.this);
                boolean isSuccess = cartDAO.addToCart(cart);
                if (isSuccess) {
                    // Nếu thêm thành công, hiển thị thông báo thành công
                    Toast.makeText(DetailActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                } else {
                    // Nếu thêm không thành công, hiển thị thông báo lỗi
                    Toast.makeText(DetailActivity.this, "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getProduct(){
        Intent intent = getIntent();
        String slug = intent.getStringExtra("product_slug");
        ProductClient productClient = new ProductClient(DetailActivity.this);
        ProductInterface productInterface = productClient.getProductInterface();
        Call<ProductData.DetailProductResponse> call = productInterface.getSingleProduct(slug);
        call.enqueue(new Callback<ProductData.DetailProductResponse>() {
            @Override
            public void onResponse(Call<ProductData.DetailProductResponse> call, Response<ProductData.DetailProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductData.DetailProductResponse productResponse = response.body();
                    Product productList = productResponse.getProducts();
                    System.out.println("ProductResponse"+productResponse);
                    displayProductDetails(productResponse);
                    addToCart(productResponse);
                } else {
                    // Handle errors
                    Toast.makeText(DetailActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductData.DetailProductResponse> call, Throwable t) {
                // Handle failures
                Toast.makeText(DetailActivity.this, "Request Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProductDetails(ProductData.DetailProductResponse productResponse) {
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtPrice);
        productImage = findViewById(R.id.productImage);

        if (productResponse != null && productResponse.getProducts() != null) {
            Product product = productResponse.getProducts();
            txtName.setText(product.getName());
            txtDescription.setText(product.getDescription());
            txtPrice.setText("$"+product.getPrice().toString());
            String imageURL = "https://picked-primate-poorly.ngrok-free.app/api/v1/product/image-product/" + product.getId();
                Picasso picasso = Picasso.get();
                picasso.load(imageURL).into(productImage);
        }
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