package com.diablo.jayson.kicksv1.UI.AddKick;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diablo.jayson.kicksv1.Models.Activity;

import java.util.ArrayList;
import java.util.List;

public class AddActivityViewModel extends ViewModel {
    //    private final CustomMutableLiveData<Activity> activityMutableLiveData = new CustomMutableLiveData<Activity>();
    private final MutableLiveData<Activity> activityMutableLiveData = new MutableLiveData<Activity>();
    private MutableLiveData<List<Activity>> activity;

//    public void setActivity(Activity activity) {
//        this.activityMutableLiveData.setValue(activity);
//    }


    public AddActivityViewModel() {
        setActivity1(new Activity());
    }

    //    public LiveData<Activity> getActivity() {
//        return activityMutableLiveData;
//    }
    public LiveData<Activity> getActivity1() {
        return activityMutableLiveData;
    }

    public void setActivity1(Activity activity1) {
        activityMutableLiveData.setValue(activity1);
    }

    public void updateActivityDescription(String activityTitle, String activityDescription) {
        Activity mainActivity = getActivity1().getValue();
        assert mainActivity != null;
        mainActivity.setActivityTitle(activityTitle);
        mainActivity.setActivityDescription(activityDescription);
        activityMutableLiveData.postValue(mainActivity);
    }

    public void updateActivityPeople(String activityNoOfPeople, ArrayList<String> invitedContacts) {
        Activity mainActivity = getActivity1().getValue();

    }
}
