package com.diablo.jayson.kicksv1.UI.Home;

import android.content.res.TypedArray;
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
import java.util.Arrays;

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
//        initializeData();
        dissappearItemsOnScroll();
        loadActivitiesFromFirebase();

        return root;

    }

    private void initializeData() {
        String[] kicksTitles = getResources()
                .getStringArray(R.array.kicks_titles);
        String[] kicksDates = getResources()
                .getStringArray(R.array.kicks_dates);
        String[] kicksTimes = getResources()
                .getStringArray(R.array.kicks_time);
        String[] kicksLocations = getResources()
                .getStringArray(R.array.kicks_location);
        String[] alreadyAttending = getResources()
                .getStringArray(R.array.already_attending_peeps);
        String[] requiredAttending = getResources()
                .getStringArray(R.array.required_peeps);
        TypedArray kicksImageResources = getResources().obtainTypedArray(R.array.kicks_images);

        mKicksData.clear();

        String[] tagsList = {"Bowling", "Skating"};

        for (int i = 0; i < kicksTitles.length; i++) {
            mKicksData.add(new Activity(kicksTitles[i], kicksTimes[i], kicksDates[i],
                    kicksLocations[i], alreadyAttending[i], requiredAttending[i],
                    "", Arrays.asList(tagsList)));
        }
        kicksImageResources.recycle();

        mAdapter.notifyDataSetChanged();
    }

    private void loadActivitiesFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("activities").document();
//        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                Activity activity = documentSnapshot.toObject(Activity.class);
//                mKicksData = new ArrayList<Activity>();
//                mKicksData.add(activity);
//                initializeRecyclerView();
//            }
//        });
//        db.collection("activities")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.e(TAG, document.getId() + " => " + document.getData());
//                                for (int i=0;i<document.getData().size();i++) {
//                                    Activity activity = document.toObject(Activity.class);
//                                    mKicksData = new ArrayList<Activity>();
//                                    mKicksData.add(activity);
//                                    initializeRecyclerView();
//                                }
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });

        Query query = FirebaseFirestore.getInstance()
                .collection("activities")
                .whereGreaterThan("imageUrl", "");
//        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
//                if (e != null){
//
//                }
//
//                assert documentSnapshots != null;
//
//                ArrayList<Activity> activities = (ArrayList<Activity>) documentSnapshots.toObjects(Activity.class);
//                int gridColumnCount = 1;
//                mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumnCount));
//                mAdapter = new ActivityFeedListAdapter(getContext(), activities);
//                mRecyclerView.setAdapter(mAdapter);
//            }
//        });

        FirestoreRecyclerOptions<Activity> options = new FirestoreRecyclerOptions.Builder<Activity>()
                .setQuery(query, Activity.class)
                .build();
        mAdapter = new ActivityFeedListAdapter(options);
        int gridColumnCount = 1;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumnCount));
        mRecyclerView.setAdapter(mAdapter);


//        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<Activity, ActivityFeedListAdapter.ActivityViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull ActivityFeedListAdapter.ActivityViewHolder viewHolder, int positions, Activity model) {
//                bindViewHolder(viewHolder,positions);
//            }
//
//            @Override
//            public ActivityFeedListAdapter.ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.activity_list_item,parent,false);
//                return new ActivityFeedListAdapter.ActivityViewHolder(view);
//            }
//        };
//        int gridColumnCount = 1;
//        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumnCount));
//        mRecyclerView.setAdapter(adapter);


//        db.collection("activities")
////                .whereEqualTo("imageUrl","ww")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()){
//                            for (QueryDocumentSnapshot document: Objects.requireNonNull(task.getResult())){
//                                Log.e(TAG,document.getId()+" => " + document.getData());
//                                Activity activity = document.toObject(Activity.class);
//                                mKicksData = new ArrayList<Activity>();
//                                mKicksData.add(activity);
//
//                            }
//                            initializeRecyclerView();
//                        }else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
    }

//    private void initializeRecyclerView() {
//        int gridColumnCount = 1;
//        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumnCount));
//        mAdapter = new ActivityFeedListAdapter(getContext(), mKicksData);
//        mRecyclerView.setAdapter(mAdapter);
//    }

    private void dissappearItemsOnScroll() {
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                mRelativelayout.setVisibility(View.INVISIBLE);
//            }
//        });
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
