package com.example.shopdienthoai.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.shopdienthoai.R;
import com.example.shopdienthoai.adapter.OrderAdapter;
import com.example.shopdienthoai.api.auth.AuthClient;
import com.example.shopdienthoai.api.auth.AuthInterface;
import com.example.shopdienthoai.model.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity {
    RecyclerView OrderView;
    OrderAdapter orderAdapter;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        OrderView = findViewById(R.id.OrderView);
        OrderView.setLayoutManager(new LinearLayoutManager(this));
        getOrder();
        turnBack();
    }

    private void getOrder(){
        AuthClient authClient = new AuthClient(OrdersActivity.this);
        AuthInterface authInterface = authClient.getAuthInterface();

        Call<List<Order>> call = authInterface.orders();
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    List<Order> orderList = response.body();
                    orderAdapter = new OrderAdapter(OrdersActivity.this, orderList);
                    OrderView.setAdapter(orderAdapter);
                } else {
                    Toast.makeText(OrdersActivity.this, "Failed to retrieve orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(OrdersActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error", "Error: " + t.getMessage());;            }
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