package com.mohaa.mazaya.dashboard.models;


import java.io.Serializable;

public class OrderProduct implements Serializable {

    public OrderProduct() {

    }
    private static final String TAG = "OrderProduct";
    private int id;
    private int product_id;
    private int merchant_id;
    private long order_number;
    private int owner_id;
    private int quantity;
    private double price;
    private double total_price;
    private long barcode;
    private long created_at;

    public OrderProduct(int product_id, int merchant_id, long order_number, int owner_id, int quantity, double price, double total_price, long barcode, long created_at) {
        this.product_id = product_id;
        this.merchant_id = merchant_id;
        this.order_number = order_number;
        this.owner_id = owner_id;
        this.quantity = quantity;
        this.price = price;
        this.total_price = total_price;
        this.barcode = barcode;
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(int merchant_id) {
        this.merchant_id = merchant_id;
    }

    public long getOrder_number() {
        return order_number;
    }

    public void setOrder_number(long order_number) {
        this.order_number = order_number;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }
}
