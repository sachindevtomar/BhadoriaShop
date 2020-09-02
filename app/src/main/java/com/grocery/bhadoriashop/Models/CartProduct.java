package com.grocery.bhadoriashop.Models;

public class CartProduct {
    //We are using logged in userid as the object key in firebase database
    private String ProductName;

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public double getQuantity() {
        return Quantity;
    }

    public void setQuantity(double quantity) {
        Quantity = quantity;
    }

    public String getQuantityIn() {
        return QuantityIn;
    }

    public void setQuantityIn(String quantityIn) {
        QuantityIn = quantityIn;
    }

    public String getProductObjectKey() {
        return ProductObjectKey;
    }

    public void setProductObjectKey(String productObjectKey) {
        ProductObjectKey = productObjectKey;
    }

    public int getItemCount() {
        return ItemCount;
    }

    public void setItemCount(int itemCount) {
        ItemCount = itemCount;
    }

    public long getCreatedDateEPoch() {
        return CreatedDateEPoch;
    }

    public void setCreatedDateEPoch(long createdDateEPoch) {
        CreatedDateEPoch = createdDateEPoch;
    }

    private double Quantity;
    private String QuantityIn;
    private String ProductObjectKey;
    private int ItemCount;

    public CartProduct(String productName, double quantity, String quantityIn, String productObjectKey, int itemCount, long createdDateEPoch) {
        ProductName = productName;
        Quantity = quantity;
        QuantityIn = quantityIn;
        ProductObjectKey = productObjectKey;
        ItemCount = itemCount;
        CreatedDateEPoch = createdDateEPoch;
    }

    private long CreatedDateEPoch;
}
