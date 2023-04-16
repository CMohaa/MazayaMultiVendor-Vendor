package com.mohaa.mazaya.dashboard.models;



public class Comment {
    private int id;
    private int usr_id;
    private String usr_name;
    private String content;
    private int pdt_id;
    private int merchant_id;
    private int rate;
    private long created_at;

    public Comment(int usr_id, String usr_name, String content, int pdt_id, int merchant_id, int rate, long created_at) {
        this.usr_id = usr_id;
        this.usr_name = usr_name;
        this.content = content;
        this.pdt_id = pdt_id;
        this.merchant_id = merchant_id;
        this.rate = rate;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(int usr_id) {
        this.usr_id = usr_id;
    }

    public String getUsr_name() {
        return usr_name;
    }

    public void setUsr_name(String usr_name) {
        this.usr_name = usr_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(int merchant_id) {
        this.merchant_id = merchant_id;
    }

    public int getPdt_id() {
        return pdt_id;
    }

    public void setPdt_id(int pdt_id) {
        this.pdt_id = pdt_id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
