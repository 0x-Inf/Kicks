package com.diablo.jayson.kicksv1.UI.UserProfile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.diablo.jayson.kicksv1.R;

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

    //Main Dash stuff
    private RelativeLayout activitiesCardOverlay;
    private ImageView activitiesCardImageView;

    //Active Activities Stuff
    private RelativeLayout activiteActivitiesRelativeLayout;
    private CardView activeActivitiesActualCard;

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
        activitiesCardImageView = root.findViewById(R.id.activitiesCardImageView);

        activiteActivitiesRelativeLayout = root.findViewById(R.id.active_activities_relative_layout);
        activeActivitiesActualCard = root.findViewById(R.id.activeActivitiesActualCard);

        activitiesCardOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        if (Math.abs(deltaY) > MIN_DISTANCE) {
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

        return root;
    }
}
