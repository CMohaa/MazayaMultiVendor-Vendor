package com.mohaa.mazaya.dashboard.manager.Call;


import com.mohaa.mazaya.dashboard.models.Order;


import java.util.ArrayList;


public class Orders {

    private ArrayList<Order> orders;

    public Orders() {

    }

    public ArrayList<Order> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<Order> orders) {
        this.orders = orders;
    }
}