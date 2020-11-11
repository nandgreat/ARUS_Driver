package com.silexsecure.arusdriver.navfragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.silexsecure.arusdriver.R;
import com.silexsecure.arusdriver.adapters.PendingOrdersAdapter;
import com.silexsecure.arusdriver.model.ProductSale;
import com.silexsecure.arusdriver.util.Helper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingOrderFragment extends Fragment {

    private static String TAG = "TrackOrderFragmentTAG";


    LinearLayoutManager mLayoutManager; //for sorting
    SharedPreferences mSharedPref; //for saving sort settings
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    ArrayList<ProductSale> arrayList;

    Helper helper;


    private FirebaseRecyclerOptions<ProductSale> options;
    private PendingOrdersAdapter adapter;
    private DatabaseReference databaseReference;

    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_track_order, container, false);

        //RecyclerView
        mRecyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        progressBar = rootView.findViewById(R.id.progressBar);

        helper = new Helper(getContext());

        String username = helper.getLoggedInUser().getFirstname();
        String phone = helper.getLoggedInUser().getPhone();

        //set layout as LinearLayout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference = FirebaseDatabase.getInstance().getReference("orders").child(phone);
        databaseReference.keepSynced(true);

        //send Query to FirebaseDatabase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Data");

        arrayList = new ArrayList<ProductSale>();

//        options = new FirebaseRecyclerOptions.Builder<ProductSale>().setQuery(databaseReference, ProductSale.class).build();

        FirebaseRecyclerOptions<ProductSale> options =
                new FirebaseRecyclerOptions.Builder<ProductSale>()
                        .setQuery(databaseReference, ProductSale.class)
                        .build();

        adapter = new PendingOrdersAdapter(options,getContext());
        progressBar.setVisibility(View.GONE);
        mRecyclerView.setAdapter(adapter);

        return rootView;
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