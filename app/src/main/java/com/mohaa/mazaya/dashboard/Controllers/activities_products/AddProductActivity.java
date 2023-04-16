package com.mohaa.mazaya.dashboard.Controllers.activities_products;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mohaa.mazaya.dashboard.Controllers.BaseActivity;
import com.mohaa.mazaya.dashboard.MainActivity;
import com.mohaa.mazaya.dashboard.R;
import com.mohaa.mazaya.dashboard.Utils.FileUtils;
import com.mohaa.mazaya.dashboard.Utils.InternetConnection;
import com.mohaa.mazaya.dashboard.Utils.ProductsUI;
import com.mohaa.mazaya.dashboard.helper.SharedPrefManager;
import com.mohaa.mazaya.dashboard.interfaces.OnRemoveClickListener;

import com.mohaa.mazaya.dashboard.manager.APIUrl;
import com.mohaa.mazaya.dashboard.manager.ApiServices.CategoryAPIService;
import com.mohaa.mazaya.dashboard.manager.ApiServices.Pdt_ImagesAPIService;
import com.mohaa.mazaya.dashboard.manager.ApiServices.ProductsAPIService;
import com.mohaa.mazaya.dashboard.manager.ApiServices.VariantsAPIService;
import com.mohaa.mazaya.dashboard.manager.Call.Categories;
import com.mohaa.mazaya.dashboard.manager.Call.Variants;
import com.mohaa.mazaya.dashboard.manager.Response.Pdt_ImageResponse;
import com.mohaa.mazaya.dashboard.manager.Response.ProductResponse;
import com.mohaa.mazaya.dashboard.manager.RetrofitApi;
import com.mohaa.mazaya.dashboard.models.Category;
import com.mohaa.mazaya.dashboard.models.Product;
import com.mohaa.mazaya.dashboard.models.Trader;
import com.mohaa.mazaya.dashboard.models.Variant;
import com.mohaa.mazaya.dashboard.networksync.CheckInternetConnection;
import com.mohaa.mazaya.dashboard.views.Pdt_ImageAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddProductActivity extends BaseActivity implements OnRemoveClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private AutoCompleteTextView main_ip;
    private Spinner spinner_main_ip;
    private ArrayAdapter<String> adapter_main;

    private AutoCompleteTextView sub_ip;
    private Spinner spinner_sub_ip;
    private ArrayAdapter<String> adapter_sub;

    private AutoCompleteTextView group_ip;
    private Spinner spinner_group_ip;
    private ArrayAdapter<String> adapter_group;
    //
    private AutoCompleteTextView company_ip;
    private Spinner spinner_company_ip;
    private ArrayAdapter<String> adapter_company;

    private AutoCompleteTextView package_ip;
    private Spinner spinner_package_ip;
    private ArrayAdapter<String> adapter_package;

    private AutoCompleteTextView status_ip;
    private Spinner spinner_status_ip;
    private ArrayAdapter<String> adapter_status;

    private AutoCompleteTextView department_ip;
    private Spinner spinner_department_ip;
    private ArrayAdapter<String> adapter_department;
    //
    private Bitmap product_logo;
    private ImageView setup_img;
    private TextView add_to_product;
    private com.rengwuxian.materialedittext.MaterialEditText product_name;
    private com.rengwuxian.materialedittext.MaterialEditText product_short_name;
    private com.rengwuxian.materialedittext.MaterialEditText product_price;
    private com.rengwuxian.materialedittext.MaterialEditText product_barcode;
    private com.rengwuxian.materialedittext.MaterialEditText product_discount;
    private com.rengwuxian.materialedittext.MaterialEditText product_desc;

    //ProgressDialog
    private ProgressDialog mLoginProgress;

    private Trader traders_list;

    private final int GALLERY = 1;
    private final int REQUEST_CODE_PERMISSIONS  = 1;
    private final int REQUEST_CODE_READ_STORAGE = 2;
    private String traderID;
    Toolbar toolbar;
    private Button btnChoose, btnUpload;
    private ArrayList<Uri> arrayList;
    private RecyclerView recyclerView;

    //
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
                    Toast.makeText(this, getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_add_products);
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
        traders_list = (Trader) getIntent().getExtras().getSerializable(ProductsUI.BUNDLE_TRADERS_LIST);
        traderID = getIntent().getStringExtra(ProductsUI.BUNDLE_TRADERS_ID);

        recyclerView = findViewById(R.id.listView);
        btnChoose = findViewById(R.id.btnChoose);
        btnChoose.setOnClickListener(v -> {
            // Display the file chooser dialog
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askForPermission();
            } else {
                showChooser();
            }
        });

        btnUpload = findViewById(R.id.btnUpload);
        //btnUpload.setOnClickListener(v -> uploadImagesToServer(1));
        arrayList = new ArrayList<>();
        setup_img = findViewById(R.id.product_logo);
        product_name = findViewById(R.id.product_name);
        product_short_name = findViewById(R.id.product_short_name);
        product_price = findViewById(R.id.productprice);
        product_barcode = findViewById(R.id.productbarcode);

        product_discount = findViewById(R.id.productdiscount);
        product_desc = findViewById(R.id.productdescription);


        mLoginProgress = new ProgressDialog(this);

        add_to_product = findViewById(R.id.add_to_product);

        setup_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, GALLERY);
            }
        });
        initCata(traders_list , traderID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
    }

    private void initCata(Trader traders_list , String TraderID) {

        main_ip = (AutoCompleteTextView) findViewById(R.id.main_ip);
        sub_ip = (AutoCompleteTextView) findViewById(R.id.sub_ip);
        group_ip = (AutoCompleteTextView) findViewById(R.id.group_ip);


        company_ip = (AutoCompleteTextView) findViewById(R.id.company_ip);
        package_ip = (AutoCompleteTextView) findViewById(R.id.package_ip);
        status_ip = (AutoCompleteTextView) findViewById(R.id.status_ip);
        department_ip = (AutoCompleteTextView) findViewById(R.id.department_ip);
        //
        spinner_main_ip = (Spinner) findViewById(R.id.spinner_main_ip);
        spinner_sub_ip = (Spinner) findViewById(R.id.spinner_sub_ip);
        spinner_group_ip = (Spinner) findViewById(R.id.spinner_group_ip);

        spinner_department_ip = (Spinner) findViewById(R.id.spinner_department_ip);
        spinner_company_ip = (Spinner) findViewById(R.id.spinner_company_ip);
        spinner_package_ip = (Spinner) findViewById(R.id.spinner_package_ip);
        spinner_status_ip = (Spinner) findViewById(R.id.spinner_status_ip);
        //
        getMainCategory(0);
        getVarients(1);
        getVarients(2);
        getVarients(3);
        getVarients(4);

        //adapter_company = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.Company_type));
        //adapter_department = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.Department_type));
        //adapter_package = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.Package_type));
        //adapter_status = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.Status_type));
        //
        //set spinner
       // spinner_department_ip.setAdapter(adapter_department);
       // spinner_company_ip.setAdapter(adapter_company);
       // spinner_package_ip.setAdapter(adapter_package);
        //spinner_status_ip.setAdapter(adapter_status);
        //



        spinner_department_ip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department_ip.setText(spinner_department_ip.getSelectedItem().toString());
                department_ip.dismissDropDown();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                department_ip.setText(spinner_department_ip.getSelectedItem().toString());
                department_ip.dismissDropDown();
            }
        });
        spinner_company_ip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                company_ip.setText(spinner_company_ip.getSelectedItem().toString());
                company_ip.dismissDropDown();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                company_ip.setText(spinner_company_ip.getSelectedItem().toString());
                company_ip.dismissDropDown();
            }
        });
        spinner_package_ip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                package_ip.setText(spinner_package_ip.getSelectedItem().toString());
                package_ip.dismissDropDown();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                package_ip.setText(spinner_package_ip.getSelectedItem().toString());
                package_ip.dismissDropDown();
            }
        });

        spinner_status_ip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status_ip.setText(spinner_status_ip.getSelectedItem().toString());
                status_ip.dismissDropDown();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                status_ip.setText(spinner_status_ip.getSelectedItem().toString());
                status_ip.dismissDropDown();
            }
        });

        add_to_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = product_name.getText().toString();//name
                final String sname = product_short_name.getText().toString();//name
                final String price = product_price.getText().toString();
                final String barcode = product_barcode.getText().toString();
                final String disc = product_discount.getText().toString();
                final String desc = product_desc.getText().toString();

                if (!TextUtils.isEmpty(name) &&!TextUtils.isEmpty(sname) &&!TextUtils.isEmpty(price) &&!TextUtils.isEmpty(barcode) && getProduct_logo()  != null) {


                    showProgressDialog();

                    uploadImage(getProduct_logo() , name ,sname ,price , barcode , TraderID  , disc , desc);

                }
            }
        });


    }
    private void uploadImage(Bitmap bitmap ,final String name  , final String sname , final String price , final String barcode , String traderID , final String Discount , final String desc ){

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
        Call<String> call_ = api.setProductImage(imgname,encodedImage , Long.parseLong(barcode));

        call_.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call_, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.i("Upload", response.body().toString());

                        ProductsAPIService api = RetrofitApi.retrofitWrite().create(ProductsAPIService.class);

                        //Defining the trader object as we need to pass it with the call
                        Product product = new Product( 0,0,0,traders_list.getId() , traders_list.getMerchant_name() , name , sname , desc , 15 , Double.parseDouble(price) , Long.parseLong(barcode) , 0 , 0 , 0 , APIUrl.BASE_URL_ProdcutsIMG +imgname + ".JPG" ,Double.parseDouble(product_discount.getText().toString()) ,0,0,0,0 , timestamp.getTime() ,0 , 0 , 0 , 0 , 0 );
                        // Log.d("helloo",encodedImage +"   >>"+imgname);
                        //Log.d("imggggg","   >>"+imgname);

                        //defining the call
                        Call<ProductResponse> call = api.createProduct(
                                spinner_group_ip.getSelectedItem().toString(),
                                spinner_sub_ip.getSelectedItem().toString(),
                                spinner_main_ip.getSelectedItem().toString(),
                                product.getMerchant_id(),
                                product.getMerchant_name(),
                                product.getProduct_name(),
                                product.getProduct_shortname(),
                                product.getProduct_desc(),
                                product.getQuantity(),
                                product.getPrice(),
                                product.getBarcode(),
                                product.getRate(),
                                product.getRatecount(),
                                "Food",
                                product.getThumb_image(),
                                product.getDiscount(),
                                department_ip.getText().toString(),
                                company_ip.getText().toString(),
                                package_ip.getText().toString(),
                                status_ip.getText().toString(),
                                product.getCreated_at(),
                                product.getView_count(),
                                product.getOrder_count(),
                                product.getShare_count(),
                                product.getWish_count(),
                                product.getSponsored()
                        );

                        call.enqueue(new Callback<ProductResponse>() {
                            @Override
                            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                                //uploadImagesToServer(response.body().getProduct().getId());
                                hideProgressDialog();
                                //Toast.makeText()

                                Toast.makeText(AddProductActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                Log.i("retrofit", response.body().getMessage());//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onFailure(Call<ProductResponse> call, Throwable t) {
                                hideProgressDialog();
                                Toast.makeText(AddProductActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                                Log.i("retrofit", t.getMessage());//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }

        if (requestCode == REQUEST_CODE_READ_STORAGE) {
            if (resultData != null) {
                if (resultData.getClipData() != null) {
                    int count = resultData.getClipData().getItemCount();
                    int currentItem = 0;
                    while (currentItem < count) {
                        Uri imageUri = resultData.getClipData().getItemAt(currentItem).getUri();
                        currentItem = currentItem + 1;

                        Log.d("Uri Selected", imageUri.toString());

                        try {
                            arrayList.add(imageUri);
                            Pdt_ImageAdapter mAdapter = new Pdt_ImageAdapter(arrayList);
                            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }
                } else if (resultData.getData() != null) {

                    final Uri uri = resultData.getData();
                    Log.i(TAG, "Uri = " + uri.toString());

                    try {
                        arrayList.add(uri);
                        Pdt_ImageAdapter mAdapter = new Pdt_ImageAdapter(arrayList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        Log.e(TAG, "File select error", e);
                    }
                }
            }
        }

        if (requestCode == GALLERY) {
            if (resultData != null) {
                Uri contentURI = resultData.getData();
                try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    setup_img.setImageBitmap(bitmap);
                    setProduct_logo(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddProductActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }




    @Override
    public void onRemoveClickListener(int position) {
        /*
        String contact = selectedList.get(position);
        // remove the contact only if it exists
        if (isContactAlreadyAdded(contact, selectedList)) {
            // remove the item at position from the contacts list and update the adapter
            selectedList.remove(position);

//            contactsListAdapter.removeFromAlreadyAddedList(contact);

            updateSelectedContactListAdapter(selectedList, position);
        } else {
            Snackbar.make(findViewById(R.id.coordinator),
                    getString(R.string.add_members_activity_catagories),
                    Snackbar.LENGTH_SHORT).show();
        }
        */
    }
    private void getVarients(int type) {
        List<String> Values = new ArrayList<>();
        VariantsAPIService service = RetrofitApi.retrofitRead().create(VariantsAPIService.class);


        Call<Variants> call = service.getVariants(type);

        call.enqueue(new Callback<Variants>() {
            @Override
            public void onResponse(Call<Variants> call, Response<Variants> response) {

                List<Variant> jsonresponse  = new ArrayList<>(response.body().getVariants());
                for (int i = 0; i <jsonresponse.size() ; i++) {
                    Values.add(jsonresponse.get(i).getName());
                }
                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AddProductActivity.this,  android.R.layout.simple_dropdown_item_1line, Values);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                switch(type) {
                    case 1:
                        spinner_department_ip.setAdapter(spinnerArrayAdapter);
                        break;
                    case 2:
                        spinner_company_ip.setAdapter(spinnerArrayAdapter);
                        break;
                    case 3:
                        spinner_package_ip.setAdapter(spinnerArrayAdapter);
                        break;
                    case 4:
                        spinner_status_ip.setAdapter(spinnerArrayAdapter);
                        break;
                    default:
                        // code block
                }




            }

            @Override
            public void onFailure(Call<Variants> call, Throwable t) {
                Toast.makeText(AddProductActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });




    }

    private void getMainCategory(int type) {
        ArrayList<String> Values_main = new ArrayList<>();
        ArrayList<String> Values_sub = new ArrayList<>();
        ArrayList<String> Values_group = new ArrayList<>();

        CategoryAPIService service = RetrofitApi.retrofitRead().create(CategoryAPIService.class);


        Call<Categories> call = service.getMainCategories(type);

        call.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {

                ArrayList<Category> jsonresponse  = new ArrayList<>(response.body().getCategories());
                for (int i = 0; i <jsonresponse.size() ; i++) {
                    Values_main.add(jsonresponse.get(i).getName());
                }
                ArrayAdapter<String> spinnerMainAdapter = new ArrayAdapter<String>(AddProductActivity.this,  android.R.layout.simple_dropdown_item_1line, Values_main);
                spinnerMainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner_main_ip.setAdapter(spinnerMainAdapter);
                spinner_main_ip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        main_ip.setText(spinner_main_ip.getSelectedItem().toString());
                        main_ip.dismissDropDown();
                        //int retval=jsonresponse.indexOf(spinner_main_ip.getSelectedItem().toString());
                        for (int i = 0; i < jsonresponse.size(); i++) {
                            if (jsonresponse.get(i).getName().equals(spinner_main_ip.getSelectedItem().toString())) {
                                Toast.makeText(AddProductActivity.this, "" + jsonresponse.get(i).getId() + "  " + jsonresponse.get(i).getName(), Toast.LENGTH_SHORT).show();
                                getSubCategory(jsonresponse.get(i).getId());
                              // setCat_gparent_index(jsonresponse.get(i).getId());
                            }
                        }

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        main_ip.setText(spinner_main_ip.getSelectedItem().toString());
                        main_ip.dismissDropDown();
                    }
                });

            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                Toast.makeText(AddProductActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });




    }
    private void getSubCategory(int type) {

        List<String> Values_sub = new ArrayList<>();


        CategoryAPIService service = RetrofitApi.retrofitRead().create(CategoryAPIService.class);


        Call<Categories> call = service.getMainCategories(type);

        call.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {

                List<Category> jsonresponse  = new ArrayList<>(response.body().getCategories());
                for (int i = 0; i <jsonresponse.size() ; i++) {
                    Values_sub.add(jsonresponse.get(i).getName());
                }
                ArrayAdapter<String> spinnerMainAdapter = new ArrayAdapter<String>(AddProductActivity.this,  android.R.layout.simple_dropdown_item_1line, Values_sub);
                spinnerMainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner_sub_ip.setAdapter(spinnerMainAdapter);

                spinner_sub_ip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        sub_ip.setText(spinner_sub_ip.getSelectedItem().toString());
                        sub_ip.dismissDropDown();
                        for (int i = 0; i < jsonresponse.size(); i++) {
                            if (jsonresponse.get(i).getName().equals(spinner_sub_ip.getSelectedItem().toString())) {
                                Toast.makeText(AddProductActivity.this, "" + jsonresponse.get(i).getId() + "  " + jsonresponse.get(i).getName(), Toast.LENGTH_SHORT).show();
                                getGroupCategory(jsonresponse.get(i).getId());
                               // setCat_parent_index(jsonresponse.get(i).getId());
                            }
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        sub_ip.setText(spinner_sub_ip.getSelectedItem().toString());
                        sub_ip.dismissDropDown();
                    }
                });


            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                Toast.makeText(AddProductActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });




    }
    private void getGroupCategory(int type) {


        List<String> Values_group = new ArrayList<>();

        CategoryAPIService service = RetrofitApi.retrofitRead().create(CategoryAPIService.class);


        Call<Categories> call = service.getMainCategories(type);

        call.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {

                List<Category> jsonresponse  = new ArrayList<>(response.body().getCategories());
                for (int i = 0; i <jsonresponse.size() ; i++) {
                    Values_group.add(jsonresponse.get(i).getName());
                }
                ArrayAdapter<String> spinnerMainAdapter = new ArrayAdapter<String>(AddProductActivity.this,  android.R.layout.simple_dropdown_item_1line, Values_group);
                spinnerMainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                spinner_group_ip.setAdapter(spinnerMainAdapter);
                spinner_group_ip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        group_ip.setText(spinner_group_ip.getSelectedItem().toString());
                        group_ip.dismissDropDown();
                        /*
                        for (int i = 0; i < jsonresponse.size(); i++) {
                            if (jsonresponse.get(i).getName().equals(spinner_sub_ip.getSelectedItem().toString())) {
                                Toast.makeText(AddProductActivity.this, "" + jsonresponse.get(i).getId() + "  " + jsonresponse.get(i).getName(), Toast.LENGTH_SHORT).show();
                                //setCat_index(jsonresponse.get(i).getId());
                            }
                        }

                         */
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        group_ip.setText(spinner_group_ip.getSelectedItem().toString());
                        group_ip.dismissDropDown();
                    }
                });
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                Toast.makeText(AddProductActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });




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

    public Bitmap getProduct_logo() {
        return product_logo;
    }

    public void setProduct_logo(Bitmap product_logo) {
        this.product_logo = product_logo;
    }
    /**
     *  Runtime Permission
     */
    private void askForPermission() {
        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE))
                != PackageManager.PERMISSION_GRANTED) {
            /* Ask for permission */
            // need to request permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar.make(this.findViewById(android.R.id.content),
                        "Please grant permissions to write data in sdcard",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        v -> ActivityCompat.requestPermissions(AddProductActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE_PERMISSIONS)).show();
            } else {
                /* Request for permission */
                ActivityCompat.requestPermissions(AddProductActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSIONS);
            }

        } else {
            showChooser();
        }
    }
    private void showChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_READ_STORAGE);
    }
    private void uploadImagesToServer(int pdt_id) {
        if (InternetConnection.checkConnection(AddProductActivity.this)) {


            Pdt_ImagesAPIService service = RetrofitApi.retrofitWrite().create(Pdt_ImagesAPIService.class);

            // create list of file parts (photo, video, ...)
            List<MultipartBody.Part> parts = new ArrayList<>();


            if (arrayList != null) {
                // create part for file (photo, video, ...)
                for (int i = 0; i < arrayList.size(); i++) {
                    parts.add(prepareFilePart("image"+i, arrayList.get(i)));
                }
            }

            String imgname = String.valueOf(Calendar.getInstance().getTimeInMillis());
            // create a map of data to pass along
            RequestBody description = createPartFromString("mohaa");
            RequestBody name = createPartFromString(imgname);
            RequestBody size = createPartFromString(""+parts.size());
            RequestBody count = createPartFromString(String.valueOf(arrayList.size()));

            // finally, execute the request
            Call<Pdt_ImageResponse> call = service.uploadMultiple(description , name, size ,count, parts);

            call.enqueue(new Callback<Pdt_ImageResponse>() {
                @Override
                public void onResponse(@NonNull Call<Pdt_ImageResponse> call, @NonNull Response<Pdt_ImageResponse> response) {

                    Toast.makeText(AddProductActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    if(response.isSuccessful()) {
                       //Toast.makeText(AddProductActivity.this,"Images successfully uploaded!", Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < parts.size(); i++) {
                            Pdt_ImagesAPIService api = RetrofitApi.retrofitWrite().create(Pdt_ImagesAPIService.class);
                            Call<Pdt_ImageResponse> call_image = api.createPdt_Image(pdt_id,1 ,APIUrl.BASE_URL_ProdcutsIMG +imgname + i + ".JPG"  );
                            int finalI = i;
                            call_image.enqueue(new Callback<Pdt_ImageResponse>() {
                                @Override
                                public void onResponse(Call<Pdt_ImageResponse> call, Response<Pdt_ImageResponse> response) {
                                    hideProgressDialog();
                                   // Toast.makeText(AddProductActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                                 //   Toast.makeText(AddProductActivity.this, APIUrl.BASE_URL_Images +imgname + finalI + ".JPG", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<Pdt_ImageResponse> call, Throwable t) {

                                }
                            });
                        }

                    } else {
                        Snackbar.make(findViewById(android.R.id.content),
                                R.string.string_some_thing_wrong, Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Pdt_ImageResponse> call, @NonNull Throwable t) {

                    Log.e(TAG, "Image upload failed!", t);
                    Snackbar.make(findViewById(android.R.id.content),
                            "Image upload failed!", Snackbar.LENGTH_LONG).show();
                }
            });

        } else {

            Toast.makeText(AddProductActivity.this,
                    R.string.string_internet_connection_not_available, Toast.LENGTH_SHORT).show();
        }
    }
    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(FileUtils.MIME_TYPE_TEXT), descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(this, fileUri);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create (MediaType.parse(FileUtils.MIME_TYPE_IMAGE), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }


}
