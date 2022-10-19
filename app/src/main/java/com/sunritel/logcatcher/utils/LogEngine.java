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
import java.util.Locale;
import java.util.Objects;

public class LogEngine {

    private final PreferenceUtil mPrefs;
    //    private final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
    private final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static File mRootPath;
    private WriteToFileThread mThread;
    private static final String mLogExternalPath = Environment.getExternalStorageDirectory().getPath();
    private static final String mLogFileNamePrefix = "system_";
    private static final String mLogFileNameSuffix = ".log";
    private static final String mLogInternalPath = "/data";
    private static final String DEFAULT_VALUE = "default";
    private static final String LOG_LOCATION_INTERNAL = "INTERNAL";
    private static final String LOG_LOCATION_EXTERNAL = "EXTERNAL";

    private static boolean result;

    public LogEngine(Context context) {
        mPrefs = new PreferenceUtil(context);
        if (mPrefs.getLogSaveLocation().equals(LOG_LOCATION_EXTERNAL)) {
            mRootPath = new File(mLogExternalPath, "oem_log");
        } else {
            mRootPath = new File(mLogInternalPath, "oem_log");
        }
        if (!mRootPath.exists()) {
            result = mRootPath.mkdir();
            Log.d(MUtil.TAG, "LogEngine --> mkdir logcat result : " + result);
            Log.d(MUtil.TAG, "LogEngine --> mkdir logcat path : " + mRootPath.getAbsolutePath());
        }
    }

    public void start() {
        try {
            // TODO 检查日志是否过期
//            checkLogIsExpire();
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
            Log.d(MUtil.TAG, "LogEngine --> LogEngine args : " + Arrays.toString(commandLine));

            String logFileName = mRootPath + File.separator + mLogFileNamePrefix + dataFormat.format(new Date()) + mLogFileNameSuffix;
            File file = new File(logFileName);
            result = file.createNewFile();
            Log.d(MUtil.TAG, "LogEngine --> log file create result: " + result);
            Log.d(MUtil.TAG, "LogEngine --> log file name: " + file.getName());
            Process process = Runtime.getRuntime().exec(commandLine);

            Log.d(MUtil.TAG, "LogEngine --> LogEngine is start");
            Log.d(MUtil.TAG, "LogEngine --> maxLogFileSize : " + mPrefs.getLogMaxSize() + "M");
            // TODO 日志存储为单文件
            mThread = new WriteToFileThread(process.getInputStream(), file, mPrefs.getLogMaxSize());
            mThread.stopSelf(false);
            mThread.start();
        } catch (IOException e) {
            Log.e(MUtil.TAG, "LogEngine --> error create log file", e);
        }
    }

    private void checkLogIsExpire() {
        Log.d(MUtil.TAG, "LogEngine --> checkLogIsExpire");
        File[] files = mRootPath.listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            try {
                Calendar calendar = Calendar.getInstance();
                String fileName = f.getName().subSequence(0, f.getName().indexOf(".")).toString();
                calendar.setTime(Objects.requireNonNull(dataFormat.parse(fileName)));
                long timeInMillis1 = calendar.getTimeInMillis();
                // 写文件的时间
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -mPrefs.getLogKeepDays());
                Log.d(MUtil.TAG, "LogEngine --> getLogKeepDays : " + (-mPrefs.getLogKeepDays()));
                // 七天前的时间
                long timeInMillis2 = cal.getTimeInMillis();

                // 文件超过七天,删除文件
                if ((timeInMillis2 - timeInMillis1) > 0) {
                    Log.d(MUtil.TAG, "LogEngine --> delete file : " + f.getName());
                    result = f.delete();
                    Log.d(MUtil.TAG, "LogEngine --> delete file result : " + result);
                }
                Log.d(MUtil.TAG, "LogEngine --> timeInMillis1 : " + timeInMillis1 + " timeInMillis2 : " + timeInMillis2 + " timeOffset : " + ((timeInMillis2 - timeInMillis1)));
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d(MUtil.TAG, "LogEngine --> delete file error : " + e.getMessage());
            }
        }
    }

    public void stop() {
        mThread.stopSelf(true);
        try {
            Runtime.getRuntime().exec("logcat -c");
        } catch (IOException e) {
            Log.d(MUtil.TAG, "LogEngine --> LogEngine stop error: " + e.getMessage());
        }
        Log.d(MUtil.TAG, "LogEngine --> LogEngine is stop");
    }

}
