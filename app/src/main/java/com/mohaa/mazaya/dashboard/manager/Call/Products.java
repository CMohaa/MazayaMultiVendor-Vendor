package com.mohaa.mazaya.dashboard.manager.Call;


import com.mohaa.mazaya.dashboard.models.Product;
import com.mohaa.mazaya.dashboard.models.Trader;

import java.util.ArrayList;

/**
 * Created by Belal on 15/04/17.
 */

public class Products {

    private ArrayList<Product> products;

    public Products() {

    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}