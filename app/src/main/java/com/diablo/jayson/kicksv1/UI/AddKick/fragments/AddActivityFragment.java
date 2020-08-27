package com.diablo.jayson.kicksv1.UI.AddKick.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.diablo.jayson.kicksv1.MainActivity;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AddKick.AddActivityCostData;
import com.diablo.jayson.kicksv1.UI.AddKick.AddActivityDateTimeData;
import com.diablo.jayson.kicksv1.UI.AddKick.AddActivityLocationData;
import com.diablo.jayson.kicksv1.UI.AddKick.AddActivityPeopleData;
import com.diablo.jayson.kicksv1.UI.AddKick.AddActivityTagData;
import com.diablo.jayson.kicksv1.UI.AddKick.AddKickViewModel;
import com.diablo.jayson.kicksv1.Utils.FirebaseUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import timber.log.Timber;

public class AddActivityFragment extends Fragment {

    private static final String TAG = AddActivityFragment.class.getSimpleName();


    private AddKickViewModel viewModel;
    private Activity activityMain;
    private AddActivityPeopleData activityPeopleData;
    private AddActivityCostData activityCostData;
    private AddActivityDateTimeData activityDateTimeData;
    private AddActivityLocationData activityLocationData;
    private AddActivityTagData activityTagData;

    private String userId;

    //Main Dash Stuff
    private RelativeLayout peopleCardOverlay, costCardOverlay, tagCardOverlay,
            dateTimeCardOverlay, locationCardOverlay, loadingScreen;
    private ConstraintLayout addActivityMainDashRelativeLayout;
    private EditText activityTitleEditText;
    private CardView addActivityPeopleCard, addActivityCostCard, addActivityTagCard, addActivityTimeAndDateCard,
            addActivityLocationCard;
    private TextView activityLocationTextView;
    private ImageView peopleCardImageView, tagCardImageView, costCardImageView, locationCardImageView, timeDateCardImage;
    private ExtendedFloatingActionButton createActivityFinishEfab;


    public static AddActivityFragment newInstance() {
        return new AddActivityFragment();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getActivity1().observe(requireActivity(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                activityMain = activity;
                for (int i = 0; i < 5; i++) {
                    switch (i) {
                        case 0:
                            if (!String.valueOf(activity.getActivityMinRequiredPeople()).isEmpty()) {
                                peopleCardImageView.setVisibility(View.VISIBLE);
                            }
                        case 1:
                            if (activity.getActivityCost() != null) {
                                costCardImageView.setVisibility(View.VISIBLE);
                            }
                        case 2:
                            if (activity.getActivityTag() != null) {
                                tagCardImageView.setVisibility(View.VISIBLE);
                            }
                        case 3:
                            if (activity.getActivityDate() != null) {
                                timeDateCardImage.setVisibility(View.VISIBLE);
                            }
                        case 4:
                            if (activity.getActivityLocationName() != null) {
                                locationCardImageView.setVisibility(View.VISIBLE);
                            }
                    }
                }
            }
        });
        if (activityMain.getActivityTitle() != null){
            activityTitleEditText.setText(activityMain.getActivityTitle());
            Toast.makeText(getContext(),activityMain.getActivityTitle(),Toast.LENGTH_SHORT).show();
        }


        assert getArguments() != null;
        if (getArguments().get("activityPeopleData") != null) {
            activityPeopleData = new AddActivityPeopleData();
            activityPeopleData = AddActivityFragmentArgs.fromBundle(getArguments()).getActivityPeopleData();
//            Toast.makeText(getContext(), String.valueOf(activityPeopleData.getActivityMaxAge()), Toast.LENGTH_LONG).show();
            updateActivityPeopleModel();
        }

        if (getArguments().get("activityCostData") != null) {
            activityCostData = new AddActivityCostData();
            activityCostData = AddActivityFragmentArgs.fromBundle(getArguments()).getActivityCostData();
            updateActivityCostModel();
        }

        if (getArguments().get("activityTagData") != null) {
            activityTagData = new AddActivityTagData();
            activityTagData = AddActivityFragmentArgs.fromBundle(getArguments()).getActivityTagData();
            updateActivityTagModel();
        }

        if (getArguments().get("activityDateTimeData") != null) {
            activityDateTimeData = new AddActivityDateTimeData();
            activityDateTimeData = AddActivityFragmentArgs.fromBundle(getArguments()).getActivityDateTimeData();
            updateActivityDateTimeModel();
        }

