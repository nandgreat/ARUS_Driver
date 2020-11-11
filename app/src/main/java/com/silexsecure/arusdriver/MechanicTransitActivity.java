package com.silexsecure.arusdriver;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.silexsecure.arusdriver.HomeActivity;
import com.silexsecure.arusdriver.MainActivity;
import com.silexsecure.arusdriver.R;
import com.silexsecure.arusdriver.TowingSuccess;
import com.silexsecure.arusdriver.model.CarRepair;
import com.silexsecure.arusdriver.model.Status;
import com.silexsecure.arusdriver.model.TowinRequestResponse;
import com.silexsecure.arusdriver.model.CarRepair;
import com.silexsecure.arusdriver.model.User;
import com.silexsecure.arusdriver.service.APIService;
import com.silexsecure.arusdriver.service.ClientAPI;
import com.silexsecure.arusdriver.service.TrackerService;
import com.silexsecure.arusdriver.util.DirectionJSONParser;
import com.silexsecure.arusdriver.util.Helper;
import com.silexsecure.arusdriver.util.SharedPreferenceHelper;
import com.silexsecure.arusdriver.util.Utils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;

public class MechanicTransitActivity extends AppCompatActivity implements OnMapReadyCallback {
    private SlidingUpPanelLayout mLayout;
    private static final String TAG = "TransitActivityTAG";

    SharedPreferenceHelper sharedPreferenceHelper;

    ConstraintLayout rootFrame, clBottomLayout, clPriceLayout, clRequesting, clSelectDestinationLayout, clVehicleMake;

    int carIcon = 0;

    private GoogleMap mMap;

    GeoQuery geoQuery;

    private CarRepair mechanicRequest;

    private int driverIndex;

    Polyline polyline;

    private AlertDialog b;

    private TextView tvEstTime;

    private LatLng selectedDriver;

    private Status status = new Status();


    private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private String serverKey =
            "key=" + "AAAAumql7aw:APA91bEpErt2o4F1e7KvoNf2tM3KjukKLC8sJqggEF0H6HPH-PcyFbGiRJgQ2fb3JW_XkV5RtUZW8FhZl83OGmhwbTW5VSruN4DIPte5kO-Jb2mzHTpOx97Q5n4KqmlcmJVgOxGxQIEd";
    private String contentType = "application/json";

    String driverUsername;
    String driverName;
    String rideStatus;
    String destination;
    String rideId;
    String driverId;
    String driverMobile;
    String driverRating;
    String carImage;
    String time;
    String driverImage;

    ProgressDialog progressDialog;

    List<Marker> markers;

    DatabaseReference ref;
    GeoFire geoFire;

    Double mylatitude, mylongitude;

    private String carDetails;
    private Marker mDriverMarker, mCustomerMarker;
    private ArrayList<LatLng> driversList;
    private String originName;
    private String destinationName;

    private User meUser;
    private Helper helper;

    private LatLng origin;

    String carMake;
    String pickupAddress;
    String pickupLatitude;
    String pickupLongitude;
    String destinationLatitude;
    String destinationLongitude;
    String amount;
    String carType;
    String requestKey;
    String customerName;
    String customerPhone;
    String mechanicStatus;

    String vehicleType;
    String vehicleColor;
    String vehicleModel;
    String regNumber;
    String carFault;
    String description;
    String customerImage;
    String requestTime;
    String paymentStatus;
    String paymentAmount;
    String paymentMethod;
    String requestStatus;
    String requestFee;
    String vehicleYear;
    String orderTime;
    String serviceType;

    String address;
    String customerLatitude;
    String customerLongitude;
    String paymentType;
    String quantityInKg;
    String driverPhone;
    Button bArrival;
    private double radius;

    DatabaseReference databaseReference;

    LatLngBounds.Builder builder;


    LatLng driverLocation;

    public MechanicTransitActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit);

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        sharedPreferenceHelper = new SharedPreferenceHelper(this);

        helper = new Helper(this);

        meUser = helper.getLoggedInUser();

        builder = new LatLngBounds.Builder();

        progressDialog = new ProgressDialog(this);

        if (HomeActivity.Companion.getSydney() != null) {
            mylatitude = HomeActivity.Companion.getSydney().latitude;
            mylongitude = HomeActivity.Companion.getSydney().longitude;
        }
        LiveData<LatLng> liveData = TrackerService.Companion.getMylocation();

        liveData.observe(this, latLng -> {
            mylatitude = latLng.latitude;
            mylongitude = latLng.longitude;
            Log.d(TAG, "onCreate: " + mylatitude);
        });

        final DatabaseReference driversLocation = FirebaseDatabase.getInstance().getReference().child(meUser.getRole().contentEquals("mechanic") ? "mechanicLocations" : "driverLocations");
        geoFire = new GeoFire(driversLocation);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        markers = new ArrayList<>();

        Button bCancelBooking = findViewById(R.id.bCancelBooking);
        bArrival = findViewById(R.id.bArrival);

        tvEstTime = findViewById(R.id.tvEstTime);
        TextView tvPickupLocation = findViewById(R.id.tvPickupLocation);
        TextView tvDestinationLocation = findViewById(R.id.tvDestinationLocation);

        TextView tvDriverName = findViewById(R.id.tvDriverName);
        TextView tvOrderPrice = findViewById(R.id.tvOrderPrice);
        TextView tvRideModel = findViewById(R.id.tvRideModel);
        TextView tvDestination = findViewById(R.id.tvDestination);
        ImageView ivDriverMobile = findViewById(R.id.ivDriverMobile);

        CircleImageView ivDriverImage = findViewById(R.id.ivDriverImage);

        driverLocation = new LatLng(9.0632064, 7.4255068);

        mechanicRequest = new CarRepair();

