package com.diablo.jayson.kicksv1.UI.Home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diablo.jayson.kicksv1.Constants;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.Contact;
import com.diablo.jayson.kicksv1.Models.Invite;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import timber.log.Timber;

public class HomeViewModel extends ViewModel {

    MutableLiveData<ArrayList<Activity>> activeActivitiesMutableLiveData;
    MutableLiveData<ArrayList<Contact>> userContactsMutableLiveData;
    MutableLiveData<ArrayList<Invite>> invitesMutableLiveData;
    MutableLiveData<ArrayList<Contact>> nearbyUsersMutableLiveData;
    MutableLiveData<ArrayList<Activity>> nearbyActivityMutableLiveData;
    MutableLiveData<Double> nearbyActivityRadiusMutableLiveData;
    ArrayList<Activity> nearbyActivitiesArrayList;
    private ArrayList<Invite> invitesArrayList;
    private ArrayList<Activity> activeActivitiesArrayList;
    private ArrayList<Contact> userContacts;

    private FirebaseFirestore db;
    private FirebaseUser user;


    public HomeViewModel() {
        activeActivitiesMutableLiveData = new MutableLiveData<>();
        userContactsMutableLiveData = new MutableLiveData<>();
        invitesMutableLiveData = new MutableLiveData<>();
        nearbyUsersMutableLiveData = new MutableLiveData<>();
        nearbyActivityMutableLiveData = new MutableLiveData<>();
        nearbyActivityRadiusMutableLiveData = new MutableLiveData<>();
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        init();
    }

    public void init() {
        getActiveActivitiesFromDb();
        getContactsDataFromDb();
        getInvitesFromDb();
        listenForNearbyPublicUsers();
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

    public MutableLiveData<ArrayList<Activity>> getNearbyActivityMutableLiveData() {
        return nearbyActivityMutableLiveData;
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

    public MutableLiveData<ArrayList<Contact>> getNearbyUsers() {
        return nearbyUsersMutableLiveData;
    }

    public void listenForNearbyPublicUsers() {

        db.collection(Constants.public_location_broadcast_collection)
                .whereGreaterThanOrEqualTo("broadcastTime", Timestamp.now())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Timber.e(e);
                            return;
                        }

                        assert queryDocumentSnapshots != null;
                        for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                            switch (documentChange.getType()) {
                                case ADDED:
                                    Timber.e("Added Location Update%s", documentChange.getDocument().getId());
                                    break;
                                case MODIFIED:
                                    Timber.e("Modified Location Update%s", documentChange.getDocument().getId());
                                    break;
                                case REMOVED:
                                    Timber.e("Removed Location Update%s", documentChange.getDocument().getId());

                            }

                            PublicLocationBroadcast publicLocationBroadcastUpdate = documentChange
                                    .getDocument().toObject(PublicLocationBroadcast.class);

                        }
                    }
                });
    }

    public void setNearbyActivityRadius(double radius) {
        nearbyActivityRadiusMutableLiveData.postValue(radius);
    }

    public MutableLiveData<Double> getNearbyActivityRadiusMutableLiveData() {
        return nearbyActivityRadiusMutableLiveData;
    }

    // function to get the activities near users location
    public void getNearbyActivity(GeoLocation center, double radius) {
        nearbyActivitiesArrayList = new ArrayList<>();
        //find Activities within radius of center

        // Each item in 'bounds' represents a startAt/endAt pair. We have to issue
        // a separate query for each pair. There can be up to 9 pairs of bounds
        // depending on overlap, but in most cases there are 4.

        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radius);
        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {
            Query q = db.collection(Constants.activities_collection_id)
                    .orderBy("geoHash")
                    .startAt(b.startHash)
                    .endAt(b.endHash);
            tasks.add(q.get());
        }

        // Collect all the query results together into a single list
        Tasks.whenAllComplete(tasks)
                .addOnCompleteListener(new OnCompleteListener<List<Task<?>>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Task<?>>> t) {
                        List<DocumentSnapshot> matchingDocs = new ArrayList<>();

                        for (Task<QuerySnapshot> task : tasks) {
                            QuerySnapshot snap = task.getResult();
                            for (DocumentSnapshot doc : snap.getDocuments()) {
                                double lat = Objects.requireNonNull(doc.toObject(Activity.class)).getActivityLocationCoordinates().getLatitude();
                                double lng = Objects.requireNonNull(doc.toObject(Activity.class)).getActivityLocationCoordinates().getLongitude();

                                // We have to filter out a few false positives due to GeoHash
                                // accuracy, but most will match
                                GeoLocation docLocation = new GeoLocation(lat, lng);
                                double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
                                if (distanceInM <= radius) {
                                    matchingDocs.add(doc);
                                }
                            }
                        }
                        // Matching docs contains the result
                        for (DocumentSnapshot documentSnapshot : matchingDocs) {
                            nearbyActivitiesArrayList.add(documentSnapshot.toObject(Activity.class));
                        }
                        nearbyActivityMutableLiveData.postValue(nearbyActivitiesArrayList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.e(e, "Failed to get geoHash queries");
                    }
                });
    }

}
