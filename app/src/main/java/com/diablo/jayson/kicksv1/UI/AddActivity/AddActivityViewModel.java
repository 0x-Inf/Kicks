package com.diablo.jayson.kicksv1.UI.AddActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.Models.Contact;
import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.Utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class AddActivityViewModel extends ViewModel {
    //    private final CustomMutableLiveData<Activity> activityMutableLiveData = new CustomMutableLiveData<Activity>();
    private final MutableLiveData<Activity> activityMutableLiveData = new MutableLiveData<Activity>();

    private MutableLiveData<ArrayList<Contact>> invitedContactsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> inviteContactsBoolean = new MutableLiveData<>();

    MutableLiveData<ArrayList<Tag>> allTagsMutableLiveData;
    ArrayList<Tag> allTagsArrayList;
    ArrayList<Tag> newTagsArrayList;

    MutableLiveData<ArrayList<Duration>> durationsMutableLiveData = new MutableLiveData<>();
    ArrayList<Duration> allDurationsArrayList;

    private MutableLiveData<Boolean> createNewTag = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Tag>> newTagsToCreate = new MutableLiveData<>();

    private FirebaseFirestore db;

//    public void setActivity(Activity activity) {
//        this.activityMutableLiveData.setValue(activity);
//    }


    public AddActivityViewModel() {
        allTagsMutableLiveData = new MutableLiveData<>();
        setActivity(new Activity());
        Timber.e("Activity View Model Created");
        db = FirebaseFirestore.getInstance();
        init();
    }

    private void init() {
        getTagsFromDb();
        getDurations();
        newTagsArrayList = new ArrayList<>();
    }

    public MutableLiveData<Activity> getActivity() {
        return activityMutableLiveData;
    }

    public void setActivity(Activity activity1) {
        activityMutableLiveData.setValue(activity1);
    }

    public MutableLiveData<ArrayList<Contact>> getInvitedContactsMutableLiveData() {
        return invitedContactsMutableLiveData;
    }

    public void setInvitedContactsMutableLiveData(ArrayList<Contact> invitedContacts) {
        invitedContactsMutableLiveData.postValue(invitedContacts);
    }

    public void setInviteContacts(boolean inviteContacts) {
        inviteContactsBoolean.postValue(inviteContacts);
    }

    public MutableLiveData<Boolean> getInviteContactsBoolean() {
        return inviteContactsBoolean;
    }

    public MutableLiveData<ArrayList<Tag>> getAllTagsMutableLiveData() {
        return allTagsMutableLiveData;
    }

    public MutableLiveData<ArrayList<Duration>> getDurationsMutableLiveData() {
        return durationsMutableLiveData;
    }


    //TODO: Add new Tags logic
    public void updateNewTagsMutableLiveData(Tag newTag) {
        newTagsArrayList.add(newTag);
        newTagsToCreate.postValue(newTagsArrayList);
    }

    public MutableLiveData<ArrayList<Tag>> getNewTagsToCreate() {
        return newTagsToCreate;
    }

    public Boolean getIfCreateNewTag() {
        return createNewTag.getValue();
    }

    public void setCreateNewTag(boolean createNewTags) {
        createNewTag.postValue(createNewTags);
    }

    public void removeNewTagFromMutableLiveData(Tag removedTag) {
        newTagsArrayList.remove(removedTag);
        newTagsToCreate.postValue(newTagsArrayList);
    }

    // Methods for updating new Activity

    public void updateActivityDescription(String activityTitle, String activityDescription) {
        Activity mainActivity = getActivity().getValue();
        assert mainActivity != null;
        mainActivity.setActivityTitle(activityTitle);
        mainActivity.setActivityDescription(activityDescription);
        activityMutableLiveData.postValue(mainActivity);
    }

    public void updateActivityPeople(String activityNoOfPeople, ArrayList<String> invitedPeopleUserIds, boolean isActivityPrivate) {
        Activity mainActivity = getActivity().getValue();
        assert mainActivity != null;
        mainActivity.setActivityNoOfPeople(activityNoOfPeople);
        mainActivity.setInvitedPeopleUserIds(invitedPeopleUserIds);
        mainActivity.setActivityPrivate(isActivityPrivate);
        activityMutableLiveData.postValue(mainActivity);
    }

    public void updateActivityTime(Timestamp activityStartDate, Timestamp activityStartTime, Duration activityDuration) {
        Activity mainActivity = getActivity().getValue();
        assert mainActivity != null;
        mainActivity.setActivityStartDate(activityStartDate);
        mainActivity.setActivityStartTime(activityStartTime);
        mainActivity.setActivityDuration(activityDuration);
        activityMutableLiveData.postValue(mainActivity);
    }

    public void updateActivityTags(ArrayList<Tag> activityTags) {
        Activity mainActivity = getActivity().getValue();
        assert mainActivity != null;
        mainActivity.setActivityTags(activityTags);
        activityMutableLiveData.postValue(mainActivity);
    }

    public void updateActivityLocation(String activityLocationName, GeoPoint activityLocationCoordinates, boolean isLocationUndisclosed) {
        Activity mainActivity = getActivity().getValue();
        assert mainActivity != null;
        mainActivity.setActivityLocationName(activityLocationName);
        mainActivity.setActivityLocationCoordinates(activityLocationCoordinates);
        mainActivity.setLocationUndisclosed(isLocationUndisclosed);
        activityMutableLiveData.postValue(mainActivity);
    }

    public void updateActivityCost(String activityCost) {
        Activity mainActivity = getActivity().getValue();
        assert mainActivity != null;
        mainActivity.setActivityCost(activityCost);
        activityMutableLiveData.postValue(mainActivity);
    }

    public void updateAttendeesAndHostAndTime() {
        Activity activityMain = getActivity().getValue();
        assert activityMain != null;
        ArrayList<AttendingUser> attendingUsers = new ArrayList<AttendingUser>();
        attendingUsers.add(FirebaseUtil.getAttendingUser());

        activityMain.setActivityAttendees(attendingUsers);
        activityMain.setHost(FirebaseUtil.getHost());
        activityMain.setActivityUploaderId(Objects.requireNonNull(FirebaseUtil.getHost()).getUid());
        Calendar calendarUploadedTime = Calendar.getInstance();
        java.sql.Timestamp uploadedTimestamp = new java.sql.Timestamp(calendarUploadedTime.getTimeInMillis());
        activityMain.setActivityUploadedTime(Timestamp.now());

    }

    public boolean missingFields() {
        Activity mainActivity = getActivity().getValue();
        assert mainActivity != null;
        if (mainActivity.getActivityTitle() != null && mainActivity.getActivityDescription() != null &&
                mainActivity.getActivityNoOfPeople() != null && mainActivity.getActivityStartTime() != null && mainActivity.getActivityStartDate() != null &&
                mainActivity.getActivityDuration() != null && mainActivity.getActivityTags() != null && mainActivity.getActivityLocationName() != null &&
                mainActivity.getActivityCost() != null) {
            return false;
        } else {
            return true;
        }
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

    private void getDurations() {
        //TODO: Make a Better Implementation of this function
        allDurationsArrayList = new ArrayList<>();
        Duration duration1 = new Duration(TimeUnit.MINUTES.toMillis(15), "15 Mins");
        Duration duration2 = new Duration(TimeUnit.MINUTES.toMillis(30), "30 Mins");
        Duration duration3 = new Duration(TimeUnit.MINUTES.toMillis(45), "45 Mins");
        Duration duration4 = new Duration(TimeUnit.HOURS.toMillis(1), "1 Hour");
        allDurationsArrayList.add(duration1);
        allDurationsArrayList.add(duration2);
        allDurationsArrayList.add(duration3);
        allDurationsArrayList.add(duration4);
        durationsMutableLiveData.postValue(allDurationsArrayList);
    }
}
