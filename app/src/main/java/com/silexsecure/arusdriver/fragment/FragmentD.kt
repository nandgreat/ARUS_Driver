package com.tayfuncesur.stepperdemo.frags

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.silexsecure.arusdriver.R
import com.silexsecure.arusdriver.util.MyApplication.json
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_d.*

class FragmentD : Fragment() {

    private lateinit var etDescription: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view: View = inflater.inflate(R.layout.fragment_d, container, false)

//        etDescription = view.findViewById(R.id.etDescription)

        if (json.has("Description"))
            etDescription.setText(json.getString("Description"))

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("MyValues", json.toString())

//        PushDownAnim.setPushDownAnimTo(llNext).setScale(PushDownAnim.MODE_STATIC_DP, 5F).setOnClickListener {
//
//            val eMail = etDescription.text.toString()
//
//            if (eMail.isEmpty()) {
//                Toast.makeText(context, "Enter Description to continue", Toast.LENGTH_LONG).show()
//            } else {
//                json.put("Description", eMail)
//                view.findNavController().navigate(R.id.fragmentDtoE)
////                activity?.findViewById<Stepper>(R.id.Stepper)?.forward()
//            }
//        }

        PushDownAnim.setPushDownAnimTo(backArrow).setScale(PushDownAnim.MODE_STATIC_DP, 5F).setOnClickListener {
            view.findNavController().popBackStack()
//            activity?.findViewById<Stepper>(R.id.Stepper)?.back()
        }
    }
}
