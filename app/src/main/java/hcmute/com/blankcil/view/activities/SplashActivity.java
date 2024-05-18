package hcmute.com.blankcil.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import hcmute.com.blankcil.R;
import hcmute.com.blankcil.config.RetrofitClient;
import hcmute.com.blankcil.constants.APIService;
import hcmute.com.blankcil.model.AuthenticateResponse;
import hcmute.com.blankcil.model.ProfileResponse;
import hcmute.com.blankcil.model.UserModel;
import hcmute.com.blankcil.utils.SharedPrefManager;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIMEOUT = 2000; // Thời gian hiển thị màn hình Splash (ms)

    ProgressBar progressBar;
    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.progressBar);

        // Set progress bar visibility
        progressBar.setVisibility(ProgressBar.VISIBLE);

        // Check login status after SPLASH_TIMEOUT milliseconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLoginStatus();
            }
        }, SPLASH_TIMEOUT);
    }

    private void checkLoginStatus() {
        String refreshToken = SharedPrefManager.getInstance(this).getRefreshToken();
        if (refreshToken != null) {
            // Make a network call to refresh the token
            // If refresh token fails, direct to login screen
            // Otherwise, proceed to MainActivity
            refreshAccessToken(refreshToken);
        } else {
            // No refresh token, proceed to LoginActivity
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        // Finish SplashActivity
    }

    private void refreshAccessToken(String refreshToken) {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        apiService = retrofitClient.getApi();
        apiService.refreshToken("Bearer " + refreshToken).enqueue(new Callback<AuthenticateResponse>() {
            @Override
            public void onResponse(retrofit2.Call<AuthenticateResponse> call, Response<AuthenticateResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String newAccessToken = response.body().getAccess_token();
                    String newRefreshToken = response.body().getRefresh_token();

                    // Save the new tokens
                    SharedPrefManager.getInstance(SplashActivity.this).saveTokens(newAccessToken, newRefreshToken);

                    // Get user profile using new access token
                    getUserProfile(newAccessToken);

//                    Toast.makeText(SplashActivity.this, "Token refreshed", Toast.LENGTH_SHORT).show();
                } else {
                    // If refresh token fails, direct to login screen
                    Toast.makeText(SplashActivity.this, "Failed to refresh token", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<AuthenticateResponse> call, Throwable t) {
                Log.d("RefreshToken", "Failed: " + t.getMessage());
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
                    SharedPrefManager.getInstance(SplashActivity.this).saveUserModel(userModel);

                    // Chuyển đến MainActivity
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SplashActivity.this, "Failed to get profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ProfileResponse> call, Throwable t) {
                Log.d("GetProfile", "Failed: " + t.getMessage());
            }
        });
    }
}
