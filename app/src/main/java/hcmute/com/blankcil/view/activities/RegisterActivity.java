package hcmute.com.blankcil.view.activities;

import android.content.Intent;
import android.os.Bundle;
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
import hcmute.com.blankcil.model.RegisterRequest;
import hcmute.com.blankcil.model.RegisterResponse;
import hcmute.com.blankcil.utils.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText fullname, email, password;
    Button registerBtn;
    private APIService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        apiService = retrofitClient.getApi();
        fullname = findViewById(R.id.edtFullName);
        email = findViewById(R.id.edtRegEmail);
        password = findViewById(R.id.edtPassword);
        registerBtn = findViewById(R.id.btnRegister);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        register();
    }

    public void register() {
        String fullName = fullname.getText().toString().trim();
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        RegisterRequest registerRequest = new RegisterRequest(fullName, userEmail, userPassword);

        if (registerRequest.isValidEmail() && registerRequest.isValidEmail()) {
            apiService.register(registerRequest).enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Save email after registration
                        SharedPrefManager.getInstance(RegisterActivity.this).saveEmail(response.body().getEmail());

                        // Navigate to EmailConfirmationActivity
                        Intent intent = new Intent(RegisterActivity.this, EmailConfirmationActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                        Toast.makeText(RegisterActivity.this, "Register successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Request failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Log.d("Register", "Register failed: " + t.getMessage());
                }
            });
        }
    }
}
