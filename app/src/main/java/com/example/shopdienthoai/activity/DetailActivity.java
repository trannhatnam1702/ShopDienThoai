package com.example.shopdienthoai.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import android.view.Window;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    TextView txtName, txtPrice, txtDescription;
    ImageView productImage, btnBack;
    Button btnBuy, btn360;
    WebView webView;
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

    public String productId;

    private void displayProductDetails(ProductData.DetailProductResponse productResponse) {
        txtName = findViewById(R.id.txtName);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtPrice);
        productImage = findViewById(R.id.productImage);
        btn360 = findViewById(R.id.btn360);

        if (productResponse != null && productResponse.getProducts() != null) {
            Product product = productResponse.getProducts();
            txtName.setText(product.getName());
            txtDescription.setText(product.getDescription());
            txtPrice.setText("$"+product.getPrice().toString());
            productId = product.getId();
            String imageURL = "https://phoneshop-production.up.railway.app/api/v1/product/image-product/" + product.getId();
                Picasso picasso = Picasso.get();
                picasso.load(imageURL).into(productImage);
        }

        btn360.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse("https://nationally-upward-kingfish.ngrok-free.app/product/model3d/" + productId);

                // Đặt package của Chrome là mục tiêu để mở URL
                intent.setPackage("com.android.chrome");

                // Đặt đường dẫn của trang web là dữ liệu của Intent
                intent.setData(uri);

                // Kiểm tra xem có trình duyệt Chrome được cài đặt trên thiết bị hay không
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // Mở trình duyệt Chrome để hiển thị URL
                    startActivity(intent);
                } else {
                    // Nếu không tìm thấy trình duyệt Chrome, mở URL trong trình duyệt mặc định của thiết bị
                    intent.setPackage(null);
                    startActivity(intent);
                }
//                final Dialog dialog = new Dialog(DetailActivity.this);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.popup_layout);
//                System.out.println("id product 2 "+productId);
//
//                webView = dialog.findViewById(R.id.web_view);
//                webView.getSettings().setJavaScriptEnabled(true);
//                webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//
////                webView.loadUrl("https://nationally-upward-kingfish.ngrok-free.app/product/model3d/" + productId);
//
//
//                String url = "file:///android_res/raw/index.html?productId=" + productId;
//                webView.loadUrl(url);
//                webView.setWebViewClient(new WebViewClient() {
//                    @Override
//                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                        super.onReceivedError(view, errorCode, description, failingUrl);
//                        // Xử lý lỗi ở đây
//                        Log.e("WebView Error", "Error code: " + errorCode + ", Description: " + description);
//                    }
//                });
//                System.out.println(webView);
//                dialog.show();
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