package com.mohaa.mazaya.dashboard.Controllers.activities_traders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mohaa.mazaya.dashboard.Controllers.BaseActivity;
import com.mohaa.mazaya.dashboard.R;
import com.mohaa.mazaya.dashboard.helper.SharedPrefManager;
import com.mohaa.mazaya.dashboard.interfaces.OnRemoveClickListener;
import com.mohaa.mazaya.dashboard.manager.APIUrl;
import com.mohaa.mazaya.dashboard.manager.ApiServices.ProductsAPIService;
import com.mohaa.mazaya.dashboard.manager.ApiServices.TraderAPIService;
import com.mohaa.mazaya.dashboard.manager.Response.TraderResponse;
import com.mohaa.mazaya.dashboard.manager.RetrofitApi;
import com.mohaa.mazaya.dashboard.models.Trader;
import com.mohaa.mazaya.dashboard.networksync.CheckInternetConnection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Random;

import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddTraderActivity extends BaseActivity implements OnRemoveClickListener {

    private static final String TAG = AddTraderActivity.class.getSimpleName();
    private Bitmap trader_logo;
    private final int GALLERY = 1;
    private final int REQUEST_CODE_PERMISSIONS  = 1;
    private final int REQUEST_CODE_READ_STORAGE = 2;
    private ImageView setup_img;
    private TextView add_to_product;

    private com.rengwuxian.materialedittext.MaterialEditText trader_name;
    private com.rengwuxian.materialedittext.MaterialEditText trader_desc;
    private com.rengwuxian.materialedittext.MaterialEditText trader_location;
    //ProgressDialog
    private ProgressDialog mLoginProgress;
    Toolbar toolbar;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trader);
        //Progress Dialog
        /*
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
                */

        // Set Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hide Title
        TextView titleToolbar = findViewById(R.id.appname);
        titleToolbar.setVisibility(View.GONE);

        // Back Button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        //set auto complete


        mLoginProgress = new ProgressDialog(this);

        setup_img = findViewById(R.id.trader_logo);
        trader_name = findViewById(R.id.trader_name);
        trader_desc = findViewById(R.id.trader_desc);


        trader_location = findViewById(R.id.trader_location);

        add_to_product = findViewById(R.id.add_to_product);
        add_to_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = trader_name.getText().toString();//name
                final String desc = trader_desc.getText().toString();


                final String location = trader_location.getText().toString();

                if (!TextUtils.isEmpty(name) &&!TextUtils.isEmpty(desc)  &&!TextUtils.isEmpty(location) && getTrader_logo() != null) {
                    mLoginProgress.setTitle(getResources().getString(R.string.loading));
                    mLoginProgress.setMessage(getResources().getString(R.string.please_wait));
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    uploadImage(getTrader_logo() , name ,desc  , location);
                }

            }
        });
        setup_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, GALLERY);
            }
        });
        /*
        setup_img.setOnClickListener(view -> mediaSelector.startChooseImageActivity(this, MediaSelector.CropType.Rectangle, result -> {
            Uri file = Uri.fromFile(new File(result));
            setNew_image_file(new File(file.getPath()));
            try{
                setup_img.setImageURI(Uri.fromFile(getNew_image_file()));
            }
            catch (Exception e) {
                Toast.makeText(this, "Error" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }));
         */
    }

    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
    }

    private void uploadImage(Bitmap bitmap ,String name ,String desc ,String  location ){

        int owner_id = SharedPrefManager.getInstance(this).getUser().getId();
        String owner_name = SharedPrefManager.getInstance(this).getUser().getName();
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        String imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());


        ProductsAPIService api = RetrofitApi.retrofitWrite().create(ProductsAPIService.class);

        Log.d("helloo",encodedImage +"   >>"+imgname);
        Log.d("imggggg","   >>"+imgname);
        Call<String> call_ = api.setTraderimage(imgname,encodedImage , owner_id);

        call_.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call_, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("Upload", response.body().toString());

                        TraderAPIService api = RetrofitApi.retrofitWrite().create(TraderAPIService.class);

                        //Defining the trader object as we need to pass it with the call
                        Trader trader = new Trader(owner_id , owner_name, name, desc , APIUrl.BASE_URL_TradersIMG +imgname + ".JPG" ,location , "Food" , 0 , 0 , 0 , 20 , 0);

                        // Log.d("helloo",encodedImage +"   >>"+imgname);
                        //Log.d("imggggg","   >>"+imgname);

                        //defining the call
                        Call<TraderResponse> call = api.createTrader(
                                trader.getAdmin_id(),
                                trader.getAdmin_name(),
                                trader.getMerchant_name(),
                                trader.getMerchant_desc(),
                                trader.getThumb_image(),
                                trader.getLocation(),
                                trader.getType(),
                                trader.getRate(),
                                trader.getRatecount(),
                                trader.getMerchant_credit(),
                                trader.getCountry_code(),
                                trader.getCount()

                        );

                        call.enqueue(new Callback<TraderResponse>() {
                            @Override
                            public void onResponse(Call<TraderResponse> call, Response<TraderResponse> response) {

                                //Toast.makeText()
                                hideProgressDialog();
                                Toast.makeText(AddTraderActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onFailure(Call<TraderResponse> call, Throwable t) {
                                hideProgressDialog();
                                Toast.makeText(AddTraderActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                        //Toast.makeText(AddTraderActivity.this, "Image Uploaded Successfully!!", Toast.LENGTH_SHORT).show();

                    } else {
                        Log.i("Upload", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    setup_img.setImageBitmap(bitmap);
                    setTrader_logo(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddTraderActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public static long generateRandom(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }


    public Bitmap getTrader_logo() {
        return trader_logo;
    }

    public void setTrader_logo(Bitmap trader_logo) {
        this.trader_logo = trader_logo;
    }

    @Override
    public void onRemoveClickListener(int position) {

    }
}
