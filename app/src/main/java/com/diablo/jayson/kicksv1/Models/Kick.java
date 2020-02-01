package com.diablo.jayson.kicksv1.Models;

public class Kick {

    private String KickName;
    private String  KickImageUrl;



    public Kick(String kickName, String imageUrl) {
        this.KickName = kickName;
        this.KickImageUrl = imageUrl;
    }



    public Kick() {

    }

    public String getKickName() {
        return KickName;
    }

    public String getKickImageUrl() {
        return KickImageUrl;
    }
    public void setKickName(String mKickName) {
        this.KickName = mKickName;
    }

    public void setKickImageUrl(String KickImageUrl) {
        this.KickImageUrl = KickImageUrl;
    }

}
