package com.silexsecure.arusdriver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.silexsecure.arusdriver.navfragments.HomeFragment;
import com.silexsecure.arusdriver.navfragments.OrderHistoryFragment;
import com.silexsecure.arusdriver.navfragments.ProfileFragment;
import com.silexsecure.arusdriver.navfragments.PendingOrderFragment;
import com.silexsecure.arusdriver.util.Helper;
import com.silexsecure.arusdriver.util.NotificationConfig;
import com.silexsecure.arusdriver.util.SessionManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {

    SessionManager sessionManager;
    ActionBar toolbar;

    private static final String TAG = "MainActivityTAG";

    private Helper helper;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();

        helper = new Helper(this);

        if(helper.getLoggedInUser() == null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
        updateFCMToken();

        loadFragment(new HomeFragment());

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Home");

    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }





    private void updateFCMToken() {
        if (helper.getLoggedInUser() != null) {
            databaseReference.child("fcm_tokens").child(helper.getLoggedInUser().getPhone()).setValue(displayFirebaseRegId());
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("Home");
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_history:
                    toolbar.setTitle("Order History");
                    fragment = new OrderHistoryFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_pending:
                    toolbar.setTitle("Pending Orders");
                    fragment = new PendingOrderFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    toolbar.setTitle("Profile");
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
    // Fetches reg id from shared preferences
    // and displays on the screen
    private String displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(NotificationConfig.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);
        return regId;

    }


}


