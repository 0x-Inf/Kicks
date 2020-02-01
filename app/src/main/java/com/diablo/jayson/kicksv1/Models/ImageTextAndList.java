package com.diablo.jayson.kicksv1.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImageTextAndList extends FeaturedKicks {


    private HashMap<String,KickInFeaturedList> KickList;
    private String KickListImageUrl;
    private String KickListName;

    public ImageTextAndList(){}



    public ImageTextAndList(HashMap<String,KickInFeaturedList> KickList, String KickListImageUrl, String KickListName) {
        this.KickList = KickList;
        this.KickListImageUrl = KickListImageUrl;
        this.KickListName = KickListName;
    }

    public HashMap<String,KickInFeaturedList> getKickList() {
        return KickList;
    }

    public String getKickListImageUrl() {
        return KickListImageUrl;
    }

    public String getKickListName() {
        return KickListName;
    }

    public void setKickList(HashMap<String,KickInFeaturedList> KickList) {
        this.KickList = KickList;
    }

    public void setKickListImageUrl(String KickListImageUrl) {
        this.KickListImageUrl = KickListImageUrl;
    }

    public void setKickListName(String KickListName) {
        this.KickListName = KickListName;
    }
}
