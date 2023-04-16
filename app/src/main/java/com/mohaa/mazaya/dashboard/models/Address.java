package com.mohaa.mazaya.dashboard.models;

import java.io.Serializable;

public class Address implements Serializable {
    private int id;
    private int user_id;
    private String name;
    private int country;
    private int government;
    private String address;
    private String mobile;
    private int state;
    public Address() {
    }

    public Address(int user_id, String name, int country, int government, String address, String mobile, int state) {
        this.user_id = user_id;
        this.name = name;
        this.country = country;
        this.government = government;
        this.address = address;
        this.mobile = mobile;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
