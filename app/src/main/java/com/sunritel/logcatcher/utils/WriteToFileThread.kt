package com.sunritel.logcatcher.utils

import android.util.Log
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class WriteToFileThread(
    private val inputStream: InputStream,
    private val file: File,
    private val maxFileSize: Int
) : Thread() {
    private var isStop = false
    private var mOnLogListener: OnLogListener? = null
    fun setOnReadLineListener(logListener: OnLogListener?) {
        mOnLogListener = logListener
    }

    override fun run() {
        super.run()
        Log.d(LogUtil.TAG, "WriteToFileThread --> run")
        try {
            val bw = BufferedWriter(FileWriter(file, true), 1024)
            val br = BufferedReader(InputStreamReader(inputStream))
            var line: String
            while (br.readLine().also { line = it } != null) {
                if (file.length() > 1024L * 1024 * maxFileSize) {
                    Log.d(
                        LogUtil.TAG,
                        "WriteToFileThread --> file size is more than " + maxFileSize + "M , stop logging"
                    )
                    isStop = true
                    bw.flush()
                    bw.close()
                    br.close()
                    inputStream.close()
                    break
                }
                bw.write(
                    """
                        $line
                    """.trimIndent()
                )
                if (mOnLogListener != null) {
                    mOnLogListener!!.readLine(line)
                }
                if (isStop) {
                    Log.d(LogUtil.TAG, "WriteToFileThread --> terminated")
                    bw.flush()
                    bw.close()
                    br.close()
                    inputStream.close()
                    break
                }
            }
        } catch (e: IOException) {
            Log.e(LogUtil.TAG, "WriteToFileThread --> readLine error", e)
        }
    }

    fun stopSelf(isStop: Boolean) {
        Log.d(LogUtil.TAG, "WriteToFileThread --> stopSelf : $isStop")
        this.isStop = isStop
    }

    interface OnLogListener {
        fun readLine(line: String?)
    }
}