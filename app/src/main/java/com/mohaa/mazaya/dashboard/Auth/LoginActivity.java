package com.mohaa.mazaya.dashboard.Auth;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mohaa.mazaya.dashboard.Controllers.BaseActivity;
import com.mohaa.mazaya.dashboard.HomeActivity;
import com.mohaa.mazaya.dashboard.R;
import com.mohaa.mazaya.dashboard.helper.SharedPrefManager;
import com.mohaa.mazaya.dashboard.manager.APIUrl;
import com.mohaa.mazaya.dashboard.manager.ApiServices.UserAPIService;
import com.mohaa.mazaya.dashboard.manager.Response.UserResponse;
import com.mohaa.mazaya.dashboard.manager.RetrofitApi;
import com.mohaa.mazaya.dashboard.networksync.CheckInternetConnection;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.core.content.res.ResourcesCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "Login" ;


    //Layout_Views
    private EditText mEmailField;
    private EditText mPasswordField;
    //
    private TextView appname;
    //ProgressDialog
    private ProgressDialog mLoginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new CheckInternetConnection(this).checkConnection();

        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        appname = findViewById(R.id.appname);
        appname.setTypeface(typeface);

        // Views
        mEmailField = findViewById(R.id.email);
        mPasswordField = findViewById(R.id.password);
        TextView mSignInButton = findViewById(R.id.normal_login_button);
        TextView mRegButton = findViewById(R.id.register_now);//forward to Reg_Activity
        mRegButton.setOnClickListener( new View.OnClickListener()
           {
               @Override
               public void onClick(View view) {
                   senttoreg();//Intent
               }
           }
        );
        mLoginProgress = new ProgressDialog(this);
        // printKeyHash();

        // Click listeners
        mSignInButton.setOnClickListener(this);

    }

    private void senttoreg() {
        Intent mainIntent = new Intent( LoginActivity.this , RegisterActivity.class );
        startActivity(mainIntent);
        finish();//unable to return
    }
    @Override
    public void onStart() {
        super.onStart();


    }


    private void checkFBLogin(String id, String email, String name, String profilePicUrl) {
        Toast.makeText(LoginActivity.this, "Welcome : " + name , Toast.LENGTH_SHORT).show();

    }



    public ProgressDialog mProgressDialog;
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading....");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    private void printKeyHash() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature  : packageInfo.signatures)
            {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.e("KEYHASH", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void signIn_Auth() {
        Log.d(TAG, "signIn");//Log Details
        String email = mEmailField.getText().toString().trim();
        String password = mPasswordField.getText().toString().trim();
        if (validateUsername(email) && validatePassword(password)) { //Username and Password Validation {
            showProgressDialog();

            UserAPIService service = RetrofitApi.retrofitRead().create(UserAPIService.class);


            Call<UserResponse> call = service.userLogin(email, password);

            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    hideProgressDialog();
                    if (!response.body().getError()) {
                        finish();
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getUser());
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }


    }



    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(mEmailField.getText().toString())) {
            mEmailField.setError(getString(R.string.required));
            result = false;
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(mPasswordField.getText().toString())) {
            mPasswordField.setError(getString(R.string.required));
            result = false;
        } else {
            mPasswordField.setError(null);
        }

        return result;
    }

    private boolean validatePassword(String pass) {


        if (pass.length() < 4 || pass.length() > 20) {
            mPasswordField.setError("Password Must consist of 4 to 20 characters");
            return false;
        }
        return true;
    }

    private boolean validateUsername(String email) {

        if (email.length() < 4 || email.length() > 30) {
            mEmailField.setError("Email Must consist of 4 to 30 characters");
            return false;
        } else if (!email.matches("^[A-za-z0-9.@]+")) {
            mEmailField.setError("Only . and @ characters allowed");
            return false;
        } else if (!email.contains("@") || !email.contains(".")) {
            mEmailField.setError("Email must contain @ and .");
            return false;
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("Login CheckPoint","LoginActivity resumed");
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

    }

    @Override
    protected void onStop () {
        super.onStop();
        Log.e("Login CheckPoint","LoginActivity stopped");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent loginIntent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(loginIntent);
        finish();
    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.normal_login_button) {
            signIn_Auth();
        }
    }
}
