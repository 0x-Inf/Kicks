package com.diablo.jayson.kicksv1.Models;

import java.io.Serializable;

public class FeaturedKicks implements Serializable {

    private String featuredType;
    private String featuredTitle;
    private String featuredSubTitle;
    private String featuredImageUrl;
    private String featuredKickId;

    public FeaturedKicks() {
    }

    public FeaturedKicks(String featuredType, String featuredTitle, String featuredSubTitle, String featuredImageUrl, String featuredKickId) {
        this.featuredType = featuredType;
        this.featuredTitle = featuredTitle;
        this.featuredSubTitle = featuredSubTitle;
        this.featuredImageUrl = featuredImageUrl;
        this.featuredKickId = featuredKickId;
    }

    public String getFeaturedKickId() {
        return featuredKickId;
    }

    public void setFeaturedKickId(String featuredKickId) {
        this.featuredKickId = featuredKickId;
    }

    public String getFeaturedType() {
        return featuredType;
    }

    public void setFeaturedType(String featuredType) {
        this.featuredType = featuredType;
    }

    public String getFeaturedTitle() {
        return featuredTitle;
    }

    public void setFeaturedTitle(String featuredTitle) {
        this.featuredTitle = featuredTitle;
    }

    public String getFeaturedSubTitle() {
        return featuredSubTitle;
    }

    public void setFeaturedSubTitle(String featuredSubTitle) {
        this.featuredSubTitle = featuredSubTitle;
    }

    public String getFeaturedImageUrl() {
        return featuredImageUrl;
    }

    public void setFeaturedImageUrl(String featuredImageUrl) {
        this.featuredImageUrl = featuredImageUrl;
    }
}
