package com.diablo.jayson.kicksv1.UI.KickSelect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diablo.jayson.kicksv1.Constants;
import com.diablo.jayson.kicksv1.Models.ExploreCategory;
import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.Models.Tag;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import timber.log.Timber;

public class ExploreViewModel extends ViewModel {

    MutableLiveData<ArrayList<Kick>> exploreKicksMutableLiveData;
    MutableLiveData<ArrayList<ExploreCategory>> exploreCategoriesLiveData;
    MutableLiveData<HashMap<String, ArrayList<Kick>>> exploreKicksHashMapMutableLiveData;
    MutableLiveData<ArrayList<Tag>> tagsArrayMutableLiveData;
    MutableLiveData<Tag> tagMutableLiveData;
    private FirebaseFirestore db;
    private ArrayList<Kick> exploreKicksData;
    private ArrayList<Tag> tagsData;
    private ArrayList<ExploreCategory> exploreCategoriesData;
    private HashMap<String, ArrayList<Kick>> exploreKicksHashMap;

    public ExploreViewModel() {
        exploreCategoriesLiveData = new MutableLiveData<>();
        exploreKicksMutableLiveData = new MutableLiveData<>();
        exploreKicksHashMapMutableLiveData = new MutableLiveData<>();
        tagsArrayMutableLiveData = new MutableLiveData<>();
        tagMutableLiveData = new MutableLiveData<>();
        init();

    }

    private void init() {
        db = FirebaseFirestore.getInstance();
        getKicksFromDb();
        getTagsDataFromDb();
    }

    public MutableLiveData<ArrayList<ExploreCategory>> getExploreCategoriesLiveData() {
        return exploreCategoriesLiveData;
    }

    public MutableLiveData<ArrayList<Kick>> getExploreKicksMutableLiveData() {
        return exploreKicksMutableLiveData;
    }

    public MutableLiveData<HashMap<String, ArrayList<Kick>>> getExploreKicksHashMapMutableLiveData() {
        return exploreKicksHashMapMutableLiveData;
    }

    public MutableLiveData<ArrayList<Tag>> getTagsArrayMutableLiveData() {
        return tagsArrayMutableLiveData;
    }

    private void getTagsDataFromDb() {
        tagsData = new ArrayList<>();
//        db.collection(Constants.tags_collection_id)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            for (QueryDocumentSnapshot document: task.getResult()){
//                                Timber.d("Gotten tag with id: %s",document.getId());
//                                tagsData.add(document.toObject(Tag.class));
//                            }
//                            tagsArrayMutableLiveData.postValue(tagsData);
//                        } else {
//                            Timber.e(task.getException(),"Error getting tag documents");
//                        }                    }
//                });

        setUpChangeListeners();

    }

    private void setUpChangeListeners() {
        db.collection(Constants.tags_collection_id)
                .whereGreaterThan("tagId", "")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Timber.w(error, "Listen Error");
                            return;
                        }

                        assert snapshots != null;
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Timber.d("New Tag: %s", dc.getDocument().getData());
                                    tagsData.add(dc.getDocument().toObject(Tag.class));
                                    tagsArrayMutableLiveData.postValue(tagsData);
                                    break;
                                case MODIFIED:
                                    Timber.d("Modified Tag");
                                    break;
                                case REMOVED:
                                    Timber.d("Removed Tag");
                            }
                        }
                    }
                });
    }

    public MutableLiveData<Tag> getTagWithId(String tagId) {
        final Tag[] requestedTag = {new Tag()};
        db.collection(Constants.tags_collection_id)
                .document(tagId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            requestedTag[0] = task.getResult().toObject(Tag.class);
                            tagMutableLiveData.postValue(requestedTag[0]);
                        } else {
                            Timber.e(task.getException(), "Failed to fetch document");
                        }
                    }
                });
        return tagMutableLiveData;
    }

    private void getKicksFromDb() {
        exploreCategoriesData = new ArrayList<>();
        exploreKicksData = new ArrayList<>();
        exploreKicksHashMap = new HashMap<>();
        ArrayList<String> exploreCategoriesIds = new ArrayList<>();
        db.collection("explore")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult()))
                                exploreCategoriesData.add(documentSnapshot.toObject(ExploreCategory.class));

                        }
                        exploreCategoriesLiveData.postValue(exploreCategoriesData);

                        for (ExploreCategory exploreCategory : exploreCategoriesData) {
                            exploreCategoriesIds.add(exploreCategory.getExploreCategoryId());
                        }
                        for (String exploreCategoryId :
                                exploreCategoriesIds) {
                            db.collection("explore").document(exploreCategoryId)
                                    .collection("kicks")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                                    exploreKicksData.add(documentSnapshot.toObject(Kick.class));
                                                }
                                                exploreKicksMutableLiveData.postValue(exploreKicksData);
                                            }
                                        }
                                    });
                            exploreKicksHashMap.put(exploreCategoryId, exploreKicksData);
//                            exploreKicksHashMap.computeIfAbsent(exploreCategoryId, k -> new ArrayList<>())
                        }
                        exploreKicksHashMapMutableLiveData.postValue(exploreKicksHashMap);
                    }
                });
    }


}
