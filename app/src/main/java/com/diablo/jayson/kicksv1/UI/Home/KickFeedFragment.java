package com.diablo.jayson.kicksv1.UI.Home;

import androidx.lifecycle.MutableLiveData;
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

import com.diablo.jayson.kicksv1.ActivityListAdapter;
import com.diablo.jayson.kicksv1.Kick;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class KickFeedFragment extends Fragment {

    private KickFeedViewModel KickFeedViewModel;
    private ArrayList<Kick> mKicksData;
    private RecyclerView mRecyclerView;
    private ActivityListAdapter mAdapter;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        KickFeedViewModel =
                ViewModelProviders.of(this).get(KickFeedViewModel.class);
        View root = inflater.inflate(R.layout.kick_feed_fragment,container,false);
        mRecyclerView = root.findViewById(R.id.recyclerview);
        int gridColumnCount = 1;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),gridColumnCount));
        mKicksData = new ArrayList<Kick>();
        mAdapter = new ActivityListAdapter(getContext(),mKicksData);
        mRecyclerView.setAdapter(mAdapter);

        initializeData();

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
            mKicksData.add(new Kick(kicksTitles[i], kicksTimes[i], kicksDates[i],
                    kicksLocations[i], alreadyAttending[i], requiredAttending[i],
                    kicksImageResources.getResourceId(i, 0)));
        }
        kicksImageResources.recycle();

        mAdapter.notifyDataSetChanged();
    }


}
