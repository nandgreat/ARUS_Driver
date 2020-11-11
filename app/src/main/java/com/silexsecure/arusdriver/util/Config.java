package com.silexsecure.arusdriver.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Config {

    public static final String BASE_URL = "https://arrow.bashdev.co/api/";
    public static final String BASE_URL_MAIN = BASE_URL + "";
    public static final String USER_URL = BASE_URL + "user/";
    public static final String LOGIN_URL = BASE_URL + "login";
    public static final String RIDE_HISTORY_URL = BASE_URL + "passenger/rides";
    public static final String NOTIFICATIONS_URL = BASE_URL + "notifications/rides";
    public static String NOTIFY = "NOTIFY";
    public static String SELECTED_CARD = "SELECTED_CARD";
    public static final String PUBLIC_KEY = "FLWPUBK_TEST-ae4457f1ce3ba0593121ece7eaf9aac2-X";
    public static final String ENCRYPTION_KEY = "FLWSECK_TESTe95e6d817dfc";
    public static final String REQUEST_TOWBUG_TOPIC = "Request_A_TowBug"; //topic has to match what the receiver subscribed to
    public static final String REQUEST_MECHANIC_TOPIC = "/topics/Request_A_Mechanic"; //topic has to match what the receiver subscribed to

    String topic = "/topics/Request_A_Mechanic";

    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static final int ERROR_DIALOG_REQUEST = 9001;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9002;
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9003;

    public static Bitmap CompressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public static Bitmap Base64ToBitmap(String base64) {

        byte[] tobyte = Base64.decode(base64, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(tobyte, 0, tobyte.length);

    }

    public static String BitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static char getNairaSymbol() {
        return '\u20A6';
    }

    public static String formatMoney(double value) {

        NumberFormat nf = DecimalFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return getNairaSymbol() + nf.format(value);
    }

    public static String getDateString(long date) {
        String date_string = "The date";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        date_string = sdf.format(new Date(date * 1000));
        return date_string;
    }

    public static String getDateTimeString(long date) {
        String date_string = "The date";
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMMM, yyyy hh:mm:ss a");
        date_string = sdf.format(new Date(date));

        return date_string;
    }

    public static String getDateDay(long date) {
        String date_string = "The date";
        SimpleDateFormat sdf = new SimpleDateFormat("d, MMM");
        date_string = sdf.format(new Date(date));

        return date_string;
    }

    public static String getDateYear(long date) {
        String date_string = "The date";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        date_string = sdf.format(new Date(date));

        return date_string;
    }

    public static String getDateMonth(long date) {
        String date_string = "The date";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM");
        date_string = sdf.format(new Date(date));

        return date_string;
    }

    public static String getDateTime(long date) {
        String date_string = "The date";
        long dtime = new Date(date).getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("k:m");
        sdf.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        date_string = sdf.format(new Date(dtime * 1000));

        return date_string;
    }


}
