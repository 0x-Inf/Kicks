package com.diablo.jayson.kicksv1.UI.AddKick;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class AddActivityDateTimeData implements Parcelable {

    private Timestamp activityStartTime;
    private Timestamp activityEndTime;
    private Timestamp activityDate;

    public AddActivityDateTimeData() {
    }

    public AddActivityDateTimeData(Timestamp activityStartTime, Timestamp activityEndTime, Timestamp activityDate) {
        this.activityStartTime = activityStartTime;
        this.activityEndTime = activityEndTime;
        this.activityDate = activityDate;
    }

    protected AddActivityDateTimeData(Parcel in) {
        activityStartTime = in.readParcelable(Timestamp.class.getClassLoader());
        activityEndTime = in.readParcelable(Timestamp.class.getClassLoader());
        activityDate = in.readParcelable(Timestamp.class.getClassLoader());
    }

    public static final Creator<AddActivityDateTimeData> CREATOR = new Creator<AddActivityDateTimeData>() {
        @Override
        public AddActivityDateTimeData createFromParcel(Parcel in) {
            return new AddActivityDateTimeData(in);
        }

        @Override
        public AddActivityDateTimeData[] newArray(int size) {
            return new AddActivityDateTimeData[size];
        }
    };

    public Timestamp getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(Timestamp activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public Timestamp getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(Timestamp activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public Timestamp getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Timestamp activityDate) {
        this.activityDate = activityDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(activityStartTime, flags);
        dest.writeParcelable(activityEndTime, flags);
        dest.writeParcelable(activityDate, flags);
    }
}
