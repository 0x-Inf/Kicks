package com.diablo.jayson.kicksv1.UI.AttendActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.diablo.jayson.kicksv1.R;

public class AlreadyAttendingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_attending);


        AttendActivityMainFragment alreadyAttending = new AttendActivityMainFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.already_attending_activity_fragment_container, alreadyAttending)
                .commit();
    }
}
