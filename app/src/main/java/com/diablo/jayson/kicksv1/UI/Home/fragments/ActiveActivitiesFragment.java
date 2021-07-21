package com.diablo.jayson.kicksv1.UI.Home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.Home.ActiveActivitiesAdapter;
import com.diablo.jayson.kicksv1.UI.Home.HomeViewModel;
import com.diablo.jayson.kicksv1.databinding.FragmentActiveActivities2Binding;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActiveActivitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActiveActivitiesFragment extends Fragment implements ActiveActivitiesAdapter.OnActiveActivitySelectedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentActiveActivities2Binding binding;
    private ActiveActivitiesAdapter activeActivitiesAdapter;
    private HomeViewModel homeViewModel;
    private ArrayList<Activity> activeActivitiesData;

    private ActiveActivitiesFragment listener;

    public ActiveActivitiesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActiveActivitiesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActiveActivitiesFragment newInstance(String param1, String param2) {
        ActiveActivitiesFragment fragment = new ActiveActivitiesFragment();
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
        binding = FragmentActiveActivities2Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        activeActivitiesData = new ArrayList<>();
        listener = this;
        homeViewModel.getActiveActivitiesMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Activity>>() {
            @Override
            public void onChanged(ArrayList<Activity> activities) {
                activeActivitiesData = activities;
                activeActivitiesAdapter = new ActiveActivitiesAdapter(activeActivitiesData, listener);
                binding.activeActivitiesRecycler.setAdapter(activeActivitiesAdapter);
            }
        });
    }

    @Override
    public void onActiveActivitySelected(Activity activeActivity) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        long timeNow = System.currentTimeMillis();
        Date activityStartDate = activeActivity.getActivityStartTime().toDate();
//        long activityTime = activeActivity.getActivityStartTime().getSeconds();
        long activityTime = activityStartDate.getTime();

        if (timeNow <= activityTime) {
            NavDirections actionMainAttendFragment = ActiveActivitiesFragmentDirections
                    .actionActiveActivitiesFragmentToAttendActivityMainFragment(activeActivity.getActivityId());
            navController.navigate(actionMainAttendFragment);
        } else {
            NavDirections actionBottomSheetFragment = ActiveActivitiesFragmentDirections
                    .actionActiveActivitiesFragmentToActiveActivitySelectedBottomSheet(activeActivity.getActivityId());
            navController.navigate(actionBottomSheetFragment);
        }


    }
}