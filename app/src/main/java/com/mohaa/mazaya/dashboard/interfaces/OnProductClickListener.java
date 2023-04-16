package com.mohaa.mazaya.dashboard.interfaces;


import com.mohaa.mazaya.dashboard.models.Product;

import java.io.Serializable;

public interface OnProductClickListener extends Serializable {
    void onProductClicked(Product contact, int position);
}
