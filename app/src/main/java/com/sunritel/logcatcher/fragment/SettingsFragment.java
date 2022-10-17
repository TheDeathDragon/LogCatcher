package com.sunritel.logcatcher.fragment;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.sunritel.logcatcher.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

}