package com.sunritel.logcatcher.enums;

import android.content.Context;

import com.sunritel.logcatcher.R;

public enum Format {
    DEFAULT("default", R.string.default_title),
    BRIEF("brief", R.string.brief_title),
    PROCESS("process", R.string.process_title),
    TAG("tag", R.string.tag_title),
    THREAD("thread", R.string.thread_title),
    TIME("time", R.string.time_title),
    THREADTIME("threadtime", R.string.threadtime_title),
    LONG("long", R.string.long_title),
    RAW("raw", R.string.raw_title);

    private final String mValue;
    private final int mTitleId;

    Format(String value, int titleId) {
        mValue = value;
        mTitleId = titleId;
    }

    public String getTitle(Context context) {
        return context.getResources().getString(mTitleId);
    }

    public String getValue() {
        return mValue;
    }
}