package com.silexsecure.arusdriver.navfragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.silexsecure.arusdriver.ChangePasswordActivity;
import com.silexsecure.arusdriver.R;
import com.silexsecure.arusdriver.util.Helper;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private TextView bLogout;
    private TextView tvPhone, tvUsername, tvEmail;
    private CircleImageView driverImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);


        bLogout = findViewById(R.id.bLogout);

        Helper helper = new Helper(this);

        tvPhone = findViewById(R.id.tvPhone);
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        driverImage = findViewById(R.id.driverImage);

        String username = helper.getLoggedInUser().getFullname();
        String phone = helper.getLoggedInUser().getPhone();
        String email = helper.getLoggedInUser().getEmail();

        tvUsername.setText(username);
        tvEmail.setText(email);

        findViewById(R.id.clChangePassword).setOnClickListener(v -> startActivity(new Intent(ProfileActivity.this, ChangePasswordActivity.class)));

        Glide.with(this)
                .load(helper.getLoggedInUser().getImage().contentEquals("noimage.jpg") ? R.drawable.default_user : helper.getLoggedInUser().getImage())
                .into(driverImage);

        tvPhone.setText(phone);

        bLogout.setOnClickListener(v -> helper.logout());
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
