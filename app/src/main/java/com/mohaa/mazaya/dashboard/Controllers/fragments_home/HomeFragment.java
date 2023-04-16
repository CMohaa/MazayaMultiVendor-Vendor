package com.mohaa.mazaya.dashboard.Controllers.fragments_home;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mohaa.mazaya.dashboard.Controllers.activities_popup.SearchActivity;
import com.mohaa.mazaya.dashboard.Controllers.activities_traders.ProductsManagmentActivity;
import com.mohaa.mazaya.dashboard.R;
import com.mohaa.mazaya.dashboard.Utils.searchbar.MaterialSearchBar;
import com.mohaa.mazaya.dashboard.networksync.CheckInternetConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    private LinearLayout products_manager_panel;
    private LinearLayout products_manager_panel_;
    private LinearLayout track_orders_panel;
    private LinearLayout charts_panel;
    private LinearLayout ads;
    private LinearLayout promo;
    private MaterialSearchBar materialSearchBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home , container , false);
        //check Internet Connection
        new CheckInternetConnection(getContext()).checkConnection();
        products_manager_panel = view.findViewById(R.id.Products_Management);
        products_manager_panel_=  view.findViewById(R.id.Products_Management_);
        track_orders_panel =  view.findViewById(R.id.track_orders);
        charts_panel =  view.findViewById(R.id.Chart_panel);
        ads =  view.findViewById(R.id.ads);
        promo =  view.findViewById(R.id.promo);
        materialSearchBar = view.findViewById(R.id.searchBar);
        init();
        return view;
    }
    private void init() {

        products_manager_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(getActivity(), ProductsManagmentActivity.class);
                startActivity(loginIntent);
            }
        });
        products_manager_panel_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent loginIntent = new Intent(getActivity(), ProductsEditActivity.class);
              //  startActivity(loginIntent);
            }
        });
        track_orders_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent loginIntent = new Intent(getActivity(), TrackOrdersActivity.class);
              //  startActivity(loginIntent);
            }
        });
        ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent loginIntent = new Intent(getActivity(), AddSpecAdsActivity.class);
              //  startActivity(loginIntent);
            }
        });
        promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent loginIntent = new Intent(getActivity(), AddSpecPromoActivity.class);
                //startActivity(loginIntent);
            }
        });
        charts_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent loginIntent = new Intent(getActivity(), ChartsActivity.class);
                //startActivity(loginIntent);
            }
        });


        materialSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productIntent = new Intent(getContext(), SearchActivity.class);
                startActivity(productIntent);
            }
        });
    }
}
