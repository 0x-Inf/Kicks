package com.diablo.jayson.kicksv1.UI.Home;

import androidx.lifecycle.ViewModelProviders;

import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.diablo.jayson.kicksv1.Adapters.ActivityFeedListAdapter;
import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class KickFeedFragment extends Fragment {

    private ArrayList<Activity> mKicksData;
    private RecyclerView mRecyclerView;
    private ActivityFeedListAdapter mAdapter;
    private RelativeLayout mRelativelayout;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.kick_feed_fragment,container,false);
        mRecyclerView = root.findViewById(R.id.recyclerview);
        mRelativelayout = root.findViewById(R.id.searchAndProfileRelativeView);
        int gridColumnCount = 1;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),gridColumnCount));
        mKicksData = new ArrayList<Activity>();
        mAdapter = new ActivityFeedListAdapter(getContext(),mKicksData);
        mRecyclerView.setAdapter(mAdapter);
        initializeData();
        dissappearItemsOnScroll();

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

        for (int i = 0; i < kicksTitles.length; i++) {
            mKicksData.add(new Activity(kicksTitles[i], kicksTimes[i], kicksDates[i],
                    kicksLocations[i], alreadyAttending[i], requiredAttending[i],
                    ""));
        }
        kicksImageResources.recycle();

        mAdapter.notifyDataSetChanged();
    }

    private void dissappearItemsOnScroll(){
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


}
