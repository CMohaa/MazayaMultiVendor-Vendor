package com.mohaa.mazaya.dashboard.Controllers.activities_popup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.mohaa.mazaya.dashboard.Controllers.BaseActivity;
import com.mohaa.mazaya.dashboard.R;
import com.mohaa.mazaya.dashboard.Utils.ProductsUI;
import com.mohaa.mazaya.dashboard.Utils.charts.models.GraphicItem;
import com.mohaa.mazaya.dashboard.Utils.charts.models.LineChartItem;
import com.mohaa.mazaya.dashboard.Utils.charts.models.PieChartItem;
import com.mohaa.mazaya.dashboard.Utils.charts.view.GraphicAdapter;
import com.mohaa.mazaya.dashboard.manager.APIUrl;
import com.mohaa.mazaya.dashboard.manager.ApiServices.ProductsAPIService;
import com.mohaa.mazaya.dashboard.manager.Call.Products;
import com.mohaa.mazaya.dashboard.manager.RetrofitApi;
import com.mohaa.mazaya.dashboard.models.Product;
import com.mohaa.mazaya.dashboard.models.Trader;
import com.mohaa.mazaya.dashboard.networksync.CheckInternetConnection;

import java.util.ArrayList;
import java.util.Random;

import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChartsActivity extends BaseActivity {


    private Trader traders_info;
    private ArrayList<Product> products_list;

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);

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


        traders_info = (Trader) getIntent().getExtras().getSerializable(ProductsUI.BUNDLE_TRADERS_LIST);


        products_list = new ArrayList<>();

        getData();
        ImageButton refresh = (ImageButton) findViewById(R.id.refresh_stats_button);
        Log.i("Dashboard", "onCreateView TOP");
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCharts();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
        updateCharts();
    }


    public void updateCharts(){
        ListView lvMain = (ListView) findViewById(R.id.listViewStats);

        final GraphicItem[] items = new GraphicItem[6];


        int total = 0;
        int names[] = new int[products_list.size()];
        int count[] = new int[products_list.size()];

        for (int x = 0 ; x < products_list.size() ; x++)
        {

            names[x]++;
            names[x]+=products_list.get(x).getView_count();
            total += products_list.get(x).getView_count();

        }
        ArrayList<PieChartItem> pie_items = new ArrayList<>();
        ArrayList<PieChartItem> pie_items2 = new ArrayList<>();
        ArrayList<LineChartItem> line_items = new ArrayList<>();

        int[] styleHeader = {
                R.color.MD_LightGreen_300,
                R.color.MD_LightBlue_300,
                R.color.MD_Amber,
                R.color.MD_LightGreen,
                R.color.MD_Grey_300,
                R.color.MD_Green,
                R.color.MD_Orange,
                R.color.MD_Purple_700,
                R.color.MD_Red,
                R.color.MD_LightBlue_900,
                R.color.MD_Yellow,
                R.color.MD_Amber_300
        };
        Random rand = new Random();
        for (int i = 0; i < products_list.size(); i++) {
            double chart = ((double)names[i] * 100/(double)total);
            float[][] values = {{0,0}, {1,0}, {2, 0}, {3,0}, {4,0}, {5,0},{6,0}};
            values[5][1]+= products_list.get(i).getRatecount();
            int style = styleHeader[rand.nextInt(styleHeader.length)];
            pie_items.add(new PieChartItem(chart, getResources().getColor(style), getResources().getColor(style), products_list.get(i).getProduct_name()));
            line_items.add(new LineChartItem(values, getResources().getColor(style), products_list.get(i).getProduct_name() + " [ " + products_list.get(i).getRatecount() + " ]"));
        }




        int icount[] = new int[products_list.size()];
        int total_quantity = 0;


        for (int x = 0 ; x < products_list.size() ; x++)
        {
            icount[x]+=products_list.get(x).getQuantity();
            total_quantity+=products_list.get(x).getQuantity();
        }

        for (int i = 0; i < products_list.size(); i++) {

            int style = styleHeader[rand.nextInt(styleHeader.length)];
            double chart = ((double)icount[i] * 100/(double)total_quantity);
            pie_items2.add(new PieChartItem(chart, getResources().getColor(style), getResources().getColor(style), products_list.get(i).getProduct_name()));
    }







        items[0] = new GraphicItem(getString(R.string.view_count_charts), GraphicAdapter.TYPE_SEPARATOR);
        items[1] = new GraphicItem(pie_items, total, GraphicAdapter.TYPE_PIE);


        items[2] = new GraphicItem(getString(R.string.product_reviews), GraphicAdapter.TYPE_SEPARATOR);
        items[3] = new GraphicItem(line_items, GraphicAdapter.TYPE_LINE);

        items[4] = new GraphicItem(getString(R.string.product_quantity), GraphicAdapter.TYPE_SEPARATOR);
        items[5] = new GraphicItem(pie_items2, total_quantity, GraphicAdapter.TYPE_PIE);



        GraphicAdapter customAdapter = new GraphicAdapter(ChartsActivity.this, R.id.text, items);
        lvMain.setAdapter(customAdapter);
    }

    public void getAllOrders()
    {
        products_list.clear();
        ProductsAPIService service = RetrofitApi.retrofitRead().create(ProductsAPIService.class);
        Call<Products> call = service.getProducts(traders_info.getId() );






        call.enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                // products_listAdapter = new Products_ListAdapter(response.body().getProducts(), ProductsTraderActivity.this);
                // recList.setAdapter(products_listAdapter);

                products_list.addAll(response.body().getProducts());




            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {

            }
        });

    }

    public void getData(){
        try {
            //swipeRefreshLayout.setRefreshing(true);
            getAllOrders();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
