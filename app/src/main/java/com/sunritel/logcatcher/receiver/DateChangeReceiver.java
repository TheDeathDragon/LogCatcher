package com.sunritel.logcatcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.sunritel.logcatcher.LogCatcherApplication;
import com.sunritel.logcatcher.service.LogSavingService;
import com.sunritel.logcatcher.utils.MUtil;
import com.sunritel.logcatcher.utils.PreferenceUtil;

public class DateChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = Build.TYPE;
        Log.d(MUtil.TAG, "DateChangeReceiver --> Build.TYPE: " + type);

        if (intent.getAction().equals(Intent.ACTION_DATE_CHANGED)) {
            Log.d(MUtil.TAG, "DateChangeReceiver --> onReceive: " + intent.getAction());
            Context mAppContext = LogCatcherApplication.getContext();
            PreferenceUtil mPreferenceUtil = new PreferenceUtil(mAppContext);
//            if (!"user".equals(type) && mPreferenceUtil.getAutoSaving()) {
            if (mPreferenceUtil.getAutoSaving()) {
                Intent mServiceIntent = LogCatcherApplication.getLogSavingServiceIntent();
                if (LogSavingService.isRunning()) {
                    mAppContext.stopService(mServiceIntent);
                    mAppContext.startService(mServiceIntent);
                }
            } else {
                Log.d(MUtil.TAG, "BootReceiver --> onReceive: Build.TYPE: " + type + "  isAutoSaving: " + mPreferenceUtil.getAutoSaving());
            }
        }
    }

}