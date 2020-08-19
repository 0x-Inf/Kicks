package com.diablo.jayson.kicksv1.UI.AttendActivity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.diablo.jayson.kicksv1.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class AttendeeSelectedBottomDialogFragment extends BottomSheetDialogFragment {

    private FloatingActionButton reportFab, historyFab, myPeopleFab;
    private Button followButton;
    private Button followingButton;

    private String attendeeId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_attendee_selected_bottom_sheet,container,false);
        reportFab = root.findViewById(R.id.reportFAB);
        historyFab = root.findViewById(R.id.activityFAB);
        myPeopleFab = root.findViewById(R.id.followingFAB);
        followButton = root.findViewById(R.id.followButton);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        reportFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections actionReportUserFragment = AttendeeSelectedBottomDialogFragmentDirections.actionAttendeeSelectedBottomDialogFragmentToReportUserFragment(attendeeId);
                navController.navigate(actionReportUserFragment);
            }
        });

        historyFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections actionHistoryFragment = AttendeeSelectedBottomDialogFragmentDirections.actionAttendeeSelectedBottomDialogFragmentToHistoryFragment(attendeeId);
                navController.navigate(actionHistoryFragment);
            }
        });
        myPeopleFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections actionMyPeopleFragment = AttendeeSelectedBottomDialogFragmentDirections.actionAttendeeSelectedBottomDialogFragmentToMyPeopleFragment(attendeeId);
                navController.navigate(actionMyPeopleFragment);
            }
        });

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        attendeeId = AttendeeSelectedBottomDialogFragmentArgs.fromBundle(getArguments()).getAttendeeId();
        getAttendeeData();
    }

    private void getAttendeeData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(attendeeId);
    }
}
