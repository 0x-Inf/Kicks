package com.diablo.jayson.kicksv1.UI.Home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.FinishedActivity;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.Home.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import timber.log.Timber;

public class ActiveActivitySelectedBottomSheet extends BottomSheetDialogFragment {

    private RatingBar ratingBar;
    private Button rateAndFinishButton;
    private Button viewActivityButton;

    private String activityId;
    private FinishedActivity finishedActivity;
    private Activity selectedActivity;
    private HomeViewModel homeViewModel;

    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_active_selected_bottom_sheet, container, false);
        ratingBar = root.findViewById(R.id.rateActivityRatingBar);
        rateAndFinishButton = root.findViewById(R.id.rateAndFinishButton);
        viewActivityButton = root.findViewById(R.id.viewActivityButton);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        finishedActivity = new FinishedActivity();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v > 1) {
                    rateAndFinishButton.setBackgroundColor(getResources().getColor(R.color.colorMain));
                    rateAndFinishButton.setTextColor(getResources().getColor(R.color.colorWhite));
                }
//                if (b){
//                    rateAndFinishButton.setBackgroundColor(getResources().getColor(R.color.colorMain));
//                }
            }
        });

        viewActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections actionActivityAttendFragment = ActiveActivitySelectedBottomSheetDirections
                        .actionActiveActivitySelectedBottomSheetToAttendActivityMainFragment(activityId);
                navController.navigate(actionActivityAttendFragment);
            }
        });

        rateAndFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ratingBar.getRating() != 0) {
                    finishedActivity.setRated(true);
                    finishedActivity.setRating(ratingBar.getRating());
                } else {
                    finishedActivity.setRated(false);
                    finishedActivity.setRating(0);
                }
                setUpFinishedActivity();
                uploadFinishedActivity();
            }
        });

        return root;
    }

    private void uploadFinishedActivity() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("users").document(firebaseUser.getUid())
                .collection("finishedActivities")
                .add(finishedActivity)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            removeActivityFromActive();
                        }
                    }
                });
    }

    private void removeActivityFromActive() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("users").document(firebaseUser.getUid())
                .collection("activeactivities")
                .document(selectedActivity.getActivityId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        homeViewModel.removeActivityFromActive(selectedActivity);
                        navController.popBackStack();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.e(e);
                    }
                });
    }

    private void setUpFinishedActivity() {
        finishedActivity.setActivity(selectedActivity);
        finishedActivity.setFinishedTime(Timestamp.now());
    }

    private void saveLatestFinishedActivity() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        activityId = ActiveActivitySelectedBottomSheetArgs.fromBundle(getArguments()).getActivityId();
        getActivityFromDb();
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    private void getActivityFromDb() {
        selectedActivity = new Activity();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("activities")
                .document(activityId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            assert documentSnapshot != null;
                            if (documentSnapshot.exists()) {
                                selectedActivity = documentSnapshot.toObject(Activity.class);
//                                Timber.e();
                            }
                        }
                        assert selectedActivity != null;
                        Timber.e(selectedActivity.getActivityId());
                    }
                });
    }
}
