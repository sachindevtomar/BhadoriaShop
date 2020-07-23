package com.grocery.bhadoriashop.Models;

public class AdminProductList {

    //constructor
    public AdminProductList(){}

    String ProductName, ProductCategory, ProductSubCategory, ProductBrand, ProductImageURL, MeasureIn, TotalWeightIn, ItemWeightIn;

    double MRPPricePerUnit, SellingPricePerUnit, TotalWeight, ItemWeight;

    int ItemCount;

    public String getProductName() {
        return ProductName;
    }

    public String getMeasureIn() {
        return MeasureIn;
    }

    public String getTotalWeightIn() {
        return TotalWeightIn;
    }

    public String getItemWeightIn() {
        return ItemWeightIn;
    }

    public double getTotalWeight() {
        return TotalWeight;
    }

    public double getItemWeight() {
        return ItemWeight;
    }

    public int getItemCount() {
        return ItemCount;
    }

    public String getProductCategory() {
        return ProductCategory;
    }

    public String getProductSubCategory() {
        return ProductSubCategory;
    }

    public String getProductBrand() {
        return ProductBrand;
    }

    public String getProductImageURL() {
        return ProductImageURL;
    }

    public double getMRPPricePerUnit() {
        return MRPPricePerUnit;
    }

    public double getSellingPricePerUnit() {
        return SellingPricePerUnit;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public void setProductCategory(String productCategory) {
        ProductCategory = productCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        ProductSubCategory = productSubCategory;
    }

    public void setProductBrand(String productBrand) {
        ProductBrand = productBrand;
    }

    public void setProductImageURL(String productImageURL) {
        ProductImageURL = productImageURL;
    }

    public void setMRPPricePerUnit(double MRPPricePerUnit) {
        this.MRPPricePerUnit = MRPPricePerUnit;
    }

    public void setSellingPricePerUnit(double sellingPricePerUnit) {
        SellingPricePerUnit = sellingPricePerUnit;
    }


    public void setMeasureIn(String measureIn) {
        MeasureIn = measureIn;
    }

    public void setTotalWeightIn(String totalWeightIn) {
        TotalWeightIn = totalWeightIn;
    }

    public void setItemWeightIn(String itemWeightIn) {
        ItemWeightIn = itemWeightIn;
    }

    public void setTotalWeight(double totalWeight) {
        TotalWeight = totalWeight;
    }

    public void setItemWeight(double itemWeight) {
        ItemWeight = itemWeight;
    }

    public void setItemCount(int itemCount) {
        ItemCount = itemCount;
    }
}
