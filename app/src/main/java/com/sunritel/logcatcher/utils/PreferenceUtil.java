package com.sunritel.logcatcher.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.sunritel.logcatcher.enums.Buffer;
import com.sunritel.logcatcher.enums.Format;
import com.sunritel.logcatcher.enums.Level;

public class PreferenceUtil {
    public static final String AUTOSAVE_KEY = "log_auto_saving";
    public static final String LOG_KEEP_DAYS_KEY = "log_keep_days";
    public static final String LOG_MAX_SIZE_PER_FILE = "log_max_size_per_file";
    public static final String LOG_SAVE_LOCATION_KEY = "log_saving_location";
    public static final String LEVEL_KEY = "log_level";
    public static final String FORMAT_KEY = "log_format";
    public static final String BUFFER_KEY = "log_buffer";

    public static final String DEFAULT_VALUE = "DEFAULT";
    public static final String DEFAULT_LOG_KEEP_DAYS = "7";
    public static final String DEFAULT_LOG_MAX_SIZE = "300";
    public static final String DEFAULT_LOG_DIR = "oem_log";

    public static final String LOG_SAVE_LOCATION_INTERNAL = "Internal";
    public static final String LOG_SAVE_LOCATION_EXTERNAL = "External";


    private final SharedPreferences sharedPrefs;

    public PreferenceUtil(Context context) {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private String getString(String key, String def) {
        return sharedPrefs.getString(key, def);
    }

    private void setString(String key, String val) {
        SharedPreferences.Editor e = sharedPrefs.edit();
        e.putString(key, val);
        e.apply();
    }

    private boolean getBoolean(String key) {
        return sharedPrefs.getBoolean(key, false);
    }

    private void setBoolean(String key, boolean val) {
        SharedPreferences.Editor e = sharedPrefs.edit();
        e.putBoolean(key, val);
        e.apply();
    }

    public boolean getAutoSaving() {
        return getBoolean(AUTOSAVE_KEY);
    }

    public void setAutoSaving(boolean isAutoSaving) {
        setBoolean(AUTOSAVE_KEY, isAutoSaving);
    }

    public int getLogKeepDays() {
        return Integer.parseInt(getString(LOG_KEEP_DAYS_KEY, DEFAULT_LOG_KEEP_DAYS));
    }

    public void setLogKeepDays(int days) {
        setString(LOG_KEEP_DAYS_KEY, String.valueOf(days));
    }

    public int getLogMaxSize() {
        return Integer.parseInt(getString(LOG_MAX_SIZE_PER_FILE, DEFAULT_LOG_MAX_SIZE));
    }

    public void setLogMaxSize(int size) {
        setString(LOG_MAX_SIZE_PER_FILE, String.valueOf(size));
    }

    public String getLogSaveLocation() {
        return getString(LOG_SAVE_LOCATION_KEY, DEFAULT_LOG_DIR);
    }

    public void setLogSaveLocation(String location) {
        setString(LOG_SAVE_LOCATION_KEY, location);
    }

    public Level getLevel() {
        return Level.valueOf(getString(LEVEL_KEY, DEFAULT_VALUE));
    }

    public void setLevel(Level level) {
        setString(LEVEL_KEY, level.toString());
    }

    public Format getFormat() {
        return Format.valueOf(getString(FORMAT_KEY, DEFAULT_VALUE));
    }

    public void setFormat(Format format) {
        setString(FORMAT_KEY, format.toString());
    }

    public Buffer getBuffer() {
        return Buffer.valueOf(getString(BUFFER_KEY, DEFAULT_VALUE));
    }

    public void setBuffer(Buffer buffer) {
        setString(BUFFER_KEY, buffer.toString());
    }

}