package com.sunritel.logcatcher.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.sunritel.logcatcher.enums.Buffer
import com.sunritel.logcatcher.enums.Format
import com.sunritel.logcatcher.enums.Level

class PreferenceUtil(context: Context?) {
    private val sharedPrefs: SharedPreferences

    init {
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context!!)
    }

    private fun getString(key: String, def: String): String? {
        return sharedPrefs.getString(key, def)
    }

    private fun setString(key: String, `val`: String) {
        val e = sharedPrefs.edit()
        e.putString(key, `val`)
        e.apply()
    }

    private fun getBoolean(key: String): Boolean {
        return sharedPrefs.getBoolean(key, true)
    }

    private fun setBoolean(key: String, `val`: Boolean) {
        val e = sharedPrefs.edit()
        e.putBoolean(key, `val`)
        e.apply()
    }

    var autoSaving: Boolean
        get() = getBoolean(AUTO_SAVE_KEY)
        set(isAutoSaving) {
            setBoolean(AUTO_SAVE_KEY, isAutoSaving)
        }
    var logKeepDays: Int
        get() = getString(
            LOG_KEEP_DAYS_KEY, DEFAULT_LOG_KEEP_DAYS
        )!!.toInt()
        set(days) {
            setString(LOG_KEEP_DAYS_KEY, days.toString())
        }
    var logMaxSize: Int
        get() = getString(
            LOG_MAX_SIZE_PER_FILE, DEFAULT_LOG_MAX_SIZE
        )!!.toInt()
        set(size) {
            setString(LOG_MAX_SIZE_PER_FILE, size.toString())
        }
    val logSaveLocation: String?
        get() = getString(LOG_SAVE_LOCATION_KEY, DEFAULT_LOG_LOCATION)

    fun setLogSaveLocation(location: String) {
        setString(LOG_SAVE_LOCATION_KEY, location)
    }

    var level: Level
        get() = Level.valueOf(getString(LEVEL_KEY, DEFAULT_VALUE)!!)
        set(level) {
            setString(LEVEL_KEY, level.toString())
        }
    var format: Format
        get() = Format.valueOf(getString(FORMAT_KEY, DEFAULT_VALUE)!!)
        set(format) {
            setString(FORMAT_KEY, format.toString())
        }
    var buffer: Buffer
        get() = Buffer.valueOf(getString(BUFFER_KEY, DEFAULT_VALUE)!!)
        set(buffer) {
            setString(BUFFER_KEY, buffer.toString())
        }

    companion object {
        const val AUTO_SAVE_KEY = "log_auto_saving"
        const val LOG_KEEP_DAYS_KEY = "log_keep_days"
        const val LOG_MAX_SIZE_PER_FILE = "log_max_size_per_file"
        const val LOG_SAVE_LOCATION_KEY = "log_saving_location"
        const val LEVEL_KEY = "log_level"
        const val FORMAT_KEY = "log_format"
        const val BUFFER_KEY = "log_buffer"
        const val DEFAULT_VALUE = "DEFAULT"
        const val DEFAULT_LOG_KEEP_DAYS = "7"
        const val DEFAULT_LOG_MAX_SIZE = "300"
        const val DEFAULT_LOG_LOCATION = "EXTERNAL"
    }
}