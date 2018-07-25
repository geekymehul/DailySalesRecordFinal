package com.example.gargc.dailysalesrecord.Model;

import java.io.Serializable;

/**
 * Created by gargc on 16-07-2018.
 */

public class CustomerContent implements Serializable {

    private String CustomerName;
    private  String EmailId;
    private  String PhoneNumber;
    private String GSTNumber;
    private String Notes;
    private String Image;
    private String Address;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }



    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }


    public String getNotes() {
        return Notes;
    }

    public String getGSTNumber() {
        return GSTNumber;
    }

    public void setGSTNumber(String GSTNumber) {
        this.GSTNumber = GSTNumber;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }



}
