package com.sunritel.logcatcher.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.sunritel.logcatcher.LogCatcherApplication
import com.sunritel.logcatcher.utils.LogEngine
import com.sunritel.logcatcher.utils.LogUtil
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LogService : Service() {

    init {
        mLogEngine = LogEngine(mAppContext)
        mService = Executors.newSingleThreadExecutor()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val runnable = Runnable {
            mLogEngine.stop()
            mLogEngine = LogEngine(mAppContext)
            mLogEngine.start()
        }
        mService.execute(runnable)
        isRunning = true
        Log.d(LogUtil.TAG, "LogService --> onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun startService(service: Intent): ComponentName? {
        Log.d(LogUtil.TAG, "LogService --> startService")
        return super.startService(service)
    }

    override fun stopService(name: Intent): Boolean {
        Log.d(LogUtil.TAG, "LogService --> stopService")
        return super.stopService(name)
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d(LogUtil.TAG, "LogService --> onBind")
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LogUtil.TAG, "LogService --> onDestroy")
        mLogEngine.stop()
        isRunning = false
        mService.shutdown()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(LogUtil.TAG, "LogService --> onCreate")
    }

    companion object {
        private lateinit var mService: ExecutorService
        private lateinit var mLogEngine: LogEngine
        private var mAppContext: Context = LogCatcherApplication.getAppContext()
        var isRunning = false
    }
}