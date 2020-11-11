package com.silexsecure.arusdriver.util;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyApplication extends Application {

    private static MyApplication mInstance;
//    private PrefManager prefManager;

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public static JSONObject json = new JSONObject();

    public static String token, userId;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
//        prefManager = new PrefManager(MyApplication.this);
//
//        if (prefManager.getCard() == null) {
//            createCashIfNotExit();
//        }

        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            // connected to the internet
            switch (activeNetwork.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    // connected to wifi
                    Log.d("NetworkDetails", "Connected to Wifi");
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    // connected to mobile data
                    Log.d("NetworkDetails", "Connected to Mobile Data");
                    break;
                default:
                    break;
            }
        } else {
            // not connected to the internet
        }
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        telephonyManager.getDeviceId();

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);


    }

//    public MainActivityTest mainActivity;

//    private void createCashIfNotExit() {
//        ArrayList<CardDetails> cash = new ArrayList<CardDetails>();
//        CardDetails cardDetail = new CardDetails();
//        cardDetail.setCash(true);
//        cardDetail.setCardNunmber("");
//        cardDetail.setCardName("");
//        cardDetail.setCardExpiringDate("");
//        cardDetail.setCardCvv("");
//        cash.add(cardDetail);
//
//        CardList cardList = new CardList();
//        cardList.setCardDetails(cash);
//        prefManager.setCard(cardList);
//    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }


    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
//            editLocation.setText("");
//            pb.setVisibility(View.INVISIBLE);

            Toast.makeText(
                    getApplicationContext(),
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v("LocationDetails", longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v("LocationDetails", latitude);

            /*------- To get city name from coordinates -------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = new ArrayList<>();
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
            String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                    + cityName;

            Log.d("CityDetails", s);
//            editLocation.setText(s);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}