package com.example.gargc.dailysalesrecord.Model;

import java.io.Serializable;

/**
 * Created by gargc on 20-07-2018.
 */

public class AddProduct implements Serializable {

    private String productName;
    private float Quantity;
    private Float actualPrice,sellingPrice,profit;

    private String cgst,sgst,cess;


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getQuantity() {
        return Quantity;
    }

    public void setQuantity(float quantity) {
        Quantity = quantity;
    }

    public Float getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(Float actualPrice) {
        this.actualPrice = actualPrice;
    }

    public Float getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Float sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Float getProfit() {
        return profit;
    }

    public void setProfit(Float profit) {
        this.profit = profit;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getCess() {
        return cess;
    }

    public void setCess(String cess) {
        this.cess = cess;
    }
}
