package com.mohaa.mazaya.dashboard.Controllers.activities_products;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.mohaa.mazaya.dashboard.R;
import com.mohaa.mazaya.dashboard.Utils.ProductsUI;

public class SubProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_products);

        Intent intent = getIntent();
        String Value  = intent.getStringExtra(ProductsUI.BUNDLE_PRODUCTS_TYPE);
        TextView textView = findViewById(R.id.submenu);
        textView.setText(Value);
    }
}
