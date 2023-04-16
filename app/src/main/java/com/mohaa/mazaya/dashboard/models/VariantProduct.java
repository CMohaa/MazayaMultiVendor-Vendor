package com.mohaa.mazaya.dashboard.models;


import java.io.Serializable;

public class VariantProduct implements Serializable {

    public VariantProduct() {

    }
    private static final String TAG = "VariantProduct";
    private int id;
    private int department_id;
    private int company_id;
    private int pack_id;
    private int status_id;
    private int pdt_id;

    public VariantProduct(int department_id, int company_id, int pack_id, int status_id, int pdt_id) {
        this.department_id = department_id;
        this.company_id = company_id;
        this.pack_id = pack_id;
        this.status_id = status_id;
        this.pdt_id = pdt_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getPack_id() {
        return pack_id;
    }

    public void setPack_id(int pack_id) {
        this.pack_id = pack_id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public int getPdt_id() {
        return pdt_id;
    }

    public void setPdt_id(int pdt_id) {
        this.pdt_id = pdt_id;
    }
}
