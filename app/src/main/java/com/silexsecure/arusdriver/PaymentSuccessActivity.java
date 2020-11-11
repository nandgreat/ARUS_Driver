package com.silexsecure.arusdriver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cdflynn.android.library.checkview.CheckView;

public class PaymentSuccessActivity extends AppCompatActivity {

    private Button bTrackOrder, bDashboard;

    private CheckView mCheckView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        bTrackOrder = findViewById(R.id.bTrackOrder);
        bDashboard = findViewById(R.id.bDashboard);

        mCheckView = findViewById(R.id.check);

        mCheckView.check();

        bTrackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bTrackIntent = new Intent(PaymentSuccessActivity.this, MainActivity.class);
                startActivity(bTrackIntent);
                finish();
            }
        });

        bDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bTrackIntent = new Intent(PaymentSuccessActivity.this, MainActivity.class);
                startActivity(bTrackIntent);
                finish();
            }
        });
    }
}
