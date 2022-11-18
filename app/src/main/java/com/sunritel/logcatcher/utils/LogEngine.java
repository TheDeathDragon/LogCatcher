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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LogEngine {

    private final PreferenceUtil mPrefs;
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
            checkLogIsExpire();
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
            Log.d(MUtil.TAG, "checkLogIsExpire: there is no log file in " + mRootPath.getAbsolutePath());
            return;
        }
        for (File f : files) {
            try {
                Log.d(MUtil.TAG, "LogEngine --> checkLogIsExpire --> file name : " + f.getName());
                Date fileLastModified = dataFormat.parse(f.getName().substring(mLogFileNamePrefix.length(), mLogFileNamePrefix.length() + 10));
                Date now = new Date();
                assert fileLastModified != null;
                int daysBetween = now.getTime() - fileLastModified.getTime() > 0 ? (int) ((now.getTime() - fileLastModified.getTime()) / (1000 * 3600 * 24)) : 0;

                Log.d(MUtil.TAG, "LogEngine --> checkLogIsExpire --> fileLastModified : " + fileLastModified);
                Log.d(MUtil.TAG, "LogEngine --> checkLogIsExpire --> daysBetween : " + daysBetween);

                if ((daysBetween) >= mPrefs.getLogKeepDays()) {
                    Log.d(MUtil.TAG, "LogEngine --> checkLogIsExpire --> log keep days reach " + mPrefs.getLogKeepDays() + " days, delete log file");
                    result = f.delete();
                    Log.d(MUtil.TAG, "LogEngine --> checkLogIsExpire --> delete file : " + f.getName() + " result : " + result);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d(MUtil.TAG, "LogEngine --> checkLogIsExpire --> error in checkLogIsExpire : " + e.getMessage());
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
