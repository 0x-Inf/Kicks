package com.diablo.jayson.kicksv1.UI.SignUp;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.diablo.jayson.kicksv1.Constants;
import com.diablo.jayson.kicksv1.Models.ProfilePicExample;
import com.diablo.jayson.kicksv1.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SignUpViewModel extends ViewModel {
    private final MutableLiveData<User> mutableLiveData = new MutableLiveData<User>();
    MutableLiveData<ArrayList<ProfilePicExample>> randomPicsMutableLiveData;

    private ArrayList<ProfilePicExample> randomProfilePics;

    private FirebaseFirestore db;

    public SignUpViewModel() {
        randomPicsMutableLiveData = new MutableLiveData<>();
    }

    public void setUser(User user) {
        mutableLiveData.setValue(user);
    }

    public LiveData<User> getUser() {
        return mutableLiveData;
    }

    public MutableLiveData<ArrayList<ProfilePicExample>> getRandomPicsMutableLiveData() {
        return randomPicsMutableLiveData;
    }

    public void getRandomPicsFromDb() {
        randomProfilePics = new ArrayList<>();
        db.collection(Constants.random_pics_collection_id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                randomProfilePics.add(documentSnapshot.toObject(ProfilePicExample.class));
                            }
                            randomPicsMutableLiveData.postValue(randomProfilePics);
                        }
                    }
                });
    }

    public void getUserNamesFromDb() {

    }
}
