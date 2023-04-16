package com.mohaa.mazaya.dashboard.interfaces;


import com.mohaa.mazaya.dashboard.models.Category;

import java.io.Serializable;

public interface OnCataClickListener extends Serializable {
    void onProductClicked(Category contact, int position);
}
