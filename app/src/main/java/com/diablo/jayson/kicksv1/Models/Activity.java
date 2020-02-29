package com.diablo.jayson.kicksv1.Models;

import java.util.List;

public class Activity {

    private String kickTitle;
    private String kickTime;
    private String kickDate;
    private String kickLocation;
    private String minRequiredPeople;
    private String maxRequiredPeeps;
    private String imageUrl;
    private List<String> tags;

    public Activity(){

    }

    //Constructor for kick data model

    public Activity(String kicktitle, String kicktime, String kickdate, String kicklocation,
                    String minrequiredpeople, String maxrequiredpeeps, String imageUrl, List<String> tags) {
        this.kickTitle = kicktitle;
        this.kickTime = kicktime;
        this.kickDate = kickdate;
        this.kickLocation = kicklocation;
        this.minRequiredPeople = minrequiredpeople;
        this.maxRequiredPeeps = maxrequiredpeeps;
        this.imageUrl = imageUrl;
        this.tags = tags;

    }

    public void setmKickTitle(String mKickTitle) {
        this.kickTitle = mKickTitle;
    }

    public void setmKickTime(String mKickTime) {
        this.kickTime = mKickTime;
    }

    public void setmKickDate(String mKickDate) {
        this.kickDate = mKickDate;
    }

    public void setmKickLocation(String mKickLocation) {
        this.kickLocation = mKickLocation;
    }

    public void setMinRequiredPeople(String minRequiredPeople) {
        this.minRequiredPeople = minRequiredPeople;
    }

    public void setMaxRequiredPeeps(String maxRequiredPeeps) {
        this.maxRequiredPeeps = maxRequiredPeeps;
    }

    public void setmImageUrl(String mImageUrl) {
        this.imageUrl = mImageUrl;
    }

    public String getkickTitle() {
        return kickTitle;
    }

    public String getkickTime() {
        return kickTime;
    }

    public String getkickDate() {
        return kickDate;
    }

    public String getkickLocation() {
        return kickLocation;
    }

    public String getimageUrl() {
        return imageUrl;
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
