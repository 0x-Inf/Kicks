package com.diablo.jayson.kicksv1.UI.Home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diablo.jayson.kicksv1.Constants;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.Contact;
import com.diablo.jayson.kicksv1.Models.Invite;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class HomeViewModel extends ViewModel {

    MutableLiveData<ArrayList<Activity>> activeActivitiesMutableLiveData;
    MutableLiveData<ArrayList<Contact>> userContactsMutableLiveData;
    MutableLiveData<ArrayList<Invite>> invitesMutableLiveData;
    ArrayList<Invite> invitesArrayList;
    ArrayList<Activity> activeActivitiesArrayList;
    ArrayList<Contact> userContacts;

    private FirebaseFirestore db;
    private FirebaseUser user;


    public HomeViewModel() {
        activeActivitiesMutableLiveData = new MutableLiveData<>();
        userContactsMutableLiveData = new MutableLiveData<>();
        invitesMutableLiveData = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        init();
    }

    public void init() {
        getActiveActivitiesFromDb();
        getContactsDataFromDb();
        getInvitesFromDb();
    }

    public MutableLiveData<ArrayList<Activity>> getActiveActivitiesMutableLiveData() {
        return activeActivitiesMutableLiveData;
    }

    public MutableLiveData<ArrayList<Contact>> getUserContactsMutableLiveData() {
        return userContactsMutableLiveData;
    }

    public MutableLiveData<ArrayList<Invite>> getInvitesMutableLiveData() {
        return invitesMutableLiveData;
    }


    private void getActiveActivitiesFromDb() {
        activeActivitiesArrayList = new ArrayList<>();
        assert user != null;
        String userId = user.getUid();
        ArrayList<String> activityIds = new ArrayList<>();
//        db.collection("activities")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
//                                activeActivitiesArrayList.add(documentSnapshot.toObject(Activity.class));
//
//                            }
//                            activeActivitiesMutableLiveData.postValue(activeActivitiesArrayList);
//                        }
//                    }
//                });

        db.collection("users").document(userId).collection("activeactivities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                activityIds.add(Objects.requireNonNull(documentSnapshot.getString("activityId")));
                                Log.e("ids", Objects.requireNonNull(documentSnapshot.getString("activityId")));
                            }
                            for (String activityId : activityIds) {
                                db.collection("activities").document(activityId)
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            activeActivitiesArrayList.add(Objects.requireNonNull(task.getResult()).toObject(Activity.class));
                                            Log.e("title", task.getResult().toObject(Activity.class).getActivityTitle());
                                        }
                                        activeActivitiesMutableLiveData.postValue(activeActivitiesArrayList);

                                    }
                                });
                            }

                        }
                    }
                });
    }

    private void getInvitesFromDb() {
        invitesArrayList = new ArrayList<>();
        db.collection(Constants.users_collection_id)
                .document(user.getUid())
                .collection(Constants.invites_collection_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                invitesArrayList.add(documentSnapshot.toObject(Invite.class));
                            }
                            invitesMutableLiveData.postValue(invitesArrayList);
                        }
                    }
                });
    }

    private void getHappeningSoonFromDb() {

    }

    private void getFeedActivitiesFromDb() {

    }

    private void getContactsDataFromDb() {
        userContacts = new ArrayList<>();
        assert user != null;
        String userId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(userId)
                .collection("contacts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                userContacts.add(documentSnapshot.toObject(Contact.class));
                            }
                            userContactsMutableLiveData.postValue(userContacts);
                        }
                    }
                });
    }

    public void removeActivityFromActive(Activity activity) {
        activeActivitiesArrayList.remove(activity);
        activeActivitiesMutableLiveData.postValue(activeActivitiesArrayList);
    }
}
