package com.silexsecure.arusdriver.fragment


import `in`.goodiebag.carouselpicker.CarouselPicker
import android.app.Activity
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.silexsecure.arusdriver.R
import com.silexsecure.arusdriver.model.Gas
import com.silexsecure.arusdriver.model.ProductRequest
import com.silexsecure.arusdriver.util.MyApplication
import com.silexsecure.arusdriver.util.SessionManager
import com.thekhaeng.pushdownanim.PushDownAnim


class FragmentA : Fragment() {

    private var cylinderPosition: Int = -1
    private var selectedCylinder: String = ""
    var imageCarousel: CarouselPicker? = null
    var textCarousel: CarouselPicker? = null
    var mixCarousel: CarouselPicker? = null

    lateinit var etGasType: TextInputEditText

    var gasList = ArrayList<Gas>()

    var selectedQuantity: Int = -1

    companion object {
        val INNER_STATIC_FIELD = ""
        var KEY_ID = ""
        val KEY_CUSTOMER = ""
        var KEY_DRIVER = ""
        var KEY_PRODUCT = ""
        var KEY_PRICE_PER_KG = 0.00
        var KEY_QTY_KG = 0.00
        var KEY_AMOUNT = 0.00
        var KEY_PAYMENT_TYPE = ""
        var KEY_ADDRESS = ""
        var KEY_TOTAL = 0.00
        var KEY_SERVICE_FEE = 0.00
        var KEY_LATTITUDE = ""
        var KEY_LONGITUDE = ""
        var KEY_PAYMENT_STATUS = ""
        var KEY_DELIVERY_STATUS = ""
        var KEY_ORDER_TIME = ""
        var GAS_INT = -1
        var GAS_SIZE = -1
        var QUANTITY_INT = -1
    }

    private val PERMISSION_ID: Int = 101

    private val LOCATION_PERMISSION_REQUEST = 232
    var mApp = MyApplication()

    private var stateId: Int = 0

    private var TAG = "FragmentATAG"

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    lateinit var ivCylinderType: ImageView
    lateinit var tvCylinderType: TextView
    lateinit var etGasQuantity: TextInputEditText
    lateinit var etGasAmount: TextInputEditText
    lateinit var gasCylinderLayout: TextInputLayout
    lateinit var gasQuantityLayout: TextInputLayout
    lateinit var gasAmountLayout: TextInputLayout
    lateinit var bProceed: Button
    lateinit var mylocation: Location
    lateinit var googleApiClient: GoogleApiClient
    private val REQUEST_CHECK_SETTINGS_GPS = 0x1
    private val REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2

    private var gasAmount: Double = 0.0

    lateinit var mFusedLocationClient: FusedLocationProviderClient

    public val productRequest = ProductRequest()

    private var mLatitudeLabel: String? = null
    private var mLongitudeLabel: String? = null
    private var mLatitudeText: TextView? = null
    private var mLongitudeText: TextView? = null

    private lateinit var sessionManager: SessionManager

    private var viewGroup: ViewGroup? = null

