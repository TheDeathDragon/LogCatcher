package com.sunritel.logcatcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.sunritel.logcatcher.service.LogSaveService;
import com.sunritel.logcatcher.utils.TagUtil;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = Build.TYPE;
        Log.d(TagUtil.TAG, "BootReceiver --> Build.TYPE: " + type);

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TagUtil.TAG, "BootReceiver --> onReceive: " + intent.getAction());
            // 默认user版本不启用
            if (!"user".equals(type)) {
                Intent svcIntent = new Intent(context, LogSaveService.class);
                context.startService(svcIntent);
            }
        }
    }
}