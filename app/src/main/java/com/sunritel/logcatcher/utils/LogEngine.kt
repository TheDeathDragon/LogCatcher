package com.sunritel.logcatcher.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LogEngine(context: Context?) {
    private val mPrefs: PreferenceUtil
    private val dataFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    init {
        mPrefs = PreferenceUtil(context)
        mRootPath = if (mPrefs.logSaveLocation == LOG_LOCATION_EXTERNAL) {
            File(mLogExternalPath, "oem_log")
        } else {
            File(mLogInternalPath, "oem_log")
        }
        if (!mRootPath!!.exists()) {
            result = mRootPath!!.mkdir()
            Log.d(LogUtil.TAG, "LogEngine --> mkdir logcat result : $result")
            Log.d(LogUtil.TAG, "LogEngine --> mkdir logcat path : " + mRootPath!!.absolutePath)
        }
    }

    fun start() {
        try {
            checkLogIsExpire()
            val commandList: MutableList<String> = ArrayList()
            commandList.add("logcat")
            if (mPrefs.format.value != DEFAULT_VALUE) {
                commandList.add("-v")
                commandList.add(mPrefs.format.value)
            }
            if (mPrefs.buffer.value != DEFAULT_VALUE) {
                commandList.add("-b")
                commandList.add(mPrefs.buffer.value)
            }
            if (mPrefs.level.value != DEFAULT_VALUE) {
                commandList.add("*:" + mPrefs.level.value)
            }
            val commandLine = commandList.toTypedArray()
            Log.d(LogUtil.TAG, "LogEngine --> LogEngine args : " + commandLine.contentToString())
            val logFileName =
                mRootPath.toString() + File.separator + mLogFileNamePrefix + dataFormat.format(
                    Date()
                ) + mLogFileNameSuffix
            val file = File(logFileName)
            result = file.createNewFile()
            Log.d(LogUtil.TAG, "LogEngine --> log file create result: $result")
            Log.d(LogUtil.TAG, "LogEngine --> log file name: " + file.name)
            val process = Runtime.getRuntime().exec(commandLine)
            Log.d(LogUtil.TAG, "LogEngine --> LogEngine is start")
            Log.d(LogUtil.TAG, "LogEngine --> maxLogFileSize : " + mPrefs.logMaxSize + "M")
            mThread = WriteToFileThread(process.inputStream, file, mPrefs.logMaxSize)
            mThread?.stopSelf(false)
            mThread?.start()
        } catch (e: IOException) {
            Log.e(LogUtil.TAG, "LogEngine --> error create log file", e)
        }
    }

    private fun checkLogIsExpire() {
        Log.d(LogUtil.TAG, "LogEngine --> checkLogIsExpire")
        val files = mRootPath!!.listFiles()
        if (files == null) {
            Log.d(
                LogUtil.TAG, "checkLogIsExpire: there is no log file in " + mRootPath!!.absolutePath
            )
            return
        }
        for (f in files) {
            try {
                Log.d(LogUtil.TAG, "LogEngine --> checkLogIsExpire --> file name : " + f.name)
                val fileLastModified = dataFormat.parse(
                    f.name.substring(
                        mLogFileNamePrefix.length, mLogFileNamePrefix.length + 10
                    )
                )
                val now = Date()
                assert(fileLastModified != null)
                val daysBetween =
                    if (now.time - fileLastModified!!.time > 0) ((now.time - fileLastModified.time) / (1000 * 3600 * 24)).toInt() else 0
                Log.d(
                    LogUtil.TAG,
                    "LogEngine --> checkLogIsExpire --> fileLastModified : $fileLastModified"
                )
                Log.d(LogUtil.TAG, "LogEngine --> checkLogIsExpire --> daysBetween : $daysBetween")
                if (daysBetween >= mPrefs.logKeepDays) {
                    Log.d(
                        LogUtil.TAG,
                        "LogEngine --> checkLogIsExpire --> log keep days reach " + mPrefs.logKeepDays + " days, delete log file"
                    )
                    result = f.delete()
                    Log.d(
                        LogUtil.TAG,
                        "LogEngine --> checkLogIsExpire --> delete file : " + f.name + " result : " + result
                    )
                }
            } catch (e: ParseException) {
                e.printStackTrace()
                Log.d(
                    LogUtil.TAG,
                    "LogEngine --> checkLogIsExpire --> error in checkLogIsExpire : " + e.message
                )
            }
        }
    }

    fun stop() {
        mThread?.stopSelf(true)
        try {
            Runtime.getRuntime().exec("logcat -c")
        } catch (e: IOException) {
            Log.d(LogUtil.TAG, "LogEngine --> LogEngine stop error: " + e.message)
        }
        Log.d(LogUtil.TAG, "LogEngine --> LogEngine is stop")
    }

    companion object {
        private var mThread: WriteToFileThread? = null
        private var mRootPath: File? = null
        private val mLogExternalPath = Environment.getExternalStorageDirectory().path
        private const val mLogFileNamePrefix = "system_"
        private const val mLogFileNameSuffix = ".log"
        private const val mLogInternalPath = "/data"
        private const val DEFAULT_VALUE = "default"
        private const val LOG_LOCATION_INTERNAL = "INTERNAL"
        private const val LOG_LOCATION_EXTERNAL = "EXTERNAL"
        private var result = false
    }
}