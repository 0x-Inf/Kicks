package com.diablo.jayson.kicksv1.UI.AddKick.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.diablo.jayson.kicksv1.UI.AddKick.AddActivityViewModel;
import com.diablo.jayson.kicksv1.Utils.FirebaseUtil;
import com.diablo.jayson.kicksv1.databinding.FragmentAddActivityBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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


    private AddActivityViewModel viewModel;
    private Activity activityMain;
    private AddActivityPeopleData activityPeopleData;
    private AddActivityCostData activityCostData;
    private AddActivityDateTimeData activityDateTimeData;
    private AddActivityLocationData activityLocationData;
    private AddActivityTagData activityTagData;
    private FragmentAddActivityBinding binding;

    private NavController navController;

    private String userId;

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
                for (int i = 0; i < 6; i++) {
                    switch (i) {
                        case 0:
                            if (activity.getActivityNoOfPeople() != null) {
                                binding.addPeopleDoneImageView.setVisibility(View.VISIBLE);
                            }
                        case 1:
                            if (activity.getActivityCost() != null) {
                                binding.addCostDoneImageView.setVisibility(View.VISIBLE);
                            }
                        case 2:
                            if (activity.getActivityTag() != null) {
                                binding.addTagDoneImageView.setVisibility(View.VISIBLE);
                            }
                        case 3:
                            if (activity.getActivityStartDate() != null && !activity.getActivityDuration().isEmpty()) {
                                binding.addTimeDoneImageView.setVisibility(View.VISIBLE);
                            }
                        case 4:
                            if (activity.getActivityLocationName() != null) {
                                binding.addLocationDoneImageView.setVisibility(View.VISIBLE);
                            }
                        case 5:
                            if (activity.getActivityTitle() != null) {
                                binding.addDescriptionDoneImageView.setVisibility(View.VISIBLE);
                            }
                    }
                }
            }
        });
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
        activityMain.setActivityLocationCoordinates(activityLocationData.getActivityLocationCoordinates());
        binding.locationCardImageView.setVisibility(View.VISIBLE);
        viewModel.setActivity1(activityMain);
    }

    private void updateActivityDateTimeModel() {
        binding.timeDateCardImageView.setVisibility(View.VISIBLE);
        viewModel.setActivity1(activityMain);
    }

    private void updateActivityTagModel() {
        activityMain.setActivityTag(activityTagData.getActivityTag());
        activityMain.setTags(activityTagData.getTags());
        binding.tagCardImageView.setVisibility(View.VISIBLE);
        viewModel.setActivity1(activityMain);
    }

    private void updateActivityCostModel() {
        activityMain.setActivityCost(activityCostData.getActivityCost());
        binding.costCardImageView.setVisibility(View.VISIBLE);
        viewModel.setActivity1(activityMain);
    }

    private void updateActivityPeopleModel() {
//        activityMain.setActivityMinRequiredPeople(activityPeopleData.getActivityMinRequiredPeople());
//        activityMain.setActivityMaxRequiredPeople(activityPeopleData.getActivityMaxRequiredPeople());
//        activityMain.setActivityMinAge(activityPeopleData.getActivityMinAge());
//        activityMain.setActivityMaxAge(activityPeopleData.getActivityMaxAge());
        activityMain.setActivityPrivate(activityPeopleData.isActivityPrivate());
        binding.peopleCardImageView.setVisibility(View.VISIBLE);
//        Timber.e(String.valueOf(activityMain.getActivityMaxRequiredPeople()));
        viewModel.setActivity1(activityMain);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAddActivityBinding.inflate(inflater, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        activityMain = new Activity();
        activityMain.setActivityPrivate(false);
        userId = "";

        //Main Dash Implementation
        binding.peopleCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections actionAddPeople = AddActivityFragmentDirections.actionNavigationAddKickToAddActivityPeopleFragment();
                navController.navigate(actionAddPeople);


//                Navigation.findNavController(requireView()).navigate(addPeopleAction);
//                addActivityPeopleRelativeLayout.setVisibility(View.VISIBLE);
//                addActivityMainDashRelativeLayout.setVisibility(View.GONE);
            }
        });
        binding.descriptionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections actionAddDescription = AddActivityFragmentDirections.actionNavigationAddKickToAddActivityDescriptionFragment();
                navController.navigate(actionAddDescription);
            }
        });
        binding.costCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections actionAddCost = AddActivityFragmentDirections.actionNavigationAddKickToAddActivityCostFragment();
                navController.navigate(actionAddCost);
//                addActivityCostRelativeLayout.setVisibility(View.VISIBLE);
//                addActivityMainDashRelativeLayout.setVisibility(View.GONE);
            }
        });
        binding.tagCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections actionAddTag = AddActivityFragmentDirections.actionNavigationAddKickToAddActivityTagFragment();
                navController.navigate(actionAddTag);
//                loadTagsFromDb();
//                addActivityTagRelativeLayout.setVisibility(View.VISIBLE);
//                addActivityMainDashRelativeLayout.setVisibility(View.GONE);
            }
        });
        binding.timeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections actionAddDateTime = AddActivityFragmentDirections.actionNavigationAddKickToAddActivityDateTimeFragment();
                navController.navigate(actionAddDateTime);
//                addActivityDateTimeRelativeLayout.setVisibility(View.VISIBLE);
//                addActivityMainDashRelativeLayout.setVisibility(View.GONE);
            }
        });
        binding.locationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections actionAddLocation = AddActivityFragmentDirections.actionNavigationAddKickToAddActivityLocationFragment();
                navController.navigate(actionAddLocation);
//                addActivityLocationRelativeLayout.setVisibility(View.VISIBLE);
//                addActivityMainDashRelativeLayout.setVisibility(View.GONE);
            }
        });
        //Uploading Activity to Db
        binding.createActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.peopleCardImageView.getVisibility() == View.GONE || binding.costCardImageView.getVisibility() == View.GONE ||
                        binding.tagCardImageView.getVisibility() == View.GONE || binding.timeDateCardImageView.getVisibility() == View.GONE ||
                        binding.locationCardImageView.getVisibility() == View.GONE || binding.descriptionCardImageView.getVisibility() == View.GONE) {
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

        return binding.getRoot();
    }

    private void hideLoadingScreen() {
        binding.loadingScreen.setVisibility(View.GONE);
    }

    private void showLoadingScreen() {
        binding.loadingScreen.setVisibility(View.VISIBLE);
        binding.loadingScreen.setOnClickListener(new View.OnClickListener() {
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
        viewModel = new ViewModelProvider(requireActivity()).get(AddActivityViewModel.class);
    }

}
