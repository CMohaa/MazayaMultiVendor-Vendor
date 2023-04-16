package com.mohaa.mazaya.dashboard.manager.Response;

/**
 * Created by Belal on 15/04/17.
 */

public class Pdt_ImageResponse {
    private boolean error;
    private String message;


    public Pdt_ImageResponse() {

    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}