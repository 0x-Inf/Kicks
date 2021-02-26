package com.diablo.jayson.kicksv1.UI.AddActivity.fragments;

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
import com.diablo.jayson.kicksv1.UI.AddActivity.AddActivityViewModel;
import com.diablo.jayson.kicksv1.UI.AddActivity.Duration;
import com.diablo.jayson.kicksv1.UI.AddActivity.DurationsListAdapter;
import com.diablo.jayson.kicksv1.databinding.FragmentAddActivityDateTimeBinding;
import com.google.firebase.Timestamp;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddActivityDateTimeFragment extends Fragment implements DurationsListAdapter.OnDurationExampleSelectedListener {

    private FragmentAddActivityDateTimeBinding binding;
    private AddActivityViewModel addActivityViewModel;
    private NavController navController;

    private ArrayList<Duration> durations = new ArrayList<>();
    private DurationsListAdapter durationsListAdapter;

    private Duration activityDuration;
    private Duration pickedActivityDuration;
    private Duration savedDuration;
    private Timestamp activityStartTime;
    private Timestamp activityStartDate;
    private boolean isCustomDuration;
    private boolean isDurationUnspecified;
    private AddActivityDateTimeFragment listener;

    public AddActivityDateTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddActivityDateTimeBinding.inflate(inflater, container, false);
//        MainActivity activity = (MainActivity) this.getActivity();
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        activityDuration = new Duration();
        pickedActivityDuration = new Duration();
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(),1,RecyclerView.HORIZONTAL,false);
//        binding.durationOptionsRecyclerView.setLayoutManager(gridLayoutManager);
//        AppDatabase appDatabase = Room.databaseBuilder(requireContext(),
//                AppDatabase.class, "database-name").build();
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();

        binding.durationUnspecifiedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean durationUnspecified) {
                isDurationUnspecified = durationUnspecified;
                if (durationUnspecified) {
                    activityDuration = new Duration(0, "Unspecified");
                    if (isCustomDuration) {
                        binding.setCustomDurationSwitch.setChecked(false);
                    }
                    binding.durationSelectionOverlay.setVisibility(View.VISIBLE);
                    binding.durationSelectionOverlay.setAlpha(0f);
                    binding.durationSelectionOverlay.animate()
                            .alpha(0.7f)
                            .setDuration(400)
                            .setListener(null);
                } else {
                    activityDuration = pickedActivityDuration;
                    binding.durationSelectionOverlay.animate()
                            .alpha(0f)
                            .setDuration(400)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    binding.durationSelectionOverlay.setVisibility(View.GONE);
                                }
                            });
                }


            }
        });
        binding.durationSelectionOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                return;
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
                    durationsListAdapter.resetCheckedPosition();
                    durationsListAdapter.notifyDataSetChanged();
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

                    pickedActivityDuration = new Duration();
                }
            }
        });

        binding.hoursDurationSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.pickedHoursTextView.setText(String.valueOf(i));
                long hoursInMs = TimeUnit.HOURS.toMillis(i);
                pickedActivityDuration = new Duration(hoursInMs, String.valueOf(i) + " Hours");
                if (i != 0) {
                    binding.pickedHoursOverLay.setVisibility(View.GONE);
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
                                super.onAnimationEnd(animation);
                                binding.pickedHoursOverLay.setVisibility(View.GONE);

                            }

                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                binding.pickedDaysOverLay.setAlpha(0.7f);
                                binding.pickedMonthsOverLay.setAlpha(0.7f);
                                binding.pickedDaysOverLay.setVisibility(View.VISIBLE);
                                binding.pickedMonthsOverLay.setVisibility(View.VISIBLE);
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
                long daysInMs = TimeUnit.DAYS.toMillis(i);
                pickedActivityDuration = new Duration(daysInMs, Math.round(i) + " Days");
                if (i != 0) {
                    binding.pickedDaysOverLay.setVisibility(View.GONE);
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
                long monthsInMs = TimeUnit.DAYS.toMillis(i * 30);
                pickedActivityDuration = new Duration(monthsInMs, Math.round(i) + " Months");
                if (i != 0) {
                    binding.pickedMonthsOverLay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
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
        if (seekBar == binding.hoursDurationSlider) {
//            binding.pickedDaysOverLay.setVisibility(View.VISIBLE);
//            binding.pickedMonthsOverLay.setVisibility(View.VISIBLE);
            binding.daysDurationSlider.setProgress(0);
            binding.monthsDurationSlider.setProgress(0);
        } else if (seekBar == binding.daysDurationSlider) {
            binding.pickedHoursOverLay.setAlpha(0.7f);
            binding.pickedMonthsOverLay.setAlpha(0.7f);
            binding.pickedHoursOverLay.setVisibility(View.VISIBLE);
            binding.pickedMonthsOverLay.setVisibility(View.VISIBLE);
            binding.hoursDurationSlider.setProgress(0);
            binding.monthsDurationSlider.setProgress(0);
        } else if (seekBar == binding.monthsDurationSlider) {
            binding.pickedHoursOverLay.setAlpha(0.7f);
            binding.pickedDaysOverLay.setAlpha(0.7f);
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

        if (!(pickedActivityDuration == null) && isCustomDuration) {
            activityDuration = pickedActivityDuration;
        }

        if ((activityPickedStartDateTimestamp.getSeconds() < Timestamp.now().getSeconds()) ||
                (activityPickedStartTimeTimestamp.getSeconds() <= Timestamp.now().getSeconds())) {
            Toast.makeText(requireContext(), "Invalid Start Date or Time", Toast.LENGTH_LONG).show();
        } else {
            activityStartDate = activityPickedStartDateTimestamp;
            activityStartTime = activityPickedStartTimeTimestamp;
            addActivityViewModel.updateActivityTime(activityStartDate, activityStartTime, activityDuration);
            navigateToNextFragment();
        }


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addActivityViewModel = new ViewModelProvider(requireActivity()).get(AddActivityViewModel.class);
        listener = this;

        addActivityViewModel.getDurationsMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Duration>>() {
            @Override
            public void onChanged(ArrayList<Duration> durationsLiveData) {
                durations = durationsLiveData;
                durationsListAdapter = new DurationsListAdapter(durations, listener);
                binding.durationOptionsRecyclerView.setAdapter(durationsListAdapter);
                binding.durationOptionsRecyclerView.addItemDecoration(new AddActivityTagFragment.HorizontalGridSpacingItemDecoration(15));
            }
        });

        addActivityViewModel.getActivity1().observe(getViewLifecycleOwner(), new Observer<Activity>() {
            @Override
            public void onChanged(Activity activity) {
                if (activity != null) {
                    if (activity.getActivityStartTime() != null &&
                            activity.getActivityStartDate() != null &&
                            !activity.getActivityDuration().getDurationName().isEmpty()) {
                        Calendar startDateCal = Calendar.getInstance();
                        Calendar startTimeCal = Calendar.getInstance();
                        Date startDate = activity.getActivityStartDate().toDate();
                        Date startTime = activity.getActivityStartTime().toDate();
                        startDateCal.setTime(startDate);
                        binding.activityDatePicker.init(startDateCal.get(Calendar.YEAR), startDateCal.get(Calendar.MONTH),
                                startDateCal.get(Calendar.DAY_OF_MONTH), null);
                        startTimeCal.setTime(startTime);
                        binding.activityTimePicker.setHour(startTimeCal.get(Calendar.HOUR));
                        binding.activityTimePicker.setMinute(startTimeCal.get(Calendar.MINUTE));
                        binding.activityTimePicker.setIs24HourView(true);

                        if (activity.getActivityDuration().getDurationName().equals("Unspecified")) {
                            binding.durationUnspecifiedSwitch.setChecked(true);
                            isDurationUnspecified = true;
                        }
                        savedDuration = activity.getActivityDuration();

                        //TODO: Seek if there is a better way to implement this
                        if (savedDuration != null) {
                            Timber.e(savedDuration.getDurationName());
                            if (durations.contains(savedDuration)) {
                                int durationIndex = durations.indexOf(savedDuration) + 1;
                                updateDurationsRecyclerView(durationIndex);
                            } else {
                                if (savedDuration.getDurationName().contains("Hours") ||
                                        savedDuration.getDurationName().contains("Days") ||
                                        savedDuration.getDurationName().contains("Months")) {
                                    binding.setCustomDurationSwitch.setChecked(true);
                                    if (savedDuration.getDurationName().contains("Hours")) {
                                        long savedHoursMs = savedDuration.getDurationTimeMs();
                                        int savedHoursInt = (int) TimeUnit.MILLISECONDS.toHours(savedHoursMs);
                                        binding.hoursDurationSlider.setProgress(savedHoursInt);
                                    } else if (savedDuration.getDurationName().contains("Days")) {
                                        long savedDaysMs = savedDuration.getDurationTimeMs();
                                        int savedDaysInt = (int) TimeUnit.MILLISECONDS.toDays(savedDaysMs);
                                        binding.daysDurationSlider.setProgress(savedDaysInt);
                                    } else if (savedDuration.getDurationName().contains("Months")) {
                                        long savedMonthsMs = savedDuration.getDurationTimeMs();
                                        int savedMonthsInt = ((int) TimeUnit.MILLISECONDS.toDays(savedMonthsMs)) / 30;
                                        binding.monthsDurationSlider.setProgress(savedMonthsInt);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });


    }

    private void updateDurationsRecyclerView(int durationIndex) {
//        binding.durationOptionsRecyclerView.findViewHolderForAdapterPosition(durationIndex).itemView.performClick();
    }

    @Override
    public void onDurationExampleSelected(Duration duration) {
        if (isCustomDuration) {
            binding.setCustomDurationSwitch.setChecked(false);
        }
        if (!isDurationUnspecified) {
            activityDuration = duration;
//            Toast.makeText(requireContext(), pickedActivityDuration, Toast.LENGTH_SHORT).show();
        }
    }
}
