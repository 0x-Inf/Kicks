package com.diablo.jayson.kicksv1.UI.UserProfile.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.diablo.jayson.kicksv1.Models.ContactRequest;
import com.diablo.jayson.kicksv1.UI.UserProfile.RequestsListAdapter;
import com.diablo.jayson.kicksv1.databinding.FragmentRequestsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRequestsBinding.inflate(inflater, container, false);
        getRequestsFromDb();
        binding.requestsRecycler.setAdapter(requestsListAdapter);
        return binding.getRoot();
    }

    private void getRequestsFromDb() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        requestData = new ArrayList<>();

        db.collection("users")
                .document(user.getUid())
                .collection("contactRequest")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                requestData.add(documentSnapshot.toObject(ContactRequest.class));
                            }
                            setUpAdapterWithData();
                        }
                    }
                });
    }

    private void setUpAdapterWithData() {
        requestsListAdapter = new RequestsListAdapter(requestData, this::onAddSelected, this::onRejectSelected, this::onRequestSelected);
    }

    @Override
    public void onAddSelected(ContactRequest contactRequest) {

    }

    @Override
    public void onRejectSelected(ContactRequest contactRequest) {

    }

    @Override
    public void onRequestSelected(ContactRequest contactRequest) {

    }
}