package hcmute.com.blankcil.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import hcmute.com.blankcil.R;
import hcmute.com.blankcil.config.RetrofitClient;
import hcmute.com.blankcil.constants.APIService;
import hcmute.com.blankcil.model.AuthenticateResponse;
import hcmute.com.blankcil.model.ConfirmRequest;
import hcmute.com.blankcil.utils.SharedPrefManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailConfirmationActivity extends AppCompatActivity implements View.OnClickListener {
    EditText ec1, ec2, ec3, ec4, ec5, ec6;
    Button verifyBtn;
    private APIService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_confirmation);

        RetrofitClient retrofitClient = RetrofitClient.getInstance();
        apiService = retrofitClient.getApi();

        ec1 = findViewById(R.id.etCode1);
        ec2 = findViewById(R.id.etCode2);
        ec3 = findViewById(R.id.etCode3);
        ec4 = findViewById(R.id.etCode4);
        ec5 = findViewById(R.id.etCode5);
        ec6 = findViewById(R.id.etCode6);
        verifyBtn = findViewById(R.id.btnVerify);
        verifyBtn.setOnClickListener(this);
        setupEditTextListeners();
    }
    private void setupEditTextListeners() {
        ec1.addTextChangedListener(new GenericTextWatcher(ec1, ec2));
        ec2.addTextChangedListener(new GenericTextWatcher(ec2, ec3));
        ec3.addTextChangedListener(new GenericTextWatcher(ec3, ec4));
        ec4.addTextChangedListener(new GenericTextWatcher(ec4, ec5));
        ec5.addTextChangedListener(new GenericTextWatcher(ec5, ec6));
        ec6.addTextChangedListener(new GenericTextWatcher(ec6, null));

        ec1.setOnKeyListener(new GenericKeyListener(null, ec1));
        ec2.setOnKeyListener(new GenericKeyListener(ec1, ec2));
        ec3.setOnKeyListener(new GenericKeyListener(ec2, ec3));
        ec4.setOnKeyListener(new GenericKeyListener(ec3, ec4));
        ec5.setOnKeyListener(new GenericKeyListener(ec4, ec5));
        ec6.setOnKeyListener(new GenericKeyListener(ec5, ec6));
    }

    private class GenericTextWatcher implements TextWatcher {
        private View currentView;
        private View nextView;

        public GenericTextWatcher(View currentView, View nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length() == 1) {
                if (nextView != null) {
                    nextView.requestFocus();
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) { }
    }

    private class GenericKeyListener implements View.OnKeyListener {
        private View previousView;
        private View currentView;

        public GenericKeyListener(View previousView, View currentView) {
            this.previousView = previousView;
            this.currentView = currentView;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (((EditText) currentView).getText().toString().isEmpty() && previousView != null) {
                    previousView.requestFocus();
                }
            }
            return false;
        }
    }
    @Override
    public void onClick(View v) {
        confirm();
    }

    public void confirm() {
        String code1 = ec1.getText().toString().trim();
        String code2 = ec2.getText().toString().trim();
        String code3 = ec3.getText().toString().trim();
        String code4 = ec4.getText().toString().trim();
        String code5 = ec5.getText().toString().trim();
        String code6 = ec6.getText().toString().trim();

        String fullCode = code1 + code2 + code3 + code4 + code5 + code6;

        ConfirmRequest confirmRequest = new ConfirmRequest(SharedPrefManager.getInstance(this).getUserEmail(), fullCode);

        apiService.confirmEmail(confirmRequest).enqueue(new Callback<AuthenticateResponse>() {
            @Override
            public void onResponse(Call<AuthenticateResponse> call, Response<AuthenticateResponse> response) {
                if(response.isSuccessful() && response.body() != null){

                    Toast.makeText(EmailConfirmationActivity.this, "Verify successful!", Toast.LENGTH_SHORT).show();
                    Log.d("EmailConfirm", SharedPrefManager.getInstance(getApplicationContext()).getUserEmail());

                    // Chuyển đến MainActivity
                    Intent intent = new Intent(EmailConfirmationActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EmailConfirmationActivity.this, "Verification failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthenticateResponse> call, Throwable t) {
                Log.d("EmailConfirm", "Failed: " + t.getMessage());
                Toast.makeText(EmailConfirmationActivity.this, "Verification failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
