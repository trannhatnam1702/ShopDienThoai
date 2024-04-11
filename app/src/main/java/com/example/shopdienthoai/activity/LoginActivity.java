package com.example.shopdienthoai.activity;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextView txtSignUp, forgotPassword;
    EditText edtEmail, edtPassword;
    Button btnLogin;

    String email, password;
    SharedPreference sharedPreference;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtSignUp = findViewById(R.id.txtLogin);
        progressBar = findViewById(R.id.progressBar);
        forgotPassword = findViewById(R.id.forgotPassword);

        sharedPreference = new SharedPreference(this);

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();

                if(validate(v))
                    login();
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(){
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put("email",email);
        params.put("password",password);

        AuthClient authClient = new AuthClient(LoginActivity.this);
        AuthInterface authInterface = authClient.getAuthInterface();

        User user = new User();
        user.setEmail(params.get("email"));
        user.setPassword(params.get("password"));

        Call<AuthData.AuthResponse> call = authInterface.login(user);
        call.enqueue(new Callback<AuthData.AuthResponse>() {
            @Override
            public void onResponse(Call<AuthData.AuthResponse> call, Response<AuthData.AuthResponse> response) {
                AuthData.AuthResponse loginResponse = response.body();

                if (loginResponse != null) {
                    if (loginResponse.isSuccess()) {
                        String token = loginResponse.getToken();
                        User user = loginResponse.getUser();
                        sharedPreference.setValueString("token", token);
                        sharedPreference.setValueString("id", user.getId());
                        sharedPreference.setValueString("name", user.getName());
                        sharedPreference.setValueString("email", user.getEmail());
                        sharedPreference.setValueString("password", user.getPassword());
                        sharedPreference.setValueString("phone", user.getPhone());
                        sharedPreference.setValueString("address", user.getAddress());
                        String testToken = sharedPreference.getValueString("token");
                        System.out.println(testToken);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        progressBar.setVisibility(View.GONE);
                    } else {
                        String errorMessage = loginResponse.getMessage();
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    // Log the response body for debugging
                    Log.e("Response", "Unexpected response body: " + response.toString());
                    Toast.makeText(LoginActivity.this, "Unexpected response body", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<AuthData.AuthResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public boolean validate(View v) {
        boolean isValid;
        if (!TextUtils.isEmpty(email)) {
            if (!TextUtils.isEmpty(password)) {
                isValid = true;

            } else {
                Toast.makeText(LoginActivity.this, "Hãy nhập Password của bạn!", Toast.LENGTH_SHORT).show();
                isValid = false;
            }
        } else {
            Toast.makeText(LoginActivity.this, "Hãy nhập Email của bạn!", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref = getSharedPreferences("user_login", MODE_PRIVATE);
        if(pref.contains("token")){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }
}