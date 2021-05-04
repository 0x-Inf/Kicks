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

import com.diablo.jayson.kicksv1.Constants;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.Contact;
import com.diablo.jayson.kicksv1.Models.Invite;
import com.diablo.jayson.kicksv1.Models.Tag;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AddActivity.ActivityTagsListAdapter;
import com.diablo.jayson.kicksv1.UI.AddActivity.AddActivityViewModel;
import com.diablo.jayson.kicksv1.UI.AddActivity.InvitedPeopleListAdapter;
import com.diablo.jayson.kicksv1.databinding.FragmentConfirmActivityDetailsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfirmActivityDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfirmActivityDetailsFragment extends Fragment implements InvitedPeopleListAdapter.OnInvitedContactSelectedListener,
        ActivityTagsListAdapter.OnActivityTagSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentConfirmActivityDetailsBinding binding;
    private AddActivityViewModel addActivityViewModel;
    private NavController navController;

    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    private Invite inviteItem;
    private Activity activityToBeCreated;

    public ConfirmActivityDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfirmActivityDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmActivityDetailsFragment newInstance(String param1, String param2) {
        ConfirmActivityDetailsFragment fragment = new ConfirmActivityDetailsFragment();
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
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConfirmActivityDetailsBinding.inflate(inflater, container, false);
        addActivityViewModel = new ViewModelProvider(requireActivity()).get(AddActivityViewModel.class);
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        ArrayList<Contact> invitedContacts = addActivityViewModel.getInvitedContactsMutableLiveData().getValue();
        InvitedPeopleListAdapter invitedPeopleListAdapter = new InvitedPeopleListAdapter(invitedContacts, this);

        activityToBeCreated = addActivityViewModel.getActivity().getValue();

        ArrayList<Tag> activityTags = activityToBeCreated.getActivityTags();
        ActivityTagsListAdapter activityTagsListAdapter = new ActivityTagsListAdapter(activityTags, this);

        String activityDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(activityToBeCreated.getActivityStartDate().toDate());
        String activityStartTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(activityToBeCreated.getActivityStartTime().toDate());

        binding.titleActualTextView.setText(activityToBeCreated.getActivityTitle());
        binding.detailsActualTextView.setText(activityToBeCreated.getActivityDescription());
        binding.noOfPeopleActualTextView.setText(activityToBeCreated.getActivityNoOfPeople());
        binding.invitedPeopleRecycler.setAdapter(invitedPeopleListAdapter);
        binding.dateActualTextView.setText(activityDate);
        binding.timeActualTextView.setText(activityStartTime);
        binding.tagsRecycler.setAdapter(activityTagsListAdapter);
        binding.locationActualTextView.setText(activityToBeCreated.getActivityLocationName());
        binding.costActualTextView.setText(activityToBeCreated.getActivityCost());


        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelCreateActivity();
            }
        });

        binding.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addActivityViewModel.missingFields()) {
                    Toast.makeText(requireContext(), "Missing Fields", Toast.LENGTH_SHORT).show();
                } else {
                    createActivity();
                }

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void createActivity() {
        addActivityViewModel.updateAttendeesAndHostAndTime();
        showLoadingScreen();
        addActivityViewModel.uploadNewActivityToDb().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Timber.e("Gotten LiveData change for activity upload success");
                    changeLoadingText("Done uploading");
                    hideLoadingScreen();
                    addActivityViewModel.setActivity(new Activity());
                    NavDirections actionAddActivityMain = ConfirmActivityDetailsFragmentDirections.actionConfirmActivityDetailsFragmentToNavigationAddKick();
                    navController.navigate(actionAddActivityMain);
                } else {
                    showErrorMessage();
                }
            }
        });
        addActivityViewModel.getUPLOAD_ACTIVITY_STATE().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                switch (s) {
                    case Constants.UPLOAD_ACTIVITY_STATE_START:
                        changeLoadingText("Starting upload");
                    case Constants.UPLOAD_ACTIVITY_STATE_TAGS:
                        changeLoadingText("Uploading New Tags");
                        addActivityViewModel.getUPLOAD_NEW_TAGS_SUCCESSFUL().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                if (aBoolean) {
                                    changeLoadingText("Done Uploading New Tags...");
                                }
                            }
                        });
                    case Constants.UPLOAD_ACTIVITY_STATE_INVITES:
                        changeLoadingText("Inviting Contacts..");
                        addActivityViewModel.getSEND_INVITES_SUCCESSFUL().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean aBoolean) {
                                if (aBoolean) {
                                    changeLoadingText("Done sending Invites...");
                                }
                            }
                        });
                    case Constants.UPLOAD_ACTIVITY_STATE_FINISH:
                        changeLoadingText("Finishing uploading");
                    default:
                        showLoadingScreen();
                }
            }
        });


