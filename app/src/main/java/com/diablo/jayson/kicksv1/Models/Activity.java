package com.diablo.jayson.kicksv1.Models;

import android.widget.TextView;

public class Activity {

    private String mKickTitle;
    private String mKickTime;
    private String mKickDate;
    private String mKickLocation;
    private String mAlreadyAttendingPeeps;
    private String mRequiredPeeps;
    private String mImageUrl;

    public Activity(){

    }

    //Constructor for kick data model

    public Activity(String kicktitle, String kicktime, String kickdate, String kicklocation,
                    String alreadyattendingpeeps, String requiredpeeps, String imageUrl) {
        this.mKickTitle = kicktitle;
        this.mKickTime = kicktime;
        this.mKickDate = kickdate;
        this.mKickLocation = kicklocation;
        this.mAlreadyAttendingPeeps = alreadyattendingpeeps;
        this.mRequiredPeeps = requiredpeeps;
        this.mImageUrl = imageUrl;
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

    public String getKickAlreadyAttendingPeeps() {
        return mAlreadyAttendingPeeps;
    }

    public String getKickRequiredPeeps() {
        return mRequiredPeeps;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
}
