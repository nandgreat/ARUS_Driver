package com.silexsecure.arusdriver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityManager;

import com.kusu.loadingbutton.LoadingButton;
import com.silexsecure.arusdriver.HomeActivity;
import com.silexsecure.arusdriver.service.APIService;
import com.silexsecure.arusdriver.service.ClientAPI;
import com.silexsecure.arusdriver.model.LoginRequest;
import com.silexsecure.arusdriver.model.LoginResponse;
import com.silexsecure.arusdriver.model.User;
import com.silexsecure.arusdriver.util.Helper;
import com.silexsecure.arusdriver.util.Utils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private EditText etPhone;
    private EditText etPassword;

    private EditText etDescription;

    private ProgressDialog progressDialog;
    private boolean loginStatus;
    private String token;

    private TextView tvSignup;

    LoadingButton bLogin;

    private Helper helper;
    private String TAG = "LoginActivityTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bLogin = findViewById(R.id.bLogin);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        tvSignup = findViewById(R.id.tvSignup);

        helper = new Helper(this);

        progressDialog = new ProgressDialog(this);

        tvSignup.setOnClickListener(v -> {
            Intent signupIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(signupIntent);
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (phone.equalsIgnoreCase("")) {
                    etPhone.setError("Phone number field cannot be empty");
                } else if (password.equalsIgnoreCase("")) {
                    etPhone.setError("Password field cannot be empty");
                } else {

                    phone = Utils.correctPhoneNumber(phone);

                    phone = phone.trim().length() == 11 ?  phone.substring(1, 11) :  phone.substring(3, 15);

                    phone = "234"+ phone;

                    progressDialog.setMessage("Logging in");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    etPhone.setEnabled(false);
                    etPassword.setEnabled(false);

                    LoginRequest request = new LoginRequest();

                    request.setPhone(phone);
                    request.setPassword(password);

                    attemptLogin(request);

                }
            }
        });
    }

    private void attemptLogin(LoginRequest request) {

        ClientAPI api = APIService.createDriverService(ClientAPI.class);

        Call<LoginResponse> call = api.LoginUser(request);

        Log.d(TAG, "attemptLogin: "+ request.getPhone());

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NotNull Call<LoginResponse> call, @NotNull final Response<LoginResponse> response) {

                etPhone.setEnabled(true);
                etPassword.setEnabled(true);
                progressDialog.dismiss();

                if (response.isSuccessful()) {

                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();

                    User user = response.body().getUser();

                    user.setToken(response.body().getToken());
                    user.setWallet(response.body().getWallet());

                    helper.setLoggedInUser(user);

                    Intent mainIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(mainIntent);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Login Credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                Log.d("LoginError", t.getMessage());
                progressDialog.dismiss();
                etPhone.setEnabled(true);
                etPassword.setEnabled(true);

                Toast.makeText(LoginActivity.this, "Network error... Please try again", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        dismissProgress();
        progressDialog.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        progressDialog.dismiss();
//        dismissProgress();
    }

    public void dismissProgress() {
        etPhone.setEnabled(true);
        etPassword.setEnabled(true);
        bLogin.hideLoading();
    }
}
