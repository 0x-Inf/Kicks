package com.diablo.jayson.kicksv1.UI.AddKick;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.diablo.jayson.kicksv1.Models.Activity;
import com.diablo.jayson.kicksv1.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddKickFragment extends Fragment {

    private AddKickViewModel mViewModel;
    private Button mDatePickerButton;
    private Button mTimePickerButton;
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
        mDatePickerButton = root.findViewById(R.id.date_picker_button);
        mTimePickerButton = root.findViewById(R.id.time_picker_button);

        mSeekBar = root.findViewById(R.id.seekBar);
        final TextView seekBarProgress = root.findViewById(R.id.seekBarProgress);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        mTimePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (i > 0){
                    seekBarProgress.setText(i+" people");
                }else {
                    seekBarProgress.setText("Not set");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddKickViewModel.class);
        // TODO: Use the ViewModel
    }


    public void showDatePicker(){
        DialogFragment newFragment = new  DatePickerFragment();
        newFragment.show(getChildFragmentManager(),"datePicker");
    }

    public void processDatePickerResult(int year, int month, int day){

    }

    public void showTimePicker(){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getChildFragmentManager(),"timePicker");
    }

    private void addNewKick(){



    }

    private void writenewKickToDb(String kickTitle, String kickLocation,String kickDate,String kickTime,
                                  String alreadyAttendingPeeps, String requiredPeeps,String imageUrl){
        String key  = mDatabase.child("kicks").push().getKey();
        Activity activity = new Activity(kickTitle,kickTime,kickDate,kickLocation,
                alreadyAttendingPeeps,requiredPeeps,imageUrl);



    }


}
