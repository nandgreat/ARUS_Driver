package com.silexsecure.arusdriver.cartowing

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.location.LocationListener
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
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
import com.silexsecure.arusdriver.ListSelectionActivity
import com.silexsecure.arusdriver.R
import com.thekhaeng.pushdownanim.PushDownAnim
import com.vanillaplacepicker.presentation.builder.VanillaPlacePicker
import com.vanillaplacepicker.utils.KeyUtils
import com.vanillaplacepicker.utils.MapType
import com.vanillaplacepicker.utils.PickerLanguage
import com.vanillaplacepicker.utils.PickerType
import kotlinx.android.synthetic.main.fragment_car_towing_details.*
import kotlinx.android.synthetic.main.fragment_car_towing_details.view.*
import java.io.IOException
import java.util.*

class CarTowingDetails : Fragment(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private lateinit var carTypeLayout: TextInputEditText
    private lateinit var carMakeLayout: TextInputEditText
    private lateinit var ivCarType: ImageView
    private lateinit var tvCarType: TextView
    private lateinit var bContinue: Button

    private var returnCarMakeID: Int = -1
    private var returnCarModelID: Int = -1

    private val CARMAKE_CODE = 33

    companion object {
        var KEY_ID = ""
        var KEY_VEHICLE_TYPE = ""
        var KEY_CAR_MAKE = ""
        var KEY_VEHICLE_COLOR = ""
        var KEY_VEHICLE_MODEL = ""
        var KEY_REG_NO = ""
        var KEY_CAR_YEAR = ""
        var KEY_CAR_FAULT = ""
        var KEY_DESCRIPTION = ""
        var KEY_REQUEST_FEE = ""
        var KEY_MECHANIC = ""
        var KEY_REQUEST_TIME = ""
        var KEY_PAYMENT_STATUS = ""
        var KEY_LONGITUDE = ""
        var KEY_LATTITUDE = ""
        var KEY_ADDRESS = ""
        var KEY_AMOUNT = ""
        var KEY_PAYMENT_METHOD = ""
        var KEY_REQUEST_STATUS = ""
        var KEY_LOCATION = ""
        var carTypeID: Int = -1
        var carMakeID: Int = -1
    }

    private val TAG : String = "CarTowingDetailsTAG"

    private val PERMISSION_ID: Int = 101

    private val LOCATION_PERMISSION_REQUEST = 232

    lateinit var mFusedLocationClient: FusedLocationProviderClient

    var permissions = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        var view: View = inflater.inflate(R.layout.fragment_car_towing_details, container, false)

        carMakeLayout = view.findViewById(R.id.carMakeLayout)
        carTypeLayout = view.findViewById(R.id.carFaultLayout)
        tvCarType = view.findViewById(R.id.tvCarType)
        ivCarType = view.findViewById(R.id.ivCarType)
        bContinue = view.findViewById(R.id.bContinue)


        carMakeLayout.setOnClickListener {
            val intent = Intent(context, ListSelectionActivity::class.java)
            intent.putExtra("from", "carmake");
            startActivityForResult(intent, CARMAKE_CODE)
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this@CarTowingDetails.requireActivity() as Activity)

        carTypeLayout.setOnClickListener {
            showAlertDialog()
        }

        if (!KEY_CAR_MAKE.isNullOrBlank())
            carMakeLayout.setText(KEY_CAR_MAKE)

        if (!KEY_VEHICLE_TYPE.isNullOrBlank()) {
            carTypeLayout.setText(KEY_VEHICLE_TYPE)
            when (carTypeID) {
                0 -> ivCarType.setImageDrawable(resources.getDrawable(R.drawable.sedan))
                1 -> ivCarType.setImageDrawable(resources.getDrawable(R.drawable.micro))
                2 -> ivCarType.setImageDrawable(resources.getDrawable(R.drawable.minivan))
                3 -> ivCarType.setImageDrawable(resources.getDrawable(R.drawable.suv))
                4 -> ivCarType.setImageDrawable(resources.getDrawable(R.drawable.truck))
            }
        }


        view.etVechicleLocation.setOnClickListener {

            if (!KEY_LATTITUDE.isEmpty() && !KEY_LONGITUDE.isEmpty()) {

                var mylat = KEY_LATTITUDE
                var myLong = KEY_LONGITUDE

                val intent = VanillaPlacePicker.Builder(this@CarTowingDetails.requireContext())
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

                startActivityForResult(intent, KeyUtils.REQUEST_PLACE_PICKER)
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

    private fun showCarMakeDialog() {
        val alertDialog = AlertDialog.Builder(this@CarTowingDetails.requireContext(), R.style.MaterialThemeDialog)
        alertDialog.setTitle("AlertDialog")
        val items = arrayOf("Toyota", "Honda", "Peogeout", "Nissan", "Range Rover", "Mercedes Benz", "BMW")

        alertDialog.setSingleChoiceItems(items, carMakeID) { dialog, which ->
            when (which) {
                0, 1, 2, 3, 4, 5, 6 -> carMakeLayout.setText(items[which])
            }
            carMakeID = which
            dialog.dismiss()

        }
        val alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }


    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this@CarTowingDetails.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this@CarTowingDetails.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }


    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
                this@CarTowingDetails.requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_ID
        )
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this@CarTowingDetails.requireActivity()) { task ->
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
                Toast.makeText(this@CarTowingDetails.requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }


    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this@CarTowingDetails.requireActivity())
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

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        )
    }


    @SuppressLint("ShowToast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PushDownAnim.setPushDownAnimTo(bContinue).setScale(PushDownAnim.MODE_STATIC_DP, 5F).setOnClickListener {

            val random = (0..9999999).random()

            if (carMakeLayout.text.isNullOrBlank()) {
                Toast.makeText(context, "Car make is required", Toast.LENGTH_LONG)
            } else if (carTypeLayout.text.isNullOrBlank()) {
                Toast.makeText(context, "Car type is required", Toast.LENGTH_LONG)
            } else {

                KEY_ID = "EECR" + random
                KEY_CAR_MAKE = carMakeLayout.text.toString()
                KEY_VEHICLE_TYPE = carTypeLayout.text.toString()

                view.findNavController().navigate(R.id.fragmentAtoB)
            }
        }
    }

    private fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(this@CarTowingDetails.requireContext(), R.style.MaterialThemeDialog)
        alertDialog.setTitle("Select Car Type")
        val items = arrayOf("Sedan", "Micro", "Mini van", "SUV", "Truck")

        alertDialog.setSingleChoiceItems(items, carTypeID) { dialog, which ->
            when (which) {
                0 -> {
                    carTypeLayout.setText(items[which])
                    tvCarType.text = items[which]
                    ivCarType.setImageDrawable(resources.getDrawable(R.drawable.sedan))
                }
                1 -> {
                    carTypeLayout.setText(items[which])
                    tvCarType.text = items[which]
                    ivCarType.setImageDrawable(resources.getDrawable(R.drawable.micro))

                }
                2 -> {
                    carTypeLayout.setText(items[which])
                    tvCarType.text = items[which]
                    ivCarType.setImageDrawable(resources.getDrawable(R.drawable.minivan))
                }
                3 -> {
                    carTypeLayout.setText(items[which])
                    tvCarType.text = items[which]
                    ivCarType.setImageDrawable(resources.getDrawable(R.drawable.suv))

                }
                4 -> {
                    carTypeLayout.setText(items[which])
                    tvCarType.text = items[which]
                    ivCarType.setImageDrawable(resources.getDrawable(R.drawable.truck))

                }
            }
            carTypeID = which
            dialog.dismiss()
        }
        val alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CARMAKE_CODE) {
            val carMake = data!!.getStringExtra("carmake")
            returnCarMakeID = data.getIntExtra("carmakeid", 0)
            carMakeLayout.setText(carMake.toString())
        }

        when (requestCode) {
            KeyUtils.REQUEST_PLACE_PICKER -> {
                val vanillaAddress = VanillaPlacePicker.onActivityResult(data)

                if (vanillaAddress != null) {
                    etVechicleLocation.setText(vanillaAddress.formattedAddress)
                    KEY_ADDRESS = vanillaAddress.formattedAddress.toString()
                    KEY_LATTITUDE = vanillaAddress.latitude.toString()
                    KEY_LONGITUDE = vanillaAddress.longitude.toString()

                    val startPoint = Location("locationA")
                    startPoint.latitude = vanillaAddress.latitude!!
                    startPoint.longitude = vanillaAddress.longitude!!

                    val endPoint = Location("locationA")
                    endPoint.latitude = 9.0797616
                    endPoint.longitude = 7.3295436

                    val distance = startPoint.distanceTo(endPoint).toDouble()

                    Log.d(TAG, "Distance between points is $distance")
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

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onLocationChanged(p0: Location?) {

    }


    private fun turnOnGPS() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
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

}