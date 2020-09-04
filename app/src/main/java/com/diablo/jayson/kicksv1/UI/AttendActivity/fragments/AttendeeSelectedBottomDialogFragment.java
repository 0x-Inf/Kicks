package com.diablo.jayson.kicksv1.UI.AttendActivity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.Contact;
import com.diablo.jayson.kicksv1.Models.ContactRequest;
import com.diablo.jayson.kicksv1.Models.User;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AttendActivity.AttendActivityViewModel;
import com.diablo.jayson.kicksv1.UI.Home.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import timber.log.Timber;

public class AttendeeSelectedBottomDialogFragment extends BottomSheetDialogFragment {

    private FloatingActionButton reportFab, historyFab, removeUserFab;
    private Button addButton;
    private Button followingButton;
    private ImageView attendeePicImageView;
    private TextView attendeeNameTextView, removeTextTextView;

    private String attendeeId;
    private User attendingUser;
    private ContactRequest contactRequest;
    private HomeViewModel homeViewModel;
    private AttendActivityViewModel attendActivityViewModel;
    private ArrayList<Contact> myContactsList;
    private ArrayList<String> contactsIds;
    private Activity activityMain;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_attendee_selected_bottom_sheet, container, false);
        reportFab = root.findViewById(R.id.reportFAB);
        historyFab = root.findViewById(R.id.activityFAB);
        removeUserFab = root.findViewById(R.id.removeUserFAB);
        addButton = root.findViewById(R.id.addContactButton);
        removeTextTextView = root.findViewById(R.id.removeTextTextView);
        attendeePicImageView = root.findViewById(R.id.attendeePicImageView);
        attendeeNameTextView = root.findViewById(R.id.attendeeNameTextView);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        contactRequest = new ContactRequest();
        getMyContactList();

        reportFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections actionReportUserFragment = AttendeeSelectedBottomDialogFragmentDirections
                        .actionAttendeeSelectedBottomDialogFragmentToReportUserFragment(attendeeId);
                navController.navigate(actionReportUserFragment);
            }
        });

        historyFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections actionHistoryFragment = AttendeeSelectedBottomDialogFragmentDirections
                        .actionAttendeeSelectedBottomDialogFragmentToHistoryFragment(attendeeId);
                navController.navigate(actionHistoryFragment);
            }
        });
        removeUserFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NavDirections actionMyPeopleFragment = AttendeeSelectedBottomDialogFragmentDirections
//                        .actionAttendeeSelectedBottomDialogFragmentToMyPeopleFragment(attendeeId);
//                navController.navigate(actionMyPeopleFragment);
                removeAttendee();
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpContactRequest();
                sendContactRequest();
            }
        });
        return root;
    }

    private void removeAttendee() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(attendeeId).collection("activeactivities")
                .document(activityMain.getActivityId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void getMyContactList() {
        myContactsList = new ArrayList<>();
        contactsIds = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        db.collection("users")
                .document(firebaseUser.getUid())
                .collection("contacts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                myContactsList.add(documentSnapshot.toObject(Contact.class));
                                contactsIds.add(Objects.requireNonNull(documentSnapshot.toObject(Contact.class)).getContactId());
                            }
                            if (contactsIds.contains(attendeeId)) {
                                addButton.setVisibility(View.GONE);
                            }
                        }
                    }
                });


    }

    private void sendContactRequest() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(attendeeId)
                .collection("contactRequests")
                .add(contactRequest)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        contactRequest.setRequestId(documentReference.getId());
                        db.collection("users").document(attendeeId).collection("contactRequests")
                                .document(documentReference.getId())
                                .update("requestId", documentReference.getId())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            navController.popBackStack();
                                        }
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Please try again", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setUpContactRequest() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        contactRequest.setRequestTime(Timestamp.now());
        assert user != null;
        contactRequest.setSenderId(user.getUid());
        contactRequest.setSenderName(user.getDisplayName());
        contactRequest.setSenderPicUrl(Objects.requireNonNull(user.getPhotoUrl()).toString());
        contactRequest.setTargetId(attendeeId);
        contactRequest.setTargetName(attendingUser.getUserName());
        contactRequest.setTargetPicUrl(attendingUser.getPhotoUrl());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        assert getArguments() != null;
        attendeeId = AttendeeSelectedBottomDialogFragmentArgs.fromBundle(getArguments()).getAttendeeId();
        getAttendeeData();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        attendActivityViewModel = new ViewModelProvider(requireActivity()).get(AttendActivityViewModel.class);
        attendActivityViewModel.getActivityData().observe(getViewLifecycleOwner(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                activityMain = new Activity();
                activityMain = activity;
                assert firebaseUser != null;
                if (activity.getHost().getUid().equals(firebaseUser.getUid())) {
                    removeUserFab.setVisibility(View.VISIBLE);
                    removeTextTextView.setVisibility(View.VISIBLE);
                    Timber.e(activity.getActivityId());
                }
            }
        });

    }

    private void getAttendeeData() {
        attendingUser = new User();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(attendeeId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            assert document != null;
                            attendingUser = document.toObject(User.class);
                        }
                        assert attendingUser != null;
                        Timber.e(attendingUser.getUid());
                        attendeeNameTextView.setText(attendingUser.getUserName());
                        Glide.with(requireContext())
                                .load(attendingUser.getPhotoUrl())
                                .apply(RequestOptions.circleCropTransform())
                                .into(attendeePicImageView);
                    }
                });
    }
}
