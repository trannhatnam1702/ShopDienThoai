package com.example.shopdienthoai.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopdienthoai.R;
import com.example.shopdienthoai.Utils.SharedPreference;
import com.example.shopdienthoai.api.auth.AuthClient;
import com.example.shopdienthoai.api.auth.AuthData;
import com.example.shopdienthoai.api.auth.AuthInterface;
import com.example.shopdienthoai.model.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    TextView txtLogin;
    EditText edtName, edtEmail, edtPassword, edtPhone, edtAddress, edtAnswer;

    Button btnRegister;

    String name, password, email, phone, address, answer;
    SharedPreference sharedPreference;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findView();
        sharedPreference = new SharedPreference(this);

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edtName.getText().toString();
                password = edtPassword.getText().toString();
                email = edtEmail.getText().toString();
                phone = edtPhone.getText().toString();
                address = edtAddress.getText().toString();
                answer = edtAnswer.getText().toString();

                if(validate(v)){
                    register();
                }
            }
        });
    }

    private void register(){
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put("name",name);
        params.put("email",email);
        params.put("password",password);
        params.put("phone",phone);
        params.put("address",address);
        params.put("answer",answer);


        AuthClient authClient = new AuthClient(RegisterActivity.this);
        AuthInterface authInterface = authClient.getAuthInterface();

        User user = new User();
        user.setName(params.get("name"));
        user.setEmail(params.get("email"));
        user.setPassword(params.get("password"));
        user.setPhone(params.get("phone"));
        user.setAddress(params.get("address"));
        user.setAnswer(params.get("answer"));

        Call<AuthData.AuthResponse> call = authInterface.register(user);
        call.enqueue(new Callback<AuthData.AuthResponse>() {
            @Override
            public void onResponse(Call<AuthData.AuthResponse> call, Response<AuthData.AuthResponse> response) {
                AuthData.AuthResponse registerResponse = response.body();

                if (registerResponse != null) {
                    if (registerResponse.isSuccess()) {
                        String token = registerResponse.getToken();
                        User user = registerResponse.getUser();
                        sharedPreference.setValueString("token", token);
                        sharedPreference.setValueString("id", user.getId());
                        sharedPreference.setValueString("name", user.getName());
                        sharedPreference.setValueString("email", user.getEmail());
                        sharedPreference.setValueString("password", user.getPassword());
                        sharedPreference.setValueString("phone", user.getPhone());
                        sharedPreference.setValueString("address", user.getAddress());
                        //Toast.makeText(RegisterActivity.this, token, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        progressBar.setVisibility(View.GONE);
                    } else {
                        String errorMessage = registerResponse.getMessage();
                        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    // Log the response body for debugging
                    Log.e("Response", "Unexpected response body: " + response.toString());
                    Toast.makeText(RegisterActivity.this, "Unexpected response body", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AuthData.AuthResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void findView(){
        txtLogin = findViewById(R.id.txtLogin);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtAnswer = findViewById(R.id.edtAnswer);
        btnRegister = findViewById(R.id.btnRegister);
    }

    public boolean validate(View v){
        boolean isValid;
            if (!TextUtils.isEmpty(name)){
                if (!TextUtils.isEmpty(email)){
                    if (!TextUtils.isEmpty(password)){
                        if (!TextUtils.isEmpty(phone)){
                            if (!TextUtils.isEmpty(address)){
                                if (!TextUtils.isEmpty(answer)){
                                    isValid=true;
                                }else{
                                    Toast.makeText(RegisterActivity.this, "Hãy nhập câu trả lời bảo mật của bạn!", Toast.LENGTH_SHORT).show();
                                    isValid=false;
                                }
                            }else{
                                Toast.makeText(RegisterActivity.this, "Hãy nhập địa chỉ của bạn!", Toast.LENGTH_SHORT).show();
                                isValid=false;
                            }
                        }else{
                            Toast.makeText(RegisterActivity.this, "Hãy nhập Số điện thoại của bạn!", Toast.LENGTH_SHORT).show();
                            isValid=false;
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this, "Hãy nhập Password của bạn!", Toast.LENGTH_SHORT).show();
                        isValid=false;
                    }
                }else{
                    Toast.makeText(RegisterActivity.this, "Hãy nhập Email của bạn!", Toast.LENGTH_SHORT).show();
                    isValid=false;
                }
            }else{
                Toast.makeText(RegisterActivity.this, "Hãy nhập Tên của bạn!", Toast.LENGTH_SHORT).show();
                isValid=false;
            }
        return isValid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref = getSharedPreferences("user_login", MODE_PRIVATE);
        if(pref.contains("token")){
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }
    }
}