package com.diablo.jayson.kicksv1.UI.Home;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diablo.jayson.kicksv1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeaturedFragment extends Fragment {


    public FeaturedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_featured, container, false);
    }

}
