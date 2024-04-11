package com.example.shopdienthoai.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopdienthoai.R;
import com.example.shopdienthoai.api.auth.AuthClient;
import com.example.shopdienthoai.api.auth.AuthData;
import com.example.shopdienthoai.api.auth.AuthInterface;
import com.example.shopdienthoai.model.NewPass;
import com.example.shopdienthoai.model.User;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword, edtAnswer;
    TextView txtLogin, txtRegister;
    Button btnlSubmit;
    String email, newPassword, answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        findView();
        forgotPass();
        toActivities();
    }

    private void toActivities(){
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void forgotPass(){
        btnlSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtEmail.getText().toString();
                newPassword = edtPassword.getText().toString();
                answer = edtAnswer.getText().toString();

                HashMap<String, String> params = new HashMap<>();
                params.put("email",email);
                params.put("answer",answer);
                params.put("newPassword",newPassword);

                AuthClient authClient = new AuthClient(ForgotPasswordActivity.this);
                AuthInterface authInterface = authClient.getAuthInterface();

                NewPass newPass = new NewPass();
                newPass.setEmail(params.get("email"));
                newPass.setAnswer(params.get("answer"));
                newPass.setNewPassword(params.get("newPassword"));

                Call<AuthData.ForgotPassResponse> call = authInterface.forgotPassword(newPass);
                call.enqueue(new Callback<AuthData.ForgotPassResponse>() {
                    @Override
                    public void onResponse(Call<AuthData.ForgotPassResponse> call, Response<AuthData.ForgotPassResponse> response) {
                        AuthData.ForgotPassResponse forgotPassResponse = response.body();
                        if (forgotPassResponse != null) {
                            if (forgotPassResponse.isSuccess()) {
                                Toast.makeText(ForgotPasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                            } else {
                                String errorMessage = forgotPassResponse.getMessage();
                                Toast.makeText(ForgotPasswordActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("Response", "Unexpected response body: " + response.toString());
                            Toast.makeText(ForgotPasswordActivity.this, "Unexpected response body", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthData.ForgotPassResponse> call, Throwable t) {
                        Toast.makeText(ForgotPasswordActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void findView(){
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtAnswer = findViewById(R.id.edtAnswer);
        btnlSubmit = findViewById(R.id.btnlSubmit);
        txtLogin = findViewById(R.id.txtLogin);
        txtRegister = findViewById(R.id.txtRegister);
    }
}