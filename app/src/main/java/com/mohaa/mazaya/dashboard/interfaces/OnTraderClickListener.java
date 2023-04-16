package com.mohaa.mazaya.dashboard.interfaces;


import com.mohaa.mazaya.dashboard.models.Trader;

import java.io.Serializable;

public interface OnTraderClickListener extends Serializable {
    void onTraderClicked(Trader contact, int position);
}
