package com.diablo.jayson.kicksv1.Models;

import java.util.ArrayList;

public class KickInFeaturedList {


    private String KickName;
    private String KickImage;
    private String KickShortDescription;
    private String kickId;
    private ArrayList<String> tags;

    public KickInFeaturedList() {
    }

    public KickInFeaturedList(String kickName, String kickImage, String kickShortDescription, String kickId, ArrayList<String> tags) {
        KickName = kickName;
        KickImage = kickImage;
        KickShortDescription = kickShortDescription;
        this.kickId = kickId;
        this.tags = tags;
    }

    public String getKickName() {
        return KickName;
    }

    public void setKickName(String kickName) {
        KickName = kickName;
    }

    public String getKickImage() {
        return KickImage;
    }

    public void setKickImage(String kickImage) {
        KickImage = kickImage;
    }

    public String getKickShortDescription() {
        return KickShortDescription;
    }

    public void setKickShortDescription(String kickShortDescription) {
        KickShortDescription = kickShortDescription;
    }

    public String getKickId() {
        return kickId;
    }

    public void setKickId(String kickId) {
        this.kickId = kickId;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
