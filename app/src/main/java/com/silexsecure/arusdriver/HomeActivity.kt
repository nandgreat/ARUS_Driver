package com.silexsecure.arusdriver

import android.Manifest
import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.BuildConfig
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.shreyaspatil.material.navigationview.MaterialNavigationView
import com.silexsecure.arusdriver.model.User
import com.silexsecure.arusdriver.navfragments.ProfileActivity
import com.silexsecure.arusdriver.service.TrackerService
import com.silexsecure.arusdriver.util.Config
import com.silexsecure.arusdriver.util.Helper
import com.silexsecure.arusdriver.util.Utils
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.nav_content_main.*

class HomeActivity : AppCompatActivity(), OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navView: MaterialNavigationView
    private val TAG = "HomeActivityTAG"
    lateinit var drawer: DrawerLayout

    // location updates interval - 10sec
    val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 3000

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 3000

    private val REQUEST_CHECK_SETTINGS = 100

    lateinit var helper: Helper

    private lateinit var mMap: GoogleMap

    private var mylatitude: Double = 0.0
    private var mylongitude: Double = 0.0

    private lateinit var mylocationCard: CardView

    private var meUser: User? = null

    companion object {
        var sydney: LatLng? = null
    }

    private lateinit var toggle: ActionBarDrawerToggle


    private var mysavedInstanceState: Bundle? = null

    private lateinit var databaseReference: DatabaseReference

    private var mLocationSettingsRequest: LocationSettingsRequest? = null
    private lateinit var mSettingsClient: SettingsClient

    private val clickListener = View.OnClickListener { view ->
        run {
            if (view.id == R.id.mylocationCard)
                sydney?.let { this@HomeActivity.animateCamera(it) }
        }
    }

    private var mLocationRequest: LocationRequest? = null

    @SuppressLint("RestrictedApi", "ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(false)
        toggle.isDrawerIndicatorEnabled = false
        toggle.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp)

        toggle.toolbarNavigationClickListener = View.OnClickListener {
            drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else {
                drawer.openDrawer(GravityCompat.START)
            }
        }

        // restore the values from saved instance state
        mysavedInstanceState = savedInstanceState

        mylocationCard = findViewById(R.id.mylocationCard)

        navView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener(this@HomeActivity)

        helper = Helper(this@HomeActivity)

        meUser = helper.loggedInUser
        Log.d(TAG, "onCreate: user present ${meUser!!.role} ${meUser!!.carType}")
        tvGreetings.text = meUser!!.fullname
        tvOnlineStatus.text = if (meUser!!.isOnlineStatus) "You're Online" else "You're Offline"
        tvOnlineStatus.setTextColor(if (meUser!!.isOnlineStatus) Color.parseColor("#4CAF50") else R.color.darkGrey)
        switchCompat.isChecked = meUser!!.isOnlineStatus

        if (meUser!!.isOnlineStatus)
            startService(Intent(this@HomeActivity, TrackerService::class.java))
        else
            stopService(Intent(this@HomeActivity, TrackerService::class.java))

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val headerView = navigationView.getHeaderView(0)
        val navUsername: TextView = headerView.findViewById<View>(R.id.tvDriverName) as TextView
        val navBalance: TextView = headerView.findViewById<View>(R.id.tvBalance) as TextView
        val mydriverImage: CircleImageView = headerView.findViewById<View>(R.id.driverProfileImage) as CircleImageView
        navUsername.text = meUser!!.fullname
        navBalance.text = "${resources.getString(R.string.naira_sign)}${Utils.moneyFormat(meUser!!.wallet.toString())}"

        if (!meUser!!.image.contentEquals("noimage.jpg")) {
            Glide.with(this).load(meUser!!.image).into(mydriverImage)
        }


        // Write a message to the database
        TrackerService.mylocation.observe(this, androidx.lifecycle.Observer {
            Log.d(TAG, "onCreate: " + it.latitude + " " + it.longitude)
        })

        databaseReference = FirebaseDatabase.getInstance().reference

        mSettingsClient = LocationServices.getSettingsClient(this)

        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        mLocationSettingsRequest = builder.build()

        switchCompat.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                Dexter.withActivity(this@HomeActivity)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(object : PermissionListener {
                            override fun onPermissionGranted(response: PermissionGrantedResponse) {
                                mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                                        .addOnCompleteListener {
                                            Log.d(TAG, "onPermissionGranted: Location On")
                                            startService(Intent(this@HomeActivity, TrackerService::class.java))
                                            Log.d(TAG, "onPermissionGranted: Service started")
                                            meUser!!.isOnlineStatus = true
                                            helper.loggedInUser = meUser
                                            switchCompat.isChecked = true
                                        }.addOnFailureListener(OnFailureListener { e: Exception ->
                                            when ((e as ApiException).statusCode) {
                                                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                                                    Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                                            "location settings ")
                                                    try {
                                                        // Show the dialog by calling startResolutionForResult(), and check the
                                                        // result in onActivityResult().
                                                        val rae = e as ResolvableApiException
                                                        rae.startResolutionForResult(this@HomeActivity, REQUEST_CHECK_SETTINGS)
                                                        switchCompat.isChecked = false
                                                    } catch (sie: SendIntentException) {
                                                        Log.i(TAG, "PendingIntent unable to execute request.")
                                                    }
                                                }
                                                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                                                    val errorMessage = "Location settings are inadequate, and cannot be " +
                                                            "fixed here. Fix in Settings."
                                                    Log.e(TAG, errorMessage)
                                                    switchCompat.isChecked = false
                                                    Toast.makeText(this@HomeActivity, errorMessage, Toast.LENGTH_LONG).show()
                                                }
                                            }
                                            Log.d(TAG, "onPermissionGranted: Location Off")
                                        })
                            }

                            override fun onPermissionDenied(response: PermissionDeniedResponse) {
                                if (response.isPermanentlyDenied) {
                                    // open device settings when the permission is
                                    // denied permanently
                                    openSettings()
                                }
                            }

                            override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                                token.continuePermissionRequest()
                            }
                        }).check()
            } else {
                stopService(Intent(this@HomeActivity, TrackerService::class.java))
                Log.d(TAG, "onCreateView: Service Stopped")
            }
            onlineStatus(isChecked)
        }

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ${meUser!!.role}")
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera

        mMap.uiSettings.isRotateGesturesEnabled = false
        mMap.uiSettings.isTiltGesturesEnabled = false

        restoreValuesFromBundle(mysavedInstanceState)

        TrackerService.mylocation.observe(this, androidx.lifecycle.Observer {
            Log.d(TAG, "onMapReady: " + it.latitude + " " + it.longitude)
            // For zooming automatically to the location of the marker

            sydney = LatLng(it.latitude, it.longitude)

            val cameraPosition = CameraPosition.Builder().target(sydney).zoom(12f).build()
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@Observer
            }
            mMap.isMyLocationEnabled = true
            val zoomLevel = 16.0f //This goes up to 21
            mMap.uiSettings.isMyLocationButtonEnabled = true
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel))

            mylocationCard.setOnClickListener(clickListener)


        })
    }


    private fun openSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null)
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    @SuppressLint("ResourceAsColor")
    private fun onlineStatus(isChecked: Boolean) {
        tvOnlineStatus.text = if (isChecked) "You're Online" else "You're Offline"
        tvOnlineStatus.setTextColor(if (isChecked) Color.parseColor("#4CAF50") else R.color.darkGrey)

//        showCurrentDriver(selectedDriver, isChecked);
        if (isChecked) {
            databaseReference.child(if (meUser!!.role.contentEquals("mechanic")) "availableMechanics" else "availableDrivers").child(meUser!!.phone).setValue(meUser).addOnCompleteListener(OnCompleteListener { task: Task<Void?>? ->
                meUser!!.isOnlineStatus = true
                helper.loggedInUser = meUser
                FirebaseMessaging.getInstance().subscribeToTopic(if(meUser!!.role!!.contentEquals("mechanic")) Config.REQUEST_MECHANIC_TOPIC else Config.REQUEST_TOWBUG_TOPIC).addOnCompleteListener {
                    Log.d(TAG, "onlineStatus: subscribed to " + if(meUser!!.role!!.contentEquals("mechanic")) Config.REQUEST_MECHANIC_TOPIC else Config.REQUEST_TOWBUG_TOPIC)
                }
            })
        } else {
            meUser!!.isOnlineStatus = false
            helper.loggedInUser = meUser
            FirebaseMessaging.getInstance().unsubscribeFromTopic(if(meUser!!.role!!.contentEquals("mechanic")) Config.REQUEST_MECHANIC_TOPIC else Config.REQUEST_TOWBUG_TOPIC).addOnCompleteListener {
                databaseReference.child(if(meUser!!.role.contentEquals("mechanic")) "availableMechanics" else "availableDrivers").child(meUser!!.phone).removeValue()
                Log.d(TAG, "onlineStatus: unsubscribe from " + if(meUser!!.role!!.contentEquals("mechanic")) Config.REQUEST_MECHANIC_TOPIC else Config.REQUEST_TOWBUG_TOPIC)
            }
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {

        when (p0.itemId) {
            R.id.navigation_home -> {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START)
                } else {
                    drawer.openDrawer(GravityCompat.START)
                }
            }
            R.id.navigation_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
            }
            R.id.navigation_history -> {
                startActivity(Intent(this, TowingHistoryActivity::class.java))
            }
            R.id.navigation_logout -> {
                stopService(Intent(this@HomeActivity, TrackerService::class.java))
                FirebaseMessaging.getInstance().unsubscribeFromTopic(if(meUser!!.role!!.contentEquals("mechanic")) Config.REQUEST_MECHANIC_TOPIC else Config.REQUEST_TOWBUG_TOPIC)
                databaseReference.child("availableDrivers").child(meUser!!.phone).removeValue()
                helper.logout()
            }

        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            drawer.openDrawer(GravityCompat.START)
        }

        return true
    }

    /**
     * Restoring values from saved instance state
     */
    private fun restoreValuesFromBundle(savedInstanceState: Bundle?) {

        if (!helper.sharedPreferenceHelper.getStringPreference("last_latitude").isNullOrEmpty()) {
            sydney = LatLng(helper.sharedPreferenceHelper.getStringPreference("last_latitude").toDouble(),
                    helper.sharedPreferenceHelper.getStringPreference("last_longitude").toDouble())
            Log.d(TAG, "restoreValuesFromBundle: " + sydney!!.latitude)
        }

        if (!helper.sharedPreferenceHelper.getStringPreference("last_latitude").isNullOrEmpty()) {

            val cameraPosition = CameraPosition.Builder().target(sydney).zoom(12f).build()
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            mMap.isMyLocationEnabled = true
            val zoomLevel = 16.0f //This goes up to 21
            mMap.uiSettings.isMyLocationButtonEnabled = true
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel))

            mylocationCard.setOnClickListener(clickListener)

        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates)
        if (sydney != null) {
            outState.putParcelable("last_known_location", sydney)
            helper.sharedPreferenceHelper.setStringPreference("last_latitude", sydney?.latitude.toString())
            helper.sharedPreferenceHelper.setStringPreference("last_longitude", sydney?.longitude.toString())
        }
        Log.d(TAG, "onSaveInstanceState: ${sydney?.latitude}")

//        outState.putString("last_updated_on", mLastUpdateTime)
    }

    private fun getCameraPositionWithBearing(latLng: LatLng): CameraPosition {
        return CameraPosition.Builder().target(latLng).zoom(16f).build()
    }

    private fun animateCamera(location: LatLng) {
        val latLng = LatLng(location.latitude, location.longitude)
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(getCameraPositionWithBearing(latLng)))
    }
}
