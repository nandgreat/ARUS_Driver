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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.flutterwave.raveandroid.RaveConstants
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.RavePayManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.silexsecure.arusdriver.PaymentSuccessActivity
import com.silexsecure.arusdriver.R
import com.silexsecure.arusdriver.fragment.FragmentA.Companion.KEY_ADDRESS
import com.silexsecure.arusdriver.fragment.FragmentA.Companion.KEY_AMOUNT
import com.silexsecure.arusdriver.fragment.FragmentA.Companion.KEY_DRIVER
import com.silexsecure.arusdriver.fragment.FragmentA.Companion.KEY_ID
import com.silexsecure.arusdriver.fragment.FragmentA.Companion.KEY_LATTITUDE
import com.silexsecure.arusdriver.fragment.FragmentA.Companion.KEY_LONGITUDE
import com.silexsecure.arusdriver.fragment.FragmentA.Companion.KEY_PAYMENT_TYPE
import com.silexsecure.arusdriver.fragment.FragmentA.Companion.KEY_PRICE_PER_KG
import com.silexsecure.arusdriver.fragment.FragmentA.Companion.KEY_QTY_KG
import com.silexsecure.arusdriver.fragment.FragmentA.Companion.KEY_SERVICE_FEE
import com.silexsecure.arusdriver.fragment.FragmentA.Companion.KEY_TOTAL
import com.silexsecure.arusdriver.model.ProductRequest
import com.silexsecure.arusdriver.navfragments.HomeFragment.helper
import com.silexsecure.arusdriver.util.Config
import com.silexsecure.arusdriver.util.Utils.moneyFormat
import com.vanillaplacepicker.presentation.builder.VanillaPlacePicker
import com.vanillaplacepicker.utils.KeyUtils
import com.vanillaplacepicker.utils.MapType
import com.vanillaplacepicker.utils.PickerLanguage
import com.vanillaplacepicker.utils.PickerType
import kotlinx.android.synthetic.main.fragment_c.view.*
import java.io.IOException
import java.util.*

