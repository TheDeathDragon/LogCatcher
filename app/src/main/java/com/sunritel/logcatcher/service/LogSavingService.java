package com.sunritel.logcatcher.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.sunritel.logcatcher.LogCatcherApplication;
import com.sunritel.logcatcher.utils.LogEngine;
import com.sunritel.logcatcher.utils.MUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogSavingService extends Service {

    private ExecutorService mService;
    private LogEngine mLogEngine;
    private static boolean isRunning = false;
    private Context mAppContext;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Runnable runnable = () -> {
            if (mLogEngine != null) {
                mLogEngine.stop();
            }
            mAppContext = LogCatcherApplication.getContext();
            mLogEngine = new LogEngine(mAppContext);
            mLogEngine.start();
        };
        mService = Executors.newSingleThreadExecutor();
        mService.execute(runnable);
        isRunning = true;
        Log.d(MUtil.TAG, "LogSavingService --> onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public ComponentName startService(Intent service) {
        Log.d(MUtil.TAG, "LogSavingService --> startService");
        return super.startService(service);
    }

    @Override
    public boolean stopService(Intent name) {
        if (mLogEngine != null) {
            mLogEngine = null;
        }
        Log.d(MUtil.TAG, "LogSavingService --> stopService");
        return super.stopService(name);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(MUtil.TAG, "LogSavingService --> onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(MUtil.TAG, "LogSavingService --> onDestroy");
        mLogEngine.stop();
        isRunning = false;
        mService.shutdown();
    }

    public static boolean isRunning() {
        return isRunning;
    }
}