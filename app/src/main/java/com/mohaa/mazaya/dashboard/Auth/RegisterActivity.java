package com.mohaa.mazaya.dashboard.Auth;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mohaa.mazaya.dashboard.Controllers.BaseActivity;
import com.mohaa.mazaya.dashboard.HomeActivity;
import com.mohaa.mazaya.dashboard.R;
import com.mohaa.mazaya.dashboard.helper.SharedPrefManager;
import com.mohaa.mazaya.dashboard.manager.APIUrl;
import com.mohaa.mazaya.dashboard.manager.ApiServices.UserAPIService;
import com.mohaa.mazaya.dashboard.manager.Response.UserResponse;
import com.mohaa.mazaya.dashboard.manager.RetrofitApi;
import com.mohaa.mazaya.dashboard.models.User;
import com.mohaa.mazaya.dashboard.networksync.CheckInternetConnection;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegisterActivity extends BaseActivity {

    private EditText edtname, edtemail, edtpass;
    private String check,name,email,password;
    public static final String TAG = "RegisterActivity";

    private ProgressDialog mLoginProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        Typeface typeface = ResourcesCompat.getFont(this, R.font.blacklist);
        TextView appname = findViewById(R.id.appname);
        appname.setTypeface(typeface);

        //Progress Dialog
        mLoginProgress = new ProgressDialog(this);



        edtname = findViewById(R.id.name);
        edtemail = findViewById(R.id.email);
        edtpass = findViewById(R.id.password);


        edtname.addTextChangedListener(nameWatcher);
        edtemail.addTextChangedListener(emailWatcher);
        edtpass.addTextChangedListener(passWatcher);



        //validate user details and register user

        TextView button =findViewById(R.id.register);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO AFTER VALDATION
                if ( validateName() && validateEmail() && validatePass()){

                    name=edtname.getText().toString().trim();
                    email=edtemail.getText().toString().trim();
                    password=edtpass.getText().toString().trim();


                    if (!TextUtils.isEmpty(name)) {
                        mLoginProgress.setTitle(getResources().getString(R.string.loading));
                        mLoginProgress.setMessage(getResources().getString(R.string.please_wait));
                        mLoginProgress.setCanceledOnTouchOutside(false);
                        mLoginProgress.show();

                        signUp(email  , password , "Male");
                    }
                    //Validation Success
                    //convertBitmapToString(profilePicture);

                }
            }
        });

        //Take already registered user to login page

        final TextView loginuser=findViewById(R.id.login_now);
        loginuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });

        //take user to reset password




    }

    private void sendRegistrationEmail(final String name, final String emails) {



    }
    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start

    }







    private boolean validatePass() {


        check = edtpass.getText().toString();

        if (check.length() < 4 || check.length() > 20) {
            return false;
        } else if (!check.matches("^[A-za-z0-9@]+")) {
            return false;
        }
        return true;
    }

    private boolean validateEmail() {

        check = edtemail.getText().toString();

        if (check.length() < 4 || check.length() > 40) {
            return false;
        } else if (!check.matches("^[A-za-z0-9.@]+")) {
            return false;
        } else if (!check.contains("@") || !check.contains(".")) {
            return false;
        }

        return true;
    }

    private boolean validateName() {

        check = edtname.getText().toString();

        return !(check.length() < 4 || check.length() > 20);

    }

    //TextWatcher for Name -----------------------------------------------------

    TextWatcher nameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (check.length() < 4 || check.length() > 20) {
                edtname.setError("Name Must consist of 4 to 20 characters");
            }
        }

    };

    //TextWatcher for Email -----------------------------------------------------

    TextWatcher emailWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (check.length() < 4 || check.length() > 40) {
                edtemail.setError("Email Must consist of 4 to 20 characters");
            } else if (!check.matches("^[A-za-z0-9.@]+")) {
                edtemail.setError("Only . and @ characters allowed");
            } else if (!check.contains("@") || !check.contains(".")) {
                edtemail.setError("Enter Valid Email");
            }

        }

    };

    //TextWatcher for pass -----------------------------------------------------

    TextWatcher passWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //none
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //none
        }

        @Override
        public void afterTextChanged(Editable s) {

            check = s.toString();

            if (check.length() < 4 || check.length() > 20) {
                edtpass.setError("Password Must consist of 4 to 20 characters");
            } else if (!check.matches("^[A-za-z0-9@]+")) {
                edtemail.setError("Only @ special character allowed");
            }
        }

    };



    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
    }

    @Override
    protected void onStop () {
        super.onStop();
    }







    private void signUp(String email , String password , String gender) {
        Log.d(TAG, "signUp");


        showProgressDialog();

        UserAPIService service = RetrofitApi.retrofitWrite().create(UserAPIService.class);

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {


                //Defining the user object as we need to pass it with the call
                User user = new User(name, email, password, gender , task.getResult().getToken());

                //defining the call
                Call<UserResponse> call = service.createUser(
                        user.getName(),
                        user.getEmail(),
                        user.getPassword(),
                        user.getGender(),
                        user.getGcmtoken()
                );

                //calling the api
                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        //hiding progress dialog
                        hideProgressDialog();

                        //displaying the message from the response as toast
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();

                        //if there is no error
                        if (!response.body().getError()) {
                            //starting profile activity
                            finish();
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(response.body().getUser());
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        hideProgressDialog();
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }



    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(edtemail.getText().toString())) {
            edtemail.setError(getString(R.string.required));
            result = false;
        } else {
            edtemail.setError(null);
        }

        if (TextUtils.isEmpty(edtpass.getText().toString())) {
            edtpass.setError(getString(R.string.required));
            result = false;
        } else {
            edtpass.setError(null);
        }


        return result;
    }



}


