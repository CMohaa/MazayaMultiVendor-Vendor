package com.mohaa.mazaya.dashboard.models;


import java.io.Serializable;

public class Order implements Serializable {

    public Order() {

    }
    private static final String TAG = "Order";
    private int id;
    private long order_number;
    private int owner_id;
    private String message;
    private int country;
    private int government;
    private String address;
    private String mobile;
    private int state;
    private long created_at;
    private int count;
    private double total_cost;

    public Order(long order_number, int owner_id, String message, int country, int government, String address, String mobile, int state, long created_at, int count, double total_cost) {
        this.order_number = order_number;
        this.owner_id = owner_id;
        this.message = message;
        this.country = country;
        this.government = government;
        this.address = address;
        this.mobile = mobile;
        this.state = state;
        this.created_at = created_at;
        this.count = count;
        this.total_cost = total_cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getOrder_number() {
        return order_number;
    }

    public void setOrder_number(long order_number) {
        this.order_number = order_number;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public int getGovernment() {
        return government;
    }

    public void setGovernment(int government) {
        this.government = government;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }
}
