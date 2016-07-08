package com.dsm.broadcast;

import com.dsm.netty.CoreService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

public class BroadcastUtils {

	/*
	   Android 3.1开始, 由于安全性的考虑.
	          程序在安装后,用户没有通过自己的操作来启动程序的话,那么这个程序将收不到android.intent.action.BOOT_COMPLETED这个Intent;
	         用户通过自己的操作启动过一次程序后, receiver将被激活, 从而收的到android.intent.action.BOOT_COMPLETED Intent.
	   
	   http://commonsware.com/blog/2011/07/13/boot-completed-regression-confirmed.html
	 */
	public static class BootBroadcastReceiver extends BroadcastReceiver {
		public static final String SYSTEMACTION_BOOT = "android.intent.action.BOOT_COMPLETED";   
		
		@Override
		public void onReceive(Context context, Intent intent) {
	    	String action = intent.getAction();
	        if (action.equals(SYSTEMACTION_BOOT)) {       
	            Log.d("BootBroadcastReceiver", action); 
	            context.startService(new Intent(context, CoreService.class));
	        } 
		}
	}
	
	public static boolean isNetworkAvailable(Context context) {  
        ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo[] info = mgr.getAllNetworkInfo();  
        if (info != null) {  
            for (int i = 0; i < info.length; i++) {  
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                	NetworkInfo netWorkInfo = info[i];
					if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
						Log.d("BroadcastUtils", "当前是wifi网络环境");
					} else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
						Log.d("BroadcastUtils", "当前是移动网络环境");
					}
                    return true;  
                }  
            }  
        }  
        return false;  
	}  
	 
	public static class SystemBroadcastReceiver extends BroadcastReceiver {
		private static final String TAG = "SystemBroadcastReceiver";
		
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d("SystemBroadcastReceiver", "接收来自系统的广播消息"); 
			String action = intent.getAction();
			 
            // 网络变化的广播
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            	if (!isNetworkAvailable(context)) {
            		Toast.makeText(context, "network disconnected!", Toast.LENGTH_SHORT).show();
            	} else {
            		Toast.makeText(context, "network connected!", Toast.LENGTH_SHORT).show();
            	}
            }
            
            else if (action.equals("android.intent.action.BATTERY_CHANGED")) {
            	 int currLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);  //当前电量  
                 int total = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);      //总电量  
                 int percent = currLevel * 100 / total;  
                 Log.i(TAG, "battery: " + percent + "%");  
            }
		}
	}
}
