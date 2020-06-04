package com.diablo.jayson.kicksv1.UI.AddKick;

import com.google.firebase.Timestamp;

public class AddActivityDateTimeData {

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
}