//        String nameOrigin = MainActivityTest.originName;

        Intent intentExtras = getIntent();
        Bundle bundle = intentExtras.getExtras();

//        if (bundle != null) {

        driverPhone = meUser.getPhone();

        carType = sharedPreferenceHelper.getStringPreference("vehicleType");
        carMake = sharedPreferenceHelper.getStringPreference("carMake");
        vehicleColor = sharedPreferenceHelper.getStringPreference("vehicleColor");
        vehicleModel = sharedPreferenceHelper.getStringPreference("vehicleModel");
        regNumber = sharedPreferenceHelper.getStringPreference("regNumber");
        carFault = sharedPreferenceHelper.getStringPreference("carFault");
        description = sharedPreferenceHelper.getStringPreference("description");
        requestKey = sharedPreferenceHelper.getStringPreference("requestKey");
        customerName = sharedPreferenceHelper.getStringPreference("customerName");
        customerPhone = sharedPreferenceHelper.getStringPreference("customerPhone");
        customerImage = sharedPreferenceHelper.getStringPreference("customerImage");
        requestTime = sharedPreferenceHelper.getStringPreference("requestTime");
        paymentStatus = sharedPreferenceHelper.getStringPreference("paymentStatus");
        pickupLongitude = sharedPreferenceHelper.getStringPreference("pickupLongitude");
        pickupLatitude = sharedPreferenceHelper.getStringPreference("pickupLatitude");
        pickupAddress = sharedPreferenceHelper.getStringPreference("pickupAddress");
        paymentAmount = sharedPreferenceHelper.getStringPreference("paymentAmount");
        paymentMethod = sharedPreferenceHelper.getStringPreference("paymentMethod");
        requestStatus = sharedPreferenceHelper.getStringPreference("requestStatus");
        requestFee = sharedPreferenceHelper.getStringPreference("requestFee");
        mechanicStatus = sharedPreferenceHelper.getStringPreference("mechanicStatus");
        orderTime = sharedPreferenceHelper.getStringPreference("orderTime");
        serviceType = sharedPreferenceHelper.getStringPreference("serviceType");


        checkRequestStatusFirebase(customerPhone, requestKey);

        Log.d("RequestInfo", "mycarMake" + carMake);
        Log.d("RequestInfo", "mypickupAddress" + pickupAddress);
        Log.d("RequestInfo", "mypickupLatitude" + pickupLatitude);
        Log.d("RequestInfo", "mypickupLongitude" + pickupLongitude);
        Log.d("RequestInfo", "mydestinationLatitude" + destinationLatitude);
        Log.d("RequestInfo", "mydestinationLongitude" + destinationLongitude);
        Log.d("RequestInfo", "myamount" + paymentAmount);
        Log.d("RequestInfo", "mycarType" + carType);
        Log.d("RequestInfo", "myrequestKey" + requestKey);
        Log.d("RequestInfo", "mycustomerName" + customerName);
        Log.d("RequestInfo", "mycustomerPhone" + customerPhone);
        Log.d("RequestInfo", "mymechanicStatus" + mechanicStatus);
        Log.d("RequestInfo", "myorderTime" + orderTime);

        AcceptRequestDialog();

        origin = new LatLng(Double.parseDouble(pickupLatitude), Double.parseDouble(pickupLongitude));

        tvDriverName.setText(customerName);
        tvOrderPrice.setText("₦" + Utils.moneyFormat(paymentAmount));

        selectedDriver = new LatLng(mylatitude, mylongitude);

        Log.d("SelectedDriver", selectedDriver.toString());

