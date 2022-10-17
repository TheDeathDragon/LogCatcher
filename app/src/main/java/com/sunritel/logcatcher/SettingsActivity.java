package com.sunritel.logcatcher;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sunritel.logcatcher.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.log_catcher_settings, new SettingsFragment())
                    .commit();
        }
    }
}