package com.silexsecure.arusdriver.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.silexsecure.arusdriver.MainActivity;
import com.silexsecure.arusdriver.R;
import com.silexsecure.arusdriver.util.NotificationConfig;
import com.silexsecure.arusdriver.util.NotificationUtils;


import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = MessagingService.class.getSimpleName();
    private static final Object ADMIN_CHANNEL_ID = "TowingChannel";

    private NotificationUtils notificationUtils;
    private int notificationID;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("data", remoteMessage.getData().toString());

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String data = remoteMessage.getData().get("title");
        String noOfMen = remoteMessage.getData().get("noOfMen");
        String noOfWomen = remoteMessage.getData().get("noOfWomen");
        notificationID = (int) Math.random();

//        sharedPreferenceHelper.setStringPreference("noOfMen", noOfMen!!)
//        sharedPreferenceHelper.setStringPreference("noOfWomen", noOfWomen!!)
//        sharedPreferenceHelper.setStringPreference("noOfChildren", noOfChildren!!)
//        sharedPreferenceHelper.setStringPreference("prefTools", prefTools!!)
//        sharedPreferenceHelper.setStringPreference("clientId", clientId!!)
//        sharedPreferenceHelper.setStringPreference("clientName", clientName!!)
//        // sharedPreferenceHelper.setStringPreference("clientPhoto", clientPhoto!!)
////        sharedPreferenceHelper.setStringPreference("clientPhone", clientPhone!!)
//        sharedPreferenceHelper.setStringPreference("clientAddress", clientAddress!!)
//        sharedPreferenceHelper.setStringPreference("requestKey", requestKey!!)
//        sharedPreferenceHelper.setStringPreference("requestStatus", requestStatus!!)
//        sharedPreferenceHelper.setStringPreference("barberName", barberName!!)
//        sharedPreferenceHelper.setStringPreference("paymentStatus", paymentStatus!!)
//        sharedPreferenceHelper.setStringPreference("paymentAmount", paymentAmount!!)
//        sharedPreferenceHelper.setStringPreference("paymentMethod", paymentMethod!!)
//        sharedPreferenceHelper.setStringPreference("dateAndTime", dateAndTime!!)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT
        );

//        BitmapFactory largeIcon = BitmapFactory.decodeResource(
//                resources,
//                R.drawable.barber_shop_logo
//        );

        Uri notificationSoundUri = RingtoneManager.getDefaultUri(
                RingtoneManager.TYPE_NOTIFICATION
        );
         NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
                this,
                 (String) ADMIN_CHANNEL_ID
         )
                 .setSmallIcon(R.drawable.jiffix_logo)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("message"))
                .setAutoCancel(true)
                .setSound(notificationSoundUri)
                .setContentIntent(pendingIntent);

        //Set notification color to match your app color template
        //Set notification color to match your app color template
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        notificationManager.notify(notificationID, notificationBuilder.build());
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(NotificationConfig.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(NotificationConfig.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        CharSequence adminChannelName  = "New notification";
        String adminChannelDescription = "Device to devie notification";
        NotificationChannel adminChannel;
                adminChannel = new NotificationChannel(
                        (String) ADMIN_CHANNEL_ID,
                adminChannelName,
                NotificationManager.IMPORTANCE_HIGH
        );
//        adminChannel.description = adminChannelDescription;
        adminChannel.enableLights(true);
//        adminChannel.lightColor = Color.RED;
        adminChannel.enableVibration(true);
        notificationManager.createNotificationChannel(adminChannel);
    }
}