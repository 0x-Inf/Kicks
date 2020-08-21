package com.diablo.jayson.kicksv1.UI.Profile.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.diablo.jayson.kicksv1.Models.Contact;
import com.diablo.jayson.kicksv1.Models.ContactRequest;
import com.diablo.jayson.kicksv1.UI.Profile.RequestsListAdapter;
import com.diablo.jayson.kicksv1.databinding.FragmentRequestsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestsFragment extends Fragment implements RequestsListAdapter.OnRequestSelectedListener,
        RequestsListAdapter.OnAddSelectedListener, RequestsListAdapter.OnRejectSelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentRequestsBinding binding;

    private RequestsListAdapter requestsListAdapter;
    private ArrayList<ContactRequest> requestData;

    private RequestsFragment onAddSelected;
    private RequestsFragment onRejectSelected;
    private RequestsFragment onRequestSelected;

    public RequestsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestsFragment newInstance(String param1, String param2) {
        RequestsFragment fragment = new RequestsFragment();
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
        binding = FragmentRequestsBinding.inflate(inflater, container, false);
        getRequestsFromDb();
        return binding.getRoot();
    }

    private void getRequestsFromDb() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        requestData = new ArrayList<>();
        onAddSelected = this;
        onRejectSelected = this;
        onRequestSelected = this;
        assert user != null;
        Log.e("YEAH", user.getUid());
        db.collection("users")
                .document(user.getUid())
                .collection("contactRequests")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                requestData.add(documentSnapshot.toObject(ContactRequest.class));
                                Log.e("requests", documentSnapshot.getId());
                            }
                            requestsListAdapter = new RequestsListAdapter(requestData, onAddSelected, onRejectSelected, onRequestSelected);
                            binding.requestsRecycler.setAdapter(requestsListAdapter);
                        }
                    }
                });
    }

    private void setUpAdapterWithData() {

    }

    @Override
    public void onAddSelected(ContactRequest contactRequest) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Contact newContact = new Contact();
        newContact.setContactId(contactRequest.getSenderId());
        newContact.setContactName(contactRequest.getSenderName());
        newContact.setContactPicUrl(contactRequest.getSenderPicUrl());
        Contact newCounterpartContact = new Contact();
        newCounterpartContact.setContactId(contactRequest.getTargetId());
        newCounterpartContact.setContactName(contactRequest.getTargetName());
        newCounterpartContact.setContactPicUrl(contactRequest.getTargetPicUrl());
        db.collection("users")
                .document(contactRequest.getTargetId())
                .collection("contacts")
                .add(newContact)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            db.collection("users").document(contactRequest.getSenderId())
                                    .collection("contacts")
                                    .add(newCounterpartContact)
                                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()) {
                                                db.collection("users")
                                                        .document(contactRequest.getTargetId())
                                                        .collection("contactRequests")
                                                        .document(contactRequest.getRequestId())
                                                        .delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                requestData.remove(contactRequest);
                                                                requestsListAdapter.notifyDataSetChanged();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {

                                                            }
                                                        });

                                            }
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Try Again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRejectSelected(ContactRequest contactRequest) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(contactRequest.getTargetId())
                .collection("contactRequests")
                .document(contactRequest.getRequestId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        requestData.remove(contactRequest);
                        requestsListAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    @Override
    public void onRequestSelected(ContactRequest contactRequest) {

    }
}