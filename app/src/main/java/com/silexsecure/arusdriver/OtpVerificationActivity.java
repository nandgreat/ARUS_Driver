package com.silexsecure.arusdriver;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.silexsecure.arusdriver.service.APIService;
import com.silexsecure.arusdriver.service.ClientAPI;
import com.silexsecure.arusdriver.model.OtpVerifyRequest;
import com.silexsecure.arusdriver.model.OtpVerifyResponse;
import com.silexsecure.arusdriver.model.User;
import com.silexsecure.arusdriver.util.Helper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerificationActivity extends AppCompatActivity {

    public static String TAG = "OTPVerifyActivityTAG";

    PinView otp;
    Button submit_otp;

    ProgressDialog progressDialog;

    private TextView tvPhone;

    Helper helper;

    PinView pinView;

    TextView tvInvalid;


    Long otpLong, userid;
    String id, phone;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

//        otp=findViewById(R.id.firstPinView);
//        submit_otp=findViewById(R.id.submit_otp);

        helper = new Helper(this);

        progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);

        pinView = findViewById(R.id.firstPinView);
        tvInvalid = findViewById(R.id.tvInvalid);
        tvPhone = findViewById(R.id.tvPhone);


        if (getIntent().getExtras() != null) {
            otpLong = getIntent().getLongExtra("otp", 0);
            userid = getIntent().getLongExtra("userid", 0);
            phone = getIntent().getStringExtra("phone");

            phone = phone.trim().length() == 11 ? phone.substring(7) : phone.substring(10);

            tvPhone.setText("+234****" + phone);

            Log.d(TAG, id + " " + otpLong + " " + phone);

        }

        pinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 4) {
//                    String otpValue = otp.getText().toString().trim();
                    if (otpLong.toString().contentEquals(s)) {

                        progressDialog.setTitle("Please Wait");
                        progressDialog.setMessage("Verifying your OTP");
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        OtpVerifyRequest otpVerifyRequest = new OtpVerifyRequest();
                        otpVerifyRequest.setOtp(otpLong.toString());

                        attemptOTPVerify(otpVerifyRequest);
                    } else {
                        tvInvalid.setVisibility(View.VISIBLE);
                    }
                } else {
                    tvInvalid.setVisibility(View.INVISIBLE);
                }

                Log.d(TAG, "onTextChanged() called with: s = [" + s + "], start = [" + start + "], before = [" + before + "], count = [" + count + "]");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void attemptOTPVerify(OtpVerifyRequest otpVerifyRequest) {

        APIService service = new APIService();

        ClientAPI api = APIService.createDriverService(ClientAPI.class);

        Call<OtpVerifyResponse> call = api.SendOTP(otpVerifyRequest);

        call.enqueue(new Callback<OtpVerifyResponse>() {
            @Override
            public void onResponse(Call<OtpVerifyResponse> call, Response<OtpVerifyResponse> response) {

                progressDialog.dismiss();

                if (response.isSuccessful()) {

                    User user = response.body().getUser();

                    user.setToken(response.body().getToken());

                    helper.setLoggedInUser(user);

                        Intent passwordIntent = new Intent(OtpVerificationActivity.this, HomeActivity.class);
                        startActivity(passwordIntent);
                        finish();
                    } else {
                        Toast.makeText(OtpVerificationActivity.this, "Failed to Verify OTP", Toast.LENGTH_SHORT).show();
                    }
                }

            @Override
            public void onFailure(Call<OtpVerifyResponse> call, Throwable t) {

            }
        });
    }

}