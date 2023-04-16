package com.mohaa.mazaya.dashboard.manager.Response;

import com.google.gson.annotations.SerializedName;
import com.mohaa.mazaya.dashboard.models.OrderProduct;



public class OrderProductResponse {
    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("orderProduct")
    private OrderProduct orderProduct;

    public OrderProductResponse(Boolean error, String message, OrderProduct orderProduct) {
        this.error = error;
        this.message = message;
        this.orderProduct = orderProduct;
    }

    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public OrderProduct getOrderProduct() {
        return orderProduct;
    }
}