    var permissions = arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)

    private var username: String? = null

    lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mLocation: Location
    private var mLocationManager: LocationManager? = null
    private var mLocationRequest: LocationRequest? = null
    private val sUINTERVAL = (2 * 1000).toLong()  /* 10 secs */
    private val sFINTERVAL: Long = 2000 /* 2 sec */
    private lateinit var locationManager: LocationManager

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        var view: View = inflater.inflate(R.layout.fragment_a, container, false)

        etGasType = view.findViewById(R.id.etGasType)
        ivCylinderType = view.findViewById(R.id.ivCylinderType)
        tvCylinderType = view.findViewById(R.id.tvCylinderType)
        etGasQuantity = view.findViewById(R.id.etGasQuantity)
        etGasAmount = view.findViewById(R.id.etGasAmount)
        bProceed = view.findViewById(R.id.bProceed)
        gasCylinderLayout = view.findViewById(R.id.gasCylinderLayout)
        gasQuantityLayout = view.findViewById(R.id.gasQuantityLayout)
        gasAmountLayout = view.findViewById(R.id.gasAmountLayout)


        var gas = Gas("4Kg Gas", R.drawable.gas_four_kg, 4.00)
        gasList.add(gas)
        gas = Gas("6Kg Gas", R.drawable.gas_six_kg, 6.00)
        gasList.add(gas)
        gas = Gas("11Kg Gas", R.drawable.gas_eleven_kg, 11.00)
        gasList.add(gas)
        gas = Gas("18Kg Gas", R.drawable.gas_eighteen_kg, 18.00)
        gasList.add(gas)
        gas = Gas("19Kg Gas", R.drawable.gas_nineteen_kg, 19.00)
        gasList.add(gas)
        gas = Gas("47Kg Gas", R.drawable.gas_fourty_seven_kg, 47.00)
        gasList.add(gas)

        viewGroup = view.findViewById<ViewGroup>(android.R.id.content)

        KEY_PRICE_PER_KG = 350.00;

        if (GAS_INT != -1) {
            val gas = gasList[GAS_INT]
            Glide.with(this.context!!).load(gas.GasImage).into(ivCylinderType)
            tvCylinderType.text = gas.GasSize
            etGasType.setText(gas.GasSize)
        }

        etGasAmount.setText(if (KEY_AMOUNT.toString().isEmpty()) null else KEY_AMOUNT.toString())

        if (QUANTITY_INT != -1) {

            var quantityString: String = ""

            Log.d(TAG, ""+ QUANTITY_INT)

            when (QUANTITY_INT) {
                0 -> quantityString = "Full - ${KEY_QTY_KG}Kg"
                1 -> quantityString = "Half - ${KEY_QTY_KG}Kg"
                3 -> quantityString = "Quarter - ${KEY_QTY_KG}Kg"
            }

            etGasQuantity.setText(quantityString)
        }

        PushDownAnim.setPushDownAnimTo(bProceed).setScale(PushDownAnim.MODE_STATIC_DP, 5F).setOnClickListener {


            if (etGasType.text.isNullOrBlank() || etGasQuantity.text.isNullOrBlank() || etGasAmount.text.isNullOrBlank()) {
                validate()
            } else {
                validate()

                val random = (0..99999).random()
                KEY_ID = "EEL-$random"
                view.findNavController().navigate(R.id.fragmentAtoB)
            }
        }

        etGasType.setOnClickListener {
            showCustomDialog()
        }

        etGasQuantity.setOnClickListener {
            gasCylinderLayout.error = if (etGasType.text.isNullOrBlank()) "Gas Size is required" else null

            if (etGasType.text.isNullOrBlank())
                return@setOnClickListener

            showCarMakeDialog()
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this@FragmentA.requireActivity() as Activity)

        return view
    }


    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {

        }
    }

    private fun buildLocationRequest() {
    }


    var textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            val number: Int = Integer.parseInt(s.toString())

