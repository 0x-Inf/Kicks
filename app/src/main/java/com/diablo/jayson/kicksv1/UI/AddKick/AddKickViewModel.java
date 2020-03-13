package com.diablo.jayson.kicksv1.UI.AddKick;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.diablo.jayson.kicksv1.Models.Activity;

public class AddKickViewModel extends ViewModel {
    private final CustomMutableLiveData<Activity> activityMutableLiveData = new CustomMutableLiveData<Activity>();

    public void setActivity(Activity activity) {
        this.activityMutableLiveData.setValue(activity);
    }

    public LiveData<Activity> getActivity() {
        return activityMutableLiveData;
    }
}
