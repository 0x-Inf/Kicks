package com.diablo.jayson.kicksv1.UI.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Adapters.ActivityFeedListAdapter;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AddKick.AddKickFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class KickFeedFragment extends Fragment {
    private static final String TAG = AddKickFragment.class.getSimpleName();

    private ArrayList<Activity> mKicksData;
    private RecyclerView mRecyclerView;
    private ActivityFeedListAdapter mAdapter;
    private RelativeLayout mRelativelayout;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.kick_feed_fragment, container, false);
        mRecyclerView = root.findViewById(R.id.recyclerview);
        mRelativelayout = root.findViewById(R.id.searchAndProfileRelativeView);
        loadActivitiesFromFirebase();

        return root;

    }


    private void loadActivitiesFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("activities").document();
        Query query = FirebaseFirestore.getInstance()
                .collection("activities")
                .whereGreaterThan("imageUrl", "");


        FirestoreRecyclerOptions<Activity> options = new FirestoreRecyclerOptions.Builder<Activity>()
                .setQuery(query, Activity.class)
                .build();
        mAdapter = new ActivityFeedListAdapter(options);
        int gridColumnCount = 1;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumnCount));
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}
