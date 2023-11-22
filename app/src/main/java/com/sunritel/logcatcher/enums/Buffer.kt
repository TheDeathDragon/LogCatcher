package com.sunritel.logcatcher.enums

import android.content.Context
import com.sunritel.logcatcher.R

enum class Buffer(val value: String, private val mTitleId: Int) {
    DEFAULT("default", R.string.default_title),
    MAIN("main", R.string.main_title),
    SYSTEM("system", R.string.system_title),
    RADIO("radio", R.string.radio_title),
    EVENTS(
        "events", R.string.events_title
    ),
    CRASH("crash", R.string.crash_title),
    ALL("all", R.string.all_title);

    fun getTitle(context: Context): String {
        return context.resources.getString(mTitleId)
    }
}