//        Glide.with(this).load(driverImage).into(ivDriverImage);

        ivDriverMobile.setOnClickListener(view -> callDriver());

        tvRideModel.setText(carMake.concat(" (").concat(carType).concat(")"));
        tvPickupLocation.setText(pickupAddress);
        tvDestinationLocation.setText(pickupAddress);
        tvDestination.setText(pickupAddress);

        mLayout = findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(view -> mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED));

        bCancelBooking.setOnClickListener(view -> cancelBooking());

        Log.d(TAG, "onCreate: Towing status " + mechanicStatus);

        bArrival.setOnClickListener(view -> {
            Log.d(TAG, "onCreate: " + mechanicStatus);
            if (mechanicStatus.contentEquals("Accepted")) {
                indicateArrival(mechanicRequest);
            } else if (mechanicStatus.contentEquals("MechanicArrived"))
                indicateStartTowing(mechanicRequest);
            else if (mechanicStatus.contentEquals("OngoingTowing")) {

                progressDialog.setTitle("Please wait");
                progressDialog.setMessage("Completing your Request");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                sendRequestToDB(mechanicRequest);

            }

        });
    }

    private void sendRequestToDB(CarRepair mechanicRequest) {

        ClientAPI api = APIService.createDriverService(ClientAPI.class);

        Call<TowinRequestResponse> call = api.sendMechanicRequestToDB(mechanicRequest);

        call.enqueue(new Callback<TowinRequestResponse>() {
            @Override
            public void onResponse(Call<TowinRequestResponse> call, retrofit2.Response<TowinRequestResponse> response) {

                progressDialog.dismiss();
                if (response.body().getMessage().contentEquals("Request Successful")) {

                    meUser.setWallet(Integer.parseInt(response.body().getWallet()));

                    helper.setLoggedInUser(meUser);

                    indicateCompletedTowing(mechanicRequest);
                } else {
                    Toast.makeText(MechanicTransitActivity.this, "Failed to Cancel Ride", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<TowinRequestResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MechanicTransitActivity.this, "Network Error. Please try again", Toast.LENGTH_LONG).show();
            }
        });


    }


    private void cancelBooking() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.cancel_towing_service, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setPositiveButton("Cancel Request", (dialog, whichButton) -> {

            mechanicRequest.setMechanicStatus("Cancelled");
            updateRequestToFirebase(mechanicRequest);

//                failedRideRequest(token, status);
        });
        dialogBuilder.setNegativeButton("Continue", (dialog, whichButton) -> {
            //pass
            dialog.dismiss();
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }


    @SuppressLint("NewApi")
    private void indicateCompletedTowing(CarRepair request) {

//        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
//        String dateAndTime = null;
//            dateAndTime = df.format(Calendar.getInstance().getTime());

//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm a");
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm a");
//        LocalDateTime now = LocalDateTime.now();
//        String dateAndTime = dtf.format(now);

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
        Date date = new Date();
        String dateAndTime = formatter.format(date);

        String topic = "/topics/" + request.getCustomerPhone(); //topic has to match what the receiver subscribed to

        request.setMechanicStatus("Completed");
        mechanicStatus = "Completed";

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();

        try {
            notifcationBody.put("title", "Vehicle Towing Request");
            notifcationBody.put("message", "Towing Driver has just Arrived");

            notifcationBody.put("carMake", request.getCarMake());
            notifcationBody.put("pickupAddress", request.getPickupAddress());
            notifcationBody.put("destinationAddress", request.getPickupAddress());
            notifcationBody.put("pickupLatitude", request.getPickupLatitude());
            notifcationBody.put("pickupLongitude", request.getPickupLongitude());
            notifcationBody.put("destinationLatitude", request.getPickupLatitude());
            notifcationBody.put("destinationLongitude", request.getPickupLongitude());
            notifcationBody.put("amount", request.getPaymentAmount());
            notifcationBody.put("carType", request.getVehicleType());
            notifcationBody.put("requestKey", requestKey);
            notifcationBody.put("customerName", request.getCustomerName());
            notifcationBody.put("customerPhone", request.getCustomerPhone());
            notifcationBody.put("mechanicStatus", request.getMechanicStatus());
            notifcationBody.put("orderTime", dateAndTime);

            notification.put("to", topic);
            notification.put("data", notifcationBody);
            Log.e("TAG", "try");
        } catch (JSONException e) {
            Log.e("TAG", "onCreate: " + e.getMessage());
        }

//        sendNotification(notification);
        updateRequestToFirebase(request);

    }

    @SuppressLint("NewApi")
    private void indicateStartTowing(CarRepair request) {

//        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
//        String dateAndTime = null;
//            dateAndTime = df.format(Calendar.getInstance().getTime());

//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm a");
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm a");
//        LocalDateTime now = LocalDateTime.now();
//        String dateAndTime = dtf.format(now);

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
        Date date = new Date();
        String dateAndTime = formatter.format(date);

        String topic = "/topics/" + request.getCustomerPhone(); //topic has to match what the receiver subscribed to

        request.setMechanicStatus("OngoingTowing");
        mechanicStatus = "OngoingTowing";

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();

        try {
            notifcationBody.put("title", "Mechanic Service Request");
            notifcationBody.put("message", "Your Mechanic has just Arrived");

            notifcationBody.put("carMake", request.getCarMake());
            notifcationBody.put("pickupAddress", request.getPickupAddress());
            notifcationBody.put("destinationAddress", request.getPickupAddress());
            notifcationBody.put("pickupLatitude", request.getPickupLatitude());
            notifcationBody.put("pickupLongitude", request.getPickupLongitude());
            notifcationBody.put("destinationLatitude", request.getPickupLatitude());
            notifcationBody.put("destinationLongitude", request.getPickupLongitude());
            notifcationBody.put("amount", request.getPaymentAmount());
            notifcationBody.put("carType", request.getVehicleType());
            notifcationBody.put("requestKey", requestKey);
            notifcationBody.put("customerName", request.getCustomerName());
            notifcationBody.put("customerPhone", request.getCustomerPhone());
            notifcationBody.put("mechanicStatus", request.getMechanicStatus());
            notifcationBody.put("orderTime", dateAndTime);

            notification.put("to", topic);
            notification.put("data", notifcationBody);
            Log.e("TAG", "try");
        } catch (JSONException e) {
            Log.e("TAG", "onCreate: " + e.getMessage());
        }

//        sendNotification(notification);
        updateRequestToFirebase(request);

    }

    @SuppressLint("NewApi")
    private void indicateMechanicArrived(CarRepair request) {

//        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
//        String dateAndTime = null;
//            dateAndTime = df.format(Calendar.getInstance().getTime());

//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm a");
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm a");
//        LocalDateTime now = LocalDateTime.now();
//        String dateAndTime = dtf.format(now);

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
        Date date = new Date();
        String dateAndTime = formatter.format(date);

        String topic = "/topics/" + request.getCustomerPhone(); //topic has to match what the receiver subscribed to

        request.setMechanicStatus("OngoingTowing");
        mechanicStatus = "OngoingTowing";


        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();

        try {
            notifcationBody.put("title", "Vehicle Towing Request");
            notifcationBody.put("message", "Towing Driver has just Arrived");

            notifcationBody.put("carMake", request.getCarMake());
            notifcationBody.put("pickupAddress", request.getPickupAddress());
            notifcationBody.put("destinationAddress", request.getPickupAddress());
            notifcationBody.put("pickupLatitude", request.getPickupLatitude());
            notifcationBody.put("pickupLongitude", request.getPickupLongitude());
            notifcationBody.put("destinationLatitude", request.getPickupLatitude());
            notifcationBody.put("destinationLongitude", request.getPickupLongitude());
            notifcationBody.put("amount", request.getPaymentAmount());
            notifcationBody.put("carType", request.getVehicleType());
            notifcationBody.put("requestKey", requestKey);
            notifcationBody.put("customerName", request.getCustomerName());
            notifcationBody.put("customerPhone", request.getCustomerPhone());
            notifcationBody.put("mechanicStatus", request.getMechanicStatus());
            notifcationBody.put("orderTime", dateAndTime);

            notification.put("to", topic);
            notification.put("data", notifcationBody);
            Log.e("TAG", "try");
        } catch (JSONException e) {
            Log.e("TAG", "onCreate: " + e.getMessage());
        }

//        sendNotification(notification);
        updateRequestToFirebase(request);

    }

    @SuppressLint("NewApi")
    private void indicateArrival(CarRepair request) {

//        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
//        String dateAndTime = null;
//            dateAndTime = df.format(Calendar.getInstance().getTime());

//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE, d MMM yyyy HH:mm a");
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm a");
//        LocalDateTime now = LocalDateTime.now();
//        String dateAndTime = dtf.format(now);

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm a");
        Date date = new Date();
        String dateAndTime = formatter.format(date);

        String topic = "/topics/" + request.getCustomerPhone(); //topic has to match what the receiver subscribed to

        request.setMechanicStatus("MechanicArrived");
        mechanicStatus = "MechanicArrived";

        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();

        try {
            notifcationBody.put("title", "Vehicle Towing Request");
            notifcationBody.put("message", "Towing Driver has just Arrived");

            notifcationBody.put("vehicleType", request.getVehicleType());
            notifcationBody.put("carMake", request.getCarMake());
            notifcationBody.put("vehicleColor", request.getVehicleColor());
            notifcationBody.put("regNumber", request.getRegNumber());
            notifcationBody.put("carFault", request.getCarFault());
            notifcationBody.put("description", request.getDescription());
            notifcationBody.put("requestKey", request.getRequestKey());
            notifcationBody.put("customerName", request.getCustomerName());
            notifcationBody.put("customerPhone", request.getCustomerPhone());
            notifcationBody.put("customerImage", request.getCustomerImage());
            notifcationBody.put("requestTime", request.getRequestTime());
            notifcationBody.put("paymentStatus", request.getPaymentStatus());
            notifcationBody.put("pickupLongitude", request.getPickupLongitude());
            notifcationBody.put("pickupLatitude", request.getPickupLatitude());
            notifcationBody.put("pickupAddress", request.getPickupAddress());
            notifcationBody.put("paymentAmount", request.getPaymentAmount());
            notifcationBody.put("paymentMethod", request.getPaymentMethod());
            notifcationBody.put("requestStatus", request.getRequestStatus());
            notifcationBody.put("requestFee", request.getRequestFee());
            notifcationBody.put("mechanicStatus", request.getMechanicStatus());
            notifcationBody.put("orderTime", request.getOrderTime());
            notifcationBody.put("serviceType", request.getServiceType());

            notification.put("to", topic);
            notification.put("data", notifcationBody);
            Log.e("TAG", "try");
        } catch (JSONException e) {
            Log.e("TAG", "onCreate: " + e.getMessage());
        }

        sendNotification(notification);
        updateRequestToFirebase(request);

    }


    private void sendNotification(JSONObject notification) {
        Log.e("TAG", "sendNotification");

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, FCM_API,
                notification, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("NotificationTAG", "onResponse: " + response);
                // Some code

            }
        }, error -> {
            //handle errors
            Toast.makeText(this, "Request error", Toast.LENGTH_LONG).show();
            Log.i("TAG", "onErrorResponse: Didn't work");
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        requestQueue.add(request);
    }


    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
//            AcceptRequestDialog();
            finish();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng origin = new LatLng(9.061121, 7.499863);
        mMap.addMarker(new MarkerOptions().position(origin).title("Your Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(origin, 15));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        mMap.setMyLocationEnabled(true);

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
                    .getString(R.string.style_json)));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }

