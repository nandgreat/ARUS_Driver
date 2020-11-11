package com.silexsecure.arusdriver;

import android.app.Application;
import android.content.Context;

import com.google.android.libraries.places.api.Places;

import org.json.JSONObject;

public class BaseApplication extends Application {

    private static String TAG = "BaseApplicationTAG";

    String apiKey = "AIzaSyANAGpY8YOYnmeOdZri8K6accUoWTezc0E";

    public static JSONObject json = new JSONObject();

    public static final String KEY_ID = "orderID";
    public static final String KEY_CUSTOMER = "customer";
    public static final String KEY_DRIVER = "driver";
    public static final String KEY_PRODUCT = "product";
    public static final String KEY_PRICE_PER_LITRE = "pricePerLitre";
    public static final String KEY_PRICE_PER_KG = "pricePerKg";
    public static final String KEY_QTY_KG = "quantityInKg";
    public static final String KEY_QTY_LT = "quantityInLitres";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_PAYMENT_TYPE = "paymentType";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_LATTITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_PAYMENT_STATUS = "paymentStatus";
    public static final String KEY_DELIVERY_STATUS = "deliveryStatus";
    public static final String KEY_ORDER_TIME = "orderTime";


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Places.
        Places.initialize(getApplicationContext(), apiKey);

//        ConnectivityReceiver.init(this);
//        EmojiManager.install(new GoogleEmojiProvider());

//        EThree.initialize(getApplicationContext(),
//                onGetTokenUserOneCallback).addCallback(onInitUserOneListener);

//        String admobAppId = getString(R.string.admob_app_id);
//        if (!TextUtils.isEmpty(admobAppId))
//            MobileAds.initialize(this, admobAppId);
//        TestFairy.begin(this, "SDK-TQmuJzG9");
    }
}