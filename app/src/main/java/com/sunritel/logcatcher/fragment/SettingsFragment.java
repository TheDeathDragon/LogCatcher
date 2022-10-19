package com.sunritel.logcatcher.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.sunritel.logcatcher.LogCatcherApplication;
import com.sunritel.logcatcher.R;
import com.sunritel.logcatcher.service.LogSavingService;
import com.sunritel.logcatcher.utils.MUtil;
import com.sunritel.logcatcher.utils.PreferenceUtil;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private SwitchPreferenceCompat log_auto_saving;
    private EditTextPreference log_keep_days;
    private EditTextPreference log_max_size_per_file;
    private ListPreference log_saving_location;
    private ListPreference log_level;
    private ListPreference log_format;
    private ListPreference log_buffer;
    private PreferenceUtil mPreferenceUtil;
    private Intent mLogSaveServiceIntent;
    public Activity mActivity;
    private Context mAppContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
        Log.d(MUtil.TAG, "SettingsFragment --> onAttach");
        Toast.makeText(mActivity, "Please stop the service before changing the settings", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        Log.d(MUtil.TAG, "SettingsFragment --> onCreatePreferences");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceUtil = new PreferenceUtil(mActivity);
        init();
        setEnabled(!mPreferenceUtil.getAutoSaving());
        Log.d(MUtil.TAG, "SettingsFragment --> onCreate");
    }

    @Override
    public void onResume() {
        super.onResume();
        setEnabled(!mPreferenceUtil.getAutoSaving());
        PreferenceManager.getDefaultSharedPreferences(mActivity).registerOnSharedPreferenceChangeListener(this);
        Log.d(MUtil.TAG, "SettingsFragment --> onResume : getAutoSaving : " + mPreferenceUtil.getAutoSaving());
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(mActivity).unregisterOnSharedPreferenceChangeListener(this);
        Log.d(MUtil.TAG, "SettingsFragment --> onPause : getAutoSaving : " + mPreferenceUtil.getAutoSaving());
    }

    @Override
    public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {

        mAppContext = LogCatcherApplication.getContext();
        mLogSaveServiceIntent = LogCatcherApplication.getLogSavingServiceIntent();

        if (log_auto_saving.equals(preference)) {
            Boolean isAutoSaving = (Boolean) newValue;
            Log.d(MUtil.TAG, "onPreferenceChange --> isAutoSaving :" + isAutoSaving);
            Log.d(MUtil.TAG, "LogSavingService.isRunning: " + LogSavingService.isRunning());
            if (LogSavingService.isRunning()) {
                Log.d(MUtil.TAG, "LogSavingService.isRunning --> stopService");
                mAppContext.stopService(mLogSaveServiceIntent);
                mLogSaveServiceIntent = null;
            }
            if (isAutoSaving) {
                Log.d(MUtil.TAG, "isAutoSaving --> startService");
                mLogSaveServiceIntent = new Intent(mAppContext, LogSavingService.class);
                mAppContext.startService(mLogSaveServiceIntent);
                setEnabled(false);
            } else {
                setEnabled(true);
            }
        } else if (log_keep_days.equals(preference)) {
        } else if (log_max_size_per_file.equals(preference)) {
        } else if (log_saving_location.equals(preference)) {
        } else if (log_level.equals(preference)) {
        } else if (log_format.equals(preference)) {
        } else if (log_buffer.equals(preference)) {
        }
        return true;
    }

    private void init() {
        log_auto_saving = findPreference(PreferenceUtil.AUTOSAVE_KEY);
        log_keep_days = findPreference(PreferenceUtil.LOG_KEEP_DAYS_KEY);
        log_max_size_per_file = findPreference(PreferenceUtil.LOG_MAX_SIZE_PER_FILE);
        log_saving_location = findPreference(PreferenceUtil.LOG_SAVE_LOCATION_KEY);
        log_level = findPreference(PreferenceUtil.LEVEL_KEY);
        log_format = findPreference(PreferenceUtil.FORMAT_KEY);
        log_buffer = findPreference(PreferenceUtil.BUFFER_KEY);
        log_auto_saving.setOnPreferenceChangeListener(this);
        log_keep_days.setOnPreferenceChangeListener(this);
        log_max_size_per_file.setOnPreferenceChangeListener(this);
        log_saving_location.setOnPreferenceChangeListener(this);
        log_level.setOnPreferenceChangeListener(this);
        log_format.setOnPreferenceChangeListener(this);
        log_buffer.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    private void setEnabled(boolean enabled) {
        Log.d(MUtil.TAG, "SettingsFragment --> setEnabled : " + enabled);
        log_keep_days.setEnabled(enabled);
        log_max_size_per_file.setEnabled(enabled);
        log_saving_location.setEnabled(false);
        log_level.setEnabled(enabled);
        log_format.setEnabled(enabled);
        log_buffer.setEnabled(enabled);
    }
}