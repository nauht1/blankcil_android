package hcmute.com.blankcil.view.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hcmute.com.blankcil.R;
import hcmute.com.blankcil.config.RetrofitClient;
import hcmute.com.blankcil.constants.APIService;
import hcmute.com.blankcil.model.AuthenticateRequest;
import hcmute.com.blankcil.model.AuthenticateResponse;
import hcmute.com.blankcil.model.ProfileResponse;
import hcmute.com.blankcil.model.UserModel;
import hcmute.com.blankcil.utils.SharedPrefManager;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email, password;
    Button loginBtn;
    private APIService apiService;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        apiService = retrofitClient.getApi();
        checkLoginStatus();

        setContentView(R.layout.activity_login);

        email = findViewById(R.id.edtLoginEmail);
        password = findViewById(R.id.edtLoginPassword);
        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        loginUser();
    }

    private void loginUser() {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        AuthenticateRequest authenticateRequest = new AuthenticateRequest(userEmail, userPassword);
        apiService.authenticate(authenticateRequest).enqueue(new Callback<AuthenticateResponse>() {
            @Override
            public void onResponse(retrofit2.Call<AuthenticateResponse> call, Response<AuthenticateResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String accessToken = response.body().getAccess_token();
                        String refreshToken = response.body().getRefresh_token();
                        SharedPrefManager.getInstance(LoginActivity.this).saveTokens(accessToken, refreshToken);
                        getUserProfile(accessToken);
                    }
                    Toast.makeText(LoginActivity.this, response.body().getAccess_token(), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<AuthenticateResponse> call, Throwable t) {
                Log.d("Login", "Login failed" + t.getMessage());
            }
        });
    }

    private void getUserProfile(String accessToken) {
        apiService.getProfile("Bearer " + accessToken).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserModel userModel = response.body().getBody();
                    // Lưu UserModel vào SharedPreferences
                    SharedPrefManager.getInstance(LoginActivity.this).saveUserModel(userModel);

                    // Chuyển đến MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Failed to get profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ProfileResponse> call, Throwable t) {
                Log.d("GetProfile", "Failed: " + t.getMessage());
            }
        });
    }

    private void checkLoginStatus() {
//        String accessToken = SharedPrefManager.getInstance(this).getAccessToken();
//        if (accessToken != null) {
//            // Token is present, you can also validate the token here if needed
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
//        }
        String refreshToken = SharedPrefManager.getInstance(this).getRefreshToken();
        if (refreshToken != null) {
            // Make a network call to refresh the token
            refreshAccessToken(refreshToken);
        }
    }

    private void refreshAccessToken(String refreshToken) {
        apiService.refreshToken("Bearer " + refreshToken).enqueue(new Callback<AuthenticateResponse>() {
            @Override
            public void onResponse(retrofit2.Call<AuthenticateResponse> call, Response<AuthenticateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String newAccessToken = response.body().getAccess_token();
                    String newRefreshToken = response.body().getRefresh_token();

                    // Save the new tokens
                    SharedPrefManager.getInstance(LoginActivity.this).saveTokens(newAccessToken, newRefreshToken);

                    // Get user profile using new access token
                    getUserProfile(newAccessToken);

                    Toast.makeText(LoginActivity.this, "Token refreshed", Toast.LENGTH_SHORT).show();
                } else {
                    // If refresh token fails, direct to login screen
                    Toast.makeText(LoginActivity.this, "Failed to refresh token", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<AuthenticateResponse> call, Throwable t) {
                Log.d("RefreshToken", "Failed: " + t.getMessage());
            }
        });
    }
}
