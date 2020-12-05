package com.diablo.jayson.kicksv1.UI.AddKick;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.Contact;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class AddActivityViewModel extends ViewModel {
    //    private final CustomMutableLiveData<Activity> activityMutableLiveData = new CustomMutableLiveData<Activity>();
    private final MutableLiveData<Activity> activityMutableLiveData = new MutableLiveData<Activity>();
    private MutableLiveData<List<Activity>> activity;
    private MutableLiveData<ArrayList<Contact>> invitedContactsMutableLiveData = new MutableLiveData<>();

//    public void setActivity(Activity activity) {
//        this.activityMutableLiveData.setValue(activity);
//    }


    public AddActivityViewModel() {
        setActivity1(new Activity());
        Timber.e("Activity View Model Created");
    }

    //    public LiveData<Activity> getActivity() {
//        return activityMutableLiveData;
//    }
    public LiveData<Activity> getActivity1() {
        return activityMutableLiveData;
    }

    public MutableLiveData<ArrayList<Contact>> getInvitedContactsMutableLiveData() {
        return invitedContactsMutableLiveData;
    }

    public void setInvitedContactsMutableLiveData(ArrayList<Contact> invitedContacts) {
        invitedContactsMutableLiveData.postValue(invitedContacts);
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

    public void updateActivityPeople(String activityNoOfPeople, ArrayList<String> invitedPeopleUserIds, boolean isActivityPrivate) {
        Activity mainActivity = getActivity1().getValue();
        assert mainActivity != null;
        mainActivity.setActivityNoOfPeople(activityNoOfPeople);
        mainActivity.setInvitedPeopleUserIds(invitedPeopleUserIds);
        mainActivity.setActivityPrivate(isActivityPrivate);
        activityMutableLiveData.postValue(mainActivity);
    }
    public void updateActivityTime(Timestamp activityStartDate, Timestamp activityStartTime, String activityDuration){
        Activity mainActivity = getActivity1().getValue();
        assert mainActivity != null;
        mainActivity.setActivityStartDate(activityStartDate);
        mainActivity.setActivityStartTime(activityStartTime);
        mainActivity.setActivityDuration(activityDuration);
        activityMutableLiveData.postValue(mainActivity);
    }
}
