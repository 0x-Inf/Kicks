package com.diablo.jayson.kicksv1.UI.AddKick;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TextInputEditText mTimePickerInput;


//    public TimePickerFragment() {
//        // Required empty public constructor
//    }

    public TimePickerFragment(TextInputEditText textInputEditText){
        this.mTimePickerInput = textInputEditText;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),this,hour,minute,true);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
//        MainActivity activity = (MainActivity) getActivity();
//        AddKickFragment fragment = new AddKickFragment();
//        assert  fragment != null;
//        fragment.processTimePickerResult(hour,minute);
        mTimePickerInput.setText(String.valueOf(hour) + ":" + String.valueOf(minute));
    }
}
