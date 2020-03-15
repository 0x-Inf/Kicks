package com.diablo.jayson.kicksv1.UI.AddKick;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.diablo.jayson.kicksv1.MainActivity;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.Host;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.Utils.FirebaseUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddKick2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddKick2Fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = AddKick2Fragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AddKickViewModel viewModel;
    private TextInputEditText mTimePickerInput, mDatePickerInput, mLocationTextInput, mActivityTitleInput;
    private MultiAutoCompleteTextView mTagsAutoCompleteInput;
    private TextInputEditText mMaxRequiredInput, mMinRequiredInput;
    private Activity activityMain;

    private List<String> tags = new ArrayList<String>() {{
        add("Karting");
        add("karting");
    }};
    public AddKick2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddKick2Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddKick2Fragment newInstance(String param1, String param2) {
        AddKick2Fragment fragment = new AddKick2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        final String message = "after";
        viewModel = new ViewModelProvider(requireActivity()).get(AddKickViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_kick2, container, false);
        RelativeLayout content = root.findViewById(R.id.addKickFirstContent);
        mTimePickerInput = root.findViewById(R.id.time_picker_input);
        mDatePickerInput = root.findViewById(R.id.date_picker_input);
        mLocationTextInput = root.findViewById(R.id.ActivityPLaceEditText);
//        mAddActivityToDb = root.findViewById(R.id.createActivityFab);
        mActivityTitleInput = root.findViewById(R.id.kickNameEditText);
        mTagsAutoCompleteInput = root.findViewById(R.id.tagsAutoCompleteTextView);
        mMinRequiredInput = root.findViewById(R.id.minPeopleInputEditText);
        mMaxRequiredInput = root.findViewById(R.id.maxPeopleEditText);
        content.setVisibility(View.INVISIBLE);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activityUploaded = new Activity();
        ExtendedFloatingActionButton uploadButton = view.findViewById(R.id.createActivityFab);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        viewModel.getActivity1().observe(requireActivity(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                activityMain = activity;
//                mActivityTitleInput.setText(activity.getkickTitle());
//                mLocationTextInput.setText(activity.getkickLocation());
//                mTimePickerInput.setText(activity.getkickTime());
//                mDatePickerInput.setText(activity.getkickDate());
//                mMinRequiredInput.setText(activity.getMinRequiredPeople());
//                mMaxRequiredInput.setText(activity.getMaxRequiredPeeps());
//                mTagsAutoCompleteInput.setText((CharSequence) activity.getTags());

            }
        });

        String kickTitle = mActivityTitleInput.getText().toString();
        String kickLocation = mLocationTextInput.getText().toString();
        String kickTime = mTimePickerInput.getText().toString();
        String kickDate = mDatePickerInput.getText().toString();
        String kickMinRequiredPeople = mMinRequiredInput.getText().toString();
        String kickMxnRequiredPeople = mMaxRequiredInput.getText().toString();
        String tags = mTagsAutoCompleteInput.getText().toString();
        Host host = FirebaseUtil.getHost();

        String[] tagsList = tags.split(",", -2);
//            String[] tagList = {};
        String tag = Arrays.toString(tagsList);

        tag = tag.replace("[", "");
        tag = tag.replace("]", "");

        String tagArray[] = tag.split(",");

        List<String> tagList = new ArrayList<>(Arrays.asList(tagArray));

//        activityMain = new Activity(host, kickTitle, kickTime, kickDate, kickLocation, kickMinRequiredPeople,
//                kickMxnRequiredPeople, "", tagList, Calendar.getInstance().getTimeInMillis(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());






//        db.collection("users")
//                .whereEqualTo("uid",activityMain.getUploaderId())
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            for (QueryDocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
//                                User user = documentSnapshot.toObject(User.class);
//                                viewModel.getActivity().observe(requireActivity(), new Observer<Activity>() {
//                                    @Override
//                                    public void onChanged(Activity activity) {
//                                        activityMain = activity;
//                                        activityMain.setUploaderId(user.getFirstName());
//                                    }
//                                });
//                            }
//                        }
//                    }
//                });


        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Wololo");

                db.collection("activities").add(activityMain)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.e(TAG, "DocumentSnapshot successfully written!");
                                Log.e(TAG, activityMain.getkickTitle());
                                startActivity(new Intent(getContext(), MainActivity.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }
        });


//                db.collection("users")
//                        .whereEqualTo("userEmail",activityMain.getUploaderId())
//                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            for (QueryDocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
//                                User user = documentSnapshot.toObject(User.class);
//                                Log.e(TAG,user.getFirstName().toString());
//                                viewModel.getActivity().observe(requireActivity(), new Observer<Activity>() {
//                                    @Override
//                                    public void onChanged(Activity activity) {
//                                        activityMain.setUploaderId(user.getFirstName());
//                                        activityMain = activity;
//                                    }
//                                });
//
//                            }
//                        }
//                    }
//                });


    }

    public void updateActivity() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

    }
}
