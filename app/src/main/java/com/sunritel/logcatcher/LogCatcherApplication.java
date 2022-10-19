package com.sunritel.logcatcher;


import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.sunritel.logcatcher.service.LogSavingService;

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
        mLogCatcherApplication = this;
        mLogSavingServiceIntent = new Intent(this, LogSavingService.class);
    }


}