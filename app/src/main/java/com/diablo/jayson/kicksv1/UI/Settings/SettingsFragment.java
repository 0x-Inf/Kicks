package com.diablo.jayson.kicksv1.UI.Settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.diablo.jayson.kicksv1.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
