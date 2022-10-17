package com.sunritel.logcatcher.enums;

import android.content.Context;

import com.sunritel.logcatcher.R;


public enum Buffer {
	DEFAULT("default", R.string.default_title),
	MAIN("main", R.string.main_title),
	EVENTS("events", R.string.events_title),
	RADIO("radio", R.string.radio_title);
	

	private final String mValue;
	private final int mTitleId;
	
	Buffer(String value, int titleId) {
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