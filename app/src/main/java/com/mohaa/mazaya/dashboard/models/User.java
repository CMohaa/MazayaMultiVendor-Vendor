package com.mohaa.mazaya.dashboard.models;


import android.os.Parcel;
import android.os.Parcelable;



import java.io.Serializable;


/**
 * Created by Mohamed El Sayed
 */

public class User  implements Serializable {


    private int id;
    private String name;
    private String email;
    private String password;
    private String gender;
    private String gcmtoken;

    public User()
    {

    }
    public User(int id, String gcmtoken) {
        this.id = id;
        this.gcmtoken = gcmtoken;
    }

    public User(String name, String email, String password, String gender , String gcmtoken) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.gcmtoken = gcmtoken;
    }

    public User(int id, String name, String email, String gender , String gcmtoken){
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.gcmtoken = gcmtoken;
    }

    public User(int id, String name, String email, String password, String gender , String gcmtoken) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.gcmtoken = gcmtoken;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGcmtoken() {
        return gcmtoken;
    }

    public void setGcmtoken(String gcmtoken) {
        this.gcmtoken = gcmtoken;
    }
}
