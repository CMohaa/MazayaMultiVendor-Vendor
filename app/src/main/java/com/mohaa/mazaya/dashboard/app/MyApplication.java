package com.mohaa.mazaya.dashboard.app;


import android.app.Application;
import android.content.Intent;

import com.mohaa.mazaya.dashboard.MainActivity;
import com.mohaa.mazaya.dashboard.helper.SharedPrefManager;



/**
 * Created by Ravi on 13/05/15.
 */

public class MyApplication extends Application {

    public static final String TAG = MyApplication.class
            .getSimpleName();



    private static MyApplication mInstance;

    private SharedPrefManager pref;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


    public SharedPrefManager getPrefManager() {
        if (pref == null) {
            pref = new SharedPrefManager(this);
        }

        return pref;
    }



    public void logout() {
        pref.logout();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
