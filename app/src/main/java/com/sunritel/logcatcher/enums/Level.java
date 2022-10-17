package com.sunritel.logcatcher.enums;

import android.content.Context;

import com.sunritel.logcatcher.R;

public enum Level {
    DEFAULT("default", R.string.default_title),
    VERBOSE("V", R.string.verbose_title),
    DEBUG("D", R.string.debug_title),
    INFO("I", R.string.info_title),
    WARN("W", R.string.warn_title),
    ERROR("E", R.string.error_title),
    FATAL("F", R.string.fatal_title);

    private final String mValue;
    private final int mTitleId;

    Level(String value, int titleId) {
        mValue = value;
        mTitleId = titleId;
    }

    public String getValue() {
        return mValue;
    }

    public String getTitle(Context context) {
        return context.getResources().getString(mTitleId);
    }
}