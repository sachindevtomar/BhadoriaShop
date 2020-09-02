package com.grocery.bhadoriashop.Models;

public class CartProduct {
    //We are using logged in userid as the object key in firebase database
    private String ProductName;
    private double Quantity;
    private String QuantityIn;
    private String ProductObjectKey;

    public CartProduct(String productName, double quantity, String quantityIn, String productObjectKey, int itemCount, long createdDateEPoch, String productImageURL, double productSellingPrice) {
        ProductName = productName;
        Quantity = quantity;
        QuantityIn = quantityIn;
        ProductObjectKey = productObjectKey;
        ItemCount = itemCount;
        CreatedDateEPoch = createdDateEPoch;
        ProductImageURL = productImageURL;
        ProductSellingPrice = productSellingPrice;
    }
    public CartProduct() {}

    private int ItemCount;
    private long CreatedDateEPoch;

    public String getProductImageURL() {
        return ProductImageURL;
    }

    public void setProductImageURL(String productImageURL) {
        ProductImageURL = productImageURL;
    }

    public double getProductSellingPrice() {
        return ProductSellingPrice;
    }

    public void setProductSellingPrice(double productSellingPrice) {
        ProductSellingPrice = productSellingPrice;
    }

    private String ProductImageURL;
    private double ProductSellingPrice;

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

}
