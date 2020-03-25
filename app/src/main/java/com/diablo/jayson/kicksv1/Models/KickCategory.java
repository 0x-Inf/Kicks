package com.diablo.jayson.kicksv1.Models;

import java.io.Serializable;

public class KickCategory implements Serializable {

    private String categoryName;
    private String categoryId;


    public KickCategory() {
    }

    public KickCategory(String categoryName, String categoryId) {
        this.categoryName = categoryName;
        this.categoryId = categoryId;
    }

    public String getCategoryName(){
        return categoryName;
    }

    public void setCategoryName(String mCategoryName) {
        this.categoryName = mCategoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}





