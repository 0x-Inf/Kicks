package com.diablo.jayson.kicksv1.UI.AddKick;

import com.google.firebase.firestore.GeoPoint;

public class AddActivityLocationData {

    private String activityLocationName;
    private GeoPoint activityLocationCoordinates;

    public AddActivityLocationData() {
    }

    public AddActivityLocationData(String activityLocationName, GeoPoint activityLocationCoordinates) {
        this.activityLocationName = activityLocationName;
        this.activityLocationCoordinates = activityLocationCoordinates;
    }

    public String getActivityLocationName() {
        return activityLocationName;
    }

    public void setActivityLocationName(String activityLocationName) {
        this.activityLocationName = activityLocationName;
    }

    public GeoPoint getActivityLocationCoordinates() {
        return activityLocationCoordinates;
    }

    public void setActivityLocationCoordinates(GeoPoint activityLocationCoordinates) {
        this.activityLocationCoordinates = activityLocationCoordinates;
    }
}
