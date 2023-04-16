package com.mohaa.mazaya.dashboard.manager.Call;


import com.mohaa.mazaya.dashboard.models.Comment;
import com.mohaa.mazaya.dashboard.models.Message;

import java.util.ArrayList;

/**
 * Created by Belal on 15/04/17.
 */

public class Comments {

    private ArrayList<Comment> comments;

    public Comments() {

    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}