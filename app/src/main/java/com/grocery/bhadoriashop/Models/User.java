package com.grocery.bhadoriashop.Models;

import java.util.ArrayList;
import java.util.List;

public class User {
    //We are using logged in userid as the object key in firebase database
    private String UserId;
    private String FullName;
    private String PhoneNumber;

    public User(String userId, String fullName, String phoneNumber, long createdDateEPoch, String email, boolean gender, boolean isAdmin, ArrayList<UserAddress> addresses) {
        UserId = userId;
        FullName = fullName;
        PhoneNumber = phoneNumber;
        CreatedDateEPoch = createdDateEPoch;
        Email = email;
        Gender = gender;   //True refer male
        IsAdmin = isAdmin;
        Addresses = addresses;
    }

    public long getCreatedDateEPoch() {
        return CreatedDateEPoch;
    }

    public void setCreatedDateEPoch(long createdDateEPoch) {
        CreatedDateEPoch = createdDateEPoch;
    }

    private long CreatedDateEPoch;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public boolean isGender() {
        return Gender;
    }

    public void setGender(boolean gender) {
        Gender = gender;
    }

    public boolean isAdmin() {
        return IsAdmin;
    }

    public void setAdmin(boolean admin) {
        IsAdmin = admin;
    }

    public ArrayList<UserAddress> getAddresses() {
        return Addresses;
    }

    public void setAddresses(ArrayList<UserAddress> addresses) {
        Addresses = addresses;
    }

    private String Email;
    private boolean Gender;
    private boolean IsAdmin;
    private ArrayList<UserAddress> Addresses;
}
