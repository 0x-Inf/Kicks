package com.diablo.jayson.kicksv1.UI.AddKick;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.diablo.jayson.kicksv1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddActivityDateTimeFragment extends Fragment {

    private AddActivityDateTimeData activityDateTimeData;

    //Date Time Stuff
    private RelativeLayout addActivityDateTimeRelativeLayout;
    private DatePicker activityDatePicker;
    private TimePicker activityTimePicker;
    private NumberPicker activityDurationPicker;
    private FloatingActionButton dateTimeSelectionDoneButton;

    public AddActivityDateTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_add_activity_date_time, container, false);

        //Date Time Views
        addActivityDateTimeRelativeLayout = root.findViewById(R.id.add_activity_time_date_relative_layout);
        activityDatePicker = root.findViewById(R.id.activity_date_picker);
        activityTimePicker = root.findViewById(R.id.activity_time_picker);
        activityDurationPicker = root.findViewById(R.id.durationPicker);
        dateTimeSelectionDoneButton = root.findViewById(R.id.dateTimeSelectionDoneButton);

        activityDateTimeData = new AddActivityDateTimeData();


        //Date Time Implementation
        final String[] durations = {"<1", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24"};


        //Populate NumberPicker values from String array values
        //Set the minimum value of NumberPicker
        activityDurationPicker.setMinValue(0); //from array first value
        //Specify the maximum value/number of NumberPicker
        activityDurationPicker.setMaxValue(durations.length - 1); //to array last value

        activityDurationPicker.setDisplayedValues(durations);

        //Sets whether the selector wheel wraps when reaching the min/max value.
        activityDurationPicker.setWrapSelectorWheel(true);
        final long[] activitySeconds = new long[1];

        activityDurationPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (durations[newVal].equals("<1")) {
                    activitySeconds[0] = 59 * 60;
                } else {
                    long hours = Long.parseLong(durations[newVal]);
                    activitySeconds[0] = hours * 3600;
                }
            }
        });
        Calendar calendar = Calendar.getInstance();

        dateTimeSelectionDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendarDate = Calendar.getInstance();
                calendarDate.set(activityDatePicker.getYear(), activityDatePicker.getMonth(), activityDatePicker.getDayOfMonth());
                Timestamp activityDateTimestamp = new Timestamp(calendarDate.getTime());
                long timestampSecondsDate = calendarDate.getTimeInMillis();
//                Timestamp activityDateTimestamp = new Timestamp(timestampSecondsDate);
                activityDateTimeData.setActivityDate(activityDateTimestamp);
                Calendar calendarTime = Calendar.getInstance();
                calendarTime.set(activityDatePicker.getYear(), activityDatePicker.getMonth(),
                        activityDatePicker.getDayOfMonth(), activityTimePicker.getHour(), activityTimePicker.getMinute());
                Timestamp activityStartTimeTimestamp = new Timestamp(calendarTime.getTime());
                long timestampSecondsTime = calendarTime.getTimeInMillis();
//                Timestamp activityStartTimestamp = new Timestamp(timestampSecondsTime);
                activityDateTimeData.setActivityStartTime(activityStartTimeTimestamp);
                calendarTime.add(Calendar.SECOND, (int) activitySeconds[0]);
                Date activityEndTime = calendarTime.getTime();
                com.google.firebase.Timestamp activityEndTimestamp = new com.google.firebase.Timestamp(activityEndTime);
//                Timestamp activityEndTimestamp = new Timestamp(calendarTime.getTimeInMillis());
                activityDateTimeData.setActivityEndTime(activityEndTimestamp);
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                NavDirections actionAddActivityMain = AddActivityDateTimeFragmentDirections.actionAddActivityDateTimeFragmentToNavigationAddKick(activityDateTimeData);
                navController.navigate(actionAddActivityMain);
//                addActivityMainDashRelativeLayout.setVisibility(View.VISIBLE);
//                addActivityDateTimeRelativeLayout.setVisibility(View.GONE);
//                Toast.makeText(getContext(),DateTimeFormat.mediumTime().print(activityMain.getActivityStartTime().getTime()),Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
}
