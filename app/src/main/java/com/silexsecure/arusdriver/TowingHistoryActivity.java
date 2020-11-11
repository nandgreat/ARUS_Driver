package com.silexsecure.arusdriver;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.silexsecure.arusdriver.adapters.HistoryAdapter;
import com.silexsecure.arusdriver.model.ProductSale;
import com.silexsecure.arusdriver.model.TowingRequest;
import com.silexsecure.arusdriver.util.Helper;


public class TowingHistoryActivity extends AppCompatActivity {

    LinearLayoutManager mLayoutManager; //for sorting
    SharedPreferences mSharedPref; //for saving sort settings
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;

    private static final String TAG = "TowingHistoryTAG";

    Helper helper;

    private FirebaseRecyclerOptions<ProductSale> options;
    private HistoryAdapter adapter;
    private DatabaseReference databaseReference;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_towing_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarmain);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        //RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        progressBar = findViewById(R.id.progressBar);

        helper = new Helper(this);

        String username = helper.getLoggedInUser().getFirstname();
        String phone = helper.getLoggedInUser().getPhone();

        //set layout as LinearLayout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference("driver_requests").child(phone);
        databaseReference.keepSynced(true);

        //send Query to FirebaseDatabase
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        FirebaseRecyclerOptions<TowingRequest> options =
                new FirebaseRecyclerOptions.Builder<TowingRequest>()
                        .setQuery(databaseReference, TowingRequest.class)
                        .build();

        adapter = new HistoryAdapter(options, this);
        progressBar.setVisibility(View.GONE);
        mRecyclerView.setAdapter(adapter);

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

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}