        if (getArguments().get("activityLocationData") != null) {
            activityLocationData = new AddActivityLocationData();
            activityLocationData = AddActivityFragmentArgs.fromBundle(getArguments()).getActivityLocationData();
            updateActivityLocationModel();
        }

//        if (String.valueOf(activityPeopleData.getActivityMinRequiredPeople()).isEmpty()){
//
//        }else {
//            Toast.makeText(getContext(),String.valueOf(activityPeopleData.getActivityMaxAge()),Toast.LENGTH_LONG).show();
//            updateActivityPeople();
//            if (activityMain.getActivityMinRequiredPeople() >= 0){
//                peopleCardImageView.setVisibility(View.VISIBLE);
//            }else {
//                peopleCardImageView.setVisibility(View.GONE);
//            }
//        }

    }

    private void updateActivityLocationModel() {
        activityMain.setActivityLocationName(activityLocationData.getActivityLocationName());
        activityMain.setActivityLocationCordinates(activityLocationData.getActivityLocationCoordinates());
        activityLocationTextView.setText(activityLocationData.getActivityLocationName());
        locationCardImageView.setVisibility(View.VISIBLE);
        viewModel.setActivity1(activityMain);
    }

    private void updateActivityDateTimeModel() {
        activityMain.setActivityDate(activityDateTimeData.getActivityDate());
        activityMain.setActivityStartTime(activityDateTimeData.getActivityStartTime());
        activityMain.setActivityEndTime(activityDateTimeData.getActivityEndTime());
        timeDateCardImage.setVisibility(View.VISIBLE);
        viewModel.setActivity1(activityMain);
    }

    private void updateActivityTagModel() {
        activityMain.setActivityTag(activityTagData.getActivityTag());
        activityMain.setTags(activityTagData.getTags());
        tagCardImageView.setVisibility(View.VISIBLE);
        viewModel.setActivity1(activityMain);
    }

    private void updateActivityCostModel() {
        activityMain.setActivityCost(activityCostData.getActivityCost());
        costCardImageView.setVisibility(View.VISIBLE);
        viewModel.setActivity1(activityMain);
    }

    private void updateActivityPeopleModel() {
        activityMain.setActivityMinRequiredPeople(activityPeopleData.getActivityMinRequiredPeople());
        activityMain.setActivityMaxRequiredPeople(activityPeopleData.getActivityMaxRequiredPeople());
        activityMain.setActivityMinAge(activityPeopleData.getActivityMinAge());
        activityMain.setActivityMaxAge(activityPeopleData.getActivityMaxAge());
        activityMain.setActivityPrivate(activityPeopleData.isActivityPrivate());
        peopleCardImageView.setVisibility(View.VISIBLE);
        Timber.e(String.valueOf(activityMain.getActivityMaxRequiredPeople()));
        viewModel.setActivity1(activityMain);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_activity, container, false);
        activityMain = new Activity();
        activityMain.setActivityPrivate(false);
        userId = "";

        //Main Dash Views
        addActivityMainDashRelativeLayout = root.findViewById(R.id.add_activity_main_dash_relative_layout);
//        peopleCardOverlay = root.findViewById(R.id.peopleCardOverlay);
        costCardOverlay = root.findViewById(R.id.costCardOverlay);
        tagCardOverlay = root.findViewById(R.id.tagCardOverlay);
        dateTimeCardOverlay = root.findViewById(R.id.dateTimeCardOverlay);
        locationCardOverlay = root.findViewById(R.id.locationCardOverlay);
        activityTitleEditText = root.findViewById(R.id.activity_title_edit_text);
        addActivityPeopleCard = root.findViewById(R.id.add_activity_people_card);
        addActivityCostCard = root.findViewById(R.id.add_activity_cost_card);
        addActivityTagCard = root.findViewById(R.id.add_activity_tag_card);
        addActivityLocationCard = root.findViewById(R.id.add_activity_location_card);
        addActivityTimeAndDateCard = root.findViewById(R.id.add_activity_time_date_card);
        activityLocationTextView = root.findViewById(R.id.activity_location_text_view);
        createActivityFinishEfab = root.findViewById(R.id.create_activity_finish_efab);
        peopleCardImageView = root.findViewById(R.id.people_card_image_view);
        tagCardImageView = root.findViewById(R.id.tag_card_image_view);
        costCardImageView = root.findViewById(R.id.cost_card_image_view);
        locationCardImageView = root.findViewById(R.id.location_card_image_view);
        timeDateCardImage = root.findViewById(R.id.time_date_card_image_view);
        loadingScreen = root.findViewById(R.id.loading_screen);


        //Main Dash Implementation
        addActivityPeopleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                NavDirections actionAddPeople = AddActivityFragmentDirections.actionNavigationAddKickToAddActivityPeopleFragment();
                navController.navigate(actionAddPeople);


