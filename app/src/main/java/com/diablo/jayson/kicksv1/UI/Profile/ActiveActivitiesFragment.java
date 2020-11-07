package com.diablo.jayson.kicksv1.UI.Profile;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActiveActivitiesFragment extends Fragment {

    private static final String TAG = ActiveActivitiesFragment.class.getSimpleName();

    private RecyclerView activeRecycler;
    private RecyclerView hostingRecycler;

    private ArrayList<Activity> activeActivities;
    private ArrayList<Activity> allActivities;
    private ArrayList<AttendingUser> attendingUsers;


    public ActiveActivitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_active_activities, container, false);
        activeRecycler = root.findViewById(R.id.activeRecyclerView);
        loadActiveActivitiesFromDb();

        return root;
    }

    private void loadActiveActivitiesFromDb() {
        allActivities = new ArrayList<Activity>();
        activeActivities = new ArrayList<Activity>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("activities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
//                                Log.e(TAG, snapshot.getId() + " => " + snapshot.getData());
                                allActivities.add(snapshot.toObject(Activity.class));
//                                for (int i = 0; i < allActivities.size(); i++) {
//                                    Log.w(TAG, allActivities.get(i).getkickTitle());
//                                }
                                ArrayList<String> users = new ArrayList<String>();
                                attendingUsers = new ArrayList<AttendingUser>();
                                for (int i = 0; i < allActivities.size(); i++) {
                                    attendingUsers = allActivities.get(i).getActivityAttendees();
                                }
                            }
                            Log.e(TAG, String.valueOf(allActivities.size()));
                            FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                            for (int i = 0; i < allActivities.size(); i++) {
                                attendingUsers = new ArrayList<AttendingUser>();
                                attendingUsers = allActivities.get(i).getActivityAttendees();
                                for (int j = 0; j < attendingUsers.size(); j++) {
                                    if (attendingUsers.get(j).getUid().equals(user1.getUid())) {
                                        activeActivities.add(allActivities.get(i));
                                    }

                                }
//                                if (allActivities.get(i).getMattendees().contains(FirebaseUtil.getAttendingUser())){
//                                    activeActivities.add(new Activity(allActivities.get(i).getHost(),
//                                            allActivities.get(i).getkickTitle(),allActivities.get(i).getkickTime(),
//                                            allActivities.get(i).getKickEndTime(),allActivities.get(i).getkickDate(),
//                                            allActivities.get(i).getkickLocation(),allActivities.get(i).getMinRequiredPeople(),
//                                            allActivities.get(i).getMaxRequiredPeeps(),allActivities.get(i).getimageUrl(),
//                                            allActivities.get(i).getTags(),allActivities.get(i).getUploadedTime(),
//                                            allActivities.get(i).getUploaderId(),allActivities.get(i).getActivityId(),
//                                            allActivities.get(i).getTag(),allActivities.get(i).getMattendees(),
//                                            allActivities.get(i).getActivityCost()));
//                                }
                            }

//                            for (Activity activity : allActivities) {
//                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                                assert user != null;
//                                if (activity.getMattendees().contains(FirebaseUtil.getAttendingUser())) {
//                                    activeActivities.add(new Activity(activity.getHost(), activity.getkickTitle(),
//                                            activity.getkickTime(), activity.getKickEndTime(), activity.getkickDate(),
//                                            activity.getkickLocation(), activity.getMinRequiredPeople(),
//                                            activity.getMaxRequiredPeeps(), activity.getimageUrl(), activity.getTags(),
//                                            activity.getUploadedTime(), activity.getUploaderId(), activity.getActivityId(),
//                                            activity.getTag(), activity.getMattendees(), activity.getActivityCost()));
//
//                                    for (int i = 0; i < activeActivities.size(); i++) {
//                                        Log.w("Active", activeActivities.get(i).getkickTitle());
//                                    }
//                                }
//
//                            }
//                            Log.e(TAG, String.valueOf(activeActivities.size()));
//                            ActiveActivitiesAdapter activeActivitiesAdapter = new ActiveActivitiesAdapter(getContext(), activeActivities);
//                            activeRecycler.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
//                            activeRecycler.setAdapter(activeActivitiesAdapter);
                        }

                    }
                });
    }

}
