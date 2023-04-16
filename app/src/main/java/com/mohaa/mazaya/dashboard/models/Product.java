package com.mohaa.mazaya.dashboard.models;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Product  implements Serializable {

    public Product() {

    }
    private static final String TAG = "Product";
    private int id;
    private int cat_id;
    private int cat_parent_id;
    private int cat_gparent_id;
    private int merchant_id;
    private String merchant_name;
    private String product_name;
    private String product_shortname;
    private String product_desc;
    private int quantity;
    private double price;
    private long barcode;
    private int rate;
    private int ratecount;
    private int type;
    private String thumb_image;
    private double discount;
    private int department;
    private int company;
    private int pack;
    private int status;
    private long created_at;
    private int view_count;
    private int order_count;
    private int share_count;
    private int wish_count;
    private int sponsored;

    public Product(int cat_id, int cat_parent_id, int cat_gparent_id, int merchant_id, String merchant_name, String product_name, String product_shortname, String product_desc, int quantity, double price, long barcode, int rate, int ratecount, int type, String thumb_image, double discount, int department, int company, int pack, int status, long created_at, int view_count, int order_count, int share_count, int wish_count, int sponsored) {
        this.cat_id = cat_id;
        this.cat_parent_id = cat_parent_id;
        this.cat_gparent_id = cat_gparent_id;
        this.merchant_id = merchant_id;
        this.merchant_name = merchant_name;
        this.product_name = product_name;
        this.product_shortname = product_shortname;
        this.product_desc = product_desc;
        this.quantity = quantity;
        this.price = price;
        this.barcode = barcode;
        this.rate = rate;
        this.ratecount = ratecount;
        this.type = type;
        this.thumb_image = thumb_image;
        this.discount = discount;
        this.department = department;
        this.company = company;
        this.pack = pack;
        this.status = status;
        this.created_at = created_at;
        this.view_count = view_count;
        this.order_count = order_count;
        this.share_count = share_count;
        this.wish_count = wish_count;
        this.sponsored = sponsored;
    }

    public Product(int id, int view_count) {
        this.id = id;
        this.view_count = view_count;
    }

    public Product(int id, int rate, int ratecount) {
        this.id = id;
        this.rate = rate;
        this.ratecount = ratecount;
    }

    public Product(int id, int merchant_id, int rate, int ratecount) {
        this.id = id;
        this.merchant_id = merchant_id;
        this.rate = rate;
        this.ratecount = ratecount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public int getCat_parent_id() {
        return cat_parent_id;
    }

    public void setCat_parent_id(int cat_parent_id) {
        this.cat_parent_id = cat_parent_id;
    }

    public int getCat_gparent_id() {
        return cat_gparent_id;
    }

    public void setCat_gparent_id(int cat_gparent_id) {
        this.cat_gparent_id = cat_gparent_id;
    }

    public int getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(int merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getMerchant_name() {
        return merchant_name;
    }

    public void setMerchant_name(String merchant_name) {
        this.merchant_name = merchant_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_shortname() {
        return product_shortname;
    }

    public void setProduct_shortname(String product_shortname) {
        this.product_shortname = product_shortname;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
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

    public long getBarcode() {
        return barcode;
    }

    public void setBarcode(long barcode) {
        this.barcode = barcode;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getRatecount() {
        return ratecount;
    }

    public void setRatecount(int ratecount) {
        this.ratecount = ratecount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }

    public int getCompany() {
        return company;
    }

    public void setCompany(int company) {
        this.company = company;
    }

    public int getPack() {
        return pack;
    }

    public void setPack(int pack) {
        this.pack = pack;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public int getOrder_count() {
        return order_count;
    }

    public void setOrder_count(int order_count) {
        this.order_count = order_count;
    }

    public int getShare_count() {
        return share_count;
    }

    public void setShare_count(int share_count) {
        this.share_count = share_count;
    }

    public int getWish_count() {
        return wish_count;
    }

    public void setWish_count(int wish_count) {
        this.wish_count = wish_count;
    }

    public int getSponsored() {
        return sponsored;
    }

    public void setSponsored(int sponsored) {
        this.sponsored = sponsored;
    }
}
