package com.example.shopdienthoai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.DropInClient;
import com.braintreepayments.api.DropInListener;
import com.braintreepayments.api.DropInRequest;
import com.braintreepayments.api.DropInResult;
import com.example.shopdienthoai.R;
import com.example.shopdienthoai.Utils.SharedPreference;
import com.example.shopdienthoai.adapter.CartAdapter;
import com.example.shopdienthoai.api.product.ProductClient;
import com.example.shopdienthoai.api.product.ProductData;
import com.example.shopdienthoai.api.product.ProductInterface;
import com.example.shopdienthoai.dao.CartDAO;
import com.example.shopdienthoai.model.Cart;
import com.example.shopdienthoai.model.Payment;
import com.example.shopdienthoai.model.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements DropInListener {
    ImageView btnBack, arrowSetting;
    RecyclerView recyclerView;
    TextView txtIsEmpty, txtAddress;
    CartAdapter cartAdapter;
    ArrayList<Cart> listCart;
    private DropInClient dropInClient;
    private DropInRequest dropInRequest;

    private SharedPreference sharedPreference;

    String nonce;

    ArrayList<Product> productList;
    Button btnCheckout;
    String json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        txtAddress = findViewById(R.id.txtAddress);
        sharedPreference = new SharedPreference(CartActivity.this);
        String address = sharedPreference.getValueString("address");
        txtAddress.setText(address);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CartDAO cartDAO = new CartDAO(this);
        listCart = cartDAO.getCartItems();
        updateTotalPrice();
        cartAdapter = new CartAdapter(this, listCart, new CartAdapter.OnQuantityChangeListener(){
            @Override
            public void onQuantityChange(int position, int newQuantity) {
                // Cập nhật giá trị cho txtSumProductPrice
                // Tính tổng giá trị ở đây và cập nhật giá trị cho txtSumProductPrice
                updateTotalPrice();
                cartAdapter.notifyDataSetChanged();
            }
        });
        cartAdapter.notifyDataSetChanged();
        if(listCart.isEmpty()){
            txtIsEmpty = findViewById(R.id.txtIsEmpty);
            txtIsEmpty.setVisibility(View.VISIBLE);
            cartAdapter.notifyDataSetChanged();
        } else {
            recyclerView.setAdapter(cartAdapter);
            cartAdapter.notifyDataSetChanged();
        }

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (position >= 0 && position < listCart.size()) {
                    Cart cartRemove = listCart.get(position);
                    int id = cartRemove.getId();
                    listCart.remove(position);
                    cartDAO.removeFromCart(id);
                    cartAdapter.notifyDataSetChanged();
                    if(listCart.isEmpty()) {
                        txtIsEmpty = findViewById(R.id.txtIsEmpty);
                        txtIsEmpty.setVisibility(View.VISIBLE);
                        cartAdapter.notifyDataSetChanged();
                    }
                    updateTotalPrice();
                } else {
                    // Xử lý trường hợp vị trí không hợp lệ (ví dụ: hiển thị thông báo lỗi)
                }

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(CartActivity.this, R.color.g_red))
                        .addSwipeLeftLabel("Xoá")
                        .setSwipeLeftLabelColor(ContextCompat.getColor(CartActivity.this, R.color.colorWhite))
                        .setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 20)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        dropInRequest = new DropInRequest();
        dropInClient = new DropInClient(CartActivity.this, dropInRequest, "sandbox_6mpgfnhz_nv2dpswt7z9wj4n7");
        dropInClient.setListener(this);

        btnCheckout = findViewById(R.id.btnCheckout);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropInClient != null) {
                    dropInClient.launchDropIn(dropInRequest);
                }
            }
        });
        toSettingAddess();
        turnBack();
    }

    private void toSettingAddess(){
        arrowSetting = findViewById(R.id.arrowSetting);
        arrowSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, SettingProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    private void updateTotalPrice() {
        int totalPrice = 0;
        for (Cart cartItem : listCart) {
            totalPrice += cartItem.getQuantity() * cartItem.getPrice();
        }
        TextView txtPrice = findViewById(R.id.txtPrice);
        txtPrice.setText("$"+String.valueOf(totalPrice));
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

    @Override
    public void onDropInSuccess(@NonNull DropInResult dropInResult) {
        nonce = dropInResult.getPaymentMethodNonce().getString();
        sendPaymentInfoToServer(nonce,listCart);
    }

    @Override
    public void onDropInFailure(@NonNull Exception error) {
        Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
    }
    private String convertCartListToJson(List<Cart> cartList) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(cartList);
    }
    private void sendPaymentInfoToServer(String nonce, List<Cart> cartList){
        //String jsonCart = convertCartListToJson(cartList);
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                ProductClient productClient = new ProductClient(CartActivity.this);
                ProductInterface productInterface = productClient.getProductInterface();

                List<Product> productList = new ArrayList<>();
                for (Cart cartItem : cartList) {
                    // Lấy chi tiết sản phẩm từ API dựa trên slug
                    Call<ProductData.DetailProductResponse> callProduct = productInterface.getSingleProduct(cartItem.getSlug());
                    try {
                        Response<ProductData.DetailProductResponse> response = callProduct.execute();
                        if (response.isSuccessful()) {
                            ProductData.DetailProductResponse productResponse = response.body();
                            Product product = productResponse.getProducts();

                            System.out.println(product);
                            if (product != null) {
                                productList.add(product);
                                System.out.println(productList);
                            }
                        } else {
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Payment payment = new Payment();
                payment.setNonce(nonce);
                payment.setCart(productList);
                System.out.println(payment);

                Gson gson = new GsonBuilder().create();
                String jsonPayment = gson.toJson(payment);
                System.out.println(jsonPayment);

                Call<ProductData.PaymentResponse> call = productInterface.processPayment(payment);
                call.enqueue(new Callback<ProductData.PaymentResponse>() {
                    @Override
                    public void onResponse(Call<ProductData.PaymentResponse> call, Response<ProductData.PaymentResponse> response) {
                        if (response.isSuccessful()) {
                            ProductData.PaymentResponse paymentResponse = response.body();
                            if (paymentResponse != null && paymentResponse.isOk()) {
                                listCart.clear();
                                CartDAO cartDAO = new CartDAO(CartActivity.this);
                                cartDAO.removeAllFromCart();
                                updateTotalPrice();
                                cartAdapter.notifyDataSetChanged();
                                Toast.makeText(CartActivity.this, "Payment successful", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CartActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String errorMessage = "Payment processing failed";
                            if (response.errorBody() != null) {
                                try {
                                    errorMessage = response.errorBody().string();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            Toast.makeText(CartActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductData.PaymentResponse> call, Throwable t) {
                        Toast.makeText(CartActivity.this, "Payment processing failed", Toast.LENGTH_SHORT).show();
                    }
                });
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                // Cập nhật UI sau khi công việc nền hoàn thành (nếu cần)
            }
        }.execute();
    }
}