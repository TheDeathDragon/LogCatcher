package com.sunritel.logcatcher.utils

import java.io.File
import java.io.IOException
import java.nio.file.Files

class LogUtil {
    @Throws(IOException::class)
    fun copyFileByStream(source: File, dest: File) {
        try {
            Files.newInputStream(source.toPath()).use { `is` ->
                Files.newOutputStream(dest.toPath()).use { os ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (`is`.read(buffer).also { length = it } > 0) {
                        os.write(buffer, 0, length)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun mergeFiles(files: Array<File>, outPutFile: File) {
        try {
            Files.newOutputStream(outPutFile.toPath()).use { os ->
                for (file in files) {
                    Files.newInputStream(file.toPath()).use { `is` ->
                        val buffer = ByteArray(1024)
                        var length: Int
                        while (`is`.read(buffer).also { length = it } > 0) {
                            os.write(buffer, 0, length)
                        }
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        const val TAG = "LogCatcher"
    }
}