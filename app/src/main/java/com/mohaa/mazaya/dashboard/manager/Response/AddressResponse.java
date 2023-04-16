package com.mohaa.mazaya.dashboard.manager.Response;



public class AddressResponse {
    private boolean error;
    private String message;

    public AddressResponse() {

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