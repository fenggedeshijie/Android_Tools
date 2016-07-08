package com.dsm.util;

import android.os.Handler;
import android.view.View;

public class ViewUtil {

	/*
	 * 避免短时间内多处点击事件(消抖)
	 * 当响应了一次点击事件后，需得经过interval时间后才能再次响应点击事件
	 */
	public static void noMoreClick(final View view, long interval) {
		view.setClickable(false);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				view.setClickable(true);
			}
		}, interval);
	}
}
