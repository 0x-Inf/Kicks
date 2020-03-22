package com.diablo.jayson.kicksv1.UI.AttendActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diablo.jayson.kicksv1.Models.Activity;

public class AttendActivityViewModel extends ViewModel {

    private final MutableLiveData<String> activityIdData = new MutableLiveData<String>();
    private final MutableLiveData<Activity> activityMutableLiveData = new MutableLiveData<Activity>();

    public void setActivityData(Activity activityData) {
        activityMutableLiveData.setValue(activityData);
    }

    public LiveData<Activity> getActivityData() {
        return activityMutableLiveData;
    }

    public void setActivityId(String activityId) {
        activityIdData.setValue(activityId);
    }

    public LiveData<String> getActivityId() {
        return activityIdData;
    }
}
