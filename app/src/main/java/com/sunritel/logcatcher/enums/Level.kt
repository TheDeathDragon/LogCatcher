package com.sunritel.logcatcher.enums

import android.content.Context
import com.sunritel.logcatcher.R

enum class Level(val value: String, private val mTitleId: Int) {
    DEFAULT("default", R.string.default_title), VERBOSE("V", R.string.verbose_title), DEBUG(
        "D", R.string.debug_title
    ),
    INFO("I", R.string.info_title), WARN("W", R.string.warn_title), ERROR(
        "E", R.string.error_title
    ),
    FATAL("F", R.string.fatal_title);

    fun getTitle(context: Context): String {
        return context.resources.getString(mTitleId)
    }
}