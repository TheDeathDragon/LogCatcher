package com.sunritel.logcatcher.enums;

import android.content.Context;

import com.sunritel.logcatcher.R;

import java.util.HashMap;

public enum Buffer {
	MAIN("main", R.string.main_title),
	EVENTS("events", R.string.events_title),
	RADIO("radio", R.string.radio_title);
	
	private static Buffer[] byOrder = new Buffer[3];

	static {
		byOrder[0] = MAIN;
		byOrder[1] = EVENTS;
		byOrder[2] = RADIO;
	}
	
	private static final HashMap<String,Buffer> VALUE_MAP = new HashMap<>();
	
	static {
		VALUE_MAP.put(MAIN.mValue, MAIN); 
		VALUE_MAP.put(EVENTS.mValue, EVENTS); 
		VALUE_MAP.put(RADIO.mValue, RADIO); 
	}
		
	private final String mValue;
	private final int mTitleId;
	
	Buffer(String value, int titleId) {
		mValue = value;
		mTitleId = titleId;
	}
	
	public String getTitle(Context context) {
		return context.getResources().getString(mTitleId);
	}	
	
	public static final Buffer byValue(String value) {
		return VALUE_MAP.get(value);
	}
	
	public static Buffer getByOrder(int order) {
		return byOrder[order];
	}
	
	public String getValue() {
		return mValue;
	}
}