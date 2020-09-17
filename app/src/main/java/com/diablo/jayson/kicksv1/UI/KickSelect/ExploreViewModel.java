package com.diablo.jayson.kicksv1.UI.KickSelect;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diablo.jayson.kicksv1.Models.ExploreCategory;
import com.diablo.jayson.kicksv1.Models.Kick;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ExploreViewModel extends ViewModel {

    MutableLiveData<ArrayList<Kick>> exploreKicksMutableLiveData;
    MutableLiveData<ArrayList<ExploreCategory>> exploreCategoriesLiveData;
    MutableLiveData<HashMap<String, ArrayList<Kick>>> exploreKicksHashMapMutableLiveData;
    private FirebaseFirestore db;
    private ArrayList<Kick> exploreKicksData;
    private ArrayList<ExploreCategory> exploreCategoriesData;
    private HashMap<String, ArrayList<Kick>> exploreKicksHashMap;

    public ExploreViewModel() {
        exploreCategoriesLiveData = new MutableLiveData<>();
        exploreKicksMutableLiveData = new MutableLiveData<>();
        exploreKicksHashMapMutableLiveData = new MutableLiveData<>();
        init();

    }

    private void init() {
        db = FirebaseFirestore.getInstance();
        getKicksFromDb();
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
