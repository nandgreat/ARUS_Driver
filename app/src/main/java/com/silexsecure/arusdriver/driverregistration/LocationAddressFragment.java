package com.silexsecure.arusdriver.driverregistration;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.silexsecure.arusdriver.R;

import com.silexsecure.arusdriver.RegisterActivity;
import com.silexsecure.arusdriver.util.CustomViewPager;

import java.util.Objects;

public class LocationAddressFragment extends Fragment {

    Button bContinue;
    CustomViewPager viewPager;
    ImageView ivDriverLicense;
    ProgressBar pbProgress;

    EditText etAddress, etCity, etState;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_location_address, container, false);

        bContinue = view.findViewById(R.id.bContinue);
        viewPager = Objects.requireNonNull(getActivity()).findViewById(R.id.viewpager_main);
        pbProgress = view.findViewById(R.id.pbProgress);
        etAddress = view.findViewById(R.id.etAddress);
        etCity = view.findViewById(R.id.etCity);
        etState = view.findViewById(R.id.etState);

//        RegisterActivity.Companion.setCurrentTab(0);


        bContinue.setOnClickListener(v -> {

            if (etAddress.getText().toString().isEmpty() || etCity.getText().toString().isEmpty() ||
                    etState.getText().toString().isEmpty()) {

                etAddress.setError(etAddress.getText().toString().isEmpty() ? "Address is required" : null);
                etCity.setError(etCity.getText().toString().isEmpty() ? "City is required" : null);
                etState.setError(etState.getText().toString().isEmpty() ? "State is required" : null);

            } else {

                RegisterActivity.Companion.getRegisterRequest().setAddress(etAddress.getText().toString());
                RegisterActivity.Companion.getRegisterRequest().setLga(etCity.getText().toString());
                RegisterActivity.Companion.getRegisterRequest().setState(etState.getText().toString());

                viewPager.setCurrentItem(3, true);
            }

        });

        // Inflate the layout for this fragment
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(DriverInfoFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
    }


}