package com.diablo.jayson.kicksv1.Models;

import com.google.firebase.Timestamp;

public class FinishedActivity {

    private Activity activity;
    private Timestamp finishedTime;
    private boolean rated;
    private float rating;

    public FinishedActivity() {
    }

    public FinishedActivity(Activity activity, Timestamp finishedTime, boolean rated, float rating) {
        this.activity = activity;
        this.finishedTime = finishedTime;
        this.rated = rated;
        this.rating = rating;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Timestamp getFinishedTime() {
        return finishedTime;
    }

    public void setFinishedTime(Timestamp finishedTime) {
        this.finishedTime = finishedTime;
    }

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
