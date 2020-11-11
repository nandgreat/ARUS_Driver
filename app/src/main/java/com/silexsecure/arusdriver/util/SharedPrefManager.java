package com.silexsecure.arusdriver.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.silexsecure.arusdriver.model.DataCustomer;

/**
 * Created by Belal on 14/04/17.
 */

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "iGoTellApp";

    private static final String KEY_USER_ID = "keyuserid";
    private static final String KEY_USER_NAME = "keyusername";
    private static final String KEY_USER_PHONE = "keyphone";
    private static final String KEY_USER_OPERATOR = "keyoperator";

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(DataCustomer customer) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, Integer.parseInt(customer.getId()));
        editor.putString(KEY_USER_NAME, customer.getUsername());
        editor.putString(KEY_USER_PHONE, customer.getMobileNumber());
        editor.putString(KEY_USER_OPERATOR, customer.getOperatorName());
        editor.apply();
        return true;
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_PHONE, null) != null;
    }

    public DataCustomer getCustomers() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new DataCustomer(
                sharedPreferences.getString(KEY_USER_NAME, null),
                sharedPreferences.getString(KEY_USER_PHONE, null),
                sharedPreferences.getInt(KEY_USER_ID, 0) + "",
                sharedPreferences.getString(KEY_USER_OPERATOR, null)
        );
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}