package com.diablo.jayson.kicksv1.UI.Home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.diablo.jayson.kicksv1.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActiveActivitySelectedBottomSheet extends BottomSheetDialogFragment {

    private RatingBar ratingBar;
    private Button rateAndFinishButton;
    private Button viewActivityButton;

    private String activityId;

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
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

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
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        activityId = ActiveActivitySelectedBottomSheetArgs.fromBundle(getArguments()).getActivityId();
        getActivityFromDb();
    }

    private void getActivityFromDb() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("activities").document(activityId).get();
    }
}
