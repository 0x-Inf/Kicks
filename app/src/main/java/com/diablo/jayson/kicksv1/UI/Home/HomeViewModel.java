package com.diablo.jayson.kicksv1.UI.Home;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class HomeViewModel extends ViewModel {

    MutableLiveData<ArrayList<Activity>> activeActivitiesMutableLiveData;
    ArrayList<Activity> activeActivitiesArrayList;


    public HomeViewModel() {
        activeActivitiesMutableLiveData = new MutableLiveData<>();
        init();
    }

    public MutableLiveData<ArrayList<Activity>> getActiveActivitiesMutableLiveData() {
        return activeActivitiesMutableLiveData;
    }

    public void init() {
        getActiveActivitiesFromDb();
    }

    private void getActiveActivitiesFromDb() {
        activeActivitiesArrayList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("activities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                activeActivitiesArrayList.add(documentSnapshot.toObject(Activity.class));
                            }
                            activeActivitiesMutableLiveData.postValue(activeActivitiesArrayList);
                        }
                    }
                });
    }
}
