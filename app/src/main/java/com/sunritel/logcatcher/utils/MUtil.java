package com.sunritel.logcatcher.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class MUtil {
    public final static String TAG = "LogCatcher";

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
