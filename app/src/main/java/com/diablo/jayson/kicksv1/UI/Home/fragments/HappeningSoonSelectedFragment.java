package com.diablo.jayson.kicksv1.UI.Home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.databinding.FragmentHappeningSoonSelectedBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HappeningSoonSelectedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HappeningSoonSelectedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String activityId;
    private String mParam2;

    private FragmentHappeningSoonSelectedBinding binding;
    private Activity happeningSoonActivity;

    public HappeningSoonSelectedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param activityId Parameter 1.
     * @return A new instance of fragment HappeningSoonSelectedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HappeningSoonSelectedFragment newInstance(String activityId) {
        HappeningSoonSelectedFragment fragment = new HappeningSoonSelectedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, activityId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            activityId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHappeningSoonSelectedBinding.inflate(inflater, container, false);
        getActivityData();
        String activityDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(happeningSoonActivity.getActivityDate().toDate());
//            String   = DateFormat.getMediumDateFormat(itemView.getContext()).format(currentActivity.getActivityDate());
        String activityStartTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(happeningSoonActivity.getActivityStartTime().toDate());
        binding.activityTitleTextView.setText(happeningSoonActivity.getActivityTitle());
        binding.activityDateTextView.setText(activityDate);
        binding.activityStartTimeTextView.setText(activityStartTime);
//        binding.activityDurationTextView.setText(String.valueOf(happeningSoonActivity.));
        binding.activityCostTextView.setText(happeningSoonActivity.getActivityCost());
        binding.activityLocationTextView.setText(happeningSoonActivity.getActivityLocationName());
        binding.hostNameTextView.setText(happeningSoonActivity.getHost().getUserName());
        binding.attendeesNumberTextView.setText(String.valueOf(happeningSoonActivity.getActivityAttendees().size()));
        binding.activityTagNameTextView.setText(happeningSoonActivity.getActivityTag().getTagName());
        binding.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return binding.getRoot();
    }

    private void getActivityData() {
        happeningSoonActivity = new Activity();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("activities").document(activityId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            happeningSoonActivity = Objects.requireNonNull(task.getResult()).toObject(Activity.class);
                        }
                    }
                });

        Toast.makeText(requireContext(), happeningSoonActivity.getActivityId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}