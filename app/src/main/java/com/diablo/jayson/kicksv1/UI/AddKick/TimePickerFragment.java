package com.diablo.jayson.kicksv1.UI.AddKick;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.diablo.jayson.kicksv1.MainActivity;
import com.diablo.jayson.kicksv1.R;
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
        MainActivity activity = (MainActivity) getActivity();
        AddKickFragment fragment = new AddKickFragment();
        assert  fragment != null;
//        fragment.processTimePickerResult(hour,minute);
        mTimePickerInput.setText(Integer.toString(hour) + ":" + Integer.toString(minute));
    }
}
