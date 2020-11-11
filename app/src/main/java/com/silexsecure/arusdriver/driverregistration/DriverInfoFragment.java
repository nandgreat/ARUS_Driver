package com.silexsecure.arusdriver.driverregistration;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.kusu.loadingbutton.LoadingButton;
import com.silexsecure.arusdriver.R;

import com.silexsecure.arusdriver.RegisterActivity;
import com.silexsecure.arusdriver.util.CustomViewPager;

import java.util.Objects;

public class DriverInfoFragment extends Fragment {

    LoadingButton bContinue;
    CustomViewPager viewPager;
    ImageView ivDriverLicense;
    ProgressBar pbProgress;

    EditText etFirstName, etLastName, etEmail, etPhone, etPassword, etConfirmPassword;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_driver_info, container, false);

        bContinue = view.findViewById(R.id.bContinue);
        viewPager = Objects.requireNonNull(getActivity()).findViewById(R.id.viewpager_main);
        pbProgress = view.findViewById(R.id.pbProgress);
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etEmail = view.findViewById(R.id.etEmail);
        etPhone = view.findViewById(R.id.etPhone);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);

//        RegisterActivity.Companion.setCurrentTab(0);


        bContinue.setOnClickListener(v -> {

            if (etFirstName.getText().toString().isEmpty() || etLastName.getText().toString().isEmpty() ||
                    etEmail.getText().toString().isEmpty() || etPhone.getText().toString().isEmpty() ||
                    etPassword.getText().toString().isEmpty() || etConfirmPassword.getText().toString().isEmpty()) {

                etFirstName.setError(etFirstName.getText().toString().isEmpty() ? "First Name is required" : null);
                etLastName.setError(etLastName.getText().toString().isEmpty() ? "Last Name is required" : null);
                etEmail.setError(etEmail.getText().toString().isEmpty() ? "Email is required" : null);
                etPhone.setError(etPhone.getText().toString().isEmpty() ? "Phone is required" : null);
                etPassword.setError(etPassword.getText().toString().isEmpty() ? "Password is required" : null);
                etConfirmPassword.setError(etConfirmPassword.getText().toString().isEmpty() ? "Confirm Password is required" : null);
            } else if (!etPassword.getText().toString().contentEquals(etConfirmPassword.getText().toString())) {
                etConfirmPassword.setError(etConfirmPassword.getText().toString().isEmpty() ? "Password does not match" : null);
            } else {

                String myPhone = etPhone.getText().toString();

                if (myPhone.length() == 11 && myPhone.startsWith("0")) {
                    myPhone = "234" + myPhone.substring(1, 11);

                    RegisterActivity.Companion.getRegisterRequest().setFullname(etFirstName.getText().toString().concat(" ").concat(etLastName.getText().toString()));
                    RegisterActivity.Companion.getRegisterRequest().setEmail(etEmail.getText().toString());
                    RegisterActivity.Companion.getRegisterRequest().setPhone(myPhone);
                    RegisterActivity.Companion.getRegisterRequest().setPassword(etPassword.getText().toString());

                    Log.d("MYTAG", "onCreateView: " + myPhone);

                    viewPager.setCurrentItem(1, true);
                }else{
                    etPhone.setError("Phone Number is invalid");
                }
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