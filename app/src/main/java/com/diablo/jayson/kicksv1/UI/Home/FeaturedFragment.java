package com.diablo.jayson.kicksv1.UI.Home;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diablo.jayson.kicksv1.Adapters.FeaturedFeedAdapter;
import com.diablo.jayson.kicksv1.Models.FeaturedKicks;
import com.diablo.jayson.kicksv1.Models.ImageAndText;
import com.diablo.jayson.kicksv1.Models.ImageTextAndList;
import com.diablo.jayson.kicksv1.Models.Kick;
import com.diablo.jayson.kicksv1.Models.KickCategory;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeaturedFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FeaturedFeedAdapter mAdapter;
    private ArrayList<ImageAndText> mImageAndTextData;
    private ArrayList<ImageTextAndList> mImageTextAndListData;
    private ArrayList<Kick> mKicksData;
    private List<FeaturedKicks> mFeaturedKicksTotal;


    public FeaturedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_featured, container, false);
        mRecyclerView = root.findViewById(R.id.featuredRecyclerView);
        int gridColumnCount = 1;
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumnCount));
        mImageAndTextData = new ArrayList<ImageAndText>();
        mImageTextAndListData = new ArrayList<ImageTextAndList>();
        mFeaturedKicksTotal = new ArrayList<>();
        mAdapter = new FeaturedFeedAdapter(getContext(), mFeaturedKicksTotal);
        mRecyclerView.setAdapter(mAdapter);
        initializeData();


        return root;
    }

    private void initializeData() {
        textAndImageData();
        textImageAndListData();
        mAdapter.notifyDataSetChanged();

    }

    private void textAndImageData() {

        String[] kickCategories = getResources().getStringArray(R.array.featured_kicks_Titles);
        String[] kickImages = getResources().getStringArray(R.array.featured_kicks_images);
        mImageAndTextData.clear();

        for (int i = 0; i < kickCategories.length; i++) {
            FeaturedKicks featuredKicks = new FeaturedKicks();
            featuredKicks.setmFeaturedImageAndText(new ImageAndText(kickCategories[i], kickImages[i]));
            mFeaturedKicksTotal.add(featuredKicks);

        }
    }

    private void textImageAndListData() {


        String[] kickCategories = getResources().getStringArray(R.array.featured_kicks_Titles);
        String[] kickImages = getResources().getStringArray(R.array.featured_kicks_images);
        mImageTextAndListData.clear();

        for (int i = 0; i < kickCategories.length; i++) {
            mKicksData = new ArrayList<>();
            initializeKickData();
            FeaturedKicks featuredKicks = new FeaturedKicks();
            featuredKicks.setmFeaturedImageTextAndList(new ImageTextAndList(mKicksData, kickImages[i], kickCategories[i]));
            mFeaturedKicksTotal.add(featuredKicks);

        }


    }

    private void initializeKickData() {
        String[] kickNames = getResources().getStringArray(R.array.featured_kicks_list_names);
        String[] kickImages = getResources().getStringArray(R.array.featured_kicks_list_images);
        mKicksData.clear();

        for (int i = 0; i < kickNames.length; i++) {
            mKicksData.add(new Kick(kickNames[i], kickImages[i]));
        }

    }


}
