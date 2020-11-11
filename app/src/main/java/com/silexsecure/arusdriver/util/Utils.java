package com.silexsecure.arusdriver.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.silexsecure.arusdriver.model.Cart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Utils {

    public static void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    public static String correctPhoneNumber(String phone) {
        if (phone.substring(0, 4).contentEquals("+234"))
            phone = "0" + phone.substring(4, 14);
        else if (phone.substring(0, 3).contentEquals("234"))
            phone = "0" + phone.substring(3, 13);

        return phone;

    }

    public static String moneyFormat(String amount){

        DecimalFormat formatter = new DecimalFormat("###,###,###.00");
        return formatter.format(Double.parseDouble(amount));
    }

    public static int getCartCount(Context context){

        Helper helper = new Helper(context);

        ArrayList<Cart> cartList = helper.getCartPreference();

        if (cartList != null)
            return cartList.size();
        else
            return 0;
    }

    public static String timeofDay(){
        Calendar cal = Calendar.getInstance();
        Date date = new Date();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        if (hour < 12 ) {
            return "Morning";
        }else if(hour >= 12 && hour < 17){
            return "Afternoon";
        }else{
            return "Evening";
        }
    }
}
