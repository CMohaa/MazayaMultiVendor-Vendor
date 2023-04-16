package com.mohaa.mazaya.dashboard.models;


import java.io.Serializable;

public class Pdt_Image implements Serializable {

    public Pdt_Image() {

    }
    private static final String TAG = "Pdt_Image";
    private int id;
    private int pdt_id;
    private int pd_id;
    private String image_url;

    public Pdt_Image(int pdt_id, int pd_id, String image_url) {
        this.pdt_id = pdt_id;
        this.pd_id = pd_id;
        this.image_url = image_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPdt_id() {
        return pdt_id;
    }

    public void setPdt_id(int pdt_id) {
        this.pdt_id = pdt_id;
    }

    public int getPd_id() {
        return pd_id;
    }

    public void setPd_id(int pd_id) {
        this.pd_id = pd_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
