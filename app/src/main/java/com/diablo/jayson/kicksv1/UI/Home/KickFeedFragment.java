package com.diablo.jayson.kicksv1.UI.Home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.diablo.jayson.kicksv1.Adapters.ActivityFeedAdapter;
import com.diablo.jayson.kicksv1.Adapters.ActivityFeedListAdapter;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AddKick.AddActivityFragment;
import com.diablo.jayson.kicksv1.UI.AttendActivity.AttendActivityActivity;
import com.diablo.jayson.kicksv1.UI.AttendActivity.MainAttendActivityActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.Objects;

public class KickFeedFragment extends Fragment implements ActivityFeedAdapter.OnActivitySelectedListener {
    private static final String TAG = AddActivityFragment.class.getSimpleName();

    private ArrayList<Activity> mKicksData;
    private RecyclerView mRecyclerView;
    private ActivityFeedListAdapter mAdapter;
    private ActivityFeedAdapter activityFeedAdapter;
    private ImageView likeButton;
    private RelativeLayout mRelativelayout, loadingScreen;
    private ViewPager2 feedViewPager2;
    private KickFeedFragment listener;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_kick_feed, container, false);
//        mRecyclerView = root.findViewById(R.id.recyclerview);
        feedViewPager2 = root.findViewById(R.id.feedViewPager2);
        loadingScreen = root.findViewById(R.id.loading_screen);
//        mRelativelayout = root.findViewById(R.id.searchAndProfileRelativeView);
        showLoadingScreen();
//        loadActivitiesFromFirebase();
        getActivitiesData();
        return root;

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


    private void loadActivitiesFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("activities").document();
        Query query = FirebaseFirestore.getInstance()
                .collection("activities")
                .orderBy("activityUploadedTime", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<Activity> options = new FirestoreRecyclerOptions.Builder<Activity>()
                .setQuery(query, Activity.class)
                .build();
//        mAdapter = new ActivityFeedListAdapter(options, this);
//        int gridColumnCount = 1;
//        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumnCount));
//        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();
        feedViewPager2.setAdapter(mAdapter);
        feedViewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
        hideLoadingScreen();


    }

    private void getActivitiesData(){
        listener = this;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mKicksData = new ArrayList<Activity>();
        db.collection("activities")
                .orderBy("activityUploadedTime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                mKicksData.add(new Activity(snapshot.toObject(Activity.class).getHost(),
                                        snapshot.toObject(Activity.class).getActivityTitle(),
                                        snapshot.toObject(Activity.class).getActivityStartTime(),
                                        snapshot.toObject(Activity.class).getActivityEndTime(),
                                        snapshot.toObject(Activity.class).getActivityDate(),
                                        snapshot.toObject(Activity.class).getActivityLocationName(),
                                        snapshot.toObject(Activity.class).getActivityLocationCoordinates(),
                                        snapshot.toObject(Activity.class).getActivityMinRequiredPeople(),
                                        snapshot.toObject(Activity.class).getActivityMaxRequiredPeople(),
                                        snapshot.toObject(Activity.class).getActivityMinAge(),
                                        snapshot.toObject(Activity.class).getActivityMaxAge(),
                                        snapshot.toObject(Activity.class).getImageUrl(),
                                        snapshot.toObject(Activity.class).getActivityUploaderId(),
                                        snapshot.toObject(Activity.class).getActivityId(),
                                        snapshot.toObject(Activity.class).getActivityCost(),
                                        snapshot.toObject(Activity.class).getActivityUploadedTime(),
                                        snapshot.toObject(Activity.class).getTags(),
                                        snapshot.toObject(Activity.class).getActivityTag(),
                                        snapshot.toObject(Activity.class).getActivityAttendees(),
                                        snapshot.toObject(Activity.class).isActivityPrivate()));
                            }

                            feedViewPager2.setAdapter(new ActivityFeedAdapter(mKicksData,feedViewPager2,getContext(),listener));
                            feedViewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);
                            hideLoadingScreen();
                        }
                    }
                });
    }

    private void hideLoadingScreen() {
        loadingScreen.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
//        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
//        mAdapter.stopListening();
    }

    @Override
    public void onActivitySelected(Activity activity) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        assert user != null;
        if (user.isAnonymous()) {
            Toast.makeText(getContext(), "Please Sign Up", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<String> users = new ArrayList<String>();
            for (int i = 0; i < activity.getActivityAttendees().size(); i++) {
                users.add(activity.getActivityAttendees().get(i).getUid());
            }
            if (users.contains(user.getUid())) {
                Intent attendActivity = new Intent(getContext(), MainAttendActivityActivity.class);
                attendActivity.putExtra("activityId", activity.getActivityId());
                attendActivity.putExtra("activityLatitude", activity.getActivityLocationCoordinates().getLatitude());
                attendActivity.putExtra("activityLongitude", activity.getActivityLocationCoordinates().getLongitude());
                attendActivity.putExtra("alreadyAttending", true);
                attendActivity.putExtra("fromGroupMessages", false);
                startActivity(attendActivity);
            } else {
//                Intent attendActivity = new Intent(getContext(), AttendActivityActivity.class);
                NavDirections actionConfirmAttend = KickFeedFragmentDirections.actionNavigationHomeToConfirmAttendFragment(activity.getActivityId());
                navController.navigate(actionConfirmAttend);
//                attendActivity.putExtra("activityId", activity.getActivityId());
//                attendActivity.putExtra("alreadyAttending", false);
//                attendActivity.putExtra("fromGroupMessages", false);
//                startActivity(attendActivity);
            }
        }


    }

//    @Override
//    public void toggleLike(Activity activity) {
//        String activityId = activity.getActivityId();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference documentReference = db.collection("activities").document(activityId);
//        db.runTransaction(new Transaction.Function<Void>() {
//
//            @Nullable
//            @Override
//            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//                DocumentSnapshot snapshot = transaction.get(documentReference);
//                double newLikes = snapshot.getDouble("likes") + 1;
//                transaction.update(documentReference, "likes", (int) newLikes);
//                return null;
//            }
//        }).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.d(TAG, "Transaction success!");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.w(TAG, "Transaction failure.", e);
//            }
//        });
//        Toast.makeText(getContext(), activityId, Toast.LENGTH_SHORT).show();
//    }
}
