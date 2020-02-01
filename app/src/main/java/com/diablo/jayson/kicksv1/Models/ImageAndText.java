package com.diablo.jayson.kicksv1.Models;

public class ImageAndText extends FeaturedKicks {
    private String KickTitle;
    private String ImageUrl;

    public ImageAndText(String KickTitle, String ImageUrl) {
        this.KickTitle = KickTitle;
        this.ImageUrl = ImageUrl;
    }

    public ImageAndText() {

    }

    public void setKickTitle(String KickTitle) {
        this.KickTitle = KickTitle;
    }

    public void setImageUrl(String ImageUrl) {
        this.ImageUrl = ImageUrl;
    }

    public String getKickTitle() {
        return KickTitle;
    }

    public String getImageUrl() {
        return ImageUrl;
    }
}
