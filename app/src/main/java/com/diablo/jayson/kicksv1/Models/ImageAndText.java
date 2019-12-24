package com.diablo.jayson.kicksv1.Models;

public class ImageAndText extends FeaturedKicks {
    private String mFeaturedKickTitle;
    private String mFeaturedKickImageUrl;

    public ImageAndText(String mFeaturedKickTitle, String mFeaturedKickImageUrl) {
        this.mFeaturedKickTitle = mFeaturedKickTitle;
        this.mFeaturedKickImageUrl = mFeaturedKickImageUrl;
    }

    public ImageAndText() {

    }

    public String getFeaturedKickTitle() {
        return mFeaturedKickTitle;
    }

    public String getFeaturedKickImageUrl() {
        return mFeaturedKickImageUrl;
    }
}