//
//        mDriverMarker = mMap.addMarker(new MarkerOptions()
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mechanic_icon_tracking))
//                .position(driverLocation)
//                .title("key"));
//        mDriverMarker.setTag("key");


//        showPolyLine();
    }

    private void showCurrentDriver() {

        carIcon = R.drawable.mechanic_icon_tracking;

        final DatabaseReference driversLocation = FirebaseDatabase.getInstance().getReference().child(meUser.getRole().contentEquals("mechanic") ? "mechanicLocations" : "driverLocations");

        final GeoFire geofire = new GeoFire(driversLocation);

        GeoQuery geoQuery = geofire.queryAtLocation(new GeoLocation(selectedDriver.latitude, selectedDriver.longitude), 16);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, GeoLocation location) {

                Log.d(TAG, "onKeyEntered: " + location.latitude + " " + location.longitude);

                final LatLng mydriverLocation = new LatLng(location.latitude, location.longitude);

                MechanicTransitActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDriverMarker = mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.mechanic_icon_tracking))
                                .position(mydriverLocation)
                                .title(key));
                        mDriverMarker.setTag(key);

                        selectedDriver = mydriverLocation;
                    }
                });


            }

            @Override
            public void onKeyExited(String key) {

                geofire.removeLocation(key);
                Log.d(TAG, "run: marker exited");

                MechanicTransitActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mDriverMarker != null) {
                            mDriverMarker.remove();
                            Log.d(TAG, "run: marker exited");
                        }
                        selectedDriver = driverLocation;
                    }
                });

            }

            @Override
            public void onKeyMoved(String key, final GeoLocation location) {

                MechanicTransitActivity.this.runOnUiThread(() -> {
                    LatLng driverPostion = new LatLng(location.latitude, location.longitude);

                    String url = getDirectionsUrl(origin, driverPostion);

                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);

                    animateMarkerNew(selectedDriver, driverPostion, mDriverMarker);

                    Log.d(TAG, "onKeyMoved: " + location.latitude + " " + location.longitude);

//                        marketIt.setPosition(new LatLng(location.latitude, location.longitude));
                    selectedDriver = driverPostion;
                });

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

    }

    private void animateMarkerNew(final LatLng startPosition, final LatLng destination, final Marker marker) {

        if (marker != null) {

            final LatLng endPosition = new LatLng(destination.latitude, destination.longitude);

            final float startRotation = marker.getRotation();
            final LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(2000); // duration 3 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
                        marker.setRotation(getBearing(startPosition, new LatLng(destination.latitude, destination.longitude)));
                    } catch (Exception ex) {
                        //I don't care atm..
                    }
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    // if (mMarker != null) {
                    // mMarker.remove();
                    // }
                    // mMarker = googleMap.addMarker(new MarkerOptions().position(endPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));

                }
            });
            valueAnimator.start();
        }
    }

    private void AcceptRequestDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.mechanic_booking_confirmation_dialog, null);

        TextView tvCustomerName = dialogView.findViewById(R.id.tvCustomerName);
        TextView tvCarMake = dialogView.findViewById(R.id.tvCarMake);
        TextView tvVehicleFault = dialogView.findViewById(R.id.tvVehicleFault);
        TextView tvCarType = dialogView.findViewById(R.id.tvCarType);
        TextView tvPickupLocation = dialogView.findViewById(R.id.tvPickupLocation);
        Button bAccept = dialogView.findViewById(R.id.bAccept);
        Button bCancel = dialogView.findViewById(R.id.bCancel);

        tvCustomerName.setText(customerName);
        tvPickupLocation.setText(pickupAddress);
        tvCarType.setText(carType);
        tvVehicleFault.setText(carFault);
        tvCarMake.setText(carMake);

        dialogBuilder.setView(dialogView);

        bAccept.setOnClickListener(v -> {

            CarRepair request = new CarRepair();

            request.setCustomerName(customerName);
            request.setCustomerPhone(customerPhone);
            request.setMechanicID(meUser.getId().toString());
            request.setMechanicName(meUser.getFullname());
            request.setMechanicPhone(meUser.getPhone());
            request.setMechanicImage(meUser.getImage());
            request.setPickupAddress(pickupAddress);
            request.setPickupLatitude(pickupLatitude);
            request.setPickupLongitude(pickupLongitude);
            request.setVehicleType(carType);
            request.setCarMake(carMake);
            request.setRequestKey(requestKey);
            request.setPaymentAmount(paymentAmount);
            request.setPaymentMethod(paymentType);
            request.setPaymentStatus("Paid");
            request.setMechanicStatus("Accepted");
            request.setOrderTime(orderTime);
            request.setMechanicLatitude(String.valueOf(mylatitude));
            request.setMechanicLongitude(String.valueOf(mylongitude));
            request.setVehicleType(vehicleType);
            request.setVehicleColor(vehicleColor);
            request.setRegNumber(regNumber);
            request.setCarFault(carFault);
            request.setDescription(description);
            request.setCustomerImage(customerImage);
            request.setServiceType("mechanic");

            mechanicStatus = "Accepted";
            checkOrderStatus(request);

        });

        bCancel.setOnClickListener(v -> {
            startActivity(new Intent(MechanicTransitActivity.this, HomeActivity.class));
            finish();
        });

        b = dialogBuilder.create();
        b.setCancelable(false);
        b.setCanceledOnTouchOutside(false);
        b.show();
    }

    private void checkOrderStatus(final CarRepair request) {

        Log.d(TAG, "checkOrderStatus: customer phone: " + request.toString());

        databaseReference.child("car_repair").child(request.getCustomerPhone()).child(request.getRequestKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("mechanicStatus").getValue().toString().contentEquals("Pending")) {
                        mechanicRequest = request;
                        Log.d(TAG, "onCreate: Towing status " + mechanicStatus);
                        b.dismiss();
                        updateRequestToFirebase(request);

                    } else {

                        Toast.makeText(MechanicTransitActivity.this, "Request No Longer Available", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MechanicTransitActivity.this, MainActivity.class));
                        finish();
                        Log.d(TAG, "onDataChange: Request no longer Available. TowStatus: " +
                                dataSnapshot.child("mechanicStatus").getKey() + " TowValue: " + dataSnapshot.child("mechanicStatus").getValue().toString());
                    }
                } else {
                    Toast.makeText(MechanicTransitActivity.this, "Request No Longer Available", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MechanicTransitActivity.this, MainActivity.class));
                    finish();
                    Log.d(TAG, "onDataChange: No such request available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String APIKEY = "key=AIzaSyBcMm2JMyyN1IryTX48BV55iV-4n9B22P0";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + APIKEY;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }


    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);

