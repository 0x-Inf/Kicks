package com.diablo.jayson.kicksv1.UI.AttendActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.diablo.jayson.kicksv1.R;

public class AttendActivityActivity extends AppCompatActivity {

    private AttendActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend_activity);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        String activityId = bundle.getString("activityId");

        viewModel = new ViewModelProvider(this).get(AttendActivityViewModel.class);

        viewModel.setActivityId(activityId);

        if (savedInstanceState != null) {
            getSupportFragmentManager().executePendingTransactions();
            Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.attend_activity_fragment_container);
            if (fragmentById != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(fragmentById)
                        .commit();
            }
        }

        ConfirmAttendFragment confirmAttend = new ConfirmAttendFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.attend_activity_fragment_container, confirmAttend)
                .commit();


        Toast.makeText(this, activityId, Toast.LENGTH_LONG).show();
    }
}
