package com.silexsecure.arusdriver.cartowing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.flutterwave.raveandroid.RaveConstants
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.RavePayManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.silexsecure.arusdriver.PaymentSuccessActivity
import com.silexsecure.arusdriver.R
import com.silexsecure.arusdriver.cartowing.CarTowingDetails.Companion.KEY_ADDRESS
import com.silexsecure.arusdriver.cartowing.CarTowingDetails.Companion.KEY_AMOUNT
import com.silexsecure.arusdriver.cartowing.CarTowingDetails.Companion.KEY_CAR_MAKE
import com.silexsecure.arusdriver.cartowing.CarTowingDetails.Companion.KEY_ID
import com.silexsecure.arusdriver.cartowing.CarTowingDetails.Companion.KEY_LATTITUDE
import com.silexsecure.arusdriver.cartowing.CarTowingDetails.Companion.KEY_LONGITUDE
import com.silexsecure.arusdriver.cartowing.CarTowingDetails.Companion.KEY_PAYMENT_METHOD
import com.silexsecure.arusdriver.cartowing.CarTowingDetails.Companion.KEY_PAYMENT_STATUS
import com.silexsecure.arusdriver.cartowing.CarTowingDetails.Companion.KEY_REQUEST_FEE
import com.silexsecure.arusdriver.cartowing.CarTowingDetails.Companion.KEY_REQUEST_STATUS
import com.silexsecure.arusdriver.cartowing.CarTowingDetails.Companion.KEY_VEHICLE_TYPE
import com.silexsecure.arusdriver.cartowing.CarTowingDetails.Companion.carTypeID
import com.silexsecure.arusdriver.fragment.FragmentA
import com.silexsecure.arusdriver.model.CarTowing
import com.silexsecure.arusdriver.navfragments.HomeFragment.helper
import com.silexsecure.arusdriver.util.Config
import com.silexsecure.arusdriver.util.SessionManager
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_towing_summary.*
import kotlinx.android.synthetic.main.fragment_towing_summary.view.*
import kotlinx.android.synthetic.main.fragment_towing_summary.view.tvAddress
import java.util.*

class TowingSummaryFragment : Fragment() {

    private lateinit var etDescription: EditText

    val cartowing = CarTowing()

    private lateinit var database: DatabaseReference

    lateinit var tvCarMake: TextView
    lateinit var tvCarColor: TextView
    lateinit var tvPaymentMethod: TextView
    lateinit var tvCarModel: TextView
    lateinit var tvCarYear: TextView
    lateinit var tvCarFault: TextView
    lateinit var tvRegNumber: TextView
    lateinit var tvRequestID: TextView
    lateinit var tvRequestFee: TextView
    lateinit var tvDescription: TextView
    lateinit var tvCarType: TextView
    lateinit var ivCarType: ImageView
    private var phone: String? = null

    private lateinit var bSendRequest: Button

    private var firstName: String? = null
    private var lastName: String? = null

    private lateinit var sessionManager: SessionManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view: View = inflater.inflate(R.layout.fragment_towing_summary, container, false)

        database = FirebaseDatabase.getInstance().reference



        phone = helper.loggedInUser.phone
        firstName = helper.loggedInUser.firstname
        lastName = helper.loggedInUser.lastname

        sessionManager = SessionManager(context)

        phone = helper.loggedInUser.phone

        tvCarMake = view.findViewById(R.id.tvCarMake)
        tvRequestID = view.findViewById(R.id.tvRequestID)
        tvRequestFee = view.findViewById(R.id.tvRequestFee)
        tvCarType = view.findViewById(R.id.tvCarType)
        ivCarType = view.findViewById(R.id.ivCarType)
        bSendRequest = view.findViewById(R.id.bSendRequest)

        if (!KEY_CAR_MAKE.isBlank()) tvCarMake.text = KEY_CAR_MAKE
        if (!KEY_ID.isBlank()) tvRequestID.text = KEY_ID
        if (!KEY_REQUEST_FEE.isBlank()) view.tvRequestFee.text = KEY_REQUEST_FEE
        if (!KEY_ADDRESS.isBlank()) view.tvAddress.text = KEY_ADDRESS
        if (!KEY_VEHICLE_TYPE.isBlank()) {
            tvCarType.text = KEY_VEHICLE_TYPE
            if (carTypeID == 0) ivCarType.setImageDrawable(resources.getDrawable(R.drawable.sedan))
            if (carTypeID == 1) ivCarType.setImageDrawable(resources.getDrawable(R.drawable.micro))
            if (carTypeID == 2) ivCarType.setImageDrawable(resources.getDrawable(R.drawable.minivan))
            if (carTypeID == 3) ivCarType.setImageDrawable(resources.getDrawable(R.drawable.suv))
            if (carTypeID == 4) ivCarType.setImageDrawable(resources.getDrawable(R.drawable.truck))
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PushDownAnim.setPushDownAnimTo(bSendRequest).setScale(PushDownAnim.MODE_STATIC_DP, 5F).setOnClickListener {

            cartowing.requestID = KEY_ID
            cartowing.vehicleType = KEY_VEHICLE_TYPE
            cartowing.carMake = KEY_CAR_MAKE
            cartowing.requestTime = Date().time.toString()
            cartowing.paymentStatus = KEY_PAYMENT_STATUS
            cartowing.longitude = KEY_LONGITUDE
            cartowing.lattitude = KEY_LATTITUDE
            cartowing.address = KEY_ADDRESS
            cartowing.paymentAmount = KEY_AMOUNT
            cartowing.paymentMethod = KEY_PAYMENT_METHOD
            cartowing.requestStatus = KEY_REQUEST_STATUS
            cartowing.requestStatus = KEY_REQUEST_FEE

//            progressBar.visibility = View.VISIBLE

            validateInformation()

//            initFirebase(cartowing)

        }

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
        val narration = "Buying ${FragmentA.KEY_QTY_KG}Kg Gas"
        if (valid) {
            val ravePayManager = RavePayManager(this).setAmount(1500.0)
                    .setCountry(country)
                    .setCurrency(currency)
                    .setEmail(email)
                    .setfName(fName)
                    .setlName(lName)
                    .setPhoneNumber(phoneNumber)
                    .setNarration(narration)
                    .setPublicKey(Config.PUBLIC_KEY)
                    .setEncryptionKey(Config.ENCRYPTION_KEY)
                    .setTxRef(referencce)
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


    private fun initFirebase(cartowing: CarTowing) {

        database.child("car_towing").child(phone.toString()).push().setValue(cartowing).addOnCompleteListener {
            progressBar.visibility = View.GONE
            val intent = Intent(activity, PaymentSuccessActivity::class.java)
            activity?.startActivity(intent)
            activity?.finish()

        }.addOnFailureListener {

            progressBar.visibility = View.GONE
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            val message = data.getStringExtra("response")
            if (message != null) {
                Log.d("rave response", message)
            }
            when (resultCode) {
                RavePayActivity.RESULT_SUCCESS -> {
                    initFirebase(cartowing)
                    //                Toast.makeText(this, "SUCCESS " + message, Toast.LENGTH_SHORT).show();
                }
                RavePayActivity.RESULT_ERROR -> {
                    Toast.makeText(context, "ERROR $message", Toast.LENGTH_SHORT).show()
                }
                RavePayActivity.RESULT_CANCELLED -> {
                    Toast.makeText(context, "CANCELLED $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
