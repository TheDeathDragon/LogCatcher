package com.sunritel.logcatcher.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.sunritel.logcatcher.LogCatcherApplication
import com.sunritel.logcatcher.utils.LogUtil
import com.sunritel.logcatcher.utils.PreferenceUtil

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val type = Build.TYPE
        Log.d(LogUtil.TAG, "BootReceiver --> Build.TYPE: $type")
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d(LogUtil.TAG, "BootReceiver --> onReceive: " + intent.action)
            val mAppContext = LogCatcherApplication.getAppContext()
            val mPreferenceUtil = PreferenceUtil(mAppContext)
            if (mPreferenceUtil.autoSaving) {
                val mServiceIntent = LogCatcherApplication.getLogSavingServiceIntent()
                mAppContext.startService(mServiceIntent)
            } else {
                Log.d(
                    LogUtil.TAG,
                    "BootReceiver --> onReceive: Build.TYPE: " + type + "  isAutoSaving: " + mPreferenceUtil.autoSaving
                )
            }
        }
    }
}