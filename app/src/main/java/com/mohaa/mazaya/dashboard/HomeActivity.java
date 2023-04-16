package com.mohaa.mazaya.dashboard;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;


import com.google.firebase.iid.FirebaseInstanceId;
import com.mohaa.mazaya.dashboard.Auth.LoginActivity;
import com.mohaa.mazaya.dashboard.Controllers.BaseActivity;
import com.mohaa.mazaya.dashboard.Controllers.activities_products.ProductsActivity;
import com.mohaa.mazaya.dashboard.Utils.PermUtil;
import com.mohaa.mazaya.dashboard.Utils.ProductsUI;
import com.mohaa.mazaya.dashboard.helper.SharedPrefManager;
import com.mohaa.mazaya.dashboard.manager.APIUrl;
import com.mohaa.mazaya.dashboard.manager.ApiServices.CategoryAPIService;
import com.mohaa.mazaya.dashboard.manager.ApiServices.ProductsAPIService;
import com.mohaa.mazaya.dashboard.manager.ApiServices.UserAPIService;
import com.mohaa.mazaya.dashboard.manager.Call.Categories;
import com.mohaa.mazaya.dashboard.manager.Response.UserResponse;
import com.mohaa.mazaya.dashboard.manager.RetrofitApi;
import com.mohaa.mazaya.dashboard.models.User;
import com.mohaa.mazaya.dashboard.networksync.CheckInternetConnection;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "HomeActivity";


    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    public NavController navController;
    public NavigationView navigationView;

    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";
    public final static String EXTRA_ORIENTATION = "EXTRA_ORIENTATION";
    public final static String EXTRA_WITH_LINE_PADDING = "EXTRA_WITH_LINE_PADDING";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        init();
    }
    /*
    @Override
    public void onPostCreate(@Nullable @Nullable Bundle savedInstanceState, @Nullable @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        actionBarDrawerToggle.syncState();
    }
*/



    private void init() {



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.my_drawer);

        navigationView = findViewById(R.id.navigationView);
        //

        LoadMenus(navigationView);


        //
        mToggle = new ActionBarDrawerToggle(this , drawerLayout , R.string.open , R.string.close);
        drawerLayout.addDrawerListener(mToggle);

        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);

        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            //registerGCM();
            sendRegistrationTokenToServer(FirebaseInstanceId.getInstance().getToken());
        }






    }

    private void LoadMenus(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        Menu submenu = menu.addSubMenu("Categories");

        CategoryAPIService service = RetrofitApi.retrofitRead().create(CategoryAPIService.class);


        Call<Categories> call = service.getMainCategories(0);

        call.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                try {
                    for (int i = 0; i < response.body().getCategories().size(); i++) {
                        int finalI = i;
                        submenu.add(response.body().getCategories().get(finalI).getName()).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                Intent intent = new Intent(HomeActivity.this, ProductsActivity.class);
                                intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST, response.body().getCategories().get(finalI));
                                intent.putExtra(ProductsUI.BUNDLE_PRODUCTS_LIST_TYPE, "Main");
                                startActivity(intent);
                                return false;
                            }
                        });
                    }
                }catch (Exception ex)
                {
                    ex.getLocalizedMessage();
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {

            }
        });
        navigationView.invalidate();
    }

    //Responsible For Adding the 3 tabs : Camera  , Home , Messages
    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    //Toast.makeText(MultiEditorActivity.this, "Approve permissions to open Pix ImagePicker", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            attemptToExitIfRoot(null);
        }


    }

    // convert the list of contact to a map of members





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        menuItem.setChecked(true);

        drawerLayout.closeDrawers();

        int id = menuItem.getItemId();

        switch (id) {

            case R.id.nav_ads:

                navController.navigate(R.id.ads_fragment);
                break;




            case R.id.nav_settings:

                navController.navigate(R.id.settings_fragment);
                break;

        }
        return true;

    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(drawerLayout, Navigation.findNavController(this, R.id.nav_host_fragment));
    }

    private void registerGCM() {
        Intent registrationComplete = null;
        String token = null;
        try {

            token = FirebaseInstanceId.getInstance().getToken();
            Log.w("GCMRegIntentService", "token:" + token);

            sendRegistrationTokenToServer(token);
            registrationComplete = new Intent(REGISTRATION_SUCCESS);
            registrationComplete.putExtra("token", token);
        } catch (Exception e) {
            Log.w("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationTokenToServer(final String token) {
        //Getting the user id from shared preferences
        //We are storing gcm token for the user in our mysql database
        UserAPIService service = RetrofitApi.retrofitWrite().create(UserAPIService.class);

        User user = new User(SharedPrefManager.getInstance(getApplicationContext()).getUser().getId(), token);
        SharedPrefManager.getInstance(getApplicationContext()).userToken(token);

        Call<UserResponse> call = service.updateGcmToken(
                user.getId(),
                user.getGcmtoken()

        );

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {

               // Toast.makeText(HomeActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                //Log.d("gcmKey", "onResponse: " + response.body().getMessage());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
