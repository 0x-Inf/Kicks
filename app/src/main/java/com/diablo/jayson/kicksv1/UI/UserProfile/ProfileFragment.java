package com.diablo.jayson.kicksv1.UI.UserProfile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = ProfileFragment.class.getSimpleName();

    //Main Dash stuff
    private RelativeLayout activitiesCardOverlay, attributesCardOverlay;
    private ImageView activitiesCardImageView;

    //Active Activities Stuff
    private RelativeLayout activiteActivitiesRelativeLayout;
    private CardView activeActivitiesActualCard;
    private RecyclerView activeActivitiesRecycler;
    private ArrayList<Activity> activeActivities;
    private ArrayList<Activity> allActivities;
    private ArrayList<AttendingUser> attendingUsers;

    //Attributes Stuff
    private RelativeLayout attributesRelativeLayout;
    private CardView attributesActualCard;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private float y1, y2;
    static final int MIN_DISTANCE = 150;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        activitiesCardOverlay = root.findViewById(R.id.activitiesCardOverlay);
        attributesCardOverlay = root.findViewById(R.id.attributesCardOverlay);
        activitiesCardImageView = root.findViewById(R.id.activitiesCardImageView);

        //Active activities views
        activiteActivitiesRelativeLayout = root.findViewById(R.id.active_activities_relative_layout);
        activeActivitiesActualCard = root.findViewById(R.id.activeActivitiesActualCard);
        activeActivitiesRecycler = root.findViewById(R.id.activeActivitiesRecycler);

        //Attributes views
        attributesRelativeLayout = root.findViewById(R.id.attributes_relative_layout);
        attributesActualCard = root.findViewById(R.id.attributesActualCard);

        //Active activities implementation
        activitiesCardOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadActiveActivitiesFromDb();
                activiteActivitiesRelativeLayout.setVisibility(View.VISIBLE);
            }
        });
        activeActivitiesActualCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        y1 = event.getY();
                        break;
//                        activiteActivitiesRelativeLayout.setVisibility(View.GONE);
//                        return true;
                    case MotionEvent.ACTION_UP:
                        y2 = event.getY();
                        float deltaY = y2 - y1;
                        if (deltaY > MIN_DISTANCE) {
                            activiteActivitiesRelativeLayout.setVisibility(View.GONE);
                            return true;
                        } else {
                            return true;
                        }
                    default:
                        return true;
                }
                return true;
            }
        });

        //Attributes Implementation
        attributesCardOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attributesRelativeLayout.setVisibility(View.VISIBLE);
            }
        });

        attributesActualCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        y1 = event.getY();
                        break;
//                        activiteActivitiesRelativeLayout.setVisibility(View.GONE);
//                        return true;
                    case MotionEvent.ACTION_UP:
                        y2 = event.getY();
                        float deltaY = y2 - y1;
                        if (deltaY > MIN_DISTANCE) {
                            attributesRelativeLayout.setVisibility(View.GONE);
                            return true;
                        } else {
                            return true;
                        }
                    default:
                        return true;
                }
                return true;
            }
        });


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
                                allActivities.add(new Activity(snapshot.toObject(Activity.class).getHost(),
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
                                        activeActivities.add(new Activity(allActivities.get(i).getHost(),
                                                allActivities.get(i).getActivityTitle(), allActivities.get(i).getActivityStartTime(),
                                                allActivities.get(i).getActivityEndTime(), allActivities.get(i).getActivityDate(),
                                                allActivities.get(i).getActivityLocationName(), allActivities.get(i).getActivityLocationCoordinates(),
                                                allActivities.get(i).getActivityMinRequiredPeople(),
                                                allActivities.get(i).getActivityMaxRequiredPeople(),
                                                allActivities.get(i).getActivityMinAge(), allActivities.get(i).getActivityMaxAge(),
                                                allActivities.get(i).getImageUrl(),
                                                allActivities.get(i).getActivityCost(),
                                                allActivities.get(i).getActivityUploaderId(), allActivities.get(i).getActivityId(),
                                                allActivities.get(i).getActivityUploadedTime(),
                                                allActivities.get(i).getTags(),
                                                allActivities.get(i).getActivityTag(), allActivities.get(i).getActivityAttendees(),
                                                allActivities.get(i).isActivityPrivate()));
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
                            Log.e(TAG, String.valueOf(activeActivities.size()));
                            ActiveActivitiesAdapter activeActivitiesAdapter = new ActiveActivitiesAdapter(getContext(), activeActivities);
                            activeActivitiesRecycler.setLayoutManager(new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false));
                            activeActivitiesRecycler.setAdapter(activeActivitiesAdapter);
                        }

                    }
                });
    }
}