//                Navigation.findNavController(requireView()).navigate(addPeopleAction);
//                addActivityPeopleRelativeLayout.setVisibility(View.VISIBLE);
//                addActivityMainDashRelativeLayout.setVisibility(View.GONE);
            }
        });
        addActivityCostCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                NavDirections actionAddCost = AddActivityFragmentDirections.actionNavigationAddKickToAddActivityCostFragment();
                navController.navigate(actionAddCost);
//                addActivityCostRelativeLayout.setVisibility(View.VISIBLE);
//                addActivityMainDashRelativeLayout.setVisibility(View.GONE);
            }
        });
        addActivityTagCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                NavDirections actionAddTag = AddActivityFragmentDirections.actionNavigationAddKickToAddActivityTagFragment();
                navController.navigate(actionAddTag);
//                loadTagsFromDb();
//                addActivityTagRelativeLayout.setVisibility(View.VISIBLE);
//                addActivityMainDashRelativeLayout.setVisibility(View.GONE);
            }
        });
        addActivityTimeAndDateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                NavDirections actionAddDateTime = AddActivityFragmentDirections.actionNavigationAddKickToAddActivityDateTimeFragment();
                navController.navigate(actionAddDateTime);
//                addActivityDateTimeRelativeLayout.setVisibility(View.VISIBLE);
//                addActivityMainDashRelativeLayout.setVisibility(View.GONE);
            }
        });
        addActivityLocationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                NavDirections actionAddLocation = AddActivityFragmentDirections.actionNavigationAddKickToAddActivityLocationFragment();
                navController.navigate(actionAddLocation);
//                addActivityLocationRelativeLayout.setVisibility(View.VISIBLE);
//                addActivityMainDashRelativeLayout.setVisibility(View.GONE);
            }
        });
        activityTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String activityTitle = s.toString();
                activityMain.setActivityTitle(activityTitle);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //Uploading Activity to Db
        createActivityFinishEfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityTitleEditText.getText().toString().isEmpty()) {
                    activityTitleEditText.setError("Set a Suitable Title");
                } else if (peopleCardImageView.getVisibility() == View.GONE || costCardImageView.getVisibility() == View.GONE ||
                        tagCardImageView.getVisibility() == View.GONE || timeDateCardImage.getVisibility() == View.GONE ||
                        locationCardImageView.getVisibility() == View.GONE) {
                    Toast.makeText(getContext(), "Missing Fields", Toast.LENGTH_SHORT).show();
                } else {
//                    String activityTitle = activityTitleEditText.getText().toString();
//                    activityMain.setActivityTitle(activityTitle);
                    updateAttendeesAndHostAndTime();
                    showLoadingScreen();
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        userId = currentUser.getUid();
                    }
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseFirestore.getInstance().collection("activities")
                            .add(activityMain)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Map<String, Object> activity = new HashMap<>();
                                    activity.put("activityReference", documentReference);
                                    activity.put("activityId", documentReference.getId());
                                    db.collection("users").document(userId).collection("activeactivities")
                                            .document(documentReference.getId())
                                            .set(activity)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Timber.d("DocumentSnapshot written with ID: %s", documentReference.getId());
                                                    activityMain = new Activity();
                                                    viewModel.setActivity1(activityMain);
                                                    startActivity(new Intent(getContext(), MainActivity.class));
                                                    hideLoadingScreen();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Timber.e(e);
                                }
                            });
                }
            }
        });
//        loadingScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                return;
//            }
//        });

        return root;
    }

    private void hideLoadingScreen() {
        loadingScreen.setVisibility(View.GONE);
    }

    private void showLoadingScreen() {
        loadingScreen.setVisibility(View.VISIBLE);
        loadingScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
    }

    private void updateAttendeesAndHostAndTime() {
        ArrayList<String> tags = new ArrayList<String>();
//        tags.add(activityTag.getTagName());

        ArrayList<AttendingUser> attendingUsers = new ArrayList<AttendingUser>();
        attendingUsers.add(FirebaseUtil.getAttendingUser());

        activityMain.setTags(tags);
        activityMain.setActivityAttendees(attendingUsers);
        activityMain.setHost(FirebaseUtil.getHost());
        activityMain.setActivityUploaderId(Objects.requireNonNull(FirebaseUtil.getHost()).getUid());
        Calendar calendarUploadedTime = Calendar.getInstance();
        Timestamp uploadedTimestamp = new Timestamp(calendarUploadedTime.getTimeInMillis());
        activityMain.setActivityUploadedTime(com.google.firebase.Timestamp.now());

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AddKickViewModel.class);
    }

}
