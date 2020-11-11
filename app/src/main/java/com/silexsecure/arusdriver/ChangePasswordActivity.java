package com.silexsecure.arusdriver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.silexsecure.arusdriver.model.ChangePasswordRequest;
import com.silexsecure.arusdriver.model.ChangePasswordResponse;
import com.silexsecure.arusdriver.model.User;
import com.silexsecure.arusdriver.service.APIService;
import com.silexsecure.arusdriver.service.ClientAPI;
import com.silexsecure.arusdriver.util.Helper;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText etOldPassword;
    EditText etNewPassword;
    EditText etNewPasswordAgain;

    TextView tvOldPasswordError;
    TextView tvNewPasswordError;
    TextView tvPasswordAgainError;

    ProgressDialog progressDialog;
    Helper helper;
    User userMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);

        etOldPassword = findViewById(R.id.etOldPassword);
        etNewPassword = findViewById(R.id.etNewPassword);
        etNewPasswordAgain = findViewById(R.id.etNewPasswordAgain);
        tvOldPasswordError = findViewById(R.id.tvOldPasswordError);
        tvNewPasswordError = findViewById(R.id.tvNewPasswordError);
        tvPasswordAgainError = findViewById(R.id.tvPasswordAgainError);

        progressDialog = new ProgressDialog(this);
        helper = new Helper(this);
        userMe = helper.getLoggedInUser();

        findViewById(R.id.bSubmit).setOnClickListener(v -> {

            String oldPassword = etOldPassword.getText().toString();
            String newPassword = etNewPassword.getText().toString();
            String newPasswordAgain = etNewPasswordAgain.getText().toString();

            boolean isNewPassError = !newPassword.isEmpty() && !newPasswordAgain.isEmpty() && !newPassword.contentEquals(newPasswordAgain);

            if (etOldPassword.getText().toString().isEmpty() || etNewPassword.getText().toString().isEmpty() ||
                    etNewPasswordAgain.getText().toString().isEmpty() || isNewPassError) {

                tvOldPasswordError.setVisibility(oldPassword.isEmpty() ? View.VISIBLE : View.GONE);
                tvPasswordAgainError.setVisibility(newPasswordAgain.isEmpty() ? View.VISIBLE : View.GONE);
                tvNewPasswordError.setVisibility(newPassword.isEmpty() ? View.VISIBLE : View.GONE);

                if (isNewPassError) {
                    tvNewPasswordError.setText(isNewPassError ? "Passwords do not match" : null);
                    tvNewPasswordError.setVisibility(isNewPassError ? View.VISIBLE : View.GONE);
                    tvPasswordAgainError.setText(isNewPassError ? "Passwords do not match" : null);
                    tvPasswordAgainError.setVisibility(isNewPassError ? View.VISIBLE : View.GONE);
                }
            } else {

                tvOldPasswordError.setVisibility(oldPassword.isEmpty() ? View.VISIBLE : View.GONE);
                tvPasswordAgainError.setVisibility(newPasswordAgain.isEmpty() ? View.VISIBLE : View.GONE);
                tvNewPasswordError.setVisibility(newPassword.isEmpty() ? View.VISIBLE : View.GONE);

                tvNewPasswordError.setText(isNewPassError ? "Passwords do not match" : null);
                tvNewPasswordError.setVisibility(isNewPassError ? View.VISIBLE : View.GONE);
                tvPasswordAgainError.setText(isNewPassError ? "Passwords do not match" : null);
                tvPasswordAgainError.setVisibility(isNewPassError ? View.VISIBLE : View.GONE);

                progressDialog.setTitle("Please wait...");
                progressDialog.setMessage("Updating your password");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

                ChangePasswordRequest request = new ChangePasswordRequest();
                request.setOldPassword(oldPassword);
                request.setPassword(newPassword);

                attemptPasswordChange(request);
            }
        });
    }

    private void attemptPasswordChange(ChangePasswordRequest request) {

        ClientAPI api = APIService.createDriverService(ClientAPI.class);

        Call<ChangePasswordResponse> call = api.changePassword(request, String.valueOf(userMe.getId()));

        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(@NotNull Call<ChangePasswordResponse> call, @NotNull Response<ChangePasswordResponse> response) {
                progressDialog.dismiss();
                if (response.code() == 200) {
                    if (response.body().getMessage().contentEquals("Password updated successfully")) {
                        Toast.makeText(ChangePasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                        etNewPassword.setText(null);
                        etNewPasswordAgain.setText(null);
                        etOldPassword.setText(null);
                    } else if (response.body().getMessage().contentEquals("Invalid password"))
                        Toast.makeText(ChangePasswordActivity.this, "Invalid old password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ChangePasswordResponse> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ChangePasswordActivity.this, "Network Error. Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}