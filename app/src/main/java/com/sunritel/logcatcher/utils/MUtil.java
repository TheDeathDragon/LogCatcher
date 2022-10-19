package com.sunritel.logcatcher.utils;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MUtil {
    public final static String TAG = "LogCatcher";

    // dateFormat: yyyy-MM-dd HH:mm:ss or yyyy-MM-dd
    public int getDaysBetween(String startDate, String endDate, String dateFormat) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(startDate));
        long start = calendar.getTimeInMillis();
        calendar.setTime(sdf.parse(endDate));
        long end = calendar.getTimeInMillis();
        long betweenDays = (end - start) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(betweenDays));
    }

    public String getToday(String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        return sdf.format(calendar.getTime());
    }

    public Date getFileModifyDate(File file) {
        long time = file.lastModified();
        return new Date(time);
    }

    public void copyFileByStream(File source, File dest) throws IOException {
        try (InputStream is = Files.newInputStream(source.toPath());
             OutputStream os = Files.newOutputStream(dest.toPath())) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mergeFiles(File[] files, File outPutFile) throws IOException {
        try (OutputStream os = Files.newOutputStream(outPutFile.toPath())) {
            for (File file : files) {
                try (InputStream is = Files.newInputStream(file.toPath())) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
