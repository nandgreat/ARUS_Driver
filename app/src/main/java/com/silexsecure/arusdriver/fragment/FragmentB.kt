package com.silexsecure.arusdriver.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.location.LocationListener
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.kusu.loadingbutton.LoadingButton
import com.silexsecure.arusdriver.R
import com.silexsecure.arusdriver.fragment.FragmentA.Companion.KEY_ADDRESS
import com.silexsecure.arusdriver.fragment.FragmentA.Companion.KEY_LATTITUDE
import com.silexsecure.arusdriver.fragment.FragmentA.Companion.KEY_LONGITUDE
import com.silexsecure.arusdriver.fragment.FragmentA.Companion.KEY_PAYMENT_TYPE
import com.silexsecure.arusdriver.model.ProductRequest
import com.silexsecure.arusdriver.util.SessionManager
import com.thekhaeng.pushdownanim.PushDownAnim
import com.vanillaplacepicker.presentation.builder.VanillaPlacePicker
import com.vanillaplacepicker.utils.KeyUtils
import com.vanillaplacepicker.utils.KeyUtils.REQUEST_PLACE_PICKER
import com.vanillaplacepicker.utils.MapType
import com.vanillaplacepicker.utils.PickerLanguage
import com.vanillaplacepicker.utils.PickerType
import java.io.IOException
import java.util.*


class FragmentB : Fragment(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private var TAG = "FragmentBTAG"

    private lateinit var paymentMethodLayout: TextInputEditText
    private lateinit var pickLocationLayout: TextInputEditText
    private lateinit var tvProduct: TextView
    private lateinit var tvPricePerLitre: TextView
    private lateinit var tvLitres: TextView
    private lateinit var tvAmount: TextView
    private lateinit var tvPaymentMethod: TextView
    private lateinit var tvPaymentStatus: TextView
    private lateinit var tvDeliveryStatus: TextView
    lateinit var mylocation: Location
    private lateinit var tvAddress: TextView
    private lateinit var bPrevious: Button
    private lateinit var bProceed: Button
    private lateinit var bSendOrder: LoadingButton

    private lateinit var googleApiClient: GoogleApiClient


    lateinit var mFusedLocationClient: FusedLocationProviderClient

    var permissions = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)


    var paymentId: Int = -1


    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }


    private val PERMISSION_ID: Int = 101

    private val LOCATION_PERMISSION_REQUEST = 232

    val productRequest = ProductRequest()

    private lateinit var database: DatabaseReference

    private lateinit var sessionManager: SessionManager

    private var username: String? = null
    private var phone: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_b, container, false)


        bPrevious = view.findViewById(R.id.bPrevious)
        bProceed = view.findViewById(R.id.bProceed)
        paymentMethodLayout = view.findViewById(R.id.paymentMethodLayout)
        pickLocationLayout = view.findViewById(R.id.pickLocationLayout)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissions()) {
//                enableView()
            } else {
                requestPermissions(permissions, LOCATION_PERMISSION_REQUEST)
            }

        } else {

        }

        database = FirebaseDatabase.getInstance().reference

        sessionManager = SessionManager(context)

        var hashMap: HashMap<String, String> = HashMap<String, String>()

        hashMap = sessionManager.userDetails

        paymentMethodLayout.setOnClickListener {
            showAlertDialog()
        }

        //        val userDetails = sessionManager.userDetails

        for (key in hashMap.keys) {

            phone = hashMap["phone"]

        }

        Log.d("FragmentBTAG", "My phone is $phone")


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this@FragmentB.requireActivity() as Activity)

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            if (isLocationEnabled()) {
                getLastLocation()
            }
        }

        pickLocationLayout.setOnClickListener {
            // Initialize the AutocompleteSupportFragment.

            if (!KEY_LATTITUDE.isEmpty() && !KEY_LONGITUDE.isEmpty()) {

                Log.d(TAG, "$KEY_LATTITUDE $KEY_LONGITUDE")

                val mylat = KEY_LATTITUDE
                val myLong = KEY_LONGITUDE

                val intent = VanillaPlacePicker.Builder(this@FragmentB.requireContext())
                        .with(PickerType.MAP_WITH_AUTO_COMPLETE) // Select Picker type to enable autocompelte, map or both
                        .withLocation(mylat.toDouble(), myLong.toDouble())
                        .setPickerLanguage(PickerLanguage.ENGLISH) // Apply language to picker
//                    .setLocationRestriction(LatLng(9.139211, 7.288743), LatLng(8.953667,7.495771)) // Restrict location bounds in map and autocomplete
                        .setCountry("NG") // Only for Autocomplete

                        /*
                  * Configuration for Map UI
                  */
                        .setMapType(MapType.NORMAL) // Choose map type (Only applicable for map screen)
//                    .setMapStyle(R.raw.style_json) // Containing the JSON style declaration for night-mode styling
                        .setMapPinDrawable(android.R.drawable.ic_menu_mylocation) // To give custom pin image for map marker
                        .build()

                startActivityForResult(intent, REQUEST_PLACE_PICKER)
            } else {
                if (!checkPermissions()) {
                    requestPermissions()
                } else {
                    if (isLocationEnabled()) {
                        getLastLocation()
                    }
                }
            }
        }

        return view
    }


