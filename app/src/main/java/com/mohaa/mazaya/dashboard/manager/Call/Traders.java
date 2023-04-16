package com.mohaa.mazaya.dashboard.manager.Call;


import com.mohaa.mazaya.dashboard.models.Message;
import com.mohaa.mazaya.dashboard.models.Trader;

import java.util.ArrayList;

/**
 * Created by Belal on 15/04/17.
 */

public class Traders {

    private ArrayList<Trader> traders;

    public Traders() {

    }

    public ArrayList<Trader> getTraders() {
        return traders;
    }

    public void setTraders(ArrayList<Trader> traders) {
        this.traders = traders;
    }
}