package com.mohaa.mazaya.dashboard.models;

import java.io.Serializable;

public class Message implements Serializable {
    private int usersId;
    private String message;
    private String sentAt;
    private String name;
    User user;
    public Message()
    {

    }
    public Message(int usersId, String message, String sentAt, String name) {
        this.usersId = usersId;
        this.message = message;
        this.sentAt = sentAt;
        this.name = name;
    }

    public int getUsersId() {
        return usersId;
    }

    public void setUsersId(int usersId) {
        this.usersId = usersId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSentAt() {
        return sentAt;
    }

    public void setSentAt(String sentAt) {
        this.sentAt = sentAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
