package com.sunritel.logcatcher;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.sunritel.logcatcher.receiver.DateChangeReceiver;
import com.sunritel.logcatcher.service.LogSavingService;
import com.sunritel.logcatcher.utils.MUtil;

public class LogCatcherApplication extends Application {

    private static Application mLogCatcherApplication;
    private static Intent mLogSavingServiceIntent;

    public static Context getContext() {
        return mLogCatcherApplication.getApplicationContext();
    }

    public static Intent getLogSavingServiceIntent() {
        return mLogSavingServiceIntent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(MUtil.TAG, "LogCatcherApplication --> onCreate");
        mLogCatcherApplication = this;
        mLogSavingServiceIntent = new Intent(this, LogSavingService.class);
        mLogCatcherApplication.getApplicationContext().registerReceiver(new DateChangeReceiver(), new IntentFilter(Intent.ACTION_DATE_CHANGED));
    }

}