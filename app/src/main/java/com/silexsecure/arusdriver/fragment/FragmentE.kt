package com.tayfuncesur.stepperdemo.frags

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.textfield.TextInputEditText
import com.silexsecure.arusdriver.util.SessionManager
import java.io.IOException
import java.util.*


class FragmentE : Fragment() {

    private var stateId: Int = 0

    private lateinit var etState: TextInputEditText
    private lateinit var sessionManager: SessionManager

    private var myProgressDialog: ProgressDialog? = null


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        myProgressDialog = ProgressDialog(context)

        sessionManager = SessionManager(context)


        var view: View = inflater.inflate(com.silexsecure.arusdriver.R.layout.fragment_e, container, false)

        val etArea = view.findViewById(com.silexsecure.arusdriver.R.id.etArea) as EditText

        etState = view.findViewById(com.silexsecure.arusdriver.R.id.etState)

//        Log.d("MyMainValues", json.toString())

        etState.setOnClickListener {

            showDialog()

        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        PushDownAnim.setPushDownAnimTo(progress).setScale(PushDownAnim.MODE_STATIC_DP, 5F).setOnClickListener {

//            activity?.findViewById<Stepper>(com.silexsecure.empireapp.R.id.Stepper)?.progress(3)?.addOnCompleteListener {
//
//                val area = etArea?.text.toString()
//                val state = etState.text.toString()
//
//                if (area.isEmpty() || state.isEmpty()) {
//                    Toast.makeText(context, "Enter Location Details to continue", Toast.LENGTH_LONG).show()
//                } else {
//
//                    myProgressDialog?.setTitle("Please Wait")
//                    myProgressDialog?.setMessage("Submitting your report")
//                    myProgressDialog?.show()
//
//                    MyApplication.json.put("StatesID", stateId)
//                    MyApplication.json.put("Area", area)
//                    MyApplication.json.put("ConsumerID", MyApplication.userId)
////                    MyApplication.json.put("Latitude", 9.59593)
////                    MyApplication.json.put("Longitude", 7.2324)
//
//                    val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json"), json.toString())
//
//                    Log.d("Requestbody", json.toString())
//
//                    val call: Call<ReportResponse> = apiService.registrationPost(requestBody)
//
//                    call.enqueue(
//                            object : Callback<ReportResponse> {
//
//                                override fun onResponse(call: Call<ReportResponse>, response: Response<ReportResponse>) {
//                                    if (response.isSuccessful) {
//                                        var ReportResponse = response.body() // Use it or ignore it
//
//                                        myProgressDialog?.dismiss()
//
//                                        Log.d("RecordStat", "Record Added")
//
////                                        sessionManager.createLoginSession(
////                                                MainActivity.name, MainActivity.userId,
////                                                MainActivity.phone, MainActivity.operator,
////                                                MainActivity.operatorID, response.body().records,
////                                                MainActivity.token
////                                        )
//
//                                        Toast.makeText(context, "Report sent Successfully", Toast.LENGTH_LONG).show()
//                                        MyApplication.json.remove("ClassOfServiceID")
//                                        MyApplication.json.remove("ConsumerID")
//                                        MyApplication.json.remove("Area")
//                                        MyApplication.json.remove("StatesID")
//                                        MyApplication.json.remove("Description")
//                                        MyApplication.json.remove("FrequencyOfOccurenceID")
//                                        MyApplication.json.remove("ServiceTypeID")
//                                        MyApplication.json.remove("RatingID")
//
//                                        val intent = Intent(this@FragmentE.context, MainActivity::class.java)
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                                        startActivity(intent)
//
//                                    } else {
////                                        Toast.makeText(context, "Failed to add item", Toast.LENGTH_SHORT).show()
//                                        Log.d("RecordStat", "Record not Added" + response.code() + " " + response.message())
//                                    }
//                                }
//
//                                override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
////                                    Toast.makeText(context, "Failed to add item", Toast.LENGTH_SHORT).show()
//                                    Log.d("NetworkError", "Record not Added")
//                                }
//                            })
//
//                    activity?.findViewById<View>(com.silexsecure.empireapp.R.id.StepperView)?.background =
//                            ContextCompat.getDrawable(context!!, com.silexsecure.empireapp.R.drawable.success_gradient)
//                }
//            }

//            PushDownAnim.setPushDownAnimTo(backArrow).setScale(PushDownAnim.MODE_STATIC_DP, 5F).setOnClickListener {
//                view.findNavController().popBackStack()
//                activity?.findViewById<Stepper>(com.silexsecure.empireapp.R.id.Stepper)?.stop()
//                activity?.findViewById<Stepper>(com.silexsecure.empireapp.R.id.Stepper)?.back()
//            }

//        PushDownAnim.setPushDownAnimTo(progressStop).setScale(PushDownAnim.MODE_STATIC_DP, 5F).setOnClickListener {
//            activity?.findViewById<Stepper>(R.id.Stepper)?.stop()
//        }

//        }
    }

    private fun lauchDialogSuccess() {

        SweetAlertDialog(this@FragmentE.context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Sucess!")
                .setContentText("Service Report Sent")
                .show()
    }

    // Method to show an alert dialog with single choice list items
    private fun showDialog() {
        // Late initialize an alert dialog object
        lateinit var dialog: AlertDialog

        // Initialize an array of colors
        val array = resources.getStringArray(com.silexsecure.arusdriver.R.array.states)

        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(this@FragmentE.requireContext())

        // Set a title for alert dialog
        builder.setTitle("Select a State")

        builder.setSingleChoiceItems(com.silexsecure.arusdriver.R.array.states, (stateId.minus(1))) { _, which ->
            // Get the dialog selected item
            val color = array[which]

            stateId = which + 1


            // Try to parse user selected color string
            try {
                // Change the layout background color using user selection

                etState.setText(color)

            } catch (e: IllegalArgumentException) {
                // Catch the color string parse exception
//                toast("$color color not supported.")
            }

            // Dismiss the dialog
            dialog.dismiss()
        }

        // Initialize the AlertDialog using builder object
        dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }

    private fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private inner class MyLocationListener : LocationListener {

        override fun onLocationChanged(loc: Location) {
            //            editLocation.setText("");
            //            pb.setVisibility(View.INVISIBLE);
            Toast.makeText(
                    context,
                    "Location changed: Lat: " + loc.latitude + " Lng: "
                            + loc.longitude, Toast.LENGTH_SHORT).show()
            val longitude = "Longitude: " + loc.longitude
            Log.v(TAG, longitude)
            val latitude = "Latitude: " + loc.latitude
            Log.v(TAG, latitude)

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
