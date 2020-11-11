package com.silexsecure.arusdriver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.silexsecure.arusdriver.model.TowingRequest;
import com.silexsecure.arusdriver.util.Helper;

import java.util.Objects;

public class TowingSuccess extends AppCompatActivity {

    Button bHome;
    RatingBar ratingBar;
    Helper helper;
    Gson gson;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_towing_success);

        bHome = findViewById(R.id.bHome);

        ratingBar = findViewById(R.id.ratingBar);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        helper = new Helper(this);
        gson = new Gson();

        String myrequest = getIntent().getStringExtra("request");

        TowingRequest request = gson.fromJson(myrequest, new TypeToken<TowingRequest>() {
        }.getType());

        bHome.setOnClickListener(v -> {

            databaseReference.child("car_towing").child(request.getCustomerPhone()).child(request.getRequestKey()).setValue(request);
            if (ratingBar.getRating() != 0.0) {
                Toast.makeText(this, "Thank you for your feedback", Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(TowingSuccess.this, HomeActivity.class));
            finish();
        });

        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> Objects.requireNonNull(request).setCustomerRating(String.valueOf(rating)));
    }
}