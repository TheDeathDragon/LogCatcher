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
    private boolean isStop;
    private OnLogListener mOnLogListener;

    public WriteToFileThread(InputStream is, File file) {
        this.is = is;
        this.file = file;
    }

    public void setOnReadLineListener(OnLogListener logListener) {
        mOnLogListener = logListener;
    }

    @Override
    public void run() {
        super.run();
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file), 1024);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                bw.write(line + "\n");
                if (mOnLogListener != null) {
                    mOnLogListener.readLine(line);
                }
                if (isStop) {
                    Log.d(TagUtil.TAG, "WriteToFileThread --> break this thread");
                    bw.flush();
                    bw.close();
                    br.close();
                    is.close();
                    break;
                }
            }
        } catch (IOException e) {
            Log.e(TagUtil.TAG, "WriteToFileThread --> readLine error", e);
        }
    }

    public void stopSelf(boolean isStop) {
        this.isStop = isStop;
    }

    public interface OnLogListener {
        void readLine(String line);
    }
}