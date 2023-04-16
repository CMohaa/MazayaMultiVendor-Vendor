package com.mohaa.mazaya.dashboard.networksync;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.Toast;




/**
 * Created by kshitij on 12/13/17.
 */

public class CheckInternetConnection {

    Context ctx;

    public CheckInternetConnection(Context context){
        ctx=context;
    }

    public void checkConnection(){

        if(!isInternetConnected()) {

            Toast.makeText(ctx, "no internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnectedOrConnecting();

    }
}
