package com.sunritel.logcatcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.sunritel.logcatcher.LogCatcherApplication;
import com.sunritel.logcatcher.utils.MUtil;
import com.sunritel.logcatcher.utils.PreferenceUtil;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = Build.TYPE;
        Log.d(MUtil.TAG, "BootReceiver --> Build.TYPE: " + type);

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(MUtil.TAG, "BootReceiver --> onReceive: " + intent.getAction());
            Context mAppContext = LogCatcherApplication.getContext();
            PreferenceUtil mPreferenceUtil = new PreferenceUtil(mAppContext);
            if (!"user".equals(type) && mPreferenceUtil.getAutoSaving()) {
                Intent mServiceIntent = LogCatcherApplication.getLogSavingServiceIntent();
                mAppContext.startService(mServiceIntent);
            } else {
                Log.d(MUtil.TAG, "BootReceiver --> onReceive: Build.TYPE: " + type + "  isAutoSaving: " + mPreferenceUtil.getAutoSaving());
            }
        }
    }
}