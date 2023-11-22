package com.sunritel.logcatcher.enums

import android.content.Context
import com.sunritel.logcatcher.R

enum class Format(val value: String, private val mTitleId: Int) {
    DEFAULT("default", R.string.default_title), BRIEF(
        "brief",
        R.string.brief_title
    ),
    PROCESS("process", R.string.process_title), TAG("tag", R.string.tag_title), THREAD(
        "thread",
        R.string.thread_title
    ),
    TIME("time", R.string.time_title), THREADTIME(
        "threadtime",
        R.string.thread_time_title
    ),
    LONG("long", R.string.long_title), RAW("raw", R.string.raw_title);

    fun getTitle(context: Context): String {
        return context.resources.getString(mTitleId)
    }
}