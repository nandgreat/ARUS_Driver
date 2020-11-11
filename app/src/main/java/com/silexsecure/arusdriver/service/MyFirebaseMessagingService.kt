package com.silexsecure.arusdriver.service
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.silexsecure.arusdriver.MechanicTransitActivity
import com.silexsecure.arusdriver.R
import com.silexsecure.arusdriver.TransitActivity
import com.silexsecure.arusdriver.util.SharedPreferenceHelper
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {


    private val ADMIN_CHANNEL_ID = "admin_channel"
    private val TAG = "MyFirebaseMessaging"

    val sharedPreferenceHelper = SharedPreferenceHelper(this)


    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random().nextInt(3000)
        val serviceType = p0.data["serviceType"]

        val intent = Intent(this, if(serviceType!!.contentEquals("mechanic")) MechanicTransitActivity::class.java else TransitActivity::class.java)

        if(serviceType.contentEquals("mechanic")){

            sharedPreferenceHelper.setStringPreference("vehicleType", p0.data["vehicleType"])
            sharedPreferenceHelper.setStringPreference("carMake", p0.data["carMake"])
            sharedPreferenceHelper.setStringPreference("vehicleColor", p0.data["vehicleColor"])
            sharedPreferenceHelper.setStringPreference("vehicleModel", p0.data["vehicleModel"])
            sharedPreferenceHelper.setStringPreference("regNumber", p0.data["regNumber"])
            sharedPreferenceHelper.setStringPreference("carFault", p0.data["carFault"])
            sharedPreferenceHelper.setStringPreference("description", p0.data["description"])
            sharedPreferenceHelper.setStringPreference("requestKey", p0.data["requestKey"])
            sharedPreferenceHelper.setStringPreference("customerName", p0.data["customerName"])
            sharedPreferenceHelper.setStringPreference("customerPhone", p0.data["customerPhone"])
            sharedPreferenceHelper.setStringPreference("customerImage", p0.data["customerImage"])
            sharedPreferenceHelper.setStringPreference("requestTime", p0.data["requestTime"])
            sharedPreferenceHelper.setStringPreference("paymentStatus", p0.data["paymentStatus"])
            sharedPreferenceHelper.setStringPreference("pickupLongitude", p0.data["pickupLongitude"])
            sharedPreferenceHelper.setStringPreference("pickupLatitude", p0.data["pickupLatitude"])
            sharedPreferenceHelper.setStringPreference("pickupAddress", p0.data["pickupAddress"])
            sharedPreferenceHelper.setStringPreference("paymentAmount", p0.data["paymentAmount"])
            sharedPreferenceHelper.setStringPreference("paymentMethod", p0.data["paymentMethod"])
            sharedPreferenceHelper.setStringPreference("requestStatus", p0.data["requestStatus"])
            sharedPreferenceHelper.setStringPreference("requestFee", p0.data["requestFee"])
            sharedPreferenceHelper.setStringPreference("vehicleYear", p0.data["vehicleYear"])
            sharedPreferenceHelper.setStringPreference("mechanicStatus", p0.data["mechanicStatus"])
            sharedPreferenceHelper.setStringPreference("orderTime", p0.data["orderTime"])
            sharedPreferenceHelper.setStringPreference("serviceType", p0.data["serviceType"])

        }else {

            sharedPreferenceHelper.setStringPreference("carMake", p0.data["carMake"])
            sharedPreferenceHelper.setStringPreference("pickupAddress", p0.data["pickupAddress"])
            sharedPreferenceHelper.setStringPreference("destinationAddress", p0.data["destinationAddress"])
            sharedPreferenceHelper.setStringPreference("pickupLatitude", p0.data["pickupLatitude"])
            sharedPreferenceHelper.setStringPreference("pickupLongitude", p0.data["pickupLongitude"])
            sharedPreferenceHelper.setStringPreference("destinationLatitude", p0.data["destinationLatitude"])
            sharedPreferenceHelper.setStringPreference("destinationLongitude", p0.data["destinationLongitude"])
            sharedPreferenceHelper.setStringPreference("amount", p0.data["amount"])
            sharedPreferenceHelper.setStringPreference("requestKey", p0.data["requestKey"])
            sharedPreferenceHelper.setStringPreference("carType", p0.data["carType"])
            sharedPreferenceHelper.setStringPreference("customerName", p0.data["customerName"])
            sharedPreferenceHelper.setStringPreference("customerPhone", p0.data["customerPhone"])
            sharedPreferenceHelper.setStringPreference("towingStatus", p0.data["towingStatus"])
            sharedPreferenceHelper.setStringPreference("orderTime", p0.data["orderTime"])
        }

        /*
        Apps targeting SDK 26 or above (Android O) must implement notification channels and add its notifications
        to at least one of them.
      */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val largeIcon = BitmapFactory.decodeResource(
            resources,
                R.drawable.arus_logo
        )

        val notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
            .setSmallIcon(R.drawable.arus_logo)
            .setLargeIcon(largeIcon)
            .setContentTitle(p0.data["title"])
            .setContentText(p0.data["message"])
            .setAutoCancel(true)
            .setSound(notificationSoundUri)
            .setContentIntent(pendingIntent)

        //Set notification color to match your app color template
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.color =
                ContextCompat.getColor(applicationContext, android.R.color.background_dark)
        }
        notificationManager.notify(notificationID, notificationBuilder.build())
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels(notificationManager: NotificationManager?) {
        val adminChannelName = "New notification"
        val adminChannelDescription = "Device to device notification"

        val adminChannel: NotificationChannel
        adminChannel = NotificationChannel(
            ADMIN_CHANNEL_ID,
            adminChannelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        notificationManager?.createNotificationChannel(adminChannel)
    }


}
