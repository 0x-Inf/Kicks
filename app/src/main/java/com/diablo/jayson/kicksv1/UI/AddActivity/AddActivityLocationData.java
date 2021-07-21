package com.diablo.jayson.kicksv1.UI.AddActivity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

public class AddActivityLocationData implements Parcelable {

    private String activityLocationName;
    private GeoPoint activityLocationCoordinates;

    public AddActivityLocationData() {
    }

    public AddActivityLocationData(String activityLocationName, GeoPoint activityLocationCoordinates) {
        this.activityLocationName = activityLocationName;
        this.activityLocationCoordinates = activityLocationCoordinates;
    }

    protected AddActivityLocationData(Parcel in) {
        activityLocationName = in.readString();
    }

    public static final Creator<AddActivityLocationData> CREATOR = new Creator<AddActivityLocationData>() {
        @Override
        public AddActivityLocationData createFromParcel(Parcel in) {
            return new AddActivityLocationData(in);
        }

        @Override
        public AddActivityLocationData[] newArray(int size) {
            return new AddActivityLocationData[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(activityLocationName);
    }
}
