package com.diablo.jayson.kicksv1.UI.AddKick.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

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
import com.diablo.jayson.kicksv1.UI.AddKick.AddActivityViewModel;
import com.diablo.jayson.kicksv1.UI.AddKick.DurationExample;
import com.diablo.jayson.kicksv1.UI.AddKick.DurationExamplesListAdapter;
import com.diablo.jayson.kicksv1.databinding.FragmentAddActivityDateTimeBinding;
import com.google.firebase.Timestamp;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddActivityDateTimeFragment extends Fragment implements DurationExamplesListAdapter.OnDurationExampleSelectedListener {

    private FragmentAddActivityDateTimeBinding binding;
    private AddActivityViewModel addActivityViewModel;
    private NavController navController;

    private ArrayList<DurationExample> durationExamples = new ArrayList<>();

    private String activityDuration;
    private String pickedActivityDuration;
    private Timestamp activityStartTime;
    private Timestamp activityStartDate;
    private boolean isCustomDuration;
    private boolean isDurationUnspecified;

    public AddActivityDateTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddActivityDateTimeBinding.inflate(inflater, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        activityDuration = "";
        pickedActivityDuration = "";

        // TODO: Implementation of the duration example recycler.
        durationExamples.add(new DurationExample("20 Min"));
        durationExamples.add(new DurationExample("30 Min"));
        durationExamples.add(new DurationExample("45 Min"));
        durationExamples.add(new DurationExample("1 Hour"));
        durationExamples.add(new DurationExample("2 Hours"));
        durationExamples.add(new DurationExample("3 Hours"));

        DurationExamplesListAdapter durationExamplesListAdapter = new DurationExamplesListAdapter(durationExamples, this);
        binding.durationOptionsRecyclerView.setAdapter(durationExamplesListAdapter);
        binding.durationOptionsRecyclerView.addItemDecoration(new AddActivityTagFragment.HorizontalGridSpacingItemDecoration(15));
        binding.durationUnspecifiedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean durationUnspecified) {
                isDurationUnspecified = durationUnspecified;
                if (durationUnspecified) {
                    activityDuration = "Unspecified";
                    if (isCustomDuration) {
                        binding.setCustomDurationSwitch.setChecked(false);
                    }
                } else {
                    activityDuration = pickedActivityDuration;
                }

            }
        });
        binding.customDurationOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
            }
        });

        binding.setCustomDurationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean setCustomDuration) {
                isCustomDuration = setCustomDuration;
                if (setCustomDuration) {
                    if (isDurationUnspecified) {
                        binding.durationUnspecifiedSwitch.setChecked(false);
                    }
                    binding.pickedHoursOverLay.setVisibility(View.VISIBLE);
                    binding.pickedDaysOverLay.setVisibility(View.VISIBLE);
                    binding.pickedMonthsOverLay.setVisibility(View.VISIBLE);

                    binding.customDurationOverlay.animate()
                            .alpha(0f)
                            .setDuration(400)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    binding.customDurationOverlay.setVisibility(View.GONE);
                                }
                            });
                }else {

                    binding.pickedHoursOverLay.setVisibility(View.GONE);
                    binding.pickedDaysOverLay.setVisibility(View.GONE);
                    binding.pickedMonthsOverLay.setVisibility(View.GONE);

                    binding.customDurationOverlay.setAlpha(0f);
                    binding.customDurationOverlay.setVisibility(View.VISIBLE);

                    binding.customDurationOverlay.animate()
                            .alpha(0.7f)
                            .setDuration(400)
                            .setListener(null);

                    pickedActivityDuration = "";
                }
            }
        });

        binding.hoursDurationSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.pickedHoursTextView.setText(String.valueOf(i));
                pickedActivityDuration = String.valueOf(i) + " Hours";
                if (i == 0) {
                    binding.pickedHoursOverLay.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                showPickedDurationOverlays(seekBar);
                binding.pickedHoursOverLay.animate()
                        .alpha(0f)
                        .setDuration(400)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                binding.pickedHoursOverLay.setVisibility(View.GONE);

                            }
                        });
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.daysDurationSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.pickedDaysTextView.setText(String.valueOf(Math.round(i)));
                pickedActivityDuration = Math.round(i) + " Days";
                if (i == 0) {
                    binding.pickedDaysOverLay.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                showPickedDurationOverlays(seekBar);
                binding.pickedDaysOverLay.animate()
                        .alpha(0f)
                        .setDuration(400)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                binding.pickedDaysOverLay.setVisibility(View.GONE);
                            }
                        });
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.monthsDurationSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.pickedMonthsTextView.setText(String.valueOf(Math.round(i)));
                pickedActivityDuration = Math.round(i) + " Months";
                if (i == 0) {
                    binding.pickedMonthsOverLay.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Toast.makeText(requireContext(),"Selecting Months",Toast.LENGTH_SHORT).show();
                showPickedDurationOverlays(seekBar);
                binding.pickedMonthsOverLay.animate()
                        .alpha(0f)
                        .setDuration(400)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                binding.pickedMonthsOverLay.setVisibility(View.GONE);
                            }
                        });
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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

    private void showPickedDurationOverlays(SeekBar seekBar) {
        // TODO: Make sure this code works
        if (seekBar == binding.hoursDurationSlider) {
            binding.pickedDaysOverLay.setVisibility(View.VISIBLE);
            binding.pickedMonthsOverLay.setVisibility(View.VISIBLE);
            binding.daysDurationSlider.setProgress(0);
            binding.monthsDurationSlider.setProgress(0);
        } else if (seekBar == binding.daysDurationSlider) {
            binding.pickedHoursOverLay.setVisibility(View.VISIBLE);
            binding.pickedMonthsOverLay.setVisibility(View.VISIBLE);
            binding.hoursDurationSlider.setProgress(0);
            binding.monthsDurationSlider.setProgress(0);
        } else if (seekBar == binding.monthsDurationSlider) {
            binding.pickedHoursOverLay.setVisibility(View.VISIBLE);
            binding.pickedDaysOverLay.setVisibility(View.VISIBLE);
            binding.daysDurationSlider.setProgress(0);
            binding.hoursDurationSlider.setProgress(0);
        }
    }

    private void navigateToNextFragment() {
        NavDirections actionAddActivityMain = AddActivityDateTimeFragmentDirections.actionAddActivityDateTimeFragmentToNavigationAddKick();
        navController.navigate(actionAddActivityMain);
    }

    private void updateAddActivityViewModel() {

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.set(binding.activityDatePicker.getYear(),binding.activityDatePicker.getMonth(), binding.activityDatePicker.getDayOfMonth());
        Timestamp activityPickedStartDateTimestamp = new Timestamp(calendarDate.getTime());

        Calendar calendarTime = Calendar.getInstance();
        calendarTime.set(binding.activityDatePicker.getYear(),binding.activityDatePicker.getMonth(), binding.activityDatePicker.getDayOfMonth(),
                binding.activityTimePicker.getHour(),binding.activityTimePicker.getMinute());
        Timestamp activityPickedStartTimeTimestamp = new Timestamp(calendarTime.getTime());

        if ((!pickedActivityDuration.isEmpty()) && isCustomDuration){
            activityDuration = pickedActivityDuration;
        }

        if ((activityPickedStartDateTimestamp.getSeconds() <= Timestamp.now().getSeconds()) ||
                (activityPickedStartTimeTimestamp.getSeconds() <= Timestamp.now().getSeconds())){
            Toast.makeText(requireContext(),"Invalid Start Date or Time",Toast.LENGTH_LONG).show();
        }else {
            activityStartDate = activityPickedStartDateTimestamp;
            activityStartTime = activityPickedStartTimeTimestamp;
            addActivityViewModel.updateActivityTime(activityStartDate,activityStartTime,activityDuration);
            navigateToNextFragment();
        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addActivityViewModel = new ViewModelProvider(requireActivity()).get(AddActivityViewModel.class);

        addActivityViewModel.getActivity1().observe(getViewLifecycleOwner(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                if (activity != null){
                    if (activity.getActivityStartTime() != null &&
                            activity.getActivityStartDate() != null &&
                            !activity.getActivityDuration().isEmpty()){
                        Calendar startDateCal = Calendar.getInstance();
                        Calendar startTimeCal = Calendar.getInstance();
                        Date startDate = activity.getActivityStartDate().toDate();
                        Date startTime = activity.getActivityStartTime().toDate();
                        startDateCal.setTime(startDate);
                        binding.activityDatePicker.init(startDateCal.get(Calendar.YEAR),startDateCal.get(Calendar.MONTH),
                                startDateCal.get(Calendar.DAY_OF_MONTH),null);
                        startTimeCal.setTime(startTime);
                        binding.activityTimePicker.setHour(startTimeCal.get(Calendar.HOUR));
                        binding.activityTimePicker.setMinute(startTimeCal.get(Calendar.MINUTE));
                        binding.activityTimePicker.setIs24HourView(true);

                        if (activity.getActivityDuration().equals("Unspecified")) {
                            binding.durationUnspecifiedSwitch.setChecked(true);
                            isDurationUnspecified = true;
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onDurationExampleSelected(DurationExample durationExample) {
        if (!isDurationUnspecified) {
            pickedActivityDuration = durationExample.getDurationName();
        }
    }
}
