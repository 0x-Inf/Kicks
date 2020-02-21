package com.diablo.jayson.kicksv1.UI.AddKick;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.Home.FeaturedFragment;
import com.edmodo.rangebar.RangeBar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddKickFragment extends Fragment {

    private static final String TAG = AddKickFragment.class.getSimpleName();
    private AddKickViewModel mViewModel;
    private Button mDatePickerButton;
    private Button mTimePickerButton;
    private TextInputEditText mTimePickerInput,mDatePickerInput;
    private Button mPickALocationButton;
    private SeekBar mSeekBar;
    private DatabaseReference mDatabase;
    private final static int AUTOCOMPLETE_REQUEST_CODE = 1;


    public static AddKickFragment newInstance() {
        return new AddKickFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.add_kick_fragment, container, false);
        mTimePickerInput = root.findViewById(R.id.time_picker_input);
        mDatePickerInput = root.findViewById(R.id.date_picker_input);

        mSeekBar = root.findViewById(R.id.seekBar);
        final TextInputEditText seekBarProgress = root.findViewById(R.id.minPeopleInputEditText);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        String[] TAGS = new String[]{"Fishing", "Skating", "Puzzles", "Karting", "Fishing", "Skating", "Puzzles", "Karting"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.ropdown_menu_popup_item,
                        TAGS);

        MultiAutoCompleteTextView editTextFilledExposedDropdown = root.findViewById(R.id.tagsAutoCompleteTextView);
        editTextFilledExposedDropdown.setAdapter(adapter);
        editTextFilledExposedDropdown.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        mDatePickerInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        mTimePickerInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });


//        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                if (i > 0 && seekBarProgress.getText() == null) {
//                    seekBarProgress.setText(i);
//                } else if (i > 0 && seekBarProgress.getText() != null) {
//                    seekBarProgress.setText(String.valueOf(Integer.parseInt(seekBarProgress.getText().toString()) + i));
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddKickViewModel.class);
        // TODO: Use the ViewModel
    }


    public void showDatePicker() {
        DialogFragment newFragment = new DatePickerFragment(mDatePickerInput);
        newFragment.show(getChildFragmentManager(), "datePicker");
    }

    public void processDatePickerResult(int year, int month, int day) {

    }


    public void showTimePicker() {
        DialogFragment newFragment = new TimePickerFragment(mTimePickerInput);
        newFragment.show(getChildFragmentManager(), "timePicker");
    }
    public void processTimePickerResult(int hour, int minute){
        String hour_string = Integer.toString(hour);
        String minute_string = Integer.toString(minute);
        String timeMessage = (hour_string+":"+minute_string);
        mTimePickerInput.setText(timeMessage);
        Log.e(TAG,timeMessage);

    }

    private void addNewKick() {


    }

    private void writenewKickToDb(String kickTitle, String kickLocation, String kickDate, String kickTime,
                                  String alreadyAttendingPeeps, String requiredPeeps, String imageUrl) {
        String key = mDatabase.child("kicks").push().getKey();
        Activity activity = new Activity(kickTitle, kickTime, kickDate, kickLocation,
                alreadyAttendingPeeps, requiredPeeps, imageUrl);


    }


}
