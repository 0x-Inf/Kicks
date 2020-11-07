package com.diablo.jayson.kicksv1.Models;

public class ExploreCategory {

    private String ExploreCategoryName;
    private String ExploreCategoryId;

    public ExploreCategory() {
    }

    public ExploreCategory(String exploreCategoryName, String exploreCategoryId) {
        ExploreCategoryName = exploreCategoryName;
        ExploreCategoryId = exploreCategoryId;
    }

    public String getExploreCategoryName() {
        return ExploreCategoryName;
    }

    public void setExploreCategoryName(String exploreCategoryName) {
        ExploreCategoryName = exploreCategoryName;
    }

    public String getExploreCategoryId() {
        return ExploreCategoryId;
    }

    public void setExploreCategoryId(String exploreCategoryId) {
        ExploreCategoryId = exploreCategoryId;
    }
}