//    @Synchronized
//    private fun setUpGClient() {
//        googleApiClient = GoogleApiClient.Builder(context!!)
//                .enableAutoManage(activity!!, 0, this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build()
//        googleApiClient.connect()
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PushDownAnim.setPushDownAnimTo(bProceed).setScale(PushDownAnim.MODE_STATIC_DP, 5F).setOnClickListener {

            view.findNavController().navigate(R.id.fragmentBtoC)
        }

        PushDownAnim.setPushDownAnimTo(bPrevious).setScale(PushDownAnim.MODE_STATIC_DP, 5F).setOnClickListener {
            view.findNavController().popBackStack()
//            activity?.findViewById<Stepper>(R.id.Stepper)?.back()
        }
    }

    private fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(this@FragmentB.requireContext(), R.style.MaterialThemeDialog)
        alertDialog.setTitle("Payment Method")
        val items = arrayOf("Cash on Delivery", "Payment online")

        alertDialog.setSingleChoiceItems(items, paymentId) { dialog, which ->
            when (which) {
                0, 1 -> {
                    paymentMethodLayout.setText(items[which])
                    KEY_PAYMENT_TYPE = items[which]
                }
            }
            paymentId = which
            dialog.dismiss()

        }
        val alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    //----- override onActivityResult function to get Vanilla Place Picker result.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                KeyUtils.REQUEST_PLACE_PICKER -> {
                    val vanillaAddress = VanillaPlacePicker.onActivityResult(data)

                    if (vanillaAddress != null) {
                        pickLocationLayout.setText(vanillaAddress.formattedAddress)
                        KEY_ADDRESS = vanillaAddress.formattedAddress.toString()
                        KEY_LATTITUDE = vanillaAddress.latitude.toString()
                        KEY_LONGITUDE = vanillaAddress.longitude.toString()
                    }

                    Log.d(TAG, "Found the place " + vanillaAddress)
                }
                LOCATION_PERMISSION_REQUEST -> {

//                       val lm : LocationManager = (LocationManathis@FragmentB@this.requireActivity().getSystemService(LOCATION_SERVICE);
//             val isGPSEnabled : Boolean = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    turnOnGPS()
                }
            }
        }
    }


    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this@FragmentB.requireActivity()) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {

                        KEY_LATTITUDE = location.latitude.toString()
                        KEY_LONGITUDE = location.longitude.toString()

                        Log.d(TAG, "My location is " + KEY_LATTITUDE + ", " + KEY_LONGITUDE)

//                        findViewById<TextView>(R.id.latTextView).text = location.latitude.toString()
//                        findViewById<TextView>(R.id.lonTextView).text = location.longitude.toString()
                    }
                }
            } else {
                Toast.makeText(this@FragmentB.requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }


    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this@FragmentB.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this@FragmentB.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
                this@FragmentB.requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_ID
        )
    }



    private fun turnOnGPS() {
        val intent: Intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(intent)
    }


    private inner class MyLocationListener : LocationListener {

        override fun onLocationChanged(loc: Location) {
            //            editLocation.setText("");
            //            pb.setVisibility(View.INVISIBLE);
            Toast.makeText(
                    context,
                    "Location changed: Lat: " + loc.latitude + " Lng: "
                            + loc.longitude, Toast.LENGTH_SHORT).show()
            val longitude = "Longitude: " + loc.longitude
            Log.v(ContentValues.TAG, longitude)
            val latitude = "Latitude: " + loc.latitude
            Log.v(ContentValues.TAG, latitude)

            /*------- To get city name from coordinates -------- */
            var cityName: String? = null
            val gcd = Geocoder(context, Locale.getDefault())
            val addresses: List<Address>
            try {
                addresses = gcd.getFromLocation(loc.latitude,
                        loc.longitude, 1)
                if (addresses.isNotEmpty()) {
                    System.out.println(addresses[0].locality)
                    cityName = addresses[0].locality
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val s = (longitude + "\n" + latitude + "\n\nMy Current City is: "
                    + cityName)

            Log.d("CityDetails", s)
            //            editLocation.setText(s);
        }

        override fun onProviderDisabled(provider: String) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }

    override fun onConnected(p0: Bundle?) {
        checkPermissions()

    }


    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this@FragmentB.requireActivity())
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        )
    }


    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
//            findViewById<TextView>(R.id.latTextView).text = mLastLocation.latitude.toString()
//            findViewById<TextView>(R.id.lonTextView).text = mLastLocation.longitude.toString()
        }
    }

    //    @Override
    //    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    //        int permissionLocation = ContextCompat.checkSelfPermission(getActivity(),
    //                Manifest.permission.ACCESS_FINE_LOCATION);
    //
    //        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
    //            if (grantResults.length > 0) {
    //                Log.d("HomeFragmentTAG", "Permission Granted");
    //            }
    //        }
    //
    //        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
    //            Log.d("HomeFragmentTAG", "Permission Granted");
    //            getMyLocation();
    //        } else {
    //            Log.d("HomeFragmentTAG", "Permission Not Granted");
    //
    //        }
    //    }
//    override fun onPause() {
//        super.onPause()
//        activity?.let { googleApiClient.stopAutoManage(it) }
//        googleApiClient.disconnect()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        activity?.let { googleApiClient.stopAutoManage(it) }
//        googleApiClient.disconnect()
//    }

    override fun onLocationChanged(p0: Location?) {

        if (p0 != null) {
            mylocation = p0
        }
        if (mylocation != null) {
            val latitude: Double = mylocation.latitude
            val longitude: Double = mylocation.longitude
//            latitudeTextView.text = "Latitude : $latitude"
//            longitudeTextView.text = "Longitude : $longitude"
            //Or Do whatever you want with your location
        }
    }

}
