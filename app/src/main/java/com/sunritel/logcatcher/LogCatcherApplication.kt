package com.sunritel.logcatcher

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.sunritel.logcatcher.receiver.DateChangeReceiver
import com.sunritel.logcatcher.service.LogService
import com.sunritel.logcatcher.utils.LogUtil

class LogCatcherApplication : Application() {
    override fun onCreate() {
        instance = this
        mAppContext = applicationContext
        super.onCreate()
        Log.d(LogUtil.TAG, "LogCatcherApplication --> onCreate")
        logSavingServiceIntent = Intent(this, LogService::class.java)
        mAppContext.applicationContext.registerReceiver(
            DateChangeReceiver(),
            IntentFilter(Intent.ACTION_DATE_CHANGED)
        )
    }

    companion object {
        private lateinit var instance: LogCatcherApplication
        private lateinit var logSavingServiceIntent: Intent
        private lateinit var mAppContext: Context
        fun getLogSavingServiceIntent(): Intent {
            return logSavingServiceIntent
        }

        fun getAppContext(): Context {
            return mAppContext
        }
    }
}