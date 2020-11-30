package com.diablo.jayson.kicksv1.UI.AddKick.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AddKick.AddActivityViewModel;
import com.diablo.jayson.kicksv1.databinding.FragmentAddActivityDateTimeBinding;
import com.google.android.material.slider.Slider;

import org.jetbrains.annotations.NotNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddActivityDateTimeFragment extends Fragment {

    private FragmentAddActivityDateTimeBinding binding;
    private AddActivityViewModel addActivityViewModel;

    private String activityDuration;


    public AddActivityDateTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddActivityDateTimeBinding.inflate(inflater, container, false);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        binding.durationUnspecifiedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        binding.setCustomDurationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        binding.hoursDurationSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {

            }
        });
        binding.hoursDurationSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {

            }
        });

        binding.daysDurationSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {

            }
        });
        binding.daysDurationSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {

            }
        });

        binding.monthsDurationSlider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {

            }
        });
        binding.monthsDurationSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {

            }
        });

        binding.doneTextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAddActivityViewModel();
            }
        });
        return binding.getRoot();
    }

    private void updateAddActivityViewModel() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addActivityViewModel = new ViewModelProvider(requireActivity()).get(AddActivityViewModel.class);
    }
}
