package com.diablo.jayson.kicksv1.UI.AddActivity.fragments;

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

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AddActivity.AddActivityCostData;
import com.diablo.jayson.kicksv1.UI.AddActivity.AddActivityDateTimeData;
import com.diablo.jayson.kicksv1.UI.AddActivity.AddActivityLocationData;
import com.diablo.jayson.kicksv1.UI.AddActivity.AddActivityPeopleData;
import com.diablo.jayson.kicksv1.UI.AddActivity.AddActivityTagData;
import com.diablo.jayson.kicksv1.UI.AddActivity.AddActivityViewModel;
import com.diablo.jayson.kicksv1.databinding.FragmentAddActivityBinding;

public class AddActivityFragment extends Fragment {

    private static final String TAG = AddActivityFragment.class.getSimpleName();


    private AddActivityViewModel addActivityViewModel;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivityViewModel = new ViewModelProvider(requireActivity()).get(AddActivityViewModel.class);
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
            }
        });
        binding.tagCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections actionAddTag = AddActivityFragmentDirections.actionNavigationAddKickToAddActivityTagFragment();
                navController.navigate(actionAddTag);
            }
        });
        binding.timeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections actionAddDateTime = AddActivityFragmentDirections.actionNavigationAddKickToAddActivityDateTimeFragment();
                navController.navigate(actionAddDateTime);
            }
        });
        binding.locationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections actionAddLocation = AddActivityFragmentDirections.actionNavigationAddKickToAddActivityLocationFragment();
                navController.navigate(actionAddLocation);
            }
        });


        //Uploading Activity to Db
        binding.createActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addActivityViewModel.missingFields()) {
                    Toast.makeText(requireContext(), "Missing Fields", Toast.LENGTH_SHORT).show();
                } else {
                    navigateToNextFragment();
                }
            }
        });

//        binding.createActivityButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (binding.peopleCardImageView.getVisibility() == View.GONE || binding.costCardImageView.getVisibility() == View.GONE ||
//                        binding.tagCardImageView.getVisibility() == View.GONE || binding.timeDateCardImageView.getVisibility() == View.GONE ||
//                        binding.locationCardImageView.getVisibility() == View.GONE || binding.descriptionCardImageView.getVisibility() == View.GONE) {
//                    Toast.makeText(getContext(), "Missing Fields", Toast.LENGTH_SHORT).show();
//                } else {
////                    String activityTitle = activityTitleEditText.getText().toString();
////                    activityMain.setActivityTitle(activityTitle);
//                    updateAttendeesAndHostAndTime();
//                    showLoadingScreen();
//                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                    if (currentUser != null) {
//                        userId = currentUser.getUid();
//                    }
//                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//                    FirebaseFirestore.getInstance().collection("activities")
//                            .add(activityMain)
//                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                @Override
//                                public void onSuccess(DocumentReference documentReference) {
//                                    Map<String, Object> activity = new HashMap<>();
//                                    activity.put("activityReference", documentReference);
//                                    activity.put("activityId", documentReference.getId());
//                                    db.collection("users").document(userId).collection("activeactivities")
//                                            .document(documentReference.getId())
//                                            .set(activity)
//                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    Timber.d("DocumentSnapshot written with ID: %s", documentReference.getId());
//                                                    activityMain = new Activity();
//                                                    viewModel.setActivity1(activityMain);
//                                                    startActivity(new Intent(getContext(), MainActivity.class));
//                                                    hideLoadingScreen();
//                                                }
//                                            })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//
//                                                }
//                                            });
//
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Timber.e(e);
//                                }
//                            });
//                }
//            }
//        });
//        loadingScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                return;
//            }
//        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addActivityViewModel.getActivity().observe(requireActivity(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                activityMain = activity;
                if (activity.getActivityNoOfPeople() != null) {
                    binding.addPeopleDoneImageView.setVisibility(View.VISIBLE);
                }
                if (activity.getActivityCost() != null) {
                    binding.addCostDoneImageView.setVisibility(View.VISIBLE);
                }
                if (activity.getActivityTags() != null) {
                    if (!activity.getActivityTags().isEmpty()) {
                        binding.addTagDoneImageView.setVisibility(View.VISIBLE);
                    }
                }
                if (activity.getActivityStartDate() != null && !activity.getActivityDuration().getDurationName().isEmpty()) {
                    binding.addTimeDoneImageView.setVisibility(View.VISIBLE);
                }
                if (activity.getActivityLocationName() != null) {
                    binding.addLocationDoneImageView.setVisibility(View.VISIBLE);
                }
                if (activity.getActivityTitle() != null) {
                    binding.addDescriptionDoneImageView.setVisibility(View.VISIBLE);
                }
//                for (int i = 0; i < 6; i++) {
//                    switch (i) {
//                        case 0:
//                            if (activity.getActivityNoOfPeople() != null) {
//                                binding.addPeopleDoneImageView.setVisibility(View.VISIBLE);
//                            }
//                        case 1:
//                            if (activity.getActivityCost() != null) {
//                                binding.addCostDoneImageView.setVisibility(View.VISIBLE);
//                            }
//                        case 2:
//                            if (activity.getActivityTags() != null) {
//                                if (!activity.getActivityTags().isEmpty()) {
//                                    binding.addTagDoneImageView.setVisibility(View.VISIBLE);
//                                }
//                            }
//                        case 3:
//                            if (activity.getActivityStartDate() != null && !activity.getActivityDuration().getDurationName().isEmpty()) {
//                                binding.addTimeDoneImageView.setVisibility(View.VISIBLE);
//                            }
//                        case 4:
//                            if (activity.getActivityLocationName() != null) {
//                                binding.addLocationDoneImageView.setVisibility(View.VISIBLE);
//                            }
//                        case 5:
//                            if (activity.getActivityTitle() != null) {
//                                binding.addDescriptionDoneImageView.setVisibility(View.VISIBLE);
//                            }
//                    }
//                }
            }
        });

    }

    private void navigateToNextFragment() {
        NavDirections actionConfirmCreateActivity = AddActivityFragmentDirections.actionNavigationAddKickToConfirmActivityDetailsFragment();
        navController.navigate(actionConfirmCreateActivity);
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

}
