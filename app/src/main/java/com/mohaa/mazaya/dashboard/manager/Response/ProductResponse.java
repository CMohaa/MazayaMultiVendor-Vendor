package com.mohaa.mazaya.dashboard.manager.Response;

import com.google.gson.annotations.SerializedName;
import com.mohaa.mazaya.dashboard.models.Product;
import com.mohaa.mazaya.dashboard.models.User;



public class ProductResponse {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("product")
    private Product product;

    public ProductResponse(Boolean error, String message, Product product) {
        this.error = error;
        this.message = message;
        this.product = product;
    }

    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Product getProduct() {
        return product;
    }
}