package com.sunritel.logcatcher.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.sunritel.logcatcher.R;
import com.sunritel.logcatcher.service.LogSaveService;
import com.sunritel.logcatcher.utils.PreferenceUtil;
import com.sunritel.logcatcher.utils.TagUtil;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private SwitchPreference log_auto_saving;
    private Preference log_keep_days;
    private ListPreference log_saving_location;
    private ListPreference log_level;
    private ListPreference log_format;
    private ListPreference log_buffer;
    private PreferenceUtil mPreferenceUtil;
    private Intent mIntent;
    public Activity mActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceUtil = new PreferenceUtil(mActivity);
        init();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

    @Override
    public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {

        if (log_auto_saving.equals(preference)) {
            Boolean isAutoSaving = (Boolean) newValue;
            Log.i(TagUtil.TAG, "onPreferenceChange --> isAutoSaving :" + isAutoSaving);
            if (mIntent == null) {
                mIntent = new Intent(mActivity, LogSaveService.class);
            }
            if (isAutoSaving) {
                mActivity.startActivity(mIntent);
            } else {
                mActivity.stopService(mIntent);
            }
        } else if (log_keep_days.equals(preference)) {
        } else if (log_saving_location.equals(preference)) {
        } else if (log_level.equals(preference)) {
        } else if (log_format.equals(preference)) {
        } else if (log_buffer.equals(preference)) {
        }
        return true;
    }

    private void init() {
        log_auto_saving = (SwitchPreference) findPreference(PreferenceUtil.AUTOSAVE_KEY);
        log_keep_days = (Preference) findPreference(PreferenceUtil.LOG_KEEP_DAYS_KEY);
        log_saving_location = (ListPreference) findPreference(PreferenceUtil.LOG_SAVE_LOCATION_KEY);
        log_level = (ListPreference) findPreference(PreferenceUtil.LEVEL_KEY);
        log_format = (ListPreference) findPreference(PreferenceUtil.FORMAT_KEY);
        log_buffer = (ListPreference) findPreference(PreferenceUtil.BUFFER_KEY);
        log_auto_saving.setOnPreferenceChangeListener(this);
        log_keep_days.setOnPreferenceChangeListener(this);
        log_saving_location.setOnPreferenceChangeListener(this);
        log_level.setOnPreferenceChangeListener(this);
        log_format.setOnPreferenceChangeListener(this);
        log_buffer.setOnPreferenceChangeListener(this);
    }
}