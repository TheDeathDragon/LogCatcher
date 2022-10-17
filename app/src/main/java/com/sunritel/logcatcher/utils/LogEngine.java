package com.sunritel.logcatcher.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LogEngine {

    private final PreferenceUtil mPrefs;
    private final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private static final File mRootPath;
    private WriteToFileThread mThread;
    private static final String mLogExternalPath = Environment.getExternalStorageDirectory().getPath();
    private static final String mLogInternalPath = "/data";
    private static final String DEFAULT_VALUE = "default";

    private static boolean result;

    static {
        mRootPath = new File(Environment.getExternalStorageDirectory().getPath(), "oem_log");
        if (!mRootPath.exists()) {
            result = mRootPath.mkdir();
            Log.d(TagUtil.TAG, "LogEngine --> mkdir logcat result : " + result);
        }
    }

    public LogEngine(Context context) {
        mPrefs = new PreferenceUtil(context);
    }

    public void start() {
        try {
            deleteFileIfNeed();
            List<String> commandList = new ArrayList<>();
            commandList.add("logcat");
            if (!mPrefs.getFormat().getValue().equals(DEFAULT_VALUE)) {
                commandList.add("-v");
                commandList.add(mPrefs.getFormat().getValue());
            }
            if (!mPrefs.getBuffer().getValue().equals(DEFAULT_VALUE)) {
                commandList.add("-b");
                commandList.add(mPrefs.getBuffer().getValue());
            }
            if (!mPrefs.getLevel().getValue().equals(DEFAULT_VALUE)) {
                commandList.add("*:" + mPrefs.getLevel().getValue());
            }
            String[] commandLine = commandList.toArray(new String[0]);

            Log.d(TagUtil.TAG, "LogEngine --> LogEngine is start");
            Log.d(TagUtil.TAG, "LogEngine --> LogEngine args : " + Arrays.toString(commandLine));
            File file = new File(mRootPath + "/" + dataFormat.format(new Date()) + ".log");
            result = file.createNewFile();
            Log.d(TagUtil.TAG, "LogEngine --> log file create result: " + result);

            Process process = Runtime.getRuntime().exec(commandLine);
            Log.d(TagUtil.TAG, "LogEngine --> maxFileSize : " + mPrefs.getLogMaxSize() + "M");
            mThread = new WriteToFileThread(process.getInputStream(), file, mPrefs.getLogMaxSize());
            mThread.stopSelf(false);
            mThread.start();
        } catch (IOException e) {
            Log.e(TagUtil.TAG, "LogEngine --> error create log file", e);
        }
    }

    private void deleteFileIfNeed() {
        Log.d(TagUtil.TAG, "LogEngine --> deleteFileIfNeed");
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
                cal.add(Calendar.DATE, -mPrefs.getLogKeepDays());
                Log.d(TagUtil.TAG, "LogEngine --> getLogKeepDays : " + (-mPrefs.getLogKeepDays()));
                // 七天前的时间
                long timeInMillis2 = cal.getTimeInMillis();

                // 文件超过七天,删除文件
                if ((timeInMillis2 - timeInMillis1) > 0) {
                    Log.d(TagUtil.TAG, "LogEngine --> delete file : " + f.getName());
                    result = f.delete();
                    Log.d(TagUtil.TAG, "LogEngine --> delete file result : " + result);
                }
                Log.d(TagUtil.TAG, "LogEngine --> timeInMillis1 : " + timeInMillis1 + " timeInMillis2 : " + timeInMillis2 + " timeOffset : " + ((timeInMillis2 - timeInMillis1)));
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d(TagUtil.TAG, "LogEngine --> delete file error : " + e.getMessage());
            }
        }
    }

    public void stop() {
        mThread.stopSelf(true);
        try {
            Runtime.getRuntime().exec("logcat -c");
        } catch (IOException e) {
            Log.d(TagUtil.TAG, "LogEngine --> LogEngine stop error: " + e.getMessage());
        }
        Log.d(TagUtil.TAG, "LogEngine --> LogEngine is stop");
    }

}
