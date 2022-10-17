package com.sunritel.logcatcher.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.sunritel.logcatcher.utils.LogEngine;

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

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 不可以再submit新的task,已经submit的将继续执行
        mService.shutdown();
    }
}