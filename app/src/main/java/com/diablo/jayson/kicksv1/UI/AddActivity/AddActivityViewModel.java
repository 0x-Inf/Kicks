package com.diablo.jayson.kicksv1.UI.AddActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.Contact;
import com.diablo.jayson.kicksv1.Models.Tag;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class AddActivityViewModel extends ViewModel {
    //    private final CustomMutableLiveData<Activity> activityMutableLiveData = new CustomMutableLiveData<Activity>();
    private final MutableLiveData<Activity> activityMutableLiveData = new MutableLiveData<Activity>();
    private MutableLiveData<List<Activity>> activity;
    private MutableLiveData<ArrayList<Contact>> invitedContactsMutableLiveData = new MutableLiveData<>();
    MutableLiveData<ArrayList<Tag>> allTagsMutableLiveData;
    ArrayList<Tag> allTagsArrayList;
    ArrayList<Tag> newTagsArrayList;
    MutableLiveData<ArrayList<Duration>> durationExamplesMutableLiveData = new MutableLiveData<>();
    ArrayList<Duration> allDurationExamplesArrayList;
    private MutableLiveData<Boolean> createNewTag = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Tag>> newTagsToCreate = new MutableLiveData<>();
    private FirebaseFirestore db;

//    public void setActivity(Activity activity) {
//        this.activityMutableLiveData.setValue(activity);
//    }


    public AddActivityViewModel() {
        allTagsMutableLiveData = new MutableLiveData<>();
        setActivity1(new Activity());
        Timber.e("Activity View Model Created");
        db = FirebaseFirestore.getInstance();
        init();
    }

    private void init() {
        getTagsFromDb();
        getDurationExamples();
        newTagsArrayList = new ArrayList<>();
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

    public MutableLiveData<ArrayList<Tag>> getAllTagsMutableLiveData() {
        return allTagsMutableLiveData;
    }

    public MutableLiveData<ArrayList<Duration>> getDurationExamplesMutableLiveData() {
        return durationExamplesMutableLiveData;
    }

    public void setInvitedContactsMutableLiveData(ArrayList<Contact> invitedContacts) {
        invitedContactsMutableLiveData.postValue(invitedContacts);
    }

    public void setActivity1(Activity activity1) {
        activityMutableLiveData.setValue(activity1);
    }

    //TODO: Add new Tags logic
    public void updateNewTagsMutableLiveData(Tag newTag) {
        newTagsArrayList.add(newTag);
        newTagsToCreate.postValue(newTagsArrayList);
    }

    public MutableLiveData<ArrayList<Tag>> getNewTagsToCreate() {
        return newTagsToCreate;
    }

    public void removeNewTagFromMutableLiveData(Tag removedTag) {
        newTagsArrayList.remove(removedTag);
        newTagsToCreate.postValue(newTagsArrayList);
    }

    // Methods for updating new Activity

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

    public void updateActivityTime(Timestamp activityStartDate, Timestamp activityStartTime, Duration activityDuration) {
        Activity mainActivity = getActivity1().getValue();
        assert mainActivity != null;
        mainActivity.setActivityStartDate(activityStartDate);
        mainActivity.setActivityStartTime(activityStartTime);
        mainActivity.setActivityDuration(activityDuration);
        activityMutableLiveData.postValue(mainActivity);
    }

    public void updateActivityTags(ArrayList<Tag> activityTags) {
        Activity mainActivity = getActivity1().getValue();
        assert mainActivity != null;
        mainActivity.setActivityTags(activityTags);
        activityMutableLiveData.postValue(mainActivity);
    }

    public void updateActivityLocation(String activityLocationName, GeoPoint activityLocationCoordinates, boolean isLocationUndisclosed) {
        Activity mainActivity = getActivity1().getValue();
        assert mainActivity != null;
        mainActivity.setActivityLocationName(activityLocationName);
        mainActivity.setActivityLocationCoordinates(activityLocationCoordinates);
        mainActivity.setLocationUndisclosed(isLocationUndisclosed);
        activityMutableLiveData.postValue(mainActivity);
    }

    // Functions for database calls
    private void getTagsFromDb() {
        allTagsArrayList = new ArrayList<>();
        db.collection("tags")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                allTagsArrayList.add(documentSnapshot.toObject(Tag.class));
                            }
                            allTagsMutableLiveData.postValue(allTagsArrayList);
                        }
                    }
                });
    }

    private void getDurationExamples() {
        //TODO: Make a Better Implementation of this function
        allDurationExamplesArrayList = new ArrayList<>();
        Duration duration1 = new Duration(TimeUnit.MINUTES.toMillis(15), "15 Mins");
        Duration duration2 = new Duration(TimeUnit.MINUTES.toMillis(30), "30 Mins");
        Duration duration3 = new Duration(TimeUnit.MINUTES.toMillis(45), "45 Mins");
        Duration duration4 = new Duration(TimeUnit.HOURS.toMillis(1), "1 Hour");
        allDurationExamplesArrayList.add(duration1);
        allDurationExamplesArrayList.add(duration2);
        allDurationExamplesArrayList.add(duration3);
        allDurationExamplesArrayList.add(duration4);
        durationExamplesMutableLiveData.postValue(allDurationExamplesArrayList);
    }
}
