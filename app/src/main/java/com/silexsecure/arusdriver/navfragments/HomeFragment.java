package com.silexsecure.arusdriver.navfragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.silexsecure.arusdriver.BuildConfig;
import com.silexsecure.arusdriver.R;
import com.silexsecure.arusdriver.SellFuel;
import com.silexsecure.arusdriver.model.FirebaseUser;
import com.silexsecure.arusdriver.model.OrderHistory;
import com.silexsecure.arusdriver.model.User;
import com.silexsecure.arusdriver.service.TrackerService;
import com.silexsecure.arusdriver.util.Helper;
import com.silexsecure.arusdriver.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final int PERMISSION_READ_STATE = 43;
    private static final int PERMISSION_READ_LOCATION = 12;

    private int countOrder = 0;

    private LocationSettingsRequest mLocationSettingsRequest;

    private ArrayList<String> permissions = new ArrayList<>(Arrays.asList(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION));

    //    private static final String topic = "/topics/Tow_A_Tow";
    private static final String topic = "/topics/Enter_topic";

    private SettingsClient mSettingsClient;

    OrderHistory order = new OrderHistory();

    //    private String countString = ""
    private GeoFire geoFire;


    private static final String TAG = "HomeFragmentTAG";

    private LocationRequest mLocationRequest;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 3000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 3000;

    private static final int REQUEST_CHECK_SETTINGS = 100;

    public static Location mylocation;

    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 332;

    private static final int LOCATION_REQUEST = 1;

    private TextView tvCount, tvStatement, tvOnlineStatus;
    private ImageView ivNoDelivery;
    private ImageView ivCarParts;

    private TextView tvRecords;
    private String strPhoneType;
    private SwitchCompat switchCompat;

    Context context;

    ImageView ivGas, ivProfile;
    ImageView ivCarRepair;

    private Geocoder gcd;

    private User meUser;

    private FirebaseDatabase database;

    private String username;

    private DatabaseReference myRef, databaseReference;

    private TextView tvGreetings;

    public static Helper helper;
    private int carIcon;
    private LatLng selectedDriver;
    private boolean isOnline = false;

    public static HomeFragment newInstance() {
        // Required empty public constructor
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        tvGreetings = view.findViewById(R.id.tvGreetings);

        mSettingsClient = LocationServices.getSettingsClient(getActivity());

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();

        helper = new Helper(getContext());

        meUser = helper.getLoggedInUser();

        DatabaseReference driversLocation = FirebaseDatabase.getInstance().getReference().child(meUser.getRole().contentEquals("mechanic") ? "mechanicLocations" : "driverLocations");

        geoFire = new GeoFire(driversLocation);

        ivGas = view.findViewById(R.id.ivGas);
        ivCarRepair = view.findViewById(R.id.ivCarRepair);
        ivCarParts = view.findViewById(R.id.ivCarParts);
        ivProfile = view.findViewById(R.id.ivProfile);
        ivNoDelivery = view.findViewById(R.id.ivNoDelivery);
        tvCount = view.findViewById(R.id.tvCount);
        tvStatement = view.findViewById(R.id.tvStatement);
        tvOnlineStatus = view.findViewById(R.id.tvOnlineStatus);
        switchCompat = view.findViewById(R.id.switchCompat);

        if (meUser.isOnlineStatus()) {
            switchCompat.setChecked(true);
            Log.d(TAG, "onCreateView: ischecked " + meUser.isOnlineStatus());
        } else {
            switchCompat.setChecked(false);
            Log.d(TAG, "onCreateView: is not checked " + meUser.isOnlineStatus());
        }

        String username = helper.getLoggedInUser().getFirstname();
        String phone = helper.getLoggedInUser().getPhone();

        tvGreetings.setText("Good ".concat(Utils.timeofDay()).concat(", ".concat(username)));

        FirebaseUser user = new FirebaseUser(username, phone, true);

        initFirebase(user);

        countOrders(phone);

        context = getActivity();

        ivGas.setOnClickListener(v -> {
            Log.d("HomeFragmentTAG", "it worked");
            Intent intent = new Intent(context, SellFuel.class);
            startActivity(intent);
        });

        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked) {

                Dexter.withActivity(getActivity())
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {

                                mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                                        .addOnCompleteListener(task -> {

                                            Log.d(TAG, "onPermissionGranted: Location On");

                                                Objects.requireNonNull(getContext()).startService(new Intent(getContext(), TrackerService.class));
                                                Log.d(TAG, "onPermissionGranted: Service started");
                                                switchCompat.setChecked(true);

                                        }).addOnFailureListener(e -> {

                                    int statusCode = ((ApiException) e).getStatusCode();
                                    switch (statusCode) {
                                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                            Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                                    "location settings ");
                                            try {
                                                // Show the dialog by calling startResolutionForResult(), and check the
                                                // result in onActivityResult().
                                                ResolvableApiException rae = (ResolvableApiException) e;
                                                rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);

                                                switchCompat.setChecked(false);

                                            } catch (IntentSender.SendIntentException sie) {
                                                Log.i(TAG, "PendingIntent unable to execute request.");
                                            }
                                            break;
                                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                                    "fixed here. Fix in Settings.";
                                            Log.e(TAG, errorMessage);

                                            switchCompat.setChecked(false);

                                            Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show();
                                    }

                                    Log.d(TAG, "onPermissionGranted: Location Off");


                                });

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                if (response.isPermanentlyDenied()) {
                                    // open device settings when the permission is
                                    // denied permanently
                                    openSettings();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

            }else{
                Objects.requireNonNull(getContext()).stopService(new Intent(getContext(), TrackerService.class));
                Log.d(TAG, "onCreateView: Service Stopped");
            }

            onlineStatus(isChecked);

        });



        return view;
    }


    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

