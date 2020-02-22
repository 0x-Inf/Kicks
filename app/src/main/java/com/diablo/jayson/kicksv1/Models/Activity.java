package com.diablo.jayson.kicksv1.Models;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Activity {

    private String mKickTitle;
    private String mKickTime;
    private String mKickDate;
    private String mKickLocation;
    private String minRequiredPeople;
    private String maxRequiredPeeps;
    private String mImageUrl;
    private List<String> tags;

    public Activity(){

    }

    //Constructor for kick data model

    public Activity(String kicktitle, String kicktime, String kickdate, String kicklocation,
                    String minrequiredpeople, String maxrequiredpeeps, String imageUrl, List<String> tags) {
        this.mKickTitle = kicktitle;
        this.mKickTime = kicktime;
        this.mKickDate = kickdate;
        this.mKickLocation = kicklocation;
        this.minRequiredPeople = minrequiredpeople;
        this.maxRequiredPeeps = maxrequiredpeeps;
        this.mImageUrl = imageUrl;
        this.tags = tags;

    }

    public String getKickTitle() {
        return mKickTitle;
    }

    public String getKickTime() {
        return mKickTime;
    }

    public String getKickDate() {
        return mKickDate;
    }

    public String getKickLocation() {
        return mKickLocation;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getMaxRequiredPeeps() {
        return maxRequiredPeeps;
    }

    public String getMinRequiredPeople() {
        return minRequiredPeople;
    }

    public List<String> getTags() {
        return tags;
    }
}
