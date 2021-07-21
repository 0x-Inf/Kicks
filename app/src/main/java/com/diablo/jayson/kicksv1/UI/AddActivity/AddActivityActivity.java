package com.diablo.jayson.kicksv1.UI.AddActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.diablo.jayson.kicksv1.R;
import com.diablo.jayson.kicksv1.UI.AddActivity.fragments.AddActivityFragment;

public class AddActivityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity);
        if (savedInstanceState != null) {
            getSupportFragmentManager().executePendingTransactions();
            Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.signupfragment_container);
            if (fragmentById != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(fragmentById)
                        .commit();
            }
        }

        AddActivityFragment addActivityFragment = new AddActivityFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.add_kick_fragment_container, addActivityFragment)
                .commit();
    }
}
