package com.diablo.jayson.kicksv1.UI.MapFeed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class AvailableActivitiesBottomDialogFragment extends BottomSheetDialogFragment implements ActivitiesMatchingAdapter.OnActivitySelectedListener {

    private MapFragmentViewModel viewModel;
    private ActivitiesMatchingAdapter.OnActivitySelectedListener listener;

    public static AvailableActivitiesBottomDialogFragment newInstance() {
        return new AvailableActivitiesBottomDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MapFragmentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_available_activities_bottom_sheet, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView matchingActivitiesRecycler = view.findViewById(R.id.matchingActivitiesRecycler);
        listener = this::onActivitySelected;
        viewModel.getTagName().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Query activitiesQuery = FirebaseFirestore.getInstance()
                        .collection("activities")
                        .whereArrayContainsAny("tags", Arrays.asList(s));


                FirestoreRecyclerOptions<Activity> activitiesOptions = new FirestoreRecyclerOptions.Builder<Activity>()
                        .setQuery(activitiesQuery, Activity.class)
                        .build();

                ActivitiesMatchingAdapter activitiesSearchListAdapter = new ActivitiesMatchingAdapter(activitiesOptions, listener);
                matchingActivitiesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                matchingActivitiesRecycler.setAdapter(activitiesSearchListAdapter);

                activitiesSearchListAdapter.startListening();
            }
        });
    }

    @Override
    public void onActivitySelected(Activity activity) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        if (Objects.requireNonNull(user.getDisplayName()).isEmpty()) {
            Toast.makeText(getContext(), "Please Sign Up", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<String> users = new ArrayList<String>();
            for (int i = 0; i < activity.getActivityAttendees().size(); i++) {
                users.add(activity.getActivityAttendees().get(i).getUid());
            }
            if (users.contains(user.getUid())) {
//                Intent attendActivity = new Intent(getContext(), MainAttendActivityActivity.class);
//                attendActivity.putExtra("activityId", activity.getActivityId());
//                attendActivity.putExtra("activityLatitude", activity.getActivityLocationCoordinates().getLatitude());
//                attendActivity.putExtra("activityLongitude", activity.getActivityLocationCoordinates().getLongitude());
//                attendActivity.putExtra("alreadyAttending", true);
//                attendActivity.putExtra("fromGroupMessages", false);
//                startActivity(attendActivity);
                NavDirections actionMainAttend = AvailableActivitiesBottomDialogFragmentDirections.actionAvailableActivitiesBottomDialogFragmentToAttendActivityMainFragment();
                navController.navigate(actionMainAttend);
            } else {
                NavDirections actionConfirmAttend = AvailableActivitiesBottomDialogFragmentDirections.actionAvailableActivitiesBottomDialogFragmentToConfirmAttendFragment();
                navController.navigate(actionConfirmAttend);
//                Intent attendActivity = new Intent(getContext(), AttendActivityActivity.class);
//                attendActivity.putExtra("activityId", activity.getActivityId());
//                attendActivity.putExtra("alreadyAttending", false);
//                attendActivity.putExtra("fromGroupMessages", false);
//                startActivity(attendActivity);

            }
        }
    }
}
