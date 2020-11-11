package com.silexsecure.arusdriver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.kusu.loadingbutton.LoadingButton;
import com.silexsecure.arusdriver.service.APIService;
import com.silexsecure.arusdriver.service.ClientAPI;
import com.silexsecure.arusdriver.model.RegisterRequest;
import com.silexsecure.arusdriver.model.RegisterResponse;
import com.silexsecure.arusdriver.util.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etPhone, etPassword, etEmail, etConfirmPassword;

    TextView tvLogin;

    LoadingButton bRegister;

    int selectedValue = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tvLogin = findViewById(R.id.tvLogin);

        initVariables();

        findViewById(R.id.tvLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstname = etFirstName.getText().toString().trim();
                String lastname = etLastName.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String confirmpassword = etConfirmPassword.getText().toString().trim();

                if (firstname.isEmpty() || lastname.isEmpty() || phone.isEmpty() || password.isEmpty() || email.isEmpty() || confirmpassword.isEmpty()) {

                    etFirstName.setError(firstname.isEmpty() ? "First Name is required" : null);
                    etLastName.setError(lastname.isEmpty() ? "Last name Name is required" : null);
                    etPassword.setError(password.isEmpty() ? "Password Name is required" : null);
                    etPhone.setError(phone.isEmpty() ? "Phone Name is required" : null);
                    etEmail.setError(email.isEmpty() ? "Email Name is required" : null);
                    etConfirmPassword.setError(confirmpassword.isEmpty() ? "Confirm password Name is required" : null);

                    Toast.makeText(SignupActivity.this, "Please fill all the Information to continue", Toast.LENGTH_SHORT).show();

                } else if (!password.contentEquals(confirmpassword)) {
                    etPassword.setError("Passwords do not match");
                    etConfirmPassword.setError("Passwords do not match");
                } else {

                    if (isNigerianNumber(phone)) {

                        phone = Utils.correctPhoneNumber(phone);

                        bRegister.showLoading();

                        etPhone.setEnabled(false);
                        etPassword.setEnabled(false);
                        etFirstName.setEnabled(false);
                        etEmail.setEnabled(false);

                        tvLogin.setEnabled(false);

                        RegisterRequest request = new RegisterRequest();

                        request.setPhone(phone);
                        request.setEmail(email);
                        request.setPasswordConfirmation(confirmpassword);
                        request.setLastname(lastname);
                        request.setPassword(password);
                        request.setFirstname(firstname);

                        attemptRegister(request);
                    } else {
//                        progressDialog.dismiss();
//                        textInputPhone.setErrorEnabled(true);
                        etPhone.setError("Phone Number is invalid");
                    }
                }
            }
        });
    }


    private boolean isNigerianNumber(String phone) {

        if (phone.length() < 11 || phone.length() > 14) {
            return false;
        } else if (phone.substring(0, 1).contentEquals("0") && phone.length() != 11) {
            return false;
        } else if (phone.substring(0, 4).contentEquals("+234") && (phone.length() != 14)) {
            return false;
        } else return !phone.substring(0, 3).contentEquals("234") || (phone.length() == 13);
    }

    private void attemptRegister(final RegisterRequest request) {

        APIService service = new APIService();
        ClientAPI api = APIService.createDriverService(ClientAPI.class);

        Call<RegisterResponse> call = api.RegisterUser(request);

        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                dismissProgress();
                if (response.isSuccessful()) {

                    if (response.code() == 200) {

                        Toast.makeText(SignupActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();

                        Intent otpIntent = new Intent(SignupActivity.this, OtpVerificationActivity.class);
                        otpIntent.putExtra("otp", response.body().getOtp());
                        otpIntent.putExtra("userid", response.body().getUserid());
                        otpIntent.putExtra("phone", request.getPhone());
                        startActivity(otpIntent);
                        finish();
                    } else if (response.code() == 409) {
                        Toast.makeText(SignupActivity.this, "Mobile Number already exists", Toast.LENGTH_LONG).show();
                    }
                } else {

                    Toast.makeText(SignupActivity.this, "Sorry Could not register you at the Moment", Toast.LENGTH_LONG).show();
                    Log.d("Registration Errors", response.message() + " " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {

                dismissProgress();
                Toast.makeText(SignupActivity.this, "Network Error.. Please try again", Toast.LENGTH_SHORT).show();

                Log.d("Registration Exception", t.getMessage());

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void initVariables() {
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        bRegister = findViewById(R.id.bRegister);
    }


    public void dismissProgress() {
        bRegister.hideLoading();
        etPhone.setEnabled(true);
        etPassword.setEnabled(true);
        etFirstName.setEnabled(true);
        etLastName.setEnabled(true);
        etConfirmPassword.setEnabled(true);
        etEmail.setEnabled(true);
        tvLogin.setEnabled(true);
    }
}
