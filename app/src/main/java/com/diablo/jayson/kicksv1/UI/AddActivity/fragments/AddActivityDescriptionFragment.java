package com.diablo.jayson.kicksv1.UI.AddActivity.fragments;

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
import com.diablo.jayson.kicksv1.UI.AddActivity.AddActivityViewModel;
import com.diablo.jayson.kicksv1.databinding.FragmentAddActivityDescriptionBinding;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddActivityDescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddActivityDescriptionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentAddActivityDescriptionBinding binding;
    private AddActivityViewModel addActivityViewModel;
    private NavController navController;


    public AddActivityDescriptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddActivityDescriptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddActivityDescriptionFragment newInstance(String param1, String param2) {
        AddActivityDescriptionFragment fragment = new AddActivityDescriptionFragment();
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
        binding = FragmentAddActivityDescriptionBinding.inflate(inflater, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        NavDirections actionAddActivityMain = AddActivityDescriptionFragmentDirections.actionAddActivityDescriptionFragmentToNavigationAddKick();
        binding.doneTextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.activityTitleEditText.getText().toString().equals("") ||
                        binding.activityDescriptionEditText.getText().toString().equals("")) {
                    navController.popBackStack();
                } else {
                    updateViewModel();
                    Timber.e("updated Model");
                    navController.navigate(actionAddActivityMain);
                }
            }
        });
        return binding.getRoot();
    }

    private void updateViewModel() {
        String activityTitle = binding.activityTitleEditText.getText().toString();
        String activityDescription = binding.activityDescriptionEditText.getText().toString();
        addActivityViewModel.updateActivityDescription(activityTitle, activityDescription);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addActivityViewModel = new ViewModelProvider(requireActivity()).get(AddActivityViewModel.class);
        addActivityViewModel.getActivity().observe(getViewLifecycleOwner(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                if (activity != null) {
                    if (activity.getActivityTitle() != null && activity.getActivityDescription() != null) {
                        binding.activityTitleEditText.setText(activity.getActivityTitle());
                        binding.activityDescriptionEditText.setText(activity.getActivityDescription());
                    }
                }
            }
        });
    }
}