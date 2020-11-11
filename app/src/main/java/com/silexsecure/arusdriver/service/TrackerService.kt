package com.silexsecure.arusdriver.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.silexsecure.arusdriver.model.User
import com.silexsecure.arusdriver.util.Helper
import com.silexsecure.arusdriver.util.SharedPreferenceHelper


class TrackerService : Service() {

    private lateinit var databaseRef: DatabaseReference
    private val sharedPreferenceHelper = SharedPreferenceHelper(this)

    private lateinit var geoFire: GeoFire
    private lateinit var phone: String

    val helper = Helper(this)

    lateinit var userMe: User

    private var request = LocationRequest()

    lateinit var locationCallback: LocationCallback

    lateinit var client: FusedLocationProviderClient

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
//        databaseRef =Firebase.database.reference
        userMe = helper.loggedInUser

        var driversLocation = FirebaseDatabase.getInstance().reference.child(if(userMe.role.contentEquals("mechanic")) "mechanicLocations" else "driverLocations");

        geoFire = GeoFire(driversLocation)

        requestLocationUpdates()
        loginToFirebase()
    }

    //registerReceiver(stopReceiver, IntentFilter(stop))
/*
    private fun buildNotification() {
        val stop = "stop"
        registerReceiver(stopReceiver, IntentFilter(stop))
        val broadcastIntent = PendingIntent.getBroadcast(
            this, 0, Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT
        )
        // Create the persistent notification
        val builder = NotificationCompat.Builder(this)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_text))
            .setOngoing(true)
            .setContentIntent(broadcastIntent)
            .setSmallIcon(R.drawable.ic_tracker)
        startForeground(1, builder.build())
    }
*/

    var stopReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "received stop broadcast")
            // Stop the service when the notification is tapped
            unregisterReceiver(this)
            stopSelf()
        }
    }

    private fun loginToFirebase() {
        // Functionality coming next step
    }

    private fun stopLocationUpdates() {

        client.removeLocationUpdates(locationCallback)
        if (mainlocation.value?.latitude != null)
            showCurrentDriver(LatLng(mainlocation.value!!.latitude, mainlocation.value!!.longitude), false)
        else {
            if (!helper.sharedPreferenceHelper.getStringPreference("last_latitude").isNullOrEmpty()) {

                showCurrentDriver(LatLng(helper.sharedPreferenceHelper.getStringPreference("last_latitude").toDouble(),
                        helper.sharedPreferenceHelper.getStringPreference("last_longitude").toDouble()), false)
            }
        }
    }

    private fun requestLocationUpdates() {
        request = LocationRequest()
        request.interval = 10000
        request.fastestInterval = 5000
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        /*val path =
            getString(R.string.firebase_path) + "/" + getString(R.string.transport_id)*/

        client = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    //databaseRef.child("Requests").child(userId!!).child(requestKey!!).child("barberId")
//                    val ref = databaseRef.child("location").child(barberId!!)
                    val location: Location? = locationResult.lastLocation
                    mainlocation.value = LatLng(location!!.latitude, location.longitude)

                    showCurrentDriver(LatLng(location.latitude, location.longitude), true)

                    Log.d(TAG, "location update ${location.latitude} ${location.longitude}")
//                        ref.setValue(location)
                }
            }
            client.requestLocationUpdates(request, locationCallback, null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Service stopped")
        stopLocationUpdates()
    }

    open fun showCurrentDriver(mylocation: LatLng?, isOnline: Boolean) {
        if (mylocation != null) {
            if (isOnline) {

//                phone = "0" + userMe.phone.substring(4, 14)

                geoFire.setLocation(userMe.phone, GeoLocation(mylocation.latitude, mylocation.longitude)) { key, error ->
                    Log.d(TAG, "onComplete: $key")
                    if (error != null) {
                        System.err.println("There was an error saving the location to GeoFire: $error")
                    } else {
                        println("Location saved on server successfully!")
                    }
                }
            } else {
                geoFire.removeLocation(userMe.phone) { key, _ -> Log.d(TAG, "onComplete: $key removed") }
            }
        } else Toast.makeText(this, "Could not find Locations", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "TrackingServiceTAG"

        private var mainlocation = MutableLiveData<LatLng>()

        val mylocation: LiveData<LatLng>
            get() = mainlocation
    }
}