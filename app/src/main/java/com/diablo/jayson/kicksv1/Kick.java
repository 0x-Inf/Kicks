package com.diablo.jayson.kicksv1;

import android.widget.TextView;

public class Kick {

    private String mKickTitle;
    private String mKickTime;
    private String mKickDate;
    private String mKickLocation;
    private String mAlreadyAttendingPeeps;
    private String mRequiredPeeps;
    private final int mImageResource;

    //Constructor for kick data model

    public Kick(String kicktitle, String kicktime, String kickdate, String kicklocation,
         String alreadyattendingpeeps, String requiredpeeps, int imageresource) {
        this.mKickTitle = kicktitle;
        this.mKickTime = kicktime;
        this.mKickDate = kickdate;
        this.mKickLocation = kicklocation;
        this.mAlreadyAttendingPeeps = alreadyattendingpeeps;
        this.mRequiredPeeps = requiredpeeps;
        this.mImageResource = imageresource;
    }

    String getKickTitle() {
        return mKickTitle;
    }

    String getKickTime() {
        return mKickTime;
    }

    String getKickDate() {
        return mKickDate;
    }

    String getKickLocation() {
        return mKickLocation;
    }

    String getKickAlreadyAttendingPeeps() {
        return mAlreadyAttendingPeeps;
    }

    String getKickRequiredPeeps() {
        return mRequiredPeeps;
    }

    int getImageResource() {
        return mImageResource;
    }
}
