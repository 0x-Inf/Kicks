package com.diablo.jayson.kicksv1.UI.AddActivity;

import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diablo.jayson.kicksv1.Constants;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.Models.Contact;
import com.diablo.jayson.kicksv1.Models.Invite;
import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.Utils.FirebaseUtil;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class AddActivityViewModel extends ViewModel {
    //    private final CustomMutableLiveData<Activity> activityMutableLiveData = new CustomMutableLiveData<Activity>();
    private final MutableLiveData<Activity> activityMutableLiveData = new MutableLiveData<Activity>();

    private MutableLiveData<ArrayList<Contact>> invitedContactsMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> inviteContactsBoolean = new MutableLiveData<>();

    private final MutableLiveData<Boolean> UPLOAD_ACTIVITY_SUCCESSFUL = new MutableLiveData<>();
    private final MutableLiveData<Boolean> UPLOAD_NEW_TAGS_SUCCESSFUL = new MutableLiveData<>();
    private final MutableLiveData<Boolean> SEND_INVITES_SUCCESSFUL = new MutableLiveData<>();
    private final MutableLiveData<String> UPLOAD_ACTIVITY_STATE = new MutableLiveData<>();

    MutableLiveData<ArrayList<Tag>> allTagsMutableLiveData;
    ArrayList<Tag> allTagsArrayList;
    ArrayList<Tag> newTagsArrayList;

    MutableLiveData<ArrayList<Duration>> durationsMutableLiveData = new MutableLiveData<>();
    ArrayList<Duration> allDurationsArrayList;

    private MutableLiveData<Boolean> createNewTag = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Tag>> newTagsToCreate = new MutableLiveData<>();

    private FirebaseFirestore db;
    private FirebaseUser currentUser;

//    public void setActivity(Activity activity) {
//        this.activityMutableLiveData.setValue(activity);
//    }


    public AddActivityViewModel() {
        allTagsMutableLiveData = new MutableLiveData<>();
        setActivity(new Activity());
        Timber.e("Activity View Model Created");
        init();
    }

    private void init() {
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
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
        String geohash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(activityLocationCoordinates.getLatitude(), activityLocationCoordinates.getLongitude()));
        mainActivity.setActivityLocationName(activityLocationName);
        mainActivity.setActivityLocationCoordinates(activityLocationCoordinates);
        mainActivity.setGeoHash(geohash);
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

    public MutableLiveData<Boolean> getUPLOAD_ACTIVITY_SUCCESSFUL() {
        return UPLOAD_ACTIVITY_SUCCESSFUL;
    }

    public void setUPLOAD_ACTIVITY_SUCCESSFUL(boolean upload_successful) {
        UPLOAD_ACTIVITY_SUCCESSFUL.postValue(upload_successful);
    }

    public MutableLiveData<Boolean> getUPLOAD_NEW_TAGS_SUCCESSFUL() {
        return UPLOAD_NEW_TAGS_SUCCESSFUL;
    }

    public void setUPLOAD_NEW_TAGS_SUCCESSFUL(boolean upload_new_tags_successful) {
        UPLOAD_NEW_TAGS_SUCCESSFUL.postValue(upload_new_tags_successful);
    }

    public MutableLiveData<Boolean> getSEND_INVITES_SUCCESSFUL() {
        return SEND_INVITES_SUCCESSFUL;
    }

    public void setSEND_INVITES_SUCCESSFUL(boolean send_invites_successful) {
        SEND_INVITES_SUCCESSFUL.postValue(send_invites_successful);
    }

    public MutableLiveData<String> getUPLOAD_ACTIVITY_STATE() {
        return UPLOAD_ACTIVITY_STATE;
    }

    public void setUPLOAD_ACTIVITY_STATE(String upload_activity_state) {
        UPLOAD_ACTIVITY_STATE.postValue(upload_activity_state);
    }

    public MutableLiveData<Boolean> uploadNewActivityToDb() {
        updateAttendeesAndHostAndTime();
        setUPLOAD_ACTIVITY_STATE(Constants.UPLOAD_ACTIVITY_STATE_START);
        SystemClock.sleep(1000);
        db.collection("activities").add(activityMutableLiveData.getValue())
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Timber.e("Successful Creation with id: %s", documentReference.getId());
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            if (getIfCreateNewTag()) {
                                setUPLOAD_ACTIVITY_STATE(Constants.UPLOAD_ACTIVITY_STATE_TAGS);
                                SystemClock.sleep(1000);
                                uploadNewTagsToDb();
                            }
                            if (getInviteContactsBoolean().getValue()) {
                                setUPLOAD_ACTIVITY_STATE(Constants.UPLOAD_ACTIVITY_STATE_INVITES);
                                SystemClock.sleep(1000);
                                sendInvitesToInvited();
                            }
                            Map<String, Object> activity = new HashMap<>();
                            activity.put("activityReference", task.getResult());
                            activity.put("activityId", task.getResult().getId());
                            db.collection("users")
                                    .document(currentUser.getUid())
                                    .collection("activeactivities")
                                    .document(task.getResult().getId())
                                    .set(activity)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Timber.d("DocumentSnapshot written with ID: %s", task.getResult().getId());
//                                        setUPLOAD_ACTIVITY_SUCCESSFUL(true);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Timber.e(e, "Failed to upload activity to user db");
//                                        setUPLOAD_ACTIVITY_SUCCESSFUL(false);
                                        }
                                    });

                            setUPLOAD_ACTIVITY_STATE(Constants.UPLOAD_ACTIVITY_STATE_FINISH);
                            SystemClock.sleep(1000);
                            setUPLOAD_ACTIVITY_SUCCESSFUL(true);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.e(e, "Failed to upload Activity to main activities collection");
                        setUPLOAD_ACTIVITY_STATE(Constants.UPLOAD_ACTIVITY_STATE_FINISH);
                        setUPLOAD_ACTIVITY_SUCCESSFUL(false);
                    }
                });

        return UPLOAD_ACTIVITY_SUCCESSFUL;
    }

    public void uploadNewTagsToDb() {
        ArrayList<Tag> tagsToUpload = getNewTagsToCreate().getValue();
//        changeLoadingText("Uploading New Tags");
        for (Tag tag : tagsToUpload) {
            db.collection("tags")
                    .add(tag)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Timber.e("New Tag uploaded successfully with id:%s", documentReference.getId());
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                String newTagId = task.getResult().getId();
                                db.collection("tags")
                                        .document(task.getResult().getId())
                                        .update("tagId", newTagId)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Timber.e("Tag id updated successfully");
                                                setUPLOAD_NEW_TAGS_SUCCESSFUL(true);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Timber.e(e, "Failed to update tag id with exception");
                                                setUPLOAD_NEW_TAGS_SUCCESSFUL(false);
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Timber.e(e, "Failed to Upload new tag with exception");
                            setUPLOAD_NEW_TAGS_SUCCESSFUL(false);
                        }
                    });
        }
    }

    public void sendInvitesToInvited() {
//        changeLoadingText("Inviting Contacts..");
        ArrayList<String> invitedContactsIds = getActivity().getValue().getInvitedPeopleUserIds();
        Invite inviteItem = new Invite();
        inviteItem.setInviteActivity(getActivity().getValue());
        inviteItem.setInviterId(currentUser.getUid());
        inviteItem.setInviterName(currentUser.getDisplayName());
        inviteItem.setInviteTime(Timestamp.now());
        inviteItem.setInviterPicUrl(currentUser.getPhotoUrl().toString());
        for (String invitedContactId : invitedContactsIds) {
            inviteItem.setInviteeId(invitedContactId);
            db.collection("users")
                    .document(invitedContactId)
                    .collection("invites")
                    .add(inviteItem)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Timber.e("Successfully created Invite with id: %s", documentReference.getId());

                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                db.collection("users")
                                        .document(invitedContactId)
                                        .collection("invites")
                                        .document(task.getResult().getId())
                                        .update("inviteId", task.getResult().getId())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Timber.e("Successfully updated invite id");
                                                setSEND_INVITES_SUCCESSFUL(true);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Timber.e(e, "Failed to update invite id field");
                                                setSEND_INVITES_SUCCESSFUL(false);
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Timber.e(e, "Failed to send invite");
                            setSEND_INVITES_SUCCESSFUL(false);
                        }
                    });
        }
//        changeLoadingText("Invites Completed!");
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
