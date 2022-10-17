package com.sunritel.logcatcher.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.sunritel.logcatcher.enums.Buffer;
import com.sunritel.logcatcher.enums.Format;
import com.sunritel.logcatcher.enums.Level;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PreferenceUtil {
    public static final String AUTOSAVE_KEY = "log_auto_saving";
    public static final String LOG_KEEP_DAYS_KEY = "log_keep_days";
    public static final String LOG_SAVE_LOCATION_KEY = "log_saving_location";
    public static final String LEVEL_KEY = "log_level";
    public static final String FORMAT_KEY = "log_format";
    public static final String BUFFER_KEY = "log_buffer";
    public static final String FILTER_PATTERN_KEY = "filterPattern";


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
        return Integer.parseInt(getString(LOG_KEEP_DAYS_KEY, "7"));
    }

    public void setLogKeepDays(int days) {
        setString(LOG_KEEP_DAYS_KEY, String.valueOf(days));
    }

    public String getLogSaveLocation() {
        return getString(LOG_SAVE_LOCATION_KEY, "oem_log");
    }

    public void setLogSaveLocation(String location) {
        setString(LOG_SAVE_LOCATION_KEY, location);
    }

    public Level getLevel() {
        return Level.valueOf(getString(LEVEL_KEY, "V"));
    }

    public void setLevel(Level level) {
        setString(LEVEL_KEY, level.toString());
    }

    public Format getFormat() {
        String f = getString(FORMAT_KEY, "BRIEF");
        if (!f.equals(f.toUpperCase())) {
            f = f.toUpperCase();
            setString(FORMAT_KEY, f);
        }
        return Format.valueOf(f);
    }

    public void setFormat(Format format) {
        setString(FORMAT_KEY, format.toString());
    }

    public Buffer getBuffer() {
        return Buffer.valueOf(getString(BUFFER_KEY, "MAIN"));
    }

    public void setBuffer(Buffer buffer) {
        setString(BUFFER_KEY, buffer.toString());
    }

    public String getFilter() {
        return getString("filter", null);
    }

    public Pattern getFilterPattern() {
        if (!isFilterPattern()) {
            return null;
        }

        String p = getString("filter", null);
        if (p == null) {
            return null;
        }
        try {
            return Pattern.compile(p, Pattern.CASE_INSENSITIVE);
        } catch (PatternSyntaxException e) {
            setString("filter", null);
            Log.w(TagUtil.TAG, "PreferenceUtil --> invalid filter pattern found, cleared");
            return null;
        }
    }

    public void setFilter(String filter) {
        setString("filter", filter);
    }

    public boolean isFilterPattern() {
        return getBoolean(FILTER_PATTERN_KEY);
    }

    public void setFilterPattern(boolean filterPattern) {
        setBoolean(FILTER_PATTERN_KEY, filterPattern);
    }

}