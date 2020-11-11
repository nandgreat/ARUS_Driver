package com.silexsecure.arusdriver.navfragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.silexsecure.arusdriver.R;
import com.silexsecure.arusdriver.util.Helper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView bLogout;
    private TextView tvPhone, tvUsername;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        bLogout = view.findViewById(R.id.bLogout);

        final Helper helper = new Helper(getContext());

        tvPhone = view.findViewById(R.id.tvPhoneNumber);
        tvUsername = view.findViewById(R.id.tvUsername);

        String username = helper.getLoggedInUser().getFirstname();
        String phone = helper.getLoggedInUser().getPhone();

        tvUsername.setText(username);

        tvPhone.setText(phone);


        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.logout();
            }
        });

//        getContext().getTheme().applyStyle(fontStyleResId, true);


        // Inflate the layout for this fragment
        return view;
    }

}
