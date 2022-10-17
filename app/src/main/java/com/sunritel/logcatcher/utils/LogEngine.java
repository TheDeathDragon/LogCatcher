package com.sunritel.logcatcher.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LogEngine {

    private final PreferenceUtil mPrefs;
    private final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private static final File mRootPath;
    private WriteToFileThread mThread;

    private static boolean result;

    static {
        mRootPath = new File(Environment.getExternalStorageDirectory().getPath(), "oem_log");
        if (!mRootPath.exists()) {
            result = mRootPath.mkdir();
            Log.d(TagUtil.TAG, "LogEngine --> mkdir logcat result: " + result);
        }
    }

    public LogEngine(Context context) {
        mPrefs = new PreferenceUtil(context);
    }

    public void start() {
        try {
            deleteFileIfNeed();
            Log.d(TagUtil.TAG, "LogEngine --> LogEngine is start: format: " +
                    mPrefs.getFormat().getValue() +
                    "  buffer: " +
                    mPrefs.getBuffer().getValue() +
                    "  level: " +
                    mPrefs.getLevel().getValue());
/*			String[] cmd = { "logcat",
                    "-v",
                    mPrefs.getFormat().getValue(),
                    "-b",
                    mPrefs.getBuffer().getValue(),
                    "*:" + mPrefs.getLevel() + "\n"
            };*/
            String[] cmd = {"logcat"};

            File file = new File(mRootPath + "/" + dataFormat.format(new Date()) + ".log");
            result = file.createNewFile();
            Log.d(TagUtil.TAG, "LogEngine --> log file create result: " + result);

            Process process = Runtime.getRuntime().exec(cmd);
            mThread = new WriteToFileThread(process.getInputStream(), file);
            mThread.stopSelf(false);
            mThread.start();
        } catch (IOException e) {
            Log.e(TagUtil.TAG, "LogEngine --> error create log file", e);
        }
    }

    private void deleteFileIfNeed() {
        File[] files = mRootPath.listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            try {
                Calendar calendar = Calendar.getInstance();
                String fileName = f.getName().subSequence(0, f.getName().indexOf(".")).toString();
                calendar.setTime(dataFormat.parse(fileName));
                long timeInMillis1 = calendar.getTimeInMillis();
                // 写文件的时间
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -7);
                // 七天前的时间
                long timeInMillis2 = cal.getTimeInMillis();

                // 文件超过七天,删除文件
                if ((timeInMillis2 - timeInMillis1) > 0) {
                    Log.d(TagUtil.TAG, "LogEngine --> delete file: " + f.getName());
                    result = f.delete();
                    Log.d(TagUtil.TAG, "LogEngine --> delete file result: " + result);

                }
                Log.d(TagUtil.TAG, "LogEngine --> timeInMillis1: " + timeInMillis1 + "  timeInMillis2: " + timeInMillis2 + "   " + ((timeInMillis2 - timeInMillis1)));
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d(TagUtil.TAG, "LogEngine --> delete file error: " + e.getMessage());
            }
        }
    }

    public void stop() {
        mThread.stopSelf(true);
        Log.d(TagUtil.TAG, "LogEngine is stop");
    }

}