//        db.collection("activities").add(activityToBeCreated)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Timber.e("Successful Creation with id: %s", documentReference.getId());
//                        if (addActivityViewModel.getIfCreateNewTag()) {
//                            uploadNewTagsToDb();
//                        }
//                        if (addActivityViewModel.getInviteContactsBoolean().getValue()) {
//                            sendInvitesToInvited();
//                        }
//                        Map<String, Object> activity = new HashMap<>();
//                        activity.put("activityReference", documentReference);
//                        activity.put("activityId", documentReference.getId());
//                        db.collection("users")
//                                .document(currentUser.getUid())
//                                .collection("activeactivities")
//                                .document(documentReference.getId())
//                                .set(activity)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        Timber.d("DocumentSnapshot written with ID: %s", documentReference.getId());
//                                        hideLoadingScreen();
//                                        addActivityViewModel.setActivity(new Activity());
//                                        NavDirections actionAddActivityMain = ConfirmActivityDetailsFragmentDirections.actionConfirmActivityDetailsFragmentToNavigationAddKick();
//                                        navController.navigate(actionAddActivityMain);
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//
//                                    }
//                                });
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(requireContext(), "Creation Failed", Toast.LENGTH_SHORT).show();
//                        hideLoadingScreen();
//                    }
//                });

    }

    private void showErrorMessage() {

    }

    private void sendInvitesToInvited() {
        changeLoadingText("Inviting Contacts..");
        ArrayList<String> invitedContactsIds = addActivityViewModel.getActivity().getValue().getInvitedPeopleUserIds();
        setUpInviteItem();
        for (String invitedContactId : invitedContactsIds) {
            inviteItem.setInviteeId(invitedContactId);
            db.collection("users")
                    .document(invitedContactId)
                    .collection("invites")
                    .add(inviteItem)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Timber.e("Successfully created Invite with id: %s", documentReference.getId());
                            db.collection("users")
                                    .document(invitedContactId)
                                    .collection("invites")
                                    .document(documentReference.getId())
                                    .update("inviteId", documentReference.getId())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Timber.e("Successfully updated invite id");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Timber.e(e, "Failed to update invite id field");
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Timber.e(e, "Failed to send invite");
                        }
                    });
        }
        changeLoadingText("Invites Completed!");
    }

    private void setUpInviteItem() {
        inviteItem = new Invite();
        inviteItem.setInviteActivity(addActivityViewModel.getActivity().getValue());
        inviteItem.setInviterId(currentUser.getUid());
        inviteItem.setInviterName(currentUser.getDisplayName());
        inviteItem.setInviteTime(Timestamp.now());
        inviteItem.setInviterPicUrl(currentUser.getPhotoUrl().toString());
    }

    private void uploadNewTagsToDb() {
        ArrayList<Tag> tagsToUpload = addActivityViewModel.getNewTagsToCreate().getValue();
        changeLoadingText("Uploading New Tags");
        for (Tag tag : tagsToUpload) {
            db.collection("tags")
                    .add(tag)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Timber.e("New Tag uploaded successfully with id:%s", documentReference.getId());
                            String newTagId = documentReference.getId();
                            db.collection("tags")
                                    .document(documentReference.getId())
                                    .update("tagId", newTagId)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Timber.e("Tag id updated successfully");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Timber.e(e, "Failed to update tag id with exception");
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Timber.e(e, "Failed to Upload new tag with exception");
                        }
                    });
        }
        changeLoadingText("Successful upload");
    }

    private void changeLoadingText(String text) {
        binding.loadingScreenText.setText(text);
    }

    private void showLoadingScreen() {
        binding.loadingScreen.setVisibility(View.VISIBLE);
    }

    private void hideLoadingScreen() {
        binding.loadingScreen.setVisibility(View.GONE);
    }

    private void cancelCreateActivity() {

    }

    @Override
    public void onInvitedContactSelected(Contact invitedContact) {

    }

    @Override
    public void onActivityTagSelected(Tag activityTag) {

    }
}