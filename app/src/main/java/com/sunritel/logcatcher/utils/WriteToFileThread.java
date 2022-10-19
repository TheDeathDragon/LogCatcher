package com.sunritel.logcatcher.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WriteToFileThread extends Thread {

    private final File file;
    private final InputStream is;
    private final int maxFileSize;
    private boolean isStop;
    private OnLogListener mOnLogListener;

    public WriteToFileThread(InputStream is, File file, int maxFileSize) {
        this.is = is;
        this.file = file;
        this.maxFileSize = maxFileSize;
    }

    public void setOnReadLineListener(OnLogListener logListener) {
        mOnLogListener = logListener;
    }

    @Override
    public void run() {
        super.run();
        Log.d(MUtil.TAG, "WriteToFileThread --> run");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file,true), 1024);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                if (file.length() > 1024L * 1024 * maxFileSize) {
                    Log.d(MUtil.TAG, "WriteToFileThread --> file size is more than " + maxFileSize + "M , stop logging");
                    isStop = true;
                    bw.flush();
                    bw.close();
                    br.close();
                    is.close();
                    break;
                }
                bw.write(line + "\n");
                if (mOnLogListener != null) {
                    mOnLogListener.readLine(line);
                }
                if (isStop) {
                    Log.d(MUtil.TAG, "WriteToFileThread --> terminated");
                    bw.flush();
                    bw.close();
                    br.close();
                    is.close();
                    break;
                }
            }
        } catch (IOException e) {
            Log.e(MUtil.TAG, "WriteToFileThread --> readLine error", e);
        }
    }

    public void stopSelf(boolean isStop) {
        Log.d(MUtil.TAG, "WriteToFileThread --> stopSelf : " + isStop);
        this.isStop = isStop;
    }

    public interface OnLogListener {
        void readLine(String line);
    }
}