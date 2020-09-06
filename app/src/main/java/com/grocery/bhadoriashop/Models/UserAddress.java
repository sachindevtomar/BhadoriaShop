package com.grocery.bhadoriashop.Models;

public class UserAddress{
    public UserAddress(String address, String pincode, boolean isPrimary) {
        Address = address;
        Pincode = pincode;
        IsPrimary = isPrimary;
    }

    String Address;
    String Pincode;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public boolean isPrimary() {
        return IsPrimary;
    }

    public void setPrimary(boolean primary) {
        IsPrimary = primary;
    }

    boolean IsPrimary;
}
