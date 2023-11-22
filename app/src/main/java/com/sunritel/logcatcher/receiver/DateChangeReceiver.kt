package com.sunritel.logcatcher.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.sunritel.logcatcher.LogCatcherApplication
import com.sunritel.logcatcher.service.LogService.Companion.isRunning
import com.sunritel.logcatcher.utils.LogUtil
import com.sunritel.logcatcher.utils.PreferenceUtil

class DateChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val type = Build.TYPE
        Log.d(LogUtil.TAG, "DateChangeReceiver --> Build.TYPE: $type")
        if (intent.action == Intent.ACTION_DATE_CHANGED) {
            Log.d(LogUtil.TAG, "DateChangeReceiver --> onReceive: " + intent.action)
            val mAppContext = LogCatcherApplication.getAppContext()
            val mPreferenceUtil = PreferenceUtil(mAppContext)
            if (mPreferenceUtil.autoSaving) {
                val mServiceIntent = LogCatcherApplication.getLogSavingServiceIntent()
                if (isRunning) {
                    mAppContext.stopService(mServiceIntent)
                    mAppContext.startService(mServiceIntent)
                }
            } else {
                Log.d(
                    LogUtil.TAG,
                    "BootReceiver --> onReceive: Build.TYPE: " + type + "  isAutoSaving: " + mPreferenceUtil.autoSaving
                )
            }
        }
    }
}