//            etLitres?.setText(number)

            if (start == 12) {
                Toast.makeText(this@FragmentA.requireContext(), "Maximum Limit Reached", Toast.LENGTH_SHORT).show()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }


    private fun showCustomDialog() {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup

        if (cylinderPosition == -1) {
            cylinderPosition = 0
            GAS_INT = cylinderPosition
            selectedCylinder = gasList.get(cylinderPosition).GasSize.toString()
        }
        Log.d(TAG, "$cylinderPosition ")

        //then we will inflate the custom alert dialog xml that we created
        val dialogView: View = LayoutInflater.from(context).inflate(R.layout.my_dialog, viewGroup, false)
        val imageCarousel: CarouselPicker = dialogView.findViewById(R.id.imageCarousel)
        val imageItems: MutableList<CarouselPicker.PickerItem> = ArrayList<CarouselPicker.PickerItem>()
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.gas_four_kg))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.gas_six_kg))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.gas_eleven_kg))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.gas_eighteen_kg))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.gas_nineteen_kg))
        imageItems.add(CarouselPicker.DrawableItem(R.drawable.gas_fourty_seven_kg))
        val imageAdapter = CarouselPicker.CarouselViewAdapter(context, imageItems, 0)
        imageCarousel.adapter = imageAdapter

        val tvGasType = dialogView.findViewById<TextView>(R.id.tvGasType)
        val bSelect = dialogView.findViewById<TextView>(R.id.bSelect)
        val bCancel = dialogView.findViewById<TextView>(R.id.bCancel)

        tvGasType.text = gasList.get(0).GasSize

        imageCarousel.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                cylinderPosition = position
                GAS_INT = cylinderPosition
                tvGasType.text = gasList[cylinderPosition].GasSize
                selectedCylinder = gasList[cylinderPosition].GasSize.toString()
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        //Now we need an AlertDialog.Builder object
        val builder = AlertDialog.Builder(this.context!!)

        //setting the view of the builder to our custom view that we already inflated
        builder?.setView(dialogView)

        //finally creating the alert dialog and displaying it
        val alertDialog = builder?.create()
        alertDialog?.show()

        bCancel.setOnClickListener {
            alertDialog?.dismiss()
        }
        bSelect.setOnClickListener {
            alertDialog?.dismiss()
            etGasType.setText(selectedCylinder)
            Log.d(TAG, "$cylinderPosition ")
            Glide.with(context!!).load(gasList[cylinderPosition].GasImage).into(ivCylinderType)
            tvCylinderType.text = selectedCylinder
            gasAmount = gasList[cylinderPosition].GasQuantity?.times(350) ?: 0.00

            KEY_AMOUNT = gasAmount
            etGasAmount.setText("₦$gasAmount")
            etGasQuantity.setText("Full - ${gasList[cylinderPosition].GasSize}")

            QUANTITY_INT = 0

            GAS_INT = cylinderPosition
            KEY_AMOUNT = gasAmount
            KEY_PRICE_PER_KG = 350.00
            KEY_QTY_KG = gasList[cylinderPosition].GasQuantity!!

            validate()
        }
    }

    private fun showCarMakeDialog() {
        val alertDialog = AlertDialog.Builder(this@FragmentA.requireContext(), R.style.MaterialThemeDialog)
        alertDialog.setTitle("Select Quantity")
        if (cylinderPosition == -1)
            cylinderPosition = 0;

        val cylinderSize = gasList[cylinderPosition].GasQuantity
        var cylinderQuantity = 0.00
        Log.d("QuantitySelect", cylinderSize.toString())
        val items = arrayOf("Full - ${cylinderSize}Kg", "Half - ${cylinderSize?.div(2)}Kg", "Quarter - ${cylinderSize?.div(4)}Kg")

        alertDialog.setSingleChoiceItems(items, selectedQuantity) { dialog, which ->
            when (which) {
                0 -> {
                    etGasQuantity.setText(items[which])
                    gasAmount = cylinderSize?.times(350) ?: 0.00
                    etGasAmount.setText("₦$gasAmount")
                }
                1 -> {
                    cylinderQuantity = cylinderSize?.div(2)!!
                    etGasQuantity.setText(items[which])
                    gasAmount = cylinderQuantity?.times(350) ?: 0.00
                    etGasAmount.setText("₦$gasAmount")
                }
                2 -> {
                    cylinderQuantity = cylinderSize?.div(4)!!
                    etGasQuantity.setText(items[which])
                    gasAmount = cylinderQuantity?.times(350) ?: 0.00
                    etGasAmount.setText("₦$gasAmount")
                }
            }
            productRequest.amount = gasAmount
            KEY_QTY_KG = cylinderQuantity
            selectedQuantity = which
            QUANTITY_INT = selectedQuantity
            dialog.dismiss()

        }
        val alert = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }


    fun validate() {
        gasCylinderLayout.error = if (!etGasType.text.isNullOrBlank()) null else "Gas Size is required"
        gasQuantityLayout.error = if (!etGasQuantity.text.isNullOrBlank()) null else "Gas Quantity is required"
        gasAmountLayout.error = if (!etGasAmount.text.isNullOrBlank()) null else "Gas Amount is required"

    }

}

