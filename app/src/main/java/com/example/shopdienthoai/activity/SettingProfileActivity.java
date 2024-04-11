package com.example.shopdienthoai.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shopdienthoai.R;
import com.example.shopdienthoai.Utils.SharedPreference;
import com.example.shopdienthoai.api.auth.AuthClient;
import com.example.shopdienthoai.api.auth.AuthData;
import com.example.shopdienthoai.api.auth.AuthInterface;
import com.example.shopdienthoai.model.User;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingProfileActivity extends AppCompatActivity {

    ImageView btnBack;
    Button btnSave;
    EditText edtUserName, edtEmail, edtPassword, edtPhone, edtAddress;
    SharedPreference sharedPreference;
    String name, password, email, phone, address;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);
        findView();
        turnBack();
        getSetUser();
        updateProfile();
    }
    private void turnBack(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void findView(){
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        edtUserName = findViewById(R.id.edtUserName);
        edtEmail = findViewById(R.id.edtEmail);
        edtAddress = findViewById(R.id.edtAddress);
        edtPassword = findViewById(R.id.edtPassword);
        edtPhone = findViewById(R.id.edtPhone);
        progressBar = findViewById(R.id.progressbarAccount);
    }

    private void getSetUser(){
        sharedPreference = new SharedPreference(SettingProfileActivity.this);
        name = sharedPreference.getValueString("name");
        email = sharedPreference.getValueString("email");
        address = sharedPreference.getValueString("address");
        phone = sharedPreference.getValueString("phone");

        edtUserName.setText(name);
        edtEmail.setText(email);
        edtAddress.setText(address);
        edtPhone.setText(phone);
    }

    private void updateProfile(){
        HashMap<String, String> params = new HashMap<>();
        params.put("name",name);
        params.put("email",email);
        params.put("password",password);
        params.put("phone",phone);
        params.put("address",address);



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                AuthClient authClient = new AuthClient(SettingProfileActivity.this);
                AuthInterface authInterface = authClient.getAuthInterface();
                User user = new User();
                user.setName(edtUserName.getText().toString());
                user.setEmail(edtEmail.getText().toString());
                user.setPassword(edtPassword.getText().toString());
                user.setPhone(edtPhone.getText().toString());
                user.setAddress(edtAddress.getText().toString());

                Call<AuthData.ProfileResponse> call = authInterface.profile(user);
                call.enqueue(new Callback<AuthData.ProfileResponse>() {
                    @Override
                    public void onResponse(Call<AuthData.ProfileResponse> call, Response<AuthData.ProfileResponse> response) {
                        AuthData.ProfileResponse profileResponse = response.body();

                        if (profileResponse != null) {
                            if (profileResponse.isSuccess()) {
                                User user = profileResponse.getUpdatedUser();
                                sharedPreference.setValueString("name", user.getName());
                                sharedPreference.setValueString("email", user.getEmail());
                                sharedPreference.setValueString("password", user.getPassword());
                                sharedPreference.setValueString("phone", user.getPhone());
                                sharedPreference.setValueString("address", user.getAddress());
                                getSetUser();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                String errorMessage = profileResponse.getMessage();
                                Toast.makeText(SettingProfileActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        } else {
                            // Log the response body for debugging
                            Log.e("Response", "Unexpected response body: " + response.toString());
                            Toast.makeText(SettingProfileActivity.this, "Unexpected response body", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthData.ProfileResponse> call, Throwable t) {
                        Toast.makeText(SettingProfileActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}