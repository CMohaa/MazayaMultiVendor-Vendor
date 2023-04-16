package com.mohaa.mazaya.dashboard.models;


import java.io.Serializable;


public class Trader  implements Serializable {



    public Trader() {
    }

    private static final String TAG = "Trader";
    private int id;
    private int admin_id;
    private String admin_name;
    private String merchant_name;
    private String merchant_desc;
    private String thumb_image;
    private String location;
    private String type;
    private int rate;
    private int ratecount;
    private double merchant_credit;
    private int country_code;
    private int count;


    public Trader(int admin_id, String admin_name, String merchant_name, String merchant_desc, String thumb_image, String location, String type, int rate, int ratecount, double merchant_credit, int country_code, int count) {
        this.admin_id = admin_id;
        this.admin_name = admin_name;
        this.merchant_name = merchant_name;
        this.merchant_desc = merchant_desc;
        this.thumb_image = thumb_image;
        this.location = location;
        this.type = type;
        this.rate = rate;
        this.ratecount = ratecount;
        this.merchant_credit = merchant_credit;
        this.country_code = country_code;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getMerchant_desc() {
        return merchant_desc;
    }

    public void setMerchant_desc(String merchant_desc) {
        this.merchant_desc = merchant_desc;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getRatecount() {
        return ratecount;
    }

    public void setRatecount(int ratecount) {
        this.ratecount = ratecount;
    }

    public double getMerchant_credit() {
        return merchant_credit;
    }

    public void setMerchant_credit(double merchant_credit) {
        this.merchant_credit = merchant_credit;
    }

    public int getCountry_code() {
        return country_code;
    }

    public void setCountry_code(int country_code) {
        this.country_code = country_code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
