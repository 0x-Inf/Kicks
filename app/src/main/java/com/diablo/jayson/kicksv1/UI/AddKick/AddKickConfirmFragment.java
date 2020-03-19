package com.diablo.jayson.kicksv1.UI.AddKick;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.diablo.jayson.kicksv1.MainActivity;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddKickConfirmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddKickConfirmFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = AddKick1Fragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView hostName, activityTimeAndDate, activityLocation, activityPeopleAndCost;

    private AddKickViewModel viewModel;

    private Activity activityMain;

    public AddKickConfirmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddKickConfirmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddKickConfirmFragment newInstance(String param1, String param2) {
        AddKickConfirmFragment fragment = new AddKickConfirmFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        hostName = view.findViewById(R.id.hostNameTextView);
        activityTimeAndDate = view.findViewById(R.id.timeAndDateTextView);
        activityLocation = view.findViewById(R.id.locationTextView);
        activityPeopleAndCost = view.findViewById(R.id.peopleAndCostTextView);
        ExtendedFloatingActionButton createActivity = view.findViewById(R.id.CreateActivityFab);
        viewModel.getActivity1().observe(requireActivity(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                activityMain = activity;
                hostName.setText(activity.getUploaderId());
                activityTimeAndDate.setText(activity.getkickTime() + " - " + activity.getKickEndTime() + " " + activity.getkickDate());
                activityLocation.setText(activity.getkickLocation());
                activityPeopleAndCost.setText(activity.getTag().getTagCost() + " Per Person (" + activity.getMinRequiredPeople() + " - " + activity.getMaxRequiredPeeps() + ") People");
            }
        });

        createActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAttendees();
                FirebaseFirestore.getInstance().collection("activities")
                        .add(activityMain)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                startActivity(new Intent(getContext(), MainActivity.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });
    }

    private void updateAttendees() {
        viewModel.getActivity1().observe(requireActivity(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                activityMain = activity;
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        viewModel = new ViewModelProvider(requireActivity()).get(AddKickViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_kick_confirm, container, false);
    }
}
