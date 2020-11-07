package com.diablo.jayson.kicksv1.UI.Settings;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.diablo.jayson.kicksv1.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // my_child_toolbar is defined in the layout file
        Toolbar mySettingsToolbar =
                (Toolbar) findViewById(R.id.my_settings_toolbar);
        setSupportActionBar(mySettingsToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Settings");

        if (savedInstanceState != null) {
            getSupportFragmentManager().executePendingTransactions();
            Fragment fragmentById = getSupportFragmentManager().findFragmentById(R.id.setting_fragment_container);
            if (fragmentById != null) {
                getSupportFragmentManager().beginTransaction()
                        .remove(fragmentById)
                        .commit();
            }
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.setting_fragment_container, new SettingsFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
