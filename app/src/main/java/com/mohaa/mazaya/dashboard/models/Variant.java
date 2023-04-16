package com.mohaa.mazaya.dashboard.models;


import java.io.Serializable;

public class Variant implements Serializable {

    public Variant() {

    }
    private static final String TAG = "Variant";
    private int id;
    private String name;
    private int type;
    private int admin_id;

    public Variant(String name, int type, int admin_id) {
        this.name = name;
        this.type = type;
        this.admin_id = admin_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }
}
