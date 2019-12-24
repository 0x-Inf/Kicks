package com.diablo.jayson.kicksv1.Models;

public class Kick {

    private String mKickName;
    private String  mImageUrl;

    public Kick(String kickName, String imageUrl) {
        this.mKickName = kickName;
        this.mImageUrl = imageUrl;
    }

    public Kick() {

    }

    public String getKickName() {
        return mKickName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
}
