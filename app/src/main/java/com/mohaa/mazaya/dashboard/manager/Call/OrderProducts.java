package com.mohaa.mazaya.dashboard.manager.Call;


import com.mohaa.mazaya.dashboard.models.OrderProduct;


import java.util.ArrayList;


public class OrderProducts {

    private ArrayList<OrderProduct> orderProducts;

    public OrderProducts() {

    }

    public ArrayList<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(ArrayList<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }
}