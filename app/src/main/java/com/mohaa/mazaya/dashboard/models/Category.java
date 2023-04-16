package com.mohaa.mazaya.dashboard.models;


import java.io.Serializable;

public class Category implements Serializable {

    public Category() {

    }
    private static final String TAG = "Category";
    private int id;
    private String name;
    private int parent_id;
    private String parent_name;
    private String thumb_image;
    private int count;

    public Category(String name, int parent_id, String parent_name, String thumb_image, int count) {
        this.name = name;
        this.parent_id = parent_id;
        this.parent_name = parent_name;
        this.thumb_image = thumb_image;
        this.count = count;
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

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
