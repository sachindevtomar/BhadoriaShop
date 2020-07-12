package com.grocery.bhadoriashop.Models;

public class Product {
    public String ProductName;
    public String ProductCategory;
    public String ProductBrand;
    public String MeasureIn;
    public double TotalWeight;
    public String TotalWeightIn;
    public int ItemCount;
    public double ItemWeight;
    public String ItemWeightIn;
    public int PricePerUnit;
    public String ProductImageURL;

    public Product(String ProductName, String ProductCategory, String ProductBrand, String MeasureIn, double TotalWeight,
                   String TotalWeightIn, int PricePerUnit, String ProductImageURL){
        this.ProductName = ProductName;
        this.ProductCategory = ProductCategory;
        this.ProductBrand = ProductBrand;
        this.MeasureIn = MeasureIn;
        this.TotalWeight = TotalWeight;
        this.TotalWeightIn = TotalWeightIn;
        this.PricePerUnit = PricePerUnit;
        this.ProductImageURL = ProductImageURL;
    }


    public Product(String ProductName, String ProductCategory, String ProductBrand, String MeasureIn, int ItemCount, double ItemWeight,
                   String ItemWeightIn, int PricePerUnit, String ProductImageURL){
        this.ProductName = ProductName;
        this.ProductCategory = ProductCategory;
        this.ProductBrand = ProductBrand;
        this.MeasureIn = MeasureIn;
        this.ItemCount = ItemCount;
        this.ItemWeight = ItemWeight;
        this.ItemWeightIn = ItemWeightIn;
        this.PricePerUnit = PricePerUnit;
        this.ProductImageURL = ProductImageURL;
    }
}
