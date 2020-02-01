package com.diablo.jayson.kicksv1.Models;

public class KickInFeaturedList {
    private String KickName;
    private String  KickImageUrl;
    private String KickShortDescription;



    public KickInFeaturedList(String kickName, String imageUrl, String kickShortDescription) {
        this.KickName = kickName;
        this.KickImageUrl = imageUrl;
        this.KickShortDescription = kickShortDescription;
    }



    public KickInFeaturedList() {

    }

    public String getKickName() {
        return KickName;
    }

    public String getKickImageUrl() {
        return KickImageUrl;
    }
    public void setKickName(String KickName) {
        this.KickName = KickName;
    }

    public void setKickImageUrl(String KickImageUrl) {
        this.KickImageUrl = KickImageUrl;
    }

    public String getKickShortDescription() {
        return KickShortDescription;
    }

    public void setKickShortDescription(String KickShortDescription) {
        this.KickShortDescription = KickShortDescription;
    }
}
