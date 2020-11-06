package com.diablo.jayson.kicksv1.UI.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.Models.AttendingUser;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AttendActivity.MainAttendActivityActivity;
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
 * Use the {@link GroupMessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupMessagesFragment extends Fragment implements GroupMessagesAdapter.OnGroupSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = ActiveActivitiesFragment.class.getSimpleName();

    private RecyclerView groupMessagesRecycler;
    private ArrayList<Activity> activeActivities;
    private ArrayList<Activity> allActivities;
    private ArrayList<AttendingUser> attendingUsers;
    private GroupMessagesFragment listener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GroupMessagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupMessagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupMessagesFragment newInstance(String param1, String param2) {
        GroupMessagesFragment fragment = new GroupMessagesFragment();
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
        View root = inflater.inflate(R.layout.fragment_group_messages, container, false);
        groupMessagesRecycler = root.findViewById(R.id.groupMessagesRecycler);
        loadActiveActivitiesFromDb();

        return root;
    }

    private void loadActiveActivitiesFromDb() {
        listener = this;

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
                            }

                            Log.e(TAG, String.valueOf(activeActivities.size()));
                            GroupMessagesAdapter groupMessagesAdapter = new GroupMessagesAdapter(getContext(), activeActivities, listener);
                            groupMessagesRecycler.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
                            groupMessagesRecycler.setAdapter(groupMessagesAdapter);
                        }

                    }
                });
    }

    @Override
    public void onGroupSelected(Activity group) {
        Intent attendActivity = new Intent(getContext(), MainAttendActivityActivity.class);
        attendActivity.putExtra("activityId", group.getActivityId());
        attendActivity.putExtra("alreadyAttending", true);
        attendActivity.putExtra("fromGroupMessages", true);
        startActivity(attendActivity);
//        Toast.makeText(getContext(), group.getActivityId(), Toast.LENGTH_LONG).show();
    }
}