class FragmentC : Fragment(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    lateinit var tvKgs: TextView
    lateinit var tvAmount: TextView
    lateinit var tvPricePerKg: TextView
    lateinit var tvAddress: TextView
    lateinit var tvOrderID: TextView
    lateinit var tvTotal: TextView
    lateinit var tvPaymentMethod: TextView
    lateinit var bSendRequest: Button
    lateinit var cardChangeAddress: ConstraintLayout
    lateinit var cardPaymentMethod: ConstraintLayout
    lateinit var mylocation: Location
    var paymentId: Int = -1

    lateinit var productRequest: ProductRequest

    private lateinit var database: DatabaseReference

    private var phone: String? = null
    private var firstName : String? = null
    private var lastName : String? = null

    private val TAG = "FragmentCTAG"

    private val PERMISSION_ID: Int = 101

    private val LOCATION_PERMISSION_REQUEST = 232

    lateinit var mFusedLocationClient: FusedLocationProviderClient

    var permissions = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.fragment_c, container, false)

        productRequest = ProductRequest()

        phone = helper.loggedInUser.phone
        firstName = helper.loggedInUser.firstname
        lastName = helper.loggedInUser.lastname

        tvKgs = view.findViewById(R.id.tvKgs)
        tvPaymentMethod = view.findViewById(R.id.tvPaymentMethod)
        tvAmount = view.findViewById(R.id.tvAmount)
        tvPricePerKg = view.findViewById(R.id.tvPricePerKg)
        tvAddress = view.findViewById(R.id.tvAddress)
        tvOrderID = view.findViewById(R.id.tvOrderID)
        bSendRequest = view.findViewById(R.id.bSendRequest)
        cardChangeAddress = view.findViewById(R.id.cardChangeAddress)
        cardPaymentMethod = view.findViewById(R.id.cardPaymentMethod)
        tvTotal = view.findViewById(R.id.tvTotal)

        KEY_SERVICE_FEE = 300.00
        KEY_TOTAL = KEY_SERVICE_FEE + KEY_AMOUNT

        tvTotal.text = "₦${moneyFormat(KEY_TOTAL.toString())}"
        tvAmount.text = "₦${moneyFormat(KEY_AMOUNT.toString())}"
        view.tvServiceFee.text = "₦${moneyFormat(KEY_SERVICE_FEE.toString())}"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermissions()) {
//                enableView()
            } else {
                requestPermissions(permissions, LOCATION_PERMISSION_REQUEST)
            }
        }

        database = FirebaseDatabase.getInstance().reference

        tvAddress.text = if (KEY_ADDRESS.isEmpty()) "Select Address" else KEY_ADDRESS
        tvKgs.text = if (KEY_QTY_KG == 0.00) 0.toString() else "${KEY_QTY_KG}KG"
        tvAmount.text = if (KEY_AMOUNT == 0.00) 0.toString() else "₦${KEY_AMOUNT}"
        tvPricePerKg.text = if (KEY_PRICE_PER_KG == 0.00) 0.toString() else "${KEY_PRICE_PER_KG}Kg"
        tvOrderID.text = if (KEY_ID.isEmpty()) "No ID" else KEY_ID
        tvPaymentMethod.text = if (KEY_PAYMENT_TYPE.isEmpty()) "Not Selected" else KEY_PAYMENT_TYPE


        productRequest.id = KEY_ID
        productRequest.driver = KEY_DRIVER
        productRequest.product = "Gas"
        productRequest.pricePerLitre = KEY_PRICE_PER_KG
        productRequest.quantityInKg = KEY_QTY_KG
        productRequest.amount = KEY_AMOUNT
        productRequest.paymentType = KEY_PAYMENT_TYPE
        productRequest.address = KEY_ADDRESS
        productRequest.latitude = KEY_LATTITUDE
        productRequest.longitude = KEY_LONGITUDE
        productRequest.deliveryStatus = "Pending"
        productRequest.orderTime = Date().time.toString()

        cardChangeAddress.setOnClickListener {

            if (!KEY_LATTITUDE.isEmpty() && !FragmentA.KEY_LONGITUDE.isEmpty()) {

                var mylat = KEY_LATTITUDE
                var myLong = KEY_LONGITUDE

                val intent = VanillaPlacePicker.Builder(this@FragmentC.requireContext())
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
        cardPaymentMethod.setOnClickListener {
            showAlertDialog()
        }

        view.bSendRequest.setOnClickListener {
            validateInformation()
        }

        return view
    }


    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        )
    }


    private fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(this@FragmentC.requireContext(), R.style.MaterialThemeDialog)
        alertDialog.setTitle("Payment Method")
        val items = arrayOf("Cash on Delivery", "Payment online")

        alertDialog.setSingleChoiceItems(items, paymentId) { dialog, which ->
            when (which) {
                0, 1 -> {
                    tvPaymentMethod.text = items[which]
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
                        tvAddress.text = vanillaAddress.formattedAddress
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
        } else if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            val message = data.getStringExtra("response")
            if (message != null) {
                Log.d("rave response", message)
            }
            when (resultCode) {
                RavePayActivity.RESULT_SUCCESS -> {
                    view!!.progressFirebase.visibility = View.VISIBLE
                    initFirebase(productRequest)
                    //                Toast.makeText(this, "SUCCESS " + message, Toast.LENGTH_SHORT).show();
                }
                RavePayActivity.RESULT_ERROR -> {
                    Toast.makeText(context, "ERROR $message", Toast.LENGTH_SHORT).show()
                }
                RavePayActivity.RESULT_CANCELLED -> {
                    Toast.makeText(context, "CANCELLED $message", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this@FragmentC.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this@FragmentC.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }


    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
                this@FragmentC.requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_ID
        )
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this@FragmentC.requireActivity()) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {

                        KEY_LATTITUDE = location.latitude.toString()
                        FragmentA.KEY_LONGITUDE = location.longitude.toString()

                        Log.d(TAG, "My location is " + KEY_LATTITUDE + ", " + FragmentA.KEY_LONGITUDE)

//                        findViewById<TextView>(R.id.latTextView).text = location.latitude.toString()
//                        findViewById<TextView>(R.id.lonTextView).text = location.longitude.toString()
                    }
                }
            } else {
                Toast.makeText(this@FragmentC.requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
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


    private fun turnOnGPS() {
        val intent: Intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(intent)
    }


    private fun validateInformation() {
        val valid = true
        val email = "paul4nank@gmail.com"
        val referencce = "ERM" + Calendar.getInstance().timeInMillis
        val currency = "NGN"
        val country = "NG"
        val fName = firstName
        val lName = lastName
        val phoneNumber = phone
        val narration = "Buying ${KEY_QTY_KG}Kg Gas"
        if (valid) {
            val ravePayManager = RavePayManager(this).setAmount(KEY_TOTAL)
                    .setCountry(country)
                    .setCurrency(currency)
                    .setEmail(email)
                    .setfName(fName)
                    .setlName(lName)
                    .setPhoneNumber(phoneNumber)
                    .setNarration(narration)
                    .setPublicKey(Config.PUBLIC_KEY)
                    .setEncryptionKey(Config.ENCRYPTION_KEY)
                    .setTxRef(KEY_ID)
                    .acceptMpesaPayments(true)
                    .acceptAccountPayments(false)
                    .acceptCardPayments(true)
                    .allowSaveCardFeature(true)
                    .acceptAchPayments(false)
                    .acceptGHMobileMoneyPayments(false)
                    .acceptUgMobileMoneyPayments(false)
                    .acceptZmMobileMoneyPayments(false)
                    .acceptRwfMobileMoneyPayments(false)
                    .acceptUkPayments(false)
                    .acceptFrancMobileMoneyPayments(false)
                    .acceptBankTransferPayments(true)
                    .acceptUssdPayments(true)
                    .acceptBarterPayments(false)
                    .onStagingEnv(false)
                    .showStagingLabel(true) //                    .setMeta(meta)
                    //                    .withTheme(R.style.TestNewTheme)
                    .shouldDisplayFee(true)


//            // Customize pay with bank transfer options (optional)
//            if (isPermanentAccountSwitch.isChecked())
//                ravePayManager.acceptBankTransferPayments(true, true);
//            else {
//                if (setExpirySwitch.isChecked()) {
//                    int duration = 0, frequency = 0;
//                    try {
//                        duration = Integer.parseInt(durationEt.getText().toString());
//                        frequency = Integer.parseInt(frequencyEt.getText().toString());
//                    } catch (NumberFormatException e) {
//                        e.printStackTrace();
//                    }
//                    ravePayManager.acceptBankTransferPayments(true, duration, frequency);
//                }
//            }
            ravePayManager.initialize()
        }
    }


    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
//            findViewById<TextView>(R.id.latTextView).text = mLastLocation.latitude.toString()
//            findViewById<TextView>(R.id.lonTextView).text = mLastLocation.longitude.toString()
        }
    }


    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this@FragmentC.requireActivity())
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        )
    }

    override fun onConnected(p0: Bundle?) {
        checkPermissions()

    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }


    override fun onLocationChanged(p0: Location?) {

        if (p0 != null) {
            mylocation = p0
        }
        if (mylocation != null) {
            val latitude: Double = mylocation.latitude
            val longitude: Double = mylocation.longitude
        }
    }

    private fun initFirebase(productRequest: ProductRequest) {

        val requestKey: String = database.child("orders").child(phone.toString()).push().key.toString()

        productRequest.requestKey = requestKey
        productRequest.customerName = "${helper.loggedInUser.firstname} ${helper.loggedInUser.lastname}"
        productRequest.customerPhone = helper.loggedInUser.phone

        database.child("orders").child(phone.toString()).child(requestKey).setValue(productRequest).addOnCompleteListener {

            view!!.progressFirebase.visibility = View.GONE

            val intent = Intent(activity, PaymentSuccessActivity::class.java)
            activity?.startActivity(intent)
            activity?.finish()

        }.addOnFailureListener {

            view!!.progressFirebase.visibility = View.GONE

        }

    }

}
