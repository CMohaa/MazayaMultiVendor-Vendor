package com.mohaa.mazaya.dashboard.manager.Call;


import com.mohaa.mazaya.dashboard.models.Category;
import com.mohaa.mazaya.dashboard.models.Product;

import java.util.ArrayList;

/**
 * Created by Belal on 15/04/17.
 */

public class Categories {

    private ArrayList<Category> categories;

    public Categories() {

    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }
}