package com.mohaa.mazaya.dashboard.Controllers.activities_traders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.mohaa.mazaya.dashboard.Controllers.fragments_home.HomeFragment;
import com.mohaa.mazaya.dashboard.Controllers.fragments_traders.ProductsFragment;
import com.mohaa.mazaya.dashboard.Controllers.fragments_traders.RatingFragment;
import com.mohaa.mazaya.dashboard.R;
import com.mohaa.mazaya.dashboard.Utils.ProductsUI;
import com.mohaa.mazaya.dashboard.Utils.RtlViewPager.RtlViewPager;
import com.mohaa.mazaya.dashboard.Utils.SectionPagerAdapter;
import com.mohaa.mazaya.dashboard.interfaces.MyResultReceiver;
import com.mohaa.mazaya.dashboard.networksync.CheckInternetConnection;

public class TraderActivity extends AppCompatActivity implements MyResultReceiver {

    private int traderID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trader);
        // Set Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        setupViewPager();
        traderID =  getIntent().getIntExtra(ProductsUI.BUNDLE_TRADERS_ID , -1);
    }
    //Responsible For Adding the 3 tabs : Camera  , Home , Messages
    private void setupViewPager()
    {


        SectionPagerAdapter adapter = new  SectionPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new ProductsFragment());//index 0
        adapter.AddFragment(new RatingFragment());//index 1
        //adapter.AddFragment(new GroupFragment()); //index 4
        // adapter.AddFragment(new FriendsFragment()); //index 4
        RtlViewPager viewPager = (RtlViewPager) findViewById(R.id.viewpager_container);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_products);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_rating);


        //tabLayout.getTabAt(4).setIcon(R.drawable.ic_chat);
        //tabLayout.getTabAt(4).setIcon(R.drawable.ic_friends);


    }

    @Override
    public int getResult() {
        return traderID;
    }
}
