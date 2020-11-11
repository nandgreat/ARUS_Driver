package com.silexsecure.arusdriver.navfragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.silexsecure.arusdriver.R;

import java.text.DateFormat;
import java.util.Date;


public class TrackSingleOrderActivity extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static String TAG = "TrackOrderFragmentTAG";

    MapView mMapView;
    private GoogleMap googleMap;
    private int carIcon;
    private LatLng selectedDriver;
    private Marker mDriverMarker;

    private GeoFire geoFire;
    private DatabaseReference ref;

    Button btnFusedLocation;
    TextView tvLocation;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;

    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private boolean isOnline = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_single_order);

        mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        ref = FirebaseDatabase.getInstance().getReference("availableDrivers");
        geoFire = new GeoFire(ref);

        selectedDriver = new LatLng(9.067657, 7.451841);

        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

//                showCurrentDriver();

//                addLocation("availableDrivers");
            }
        });
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
//        stopLocationUpdates();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
//        stopLocationUpdates();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
//        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    private static float computeRotation(float fraction, float start, float end) {
        float normalizeEnd = end - start; // rotate start to 0
        float normalizedEndAbs = (normalizeEnd + 360) % 360;

        float direction = (normalizedEndAbs > 180) ? -1 : 1; // -1 = anticlockwise, 1 = clockwise
        float rotation;
        if (direction > 0) {
            rotation = normalizedEndAbs;
        } else {
            rotation = normalizedEndAbs - 360;
        }

        float result = fraction * rotation + start;
        return (result + 360) % 360;
    }


    protected void startLocationUpdates() {
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
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }


    private interface LatLngInterpolatorNew {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolatorNew {
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

    private int radius = 1000000000;

    private void showCurrentDriver(LatLng mylocation) {

        carIcon = R.drawable.truck_icon;

        final DatabaseReference driversLocation = FirebaseDatabase.getInstance().getReference().child("availableDrivers");

        final GeoFire geofire = new GeoFire(driversLocation);

        GeoQuery geoQuery = geofire.queryAtLocation(new GeoLocation(mylocation.latitude, mylocation.longitude), radius);

//        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(final String key, GeoLocation location) {

                Log.d(TAG, "Key Entered");

                TrackSingleOrderActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mDriverMarker = googleMap.addMarker(new MarkerOptions()
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

                TrackSingleOrderActivity.this.runOnUiThread(() -> {

//                        mDriverMarker.setPosition(driverPostion);


//                        animateMarkerNew(selectedDriver, driverPostion, mDriverMarker);


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


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());

    }

    private void addLocation(String availableDrivers) {

        geoFire.setLocation("07031676998", new GeoLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), (key, error) -> {

            if (error != null) {
                System.err.println("There was an error saving the location to GeoFire: " + error);
            } else {
                System.out.println("Location saved on server successfully!");
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        Log.d(TAG, "Firing onLocationChanged................................" + mCurrentLocation.getLatitude() + " " + mCurrentLocation.getLongitude());
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        // For showing a move to my location button
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
        googleMap.setMyLocationEnabled(true);

        // For dropping a marker at a point on the Map
        LatLng sydney = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//                googleMap.addMarker(new MarkerOptions().position(sydney).title("Silex Secure").snippet("Marker Description"));

        // For zooming automatically to the location of the marker
//        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
//        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        googleMap.isMyLocationEnabled();
//        googleMap.getUiSettings().isMyLocationButtonEnabled();

        if (isOnline)
            showCurrentDriver(sydney);

//        addLocation("availableDrivers");
//        updateUI();
    }
}
