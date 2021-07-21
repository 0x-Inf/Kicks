package com.diablo.jayson.kicksv1.UI.AddActivity.fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private TextInputEditText mDatePickerInput;
    final Calendar c = Calendar.getInstance();


    public DatePickerFragment(TextInputEditText textInputEditText) {
        // Required empty public constructor
        this.mDatePickerInput = textInputEditText;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//        AddKick3Fragment fragment = (AddKick3Fragment) getActivity();
//        fragment.processDatePickerResult(year,month,day);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String selectedDate = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH).format(c.getTime());

        mDatePickerInput.setText(selectedDate);
//        Objects.requireNonNull(getTargetFragment()).onActivityResult(
//                getTargetRequestCode(),
//                Activity.RESULT_OK,
//                new Intent().putExtra("selectedDate", selectedDate)
//        );
    }
}
