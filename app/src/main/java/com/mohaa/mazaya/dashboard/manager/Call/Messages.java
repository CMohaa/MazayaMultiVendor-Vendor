package com.mohaa.mazaya.dashboard.manager.Call;


import com.mohaa.mazaya.dashboard.models.Message;

import java.util.ArrayList;

/**
 * Created by Belal on 15/04/17.
 */

public class Messages {

    private ArrayList<Message> messages;

    public Messages() {

    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}