package com.diablo.jayson.kicksv1.UI.AttendActivity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.Contact;
import com.diablo.jayson.kicksv1.Models.Invite;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AttendActivity.AttendActivityViewModel;
import com.diablo.jayson.kicksv1.UI.AttendActivity.ContactListAdapter;
import com.diablo.jayson.kicksv1.UI.AttendActivity.PickedContactsAdapter;
import com.diablo.jayson.kicksv1.UI.Home.HomeViewModel;
import com.diablo.jayson.kicksv1.databinding.FragmentInvitePeopleBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InvitePeopleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvitePeopleFragment extends Fragment implements ContactListAdapter.OnContactSelectedListener, PickedContactsAdapter.OnPickedContactSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentInvitePeopleBinding binding;
    private HomeViewModel homeViewModel;
    private AttendActivityViewModel attendActivityViewModel;
    private NavController navController;

    private ContactListAdapter contactListAdapter;
    private PickedContactsAdapter pickedContactsAdapter;
    private ArrayList<Contact> contactsData;
    private InvitePeopleFragment contactSelectedListener;

    private ArrayList<String> pickedContactIdsData;
    private ArrayList<Contact> pickedContactsData;
    private Invite inviteItem;
    private Activity inviteActivity;
    private String inviterId;

    private FirebaseUser firebaseUser;


    public InvitePeopleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InvitePeopleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InvitePeopleFragment newInstance(String param1, String param2) {
        InvitePeopleFragment fragment = new InvitePeopleFragment();
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
        attendActivityViewModel = new ViewModelProvider(requireActivity()).get(AttendActivityViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentInvitePeopleBinding.inflate(inflater, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        getContactsDataFromDb();
        inviteActivity = new Activity();
        pickedContactIdsData = new ArrayList<>();
        pickedContactsData = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        pickedContactsAdapter = new PickedContactsAdapter(pickedContactsData, this);

        binding.selectedPeopleRecycler.setAdapter(pickedContactsAdapter);

        attendActivityViewModel.getActivityData().observe(getViewLifecycleOwner(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                inviteActivity = activity;
            }
        });
        binding.invitesDoneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpInviteItem();
                for (String pickedContact : pickedContactIdsData) {
                    inviteItem.setInviteeId(pickedContact);
                    db.collection("users")
                            .document(pickedContact)
                            .collection("invites")
                            .add(inviteItem)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        Timber.e(Objects.requireNonNull(task.getResult()).getId());
                                        db.collection("users").document(pickedContact)
                                                .collection("invites")
                                                .document(task.getResult().getId())
                                                .update("inviteId", task.getResult().getId())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        navController.popBackStack();
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
                }
            }
        });
        return binding.getRoot();
    }


    private void setUpInviteItem() {
        inviteItem = new Invite();
        inviteItem.setInviteActivity(inviteActivity);
        inviteItem.setInviteTime(Timestamp.now());
        inviteItem.setInviterName(firebaseUser.getDisplayName());
        inviteItem.setInviterId(firebaseUser.getUid());
        inviteItem.setInviterPicUrl(Objects.requireNonNull(firebaseUser.getPhotoUrl()).toString());

    }

    private void getContactsDataFromDb() {
        contactsData = new ArrayList<>();
        contactSelectedListener = this;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        assert firebaseUser != null;
        db.collection("users")
                .document(firebaseUser.getUid())
                .collection("contacts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                contactsData.add(documentSnapshot.toObject(Contact.class));
                            }
                            contactListAdapter = new ContactListAdapter(contactsData, contactSelectedListener);
                            binding.contactsRecycler.setAdapter(contactListAdapter);
                        }
                    }
                });
    }

    @Override
    public void onContactSelected(Contact contact) {
        pickedContactIdsData.add(contact.getContactId());
        pickedContactsData.add(contact);
        contactsData.remove(contact);
        contactListAdapter.notifyDataSetChanged();
        pickedContactsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPickedContactSelected(Contact contact) {
        pickedContactsData.remove(contact);
        contactsData.add(contact);
        pickedContactsAdapter.notifyDataSetChanged();
        contactListAdapter.notifyDataSetChanged();
    }
}