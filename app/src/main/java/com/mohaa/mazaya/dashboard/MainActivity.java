package com.mohaa.mazaya.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import com.mohaa.mazaya.dashboard.Auth.LoginActivity;
import com.mohaa.mazaya.dashboard.helper.SharedPrefManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static int TIME_OUT = 3000; //Time to launch the another activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    @Override
    protected void onStart() {
        super.onStart();




        /*
        final ComponentName onBootReceiver = new ComponentName(getApplication().getPackageName(), MyBroadcastReceiver.class.getName());
        if(getPackageManager().getComponentEnabledSetting(onBootReceiver) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED)
            getPackageManager().setComponentEnabledSetting(onBootReceiver,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,PackageManager.DONT_KILL_APP);
        Log.e(TAG, "Notification :  getPackageManager().setComponentEnabledSetting(onBootReceiver,PackageManager.COMPONENT_ENABLED_STATE_ENABLED");
        */



        Typeface typeface = ResourcesCompat.getFont(this, R.font.jomhuriaregular);

        TextView appname= findViewById(R.id.appname);
        appname.setTypeface(typeface);

        YoYo.with(Techniques.Bounce)
                .duration(7000)
                .playOn(findViewById(R.id.logo));

        YoYo.with(Techniques.FadeInUp)
                .duration(5000)
                .playOn(findViewById(R.id.appname));

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                sendtoHome();
            }
        }, TIME_OUT);


    }

    private void sendtoHome() {

        //if user is already logged in openeing the profile activity
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }
        else{
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();//Don't Return AnyMore TO the last page
        }



    }
}