//                Log.d(TAG, "doInBackground: " + data);
            } catch (Exception e) {
//                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            parserTask.execute(result);

        }
    }


    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private void updateRequestToFirebase(final CarRepair request) {
        Log.d(TAG, "updateRequestToFirebase: " + request.getCarMake());
//        String phone = "0" + request.getCustomerPhone().substring(4, 14);
        databaseReference.child("car_repair").child(request.getCustomerPhone()).child(request.getRequestKey()).setValue(request).addOnCompleteListener(task -> {

            if (mLayout != null &&
                    (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }

            if (request.getMechanicStatus().contentEquals("OngoingTowing")) {
                bArrival.setText("Complete");
                Toast.makeText(MechanicTransitActivity.this, "Towing is Ongoing", Toast.LENGTH_LONG).show();
                Log.d(TAG, "updateRequestToFirebase: Towing status " + mechanicStatus);

                getSupportActionBar().setTitle("Vehicle Towing in Progress");

            } else if (request.getMechanicStatus().contentEquals("MechanicArrived")) {
                bArrival.setText("Complete");
                Toast.makeText(MechanicTransitActivity.this, "You have arrived Service Location", Toast.LENGTH_LONG).show();

                Gson gson = new Gson();
                String myjson = gson.toJson(request, new TypeToken<CarRepair>() {
                }.getType());
                Intent successIntent = new Intent(MechanicTransitActivity.this, MechanicSuccess.class);
                successIntent.putExtra("request", myjson);
                startActivity(successIntent);
                finish();

            } else {
                getSupportActionBar().setTitle("Ride to Location");
                Toast.makeText(MechanicTransitActivity.this, "Request Accepted", Toast.LENGTH_LONG).show();
            }
            databaseReference.child("driver_requests").child(request.getMechanicPhone()).child(requestKey).setValue(request);

            Log.d(TAG, "updateRequestToFirebase: Towing status " + mechanicStatus);

            selectedDriver = new LatLng(Double.parseDouble(request.getMechanicLatitude()), Double.parseDouble(request.getMechanicLongitude()));

            if (!request.getMechanicStatus().contentEquals("MechanicArrived")) {

                mCustomerMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(request.getPickupLatitude()), Double.parseDouble(request.getPickupLongitude()))).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start)));
                mDriverMarker = mMap.addMarker(new MarkerOptions().position(selectedDriver).icon(BitmapDescriptorFactory.fromResource(R.drawable.mechanic_icon_tracking)));
            }
            if (request.getMechanicStatus().contentEquals("OngoingTowing")) {
                trackPickupDestination(request);
            } else if (request.getMechanicStatus().contentEquals("Accepted"))
                trackDriver();

        });
    }

    public void trackDriver() {

        geoQuery = geoFire.queryAtLocation(new GeoLocation(mylatitude, mylongitude), 10);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, final GeoLocation location) {

                System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));

                if (key.contentEquals(meUser.getPhone())) {

                    MechanicTransitActivity.this.runOnUiThread(() -> {

                        bArrival.setVisibility(View.VISIBLE);

                        mMap.clear();
                        markers.clear();

                        Log.d(TAG, "run: " + key + " " + pickupAddress);

                        selectedDriver = new LatLng(location.latitude, location.longitude);
                        origin = new LatLng(Double.parseDouble(mechanicRequest.getPickupLatitude()),
                                Double.parseDouble(mechanicRequest.getPickupLongitude()));

                        mCustomerMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(pickupLatitude), Double.parseDouble(pickupLongitude))).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start)));
                        mDriverMarker = mMap.addMarker(new MarkerOptions().position(selectedDriver).icon(BitmapDescriptorFactory.fromResource(R.drawable.mechanic_icon_tracking)));

                        markers.add(mCustomerMarker);
                        markers.add(mDriverMarker);

                        String url = getDirectionsUrl(selectedDriver, origin);

                        builder = new LatLngBounds.Builder();

                        DownloadTask downloadTask = new DownloadTask();

                        downloadTask.execute(url);

                        for (Marker marker : markers) {
                            builder.include(marker.getPosition());
                        }
                        LatLngBounds bounds = builder.build();

                        int padding = 120; // offset from edges of the map in pixels
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        mMap.animateCamera(cu);
                    });
                }
            }

            @Override
            public void onKeyExited(String key) {
                System.out.println(String.format("Key %s is no longer in the search area", key));
            }

            @Override
            public void onKeyMoved(String key, final GeoLocation location) {
                System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));

                Log.d(TAG, "onLocationResult: " + location.latitude + " longitude: " + location.longitude);

                MechanicTransitActivity.this.runOnUiThread(() -> {

                    LatLng newDriverLocation = new LatLng(location.latitude, location.longitude);

                    String url = getDirectionsUrl(newDriverLocation, origin);

                    builder = new LatLngBounds.Builder();

                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);

                    Log.d(TAG, "old driver latitude: " + selectedDriver.latitude + " new driver latitude: " + newDriverLocation.latitude);

                    animateMarkerNew(selectedDriver, newDriverLocation, mDriverMarker);

                    markers.add(1, mDriverMarker);

                    for (Marker marker : markers) {
                        builder.include(marker.getPosition());
                    }
                    LatLngBounds bounds = builder.build();

                    int padding = 120; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMap.animateCamera(cu);
                    selectedDriver = newDriverLocation;

//                        origin = newDriverLocation;
                });
            }

            @Override
            public void onGeoQueryReady() {
                System.out.println("All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                System.err.println("There was an error with this query: " + error);
            }
        });

    }

    public void trackPickupDestination(CarRepair request) {

        geoQuery = geoFire.queryAtLocation(new GeoLocation(selectedDriver.latitude, selectedDriver.longitude), 10);

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, final GeoLocation location) {

                System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, location.latitude, location.longitude));

                if (key.contentEquals(meUser.getPhone())) {

                    MechanicTransitActivity.this.runOnUiThread(() -> {

                        bArrival.setVisibility(View.VISIBLE);

                        mMap.clear();
                        markers.clear();

                        Log.d(TAG, "run: " + key + " " + request.getPickupAddress());

                        selectedDriver = new LatLng(location.latitude, location.longitude);

                        mCustomerMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(request.getPickupLatitude()), Double.parseDouble(request.getPickupLongitude()))).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start)));
                        mDriverMarker = mMap.addMarker(new MarkerOptions().position(selectedDriver).icon(BitmapDescriptorFactory.fromResource(R.drawable.mechanic_icon_tracking)));

                        markers.add(mCustomerMarker);
                        markers.add(mDriverMarker);
                        origin = new LatLng(Double.parseDouble(request.getPickupLatitude()), Double.parseDouble(request.getPickupLongitude()));

                        LatLng newDriverLocation = new LatLng(location.latitude, location.longitude);

                        builder = new LatLngBounds.Builder();

                        String url = getDirectionsUrl(selectedDriver, origin);

                        DownloadTask downloadTask = new DownloadTask();

                        downloadTask.execute(url);

                        markers.add(1, mDriverMarker);

                        for (Marker marker : markers) {
                            builder.include(marker.getPosition());
                        }
                        LatLngBounds bounds = builder.build();

                        int padding = 120; // offset from edges of the map in pixels
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        mMap.animateCamera(cu);

                    });
                }
            }

            @Override
            public void onKeyExited(String key) {
                System.out.println(String.format("Key %s is no longer in the search area", key));
            }

            @Override
            public void onKeyMoved(String key, final GeoLocation location) {
                System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, location.latitude, location.longitude));

                Log.d(TAG, "onLocationResult: " + location.latitude + " longitude: " + location.longitude);

                MechanicTransitActivity.this.runOnUiThread(() -> {

                    LatLng newDriverLocation = new LatLng(location.latitude, location.longitude);

                    String url = getDirectionsUrl(newDriverLocation, origin);

                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);

                    animateMarkerNew(selectedDriver, newDriverLocation, mDriverMarker);

                    markers.add(1, mDriverMarker);

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (Marker marker : markers) {
                        builder.include(marker.getPosition());
                    }
                    LatLngBounds bounds = builder.build();

                    int padding = 120; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMap.animateCamera(cu);
                    selectedDriver = newDriverLocation;
                });
            }

            @Override
            public void onGeoQueryReady() {
                System.out.println("All initial data has been loaded and events have been fired!");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                System.err.println("There was an error with this query: " + error);
            }
        });
    }

    private interface LatLngInterpolatorNew {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements MechanicTransitActivity.LatLngInterpolatorNew {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }


    public void callDriver() {

        new iOSDialogBuilder(MechanicTransitActivity.this)
                .setTitle("Call Driver")
                .setSubtitle("Do you want to call " + driverUsername + "?")
                .setBoldPositiveLabel(true)
                .setCancelable(false)
                .setPositiveListener("Call", new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + driverPhone));
                        startActivity(intent);
                        Toast.makeText(MechanicTransitActivity.this, "Calling the driver", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeListener("Cancel", new iOSDialogClickListener() {
                    @Override
                    public void onClick(iOSDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build().show();
    }


//    public void showPolyLine() {
////        if (MainActivityTest.mainPolyline != null) {
////
////            MainActivityTest.mainPolyline.remove();
////            mMap.clear();
////        }
//
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//
//        try {
//            List<Address> addresses = geocoder.getFromLocation(origin.latitude, origin.longitude, 1);
//
//            if (addresses != null) {
//                Address returnedAddress = addresses.get(0);
//                StringBuilder strReturnedAddress = new StringBuilder();
//                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
//                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("");
//                }
//                Log.d("MainAddress", strReturnedAddress.toString());
//            } else {
//                Log.d("No Addresss", "No Address returned!");
//            }
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            Log.d("Address Cannot", "Canont get Address!");
//        }
//        if (MainActivityTest.lastDirection.isOK()) {
//
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(origin)
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start));
//            Log.d("TimeString ", MainActivityTest.rawBodyMain);
//
//            Route route = MainActivityTest.lastDirection.getRouteList().get(0);
//
//            mMap.addMarker(new MarkerOptions().position(origin)
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_start))
//                    .title(MainActivityTest.address));
//
//            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
//
////            MainActivityTest.mainPolyline = mMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.BLACK));
//            MainActivityTest.setCameraWithCoordinationBounds(route);
//
//            showCurrentDriver();
//        }
//
//    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    private void showCurrentDriver(String driverPhone) {

        carIcon = R.drawable.mechanic_icon_tracking;

        final DatabaseReference driversLocation = FirebaseDatabase.getInstance().getReference().child(meUser.getRole().contentEquals("mechanic") ? "mechanicLocations" : "driverLocations").child(driverPhone);

        final GeoFire geofire = new GeoFire(driversLocation);

        GeoQuery geoQuery = geofire.queryAtLocation(new GeoLocation(9.067657, 7.451841), radius);

