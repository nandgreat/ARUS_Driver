package com.silexsecure.arusdriver;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.silexsecure.arusdriver.service.TrackerService;
import com.silexsecure.arusdriver.util.Helper;

public class SplashScreenActivity extends AppCompatActivity {
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    /**
     * Called when the activity is first created.
     */
    Thread splashTread = new Thread() {
        @Override
        public void run() {
            try {
                int waited = 0;
                // Splash screen pause time
                while (waited < 3500) {
                    sleep(100);
                    waited += 100;
                }

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
                if (!previouslyStarted) {
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
                    edit.commit();

                    Intent onboardingIntent = new Intent(SplashScreenActivity.this, OnboardingActivity.class);
                    startActivity(onboardingIntent);
                } else {
                    Helper helper = new Helper(SplashScreenActivity.this);
                    if (helper.getLoggedInUser() == null) {
                        stopService(new Intent(SplashScreenActivity.this, TrackerService.class));
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("/topics/Enter_topic");
                        startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this,
                                HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }
                }
                SplashScreenActivity.this.finish();
            } catch (InterruptedException e) {
                // do nothing
            } finally {
                SplashScreenActivity.this.finish();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        StartAnimations();
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        LinearLayout iv = findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashTread.start();
    }
}
