package com.diablo.jayson.kicksv1.UI.AddKick;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diablo.jayson.kicksv1.Models.Activity;

import java.util.List;

public class AddKickViewModel extends ViewModel {
    //    private final CustomMutableLiveData<Activity> activityMutableLiveData = new CustomMutableLiveData<Activity>();
    private final MutableLiveData<Activity> mutableLiveData = new MutableLiveData<Activity>();
    private MutableLiveData<List<Activity>> activity;

//    public void setActivity(Activity activity) {
//        this.activityMutableLiveData.setValue(activity);
//    }

    public void setActivity1(Activity activity1) {
        mutableLiveData.setValue(activity1);
    }

    //    public LiveData<Activity> getActivity() {
//        return activityMutableLiveData;
//    }
    public LiveData<Activity> getActivity1() {
        return mutableLiveData;
    }
}