//        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, GeoLocation location) {

                Log.d(TAG, "Key Entered");

                MechanicTransitActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mDriverMarker = mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(carIcon))
                                .position(selectedDriver)
                                .title(key));
                        mDriverMarker.setTag(key);
                    }
                });


            }

            @Override
            public void onKeyExited(String key) {

                geofire.removeLocation(key);
                Log.d(TAG, "KeyRemoved");

            }

            @Override
            public void onKeyMoved(String key, final GeoLocation location) {

                Log.d(TAG, "KeyMoved" + location.latitude + " " + location.longitude);
                final LatLng driverPostion = new LatLng(location.latitude, location.longitude);

                final Location mylocation = new Location("");
                mylocation.setLatitude(location.latitude);
                mylocation.setLongitude(location.longitude);

                MechanicTransitActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

//                        mDriverMarker.setPosition(driverPostion);


//                        animateMarkerNew(selectedDriver, driverPostion, mDriverMarker);


                    }
                });


//                        marketIt.setPosition(new LatLng(location.latitude, location.longitude));
                selectedDriver = driverPostion;
            }

            @Override
            public void onGeoQueryReady() {
                Log.d(TAG, "Key Ready");

//                    radius++;
//                    showCurrentDriver("kslfjlks");


            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.d(TAG, "Key Error");

            }
        });

    }

    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        double v2 = Math.toDegrees(Math.atan(lng / lat));
        if (begin.latitude < end.latitude && begin.longitude < end.longitude) {
            double v1 = v2;
            final double v = v1;
            return (float) v;
        } else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - v2) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (v2 + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - v2) + 270);
        return -1;
    }

    private void checkRequestStatusFirebase(String customerPhone, String requestKey) {

        databaseReference.child("car_repair").child(customerPhone).child(requestKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    mechanicRequest = dataSnapshot.getValue(CarRepair.class);
                    Log.d(TAG, "onDataChange: request exists" + mechanicRequest.getMechanicStatus());

                    if (mechanicRequest.getMechanicStatus().contentEquals("Cancelled")) {

                        Log.d(TAG, "onDataChange: request available and accepted " + dataSnapshot.child("mechanicStatus"));

                        Toast.makeText(MechanicTransitActivity.this, "Request has been Cancelled", Toast.LENGTH_SHORT).show();

                        Gson gson = new Gson();
                        String myjson = gson.toJson(mechanicRequest);
                        Intent transitIntent = new Intent(MechanicTransitActivity.this, HomeActivity.class);
                        startActivity(transitIntent);
                        finish();
                    }
                } else {
//                    Toast.makeText(TransitActivity.this, "Request No Longer Available", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MechanicTransitActivity.this, HomeActivity.class));
                    finish();
                    Log.d(TAG, "onDataChange: No such request available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        databaseReference.child("car_repair").child(meUser.getPhone()).child(requestKey).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                if (dataSnapshot.exists()) {
//                    Log.d(TAG, "onDataChange: request exists");
//
//                    if (dataSnapshot.child("mechanicStatus").getValue().toString().contentEquals("Accepted")) {
//                        Log.d(TAG, "onDataChange: request available and accepted" + dataSnapshot.child("mechanicStatus"));
//
//                        Gson gson = new Gson();
//                        String myjson = gson.toJson(request);
//                        Intent transitIntent = new Intent(TransitActivity.this, TransitActivity.class);
//                        transitIntent.putExtra("request", myjson);
//                        startActivity(transitIntent);
//                        finish();
//                    }
//                } else {
//                    Toast.makeText(TransitActivity.this, "Request No Longer Available", Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(TransitActivity.this, MainActivity.class));
//                    finish();
//                    Log.d(TAG, "onDataChange: No such request available");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionJSONParser parser = new DirectionJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String distanceValue = "";
            String duration = "";
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);


                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        distanceValue = (String) point.get("distanceValue");

                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                    builder.include(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);
            }

            if (polyline != null)
                polyline.remove();// Drawing polyline in the Google Map for the i-th route

            polyline = mMap.addPolyline(lineOptions);

            for (Marker marker : markers) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();

            int padding = 120; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            tvEstTime.setText(duration.concat(""));

            mMap.animateCamera(cu);
//            tvTime.setText(duration);
//            towingAmount = (Integer.parseInt(distanceValue) / 1000) * 200;
//            request.setAmount(towingAmount);
//            tvAmount.setText("₦".concat(Utils.moneyFormat(String.valueOf(towingAmount))));
//
//            clBottomLayout.setVisibility(View.VISIBLE);
//            clPriceLayout.setVisibility(View.VISIBLE);
        }
    }

}