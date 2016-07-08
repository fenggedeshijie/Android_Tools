package com.dsm.handler;

import android.os.Handler;
import android.os.Message;

public class MyHandler extends Handler{

	private static final int MSG_TYPE_01 = 1;
	private static final int MSG_TYPE_02 = 2;
	private OnUpdateTitleListener mTitle = null;
	
	public interface OnUpdateTitleListener {
		public void setUpdataTitle();
	}
	
	public void setOnUpdateTitelListener(OnUpdateTitleListener l) {
		mTitle = l;
	}
	
	@Override
	public void handleMessage(Message msg) {
		switch(msg.what) {
		case MSG_TYPE_01:
			break;
		case MSG_TYPE_02:
			mTitle.setUpdataTitle();
			break;
		}
	}
	
}
