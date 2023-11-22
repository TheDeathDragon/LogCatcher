package com.sunritel.logcatcher.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat
import com.sunritel.logcatcher.LogCatcherApplication
import com.sunritel.logcatcher.R
import com.sunritel.logcatcher.service.LogService
import com.sunritel.logcatcher.utils.LogUtil
import com.sunritel.logcatcher.utils.PreferenceUtil

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener,
    OnSharedPreferenceChangeListener {
    private lateinit var logAutoSaving: SwitchPreferenceCompat
    private lateinit var logKeepDays: EditTextPreference
    private lateinit var logMaxSizePerFile: EditTextPreference
    private lateinit var logSavingLocation: ListPreference
    private lateinit var logLevel: ListPreference
    private lateinit var logFormat: ListPreference
    private lateinit var logBuffer: ListPreference
    private lateinit var mPreferenceUtil: PreferenceUtil
    private lateinit var mLogSaveServiceIntent: Intent
    private lateinit var mActivity: Activity
    private lateinit var mAppContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
        Log.d(LogUtil.TAG, "SettingsFragment --> onAttach")
        Toast.makeText(
            mActivity,
            "Please stop the service before changing the settings",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.log_settings_preferences, rootKey)
        Log.d(LogUtil.TAG, "SettingsFragment --> onCreatePreferences")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPreferenceUtil = PreferenceUtil(mActivity)
        init()
        setEnabled(!mPreferenceUtil.autoSaving)
        Log.d(LogUtil.TAG, "SettingsFragment --> onCreate")
    }

    override fun onResume() {
        super.onResume()
        setEnabled(!mPreferenceUtil.autoSaving)
        PreferenceManager.getDefaultSharedPreferences(mActivity)
            .registerOnSharedPreferenceChangeListener(this)
        Log.d(
            LogUtil.TAG,
            "SettingsFragment --> onResume : getAutoSaving : " + mPreferenceUtil.autoSaving
        )
    }

    override fun onPause() {
        super.onPause()
        PreferenceManager.getDefaultSharedPreferences(mActivity)
            .unregisterOnSharedPreferenceChangeListener(this)
        Log.d(
            LogUtil.TAG,
            "SettingsFragment --> onPause : getAutoSaving : " + mPreferenceUtil.autoSaving
        )
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        mAppContext = LogCatcherApplication.getAppContext()
        mLogSaveServiceIntent = LogCatcherApplication.getLogSavingServiceIntent()
        if (logAutoSaving == preference) {
            val isAutoSaving = newValue as Boolean
            Log.d(LogUtil.TAG, "onPreferenceChange --> isAutoSaving :$isAutoSaving")
            Log.d(LogUtil.TAG, "LogSavingService.isRunning: " + LogService.isRunning)
            if (LogService.isRunning) {
                Log.d(LogUtil.TAG, "LogSavingService.isRunning --> stopService")
                mAppContext.stopService(mLogSaveServiceIntent)
            }
            if (isAutoSaving) {
                Log.d(LogUtil.TAG, "isAutoSaving --> startService")
                mLogSaveServiceIntent = Intent(mAppContext, LogService::class.java)
                mAppContext.startService(mLogSaveServiceIntent)
                setEnabled(false)
            } else {
                setEnabled(true)
            }
        } else if (logKeepDays == preference) {
            Log.d(LogUtil.TAG, "onPreferenceChange --> logKeepDays")
        } else if (logMaxSizePerFile == preference) {
            Log.d(LogUtil.TAG, "onPreferenceChange --> logMaxSizePerFile")
        } else if (logSavingLocation == preference) {
            Log.d(LogUtil.TAG, "onPreferenceChange --> logSavingLocation")
        } else if (logLevel == preference) {
            Log.d(LogUtil.TAG, "onPreferenceChange --> logLevel")
        } else if (logFormat == preference) {
            Log.d(LogUtil.TAG, "onPreferenceChange --> logFormat")
        } else if (logBuffer == preference) {
            Log.d(LogUtil.TAG, "onPreferenceChange --> logBuffer")
        }
        return true
    }

    private fun init() {
        logAutoSaving = findPreference(PreferenceUtil.AUTO_SAVE_KEY)!!
        logKeepDays = findPreference(PreferenceUtil.LOG_KEEP_DAYS_KEY)!!
        logMaxSizePerFile = findPreference(PreferenceUtil.LOG_MAX_SIZE_PER_FILE)!!
        logSavingLocation = findPreference(PreferenceUtil.LOG_SAVE_LOCATION_KEY)!!
        logLevel = findPreference(PreferenceUtil.LEVEL_KEY)!!
        logFormat = findPreference(PreferenceUtil.FORMAT_KEY)!!
        logBuffer = findPreference(PreferenceUtil.BUFFER_KEY)!!
        logAutoSaving.onPreferenceChangeListener = this
        logKeepDays.onPreferenceChangeListener = this
        logMaxSizePerFile.onPreferenceChangeListener = this
        logSavingLocation.onPreferenceChangeListener = this
        logLevel.onPreferenceChangeListener = this
        logFormat.onPreferenceChangeListener = this
        logBuffer.onPreferenceChangeListener = this
    }

    private fun setEnabled(enabled: Boolean) {
        Log.d(LogUtil.TAG, "SettingsFragment --> setEnabled : $enabled")
        logKeepDays.isEnabled = enabled
        logMaxSizePerFile.isEnabled = enabled
        logSavingLocation.isEnabled = false
        logLevel.isEnabled = enabled
        logFormat.isEnabled = enabled
        logBuffer.isEnabled = enabled
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Log.d(
            LogUtil.TAG,
            "SettingsFragment --> onSharedPreferenceChanged , key : ${key ?: "NULL"}"
        )
    }
}