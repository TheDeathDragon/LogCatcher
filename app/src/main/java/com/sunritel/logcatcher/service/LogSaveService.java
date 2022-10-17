package com.sunritel.logcatcher.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.sunritel.logcatcher.utils.LogEngine;
import com.sunritel.logcatcher.utils.TagUtil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LogSaveService extends Service {

    private ScheduledExecutorService mService;
    private LogEngine mLogEngine;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Runnable runnable = () -> {
            if (mLogEngine != null) {
                mLogEngine.stop();
            }
            mLogEngine = new LogEngine(LogSaveService.this);
            mLogEngine.start();
        };
        mService = Executors.newSingleThreadScheduledExecutor();
        mService.scheduleAtFixedRate(runnable, 0, 24, TimeUnit.HOURS);
        Log.d(TagUtil.TAG, "LogSaveService --> onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public ComponentName startService(Intent service) {
        Log.d(TagUtil.TAG, "LogSaveService --> startService");
        return super.startService(service);
    }

    @Override
    public boolean stopService(Intent name) {
        if (mLogEngine != null) {
            mLogEngine = null;
        }
        Log.d(TagUtil.TAG, "LogSaveService --> stopService");
        return super.stopService(name);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TagUtil.TAG, "LogSaveService --> onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TagUtil.TAG, "LogSaveService --> onDestroy");
        // 不可以再submit新的task,已经submit的将继续执行
        mLogEngine.stop();
        mService.shutdown();
    }
}