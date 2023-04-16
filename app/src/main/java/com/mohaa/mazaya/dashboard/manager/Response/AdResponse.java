package com.mohaa.mazaya.dashboard.manager.Response;



public class AdResponse {
    private boolean error;
    private String message;

    public AdResponse() {

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