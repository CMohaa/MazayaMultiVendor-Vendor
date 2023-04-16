package com.mohaa.mazaya.dashboard.Controllers.activities_traders;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mohaa.mazaya.dashboard.Controllers.BaseActivity;
import com.mohaa.mazaya.dashboard.Controllers.activities_products.ProductsTraderActivity;
import com.mohaa.mazaya.dashboard.R;
import com.mohaa.mazaya.dashboard.Utils.GridSpacingItemDecoration;
import com.mohaa.mazaya.dashboard.Utils.ProductsUI;
import com.mohaa.mazaya.dashboard.interfaces.OnTraderClickListener;
import com.mohaa.mazaya.dashboard.manager.APIUrl;
import com.mohaa.mazaya.dashboard.manager.ApiServices.ProductsAPIService;
import com.mohaa.mazaya.dashboard.manager.ApiServices.TraderAPIService;
import com.mohaa.mazaya.dashboard.manager.Call.Traders;
import com.mohaa.mazaya.dashboard.manager.RetrofitApi;
import com.mohaa.mazaya.dashboard.models.Trader;
import com.mohaa.mazaya.dashboard.networksync.CheckInternetConnection;
import com.mohaa.mazaya.dashboard.views.TraderAdapter;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductsManagmentActivity extends BaseActivity implements OnTraderClickListener {

    private static final String TAG = "TradersManagment";
    private LinearLayout create_shop;

    private Menu menu;

    private RecyclerView recList;


    private ArrayList<Trader> trader_list;
    private RecyclerView products_recyclerView;
    private StaggeredGridLayoutManager products_staggeredGridLayoutManager;
    private TraderAdapter traderAdapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_managment);


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
        //makeFullScreen();


        create_shop = (LinearLayout)  findViewById(R.id.create_shop);
        create_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productIntent = new Intent(ProductsManagmentActivity.this, AddTraderActivity.class);

                startActivity(productIntent);
            }
        });

        recList = (RecyclerView) findViewById(R.id.recyclerview);

        trader_list = new ArrayList<>();
        traderAdapter = new TraderAdapter(trader_list , this);



        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recList.setLayoutManager(mLayoutManager);
        recList.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recList.setItemAnimator(new DefaultItemAnimator());
        recList.setAdapter(traderAdapter);
        // loadProducts();

        // Get Data and Fill Grid

        getData();
        //

    }

    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
    }

    //Responsible For Adding the 3 tabs : Camera  , Home , Messages
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    public void getAllPosts()
    {


        TraderAPIService service = RetrofitApi.retrofitRead().create(TraderAPIService.class);


        Call<Traders> call = service.getTraders();

        call.enqueue(new Callback<Traders>() {
            @Override
            public void onResponse(Call<Traders> call, Response<Traders> response) {
                traderAdapter = new TraderAdapter(response.body().getTraders(), ProductsManagmentActivity.this);
                recList.setAdapter(traderAdapter);
                trader_list.addAll(response.body().getTraders());
                traderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Traders> call, Throwable t) {

            }
        });

    }


    public void getData(){
        try {
            //swipeRefreshLayout.setRefreshing(true);
            getAllPosts();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }












    @Override
    public void onTraderClicked(Trader contact, int position) {

        Intent loginIntent = new Intent(ProductsManagmentActivity.this, ProductsTraderActivity.class);
        loginIntent.putExtra(ProductsUI.BUNDLE_TRADERS_LIST, (Serializable) contact);
        loginIntent.putExtra(ProductsUI.BUNDLE_TRADERS_IMAGE, contact.getThumb_image());
        loginIntent.putExtra(ProductsUI.BUNDLE_TRADERS_ID, contact.getId());
        startActivity(loginIntent);

    }


}

