package com.mohaa.mazaya.dashboard.Utils;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.mohaa.mazaya.dashboard.helper.SharedPrefManager;


import androidx.fragment.app.FragmentActivity;

/**
 * Created by Mohamed El Sayed
 */
public class LogoutHelper {

    private static final String TAG = LogoutHelper.class.getSimpleName();
    private static ClearImageCacheAsyncTask clearImageCacheAsyncTask;

    public static void signOut(FragmentActivity fragmentActivity) {

        if (SharedPrefManager.getInstance(fragmentActivity.getApplicationContext()).isLoggedIn()) {
            logoutFirebase(fragmentActivity.getApplicationContext());
        }
        if (clearImageCacheAsyncTask == null) {
            clearImageCacheAsyncTask = new ClearImageCacheAsyncTask(fragmentActivity.getApplicationContext());
            clearImageCacheAsyncTask.execute();
        }
    }



    private static void logoutFirebase(Context context) {
        //OrdersBase.getInstance().dispose();//TODO
        try {
            SharedPrefManager.getInstance(context).logout();
        }
        catch (Exception e)
        {
            Log.d(TAG, "logoutFirebase: " + e.getLocalizedMessage());
        }
    }


    private static class ClearImageCacheAsyncTask extends AsyncTask<Void, Void, Void> {
        private Context context;

        public ClearImageCacheAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Glide.get(context.getApplicationContext()).clearDiskCache();
            return null;
        }

        @Override
        protected void onPostExecute(Void o) {
            super.onPostExecute(o);
            clearImageCacheAsyncTask = null;
            Glide.get(context.getApplicationContext()).clearMemory();
        }
    }
}
