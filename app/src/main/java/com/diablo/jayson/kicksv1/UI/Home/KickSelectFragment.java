package com.diablo.jayson.kicksv1.UI.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.diablo.jayson.kicksv1.Adapters.KickCategoryListAdapter;
import com.diablo.jayson.kicksv1.Models.KickCategory;
import com.diablo.jayson.kicksv1.R;

import java.util.ArrayList;

public class KickSelectFragment extends Fragment {

    private RecyclerView mCategoryRecyclerView;
    private RecyclerView mKickRecyclerView;
    private KickCategoryListAdapter mCatAdapter;
    private ArrayList<KickCategory> mKickCategoryData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.kick_select_fragment,container,false);
        mCategoryRecyclerView = root.findViewById(R.id.categoryRecyclerView);
        mKickRecyclerView = root.findViewById(R.id.kicksRecyclerView);
        mCategoryRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));

        mKickCategoryData = new ArrayList<KickCategory>();
        mCatAdapter = new KickCategoryListAdapter(getContext(),mKickCategoryData);
        mCategoryRecyclerView.setAdapter(mCatAdapter);
        initializeData();

        return root;
    }

    private void initializeData(){

        String [] kickCategories = getResources().getStringArray(R.array.kicks_categories);
        mKickCategoryData.clear();

        for (int i = 0;i<kickCategories.length;i++){
            mKickCategoryData.add(new KickCategory(kickCategories[i]));

        }

        mCatAdapter.notifyDataSetChanged();


    }

}
