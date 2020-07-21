package com.diablo.jayson.kicksv1.UI.Home.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.Home.HappeningSoonActivitiesAdapter;
import com.diablo.jayson.kicksv1.databinding.FragmentDashboardBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentDashboardBinding binding;

    private HappeningSoonActivitiesAdapter soonActivitiesAdapter;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        binding.activeActivitiesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections actionActiveActivities = DashboardFragmentDirections.actionDashboardFragmentToActiveActivitiesFragment();
                navController.navigate(actionActiveActivities);
            }
        });
        binding.browseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections actionBrowse = DashboardFragmentDirections.actionDashboardFragmentToNavigationBrowse();
                navController.navigate(actionBrowse);
            }
        });
        binding.invitesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections actionInvites = DashboardFragmentDirections.actionDashboardFragmentToInvitesFragment();
                navController.navigate(actionInvites);
            }
        });
        binding.mapCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections actionMap = DashboardFragmentDirections.actionDashboardFragmentToMapFragment();
                navController.navigate(actionMap);
            }
        });

        DateFormat formatter = new SimpleDateFormat("EEEE  d LLLL");
        Date dateToday = new Date();
        String dateString = formatter.format(dateToday);
        Log.e("The date", dateString);
        binding.dateTextView.setText(dateString);
        return root;
    }
}