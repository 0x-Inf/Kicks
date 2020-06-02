package com.diablo.jayson.kicksv1.UI.AddKick;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diablo.jayson.kicksv1.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddActivityTagFragment extends Fragment {

    public AddActivityTagFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_activity_tag, container, false);
    }
}
