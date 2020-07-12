package com.grocery.bhadoriashop.Models;

public class Product {
    public String ProductName;
    public String ProductCategory;
    public String ProductSubCategory;
    public String ProductBrand;
    public String MeasureIn;
    public double TotalWeight;
    public String TotalWeightIn;
    public int ItemCount;
    public double ItemWeight;
    public String ItemWeightIn;
    public double MRPPricePerUnit;
    public double SellingPricePerUnit;
    public String ProductImageURL;

//    public Product(String ProductName, String ProductCategory, String ProductSubCategory, String ProductBrand, String MeasureIn, double TotalWeight, String TotalWeightIn, double MRPPricePerUnit, double SellingPricePerUnit, String ProductImageURL){
//        this.ProductName = ProductName;
//        this.ProductCategory = ProductCategory;
//        this.ProductSubCategory = ProductSubCategory;
//        this.ProductBrand = ProductBrand;
//        this.MeasureIn = MeasureIn;
//        this.TotalWeight = TotalWeight;
//        this.TotalWeightIn = TotalWeightIn;
//        this.MRPPricePerUnit = MRPPricePerUnit;
//        this.SellingPricePerUnit = SellingPricePerUnit;
//        this.ProductImageURL = ProductImageURL;
//    }


    public Product(String ProductName, String ProductCategory, String ProductSubCategory, String ProductBrand, String MeasureIn, double TotalWeight, String TotalWeightIn, int ItemCount, double ItemWeight, String ItemWeightIn, double MRPPricePerUnit, double SellingPricePerUnit, String ProductImageURL){
        this.ProductName = ProductName;
        this.ProductCategory = ProductCategory;
        this.ProductSubCategory = ProductSubCategory;
        this.ProductBrand = ProductBrand;
        this.MeasureIn = MeasureIn;
        this.TotalWeight = TotalWeight;
        this.TotalWeightIn = TotalWeightIn;
        this.ItemCount = ItemCount;
        this.ItemWeight = ItemWeight;
        this.ItemWeightIn = ItemWeightIn;
        this.MRPPricePerUnit = MRPPricePerUnit;
        this.SellingPricePerUnit = SellingPricePerUnit;
        this.ProductImageURL = ProductImageURL;
    }
}
