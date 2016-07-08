package com.dsm.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class CoreService extends Service {

	private static final String TAG = CoreService.class.getSimpleName();
	private Context mContext = this;
	private MyBinder mBinder = new MyBinder();
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "onCreate executed");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand executed");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy executed");
	}
	
	public class MyBinder extends Binder {
		public void startDownload() {
			Log.d(TAG, "startDownload executed");
		}
		
		public boolean isServiceRunning() {
			return CoreService.this.isServiceRunning(mContext, CoreService.class.getName());
		}
	}
	
	// 判断一个服务是否启动
	private boolean isServiceRunning(Context context, String serviceName) {
		ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		
		List<RunningServiceInfo> serviceInfoes = activityManager.getRunningServices(100);
		for (RunningServiceInfo serviceInfo : serviceInfoes) {
			System.out.println("The Service Info is " + serviceInfo.service.getClassName());
			if (serviceInfo.service.getClassName().equals(serviceName)) {
				return true;
			}
		}
		return false;
	}

}