//        databaseReference.child("availableDrivers").child(meUser.getPhone()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                boolean isChecked;
//
//                isChecked = dataSnapshot.exists();
//                Log.d(TAG, "onDataChange: isonline " + isChecked);
//                tvOnlineStatus.setText(isChecked ? "You're Online" : "You're Offline");
//                tvOnlineStatus.setTextColor(isChecked ? Color.parseColor("#4CAF50") : R.color.darkGrey);
//                switchCompat.setChecked(isChecked);
//                meUser.setOnlineStatus(isChecked);
//                helper.setLoggedInUser(meUser);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    @SuppressLint("ResourceAsColor")
    private void onlineStatus(final boolean isChecked) {

        tvOnlineStatus.setText(isChecked ? "You're Online" : "You're Offline");
        tvOnlineStatus.setTextColor(isChecked ? Color.parseColor("#4CAF50") : R.color.darkGrey);

//        showCurrentDriver(selectedDriver, isChecked);

        if (isChecked) {
            databaseReference.child(meUser.getRole().contentEquals("mechanic") ? "availableMechanics" : "availableDrivers").child(meUser.getPhone()).setValue(meUser).addOnCompleteListener(task -> {
                meUser.setOnlineStatus(true);
                helper.setLoggedInUser(meUser);
                FirebaseMessaging.getInstance().subscribeToTopic(topic);
            });

        } else {
            meUser.setOnlineStatus(false);
            helper.setLoggedInUser(meUser);

            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
            databaseReference.child(meUser.getRole().contentEquals("mechanic") ? "availableMechanics" : "availableDrivers").child(meUser.getPhone()).removeValue();
        }
    }

    private int radius = 6;

    private void showCurrentDriver(LatLng mylocation, Boolean isOnline) {

        carIcon = R.drawable.truck_icon;

        if (mylocation != null) {

            if (isOnline) {
                geoFire.setLocation(meUser.getPhone(), new GeoLocation(mylocation.latitude, mylocation.longitude), (key, error) -> {

                    Log.d(TAG, "onComplete: " + key);

                    if (error != null) {
                        System.err.println("There was an error saving the location to GeoFire: " + error);
                    } else {
                        System.out.println("Location saved on server successfully!");
                    }
                });
            } else {
                geoFire.removeLocation(meUser.getPhone(), (key, error) -> Log.d(TAG, "onComplete: " + key + " removed"));
            }
        } else
            Toast.makeText(getContext(), "Could not find Locations", Toast.LENGTH_SHORT).show();
    }



    public void countOrders(final String phone) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("orders").child(phone);
        countOrder = 0;
//You must remember to remove the listener when you finish using it, also to keep track of changes you can use the ChildChange

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                countOrder = (int) dataSnapshot.getChildrenCount();

                Log.e(TAG, dataSnapshot.getKey() + "----------" + dataSnapshot.getChildrenCount() + "");


                if (countOrder != 0) {

                    tvCount.setVisibility(View.VISIBLE);
                    tvCount.setText(String.valueOf(countOrder));
                    ivNoDelivery.setVisibility(View.INVISIBLE);
                    tvStatement.setText(countOrder == 1 ? "Gas Order to Deliver" : "Gas Orders to Deliver");
                } else {
                    tvCount.setVisibility(View.INVISIBLE);
                    ivNoDelivery.setVisibility(View.VISIBLE);
                    tvStatement.setText("No Gas Order to Deliver");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initFirebase(final FirebaseUser user) {

        myRef = database.getReference("users");
        Log.d(TAG, "User exists" + myRef);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(user.getPhone()).exists() && dataSnapshot.getChildrenCount() > 0) {
                    //do ur stuff
                    Log.d(TAG, "User Already exists");
                } else {
                    //do something if not exists
                    myRef.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "User is inserted");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(getContext(), "Error inserting", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Error inserting");

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });

        myRef.child(user.getPhone()).setValue(user);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ivGas:
                Intent intent = new Intent(context, SellFuel.class);
                startActivity(intent);
                break;
        }

    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
