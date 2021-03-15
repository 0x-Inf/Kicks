package com.diablo.jayson.kicksv1.UI.AddActivity.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

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
import com.diablo.jayson.kicksv1.databinding.FragmentAddActivityCostBinding;

import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddActivityCostFragment extends Fragment {

    private FragmentAddActivityCostBinding binding;
    private AddActivityViewModel addActivityViewModel;

    private NavController navController;

    private String activityCost;
    private boolean isCostUnspecified;

    public AddActivityCostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddActivityCostBinding.inflate(inflater, container, false);
        addActivityViewModel = new ViewModelProvider(requireActivity()).get(AddActivityViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        isCostUnspecified = false;

        binding.costUnspecifiedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean unspecified) {
                isCostUnspecified = unspecified;
                if (unspecified) {
                    activityCost = "Unspecified";
                    binding.editCostOverlay.setVisibility(View.VISIBLE);
                    binding.editCostOverlay.setAlpha(0f);
                    binding.editCostOverlay.animate()
                            .alpha(0.7f)
                            .setDuration(400)
                            .setListener(null);
                } else {
                    binding.editCostOverlay.animate()
                            .alpha(0f)
                            .setDuration(400)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    binding.editCostOverlay.setVisibility(View.GONE);
                                }
                            });
                }
            }
        });

        binding.editCostOverlay.setOnClickListener(view -> {
            return;
        });

        binding.doneTextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.costInputEditText.getText().toString().isEmpty() && !isCostUnspecified) {
                    navController.popBackStack();
                } else {
                    updateActivityViewModel();
                }

            }
        });


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addActivityViewModel.getActivity1().observe(getViewLifecycleOwner(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                if (activity != null) {
                    if (activity.getActivityCost() != null) {
                        if (activity.getActivityCost().equals("Unspecified")) {
                            Timber.e("Unspecified Cost");
//                            isCostUnspecified = true;
                            binding.costUnspecifiedSwitch.setChecked(true);
                            activityCost = "Unspecified";
                        } else {
                            Timber.e("specified Cost");
                            activityCost = activity.getActivityCost();
                            binding.costInputEditText.setText(activityCost);
                        }
                    }
                }
            }
        });

    }

    private void updateActivityViewModel() {
        if (!isCostUnspecified) {
            activityCost = binding.costInputEditText.getText().toString();
        }
        addActivityViewModel.updateActivityCost(activityCost);
        navigateToNextFragment();
    }

    private void navigateToNextFragment() {
        NavDirections actionAddActivityMain = AddActivityCostFragmentDirections.actionAddActivityCostFragmentToNavigationAddKick();
        navController.navigate(actionAddActivityMain);
    }
}
