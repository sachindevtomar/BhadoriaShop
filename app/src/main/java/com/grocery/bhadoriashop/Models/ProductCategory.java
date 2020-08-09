package com.grocery.bhadoriashop.Models;

public class ProductCategory {
    private String CategoryName;

    private String CategoryImageURL;

    public ProductCategory(String CategoryName, String CategoryImageURL){
        this.CategoryName = CategoryName;
        this.CategoryImageURL = CategoryImageURL;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String CategoryName) {
        this.CategoryName = CategoryName;
    }

    public String getCategoryImageURL() {
        return CategoryImageURL;
    }

    public void setCategoryImageURL(String CategoryImageURL) {
        this.CategoryImageURL = CategoryImageURL;
    }
}
