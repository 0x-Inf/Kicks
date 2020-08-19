package com.diablo.jayson.kicksv1.UI.AttendActivity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AttendActivity.AttendActivityViewModel;
import com.diablo.jayson.kicksv1.databinding.FragmentActivityDetailsBinding;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivityDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivityDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentActivityDetailsBinding binding;
    private AttendActivityViewModel viewModel;
    private Activity attendedActivity;

    private String activityId;

    public ActivityDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActivityDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivityDetailsFragment newInstance(String param1, String param2) {
        ActivityDetailsFragment fragment = new ActivityDetailsFragment();
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
        viewModel = new ViewModelProvider(requireActivity()).get(AttendActivityViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentActivityDetailsBinding.inflate(inflater, container, false);
        getActivityData();

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        binding.editTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections actionEditTime = ActivityDetailsFragmentDirections.actionActivityDetailsFragmentToEditTimeFragment();
                navController.navigate(actionEditTime);
            }
        });

        binding.editDescriptionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections actionEditDescription = ActivityDetailsFragmentDirections.actionActivityDetailsFragmentToEditDescriptionBottomSheetFragment();
                navController.navigate(actionEditDescription);
            }
        });

        binding.editLocationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavDirections actionEditLocation = ActivityDetailsFragmentDirections.actionActivityDetailsFragmentToEditLocationBottomSheetFragment();
                navController.navigate(actionEditLocation);
            }
        });

        return binding.getRoot();
    }

    private void getActivityData() {
        attendedActivity = new Activity();
        viewModel.getActivityData().observe(requireActivity(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                attendedActivity = activity;

            }
        });
    }
}