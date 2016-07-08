package com.dsm.broadcast;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsm.mystudy.BaseActivity;
import com.dsm.mystudy.R;

public class BroadcastActivity extends BaseActivity{
	
	private BroadcastUtils.SystemBroadcastReceiver systemBroadcastRecevier;
	
	private BroadcastReceiver customBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			
		}
		
	};
	
	@Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

        initHeadViews();
        initContentViews();
    }
	    
	@TargetApi(Build.VERSION_CODES.KITKAT) 
	private void initHeadViews() {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);     // 透明状态栏  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); // 透明导航栏 
        
		mHeadback = (ImageView) this.findViewById(R.id.im_headback);
		mHeadback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mHeadtitle = (TextView) this.findViewById(R.id.im_headtitle);
		mHeadtitle.setText("Broadcast Receiver");
	}
	    
	private void initContentViews() {
		// 注册广播接收器
		systemBroadcastRecevier = new BroadcastUtils.SystemBroadcastReceiver();
	    IntentFilter filter = new IntentFilter();
	    filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	    registerReceiver(systemBroadcastRecevier, filter);
	    
	    IntentFilter cusFilter = new IntentFilter();
	    cusFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
	    registerReceiver(customBroadcastReceiver, cusFilter);
	}
    
    @Override  
    protected void onDestroy() {
    	// 注销广播接收器
    	unregisterReceiver(systemBroadcastRecevier);
    	unregisterReceiver(customBroadcastReceiver);
        super.onDestroy();  
    }  
	
}
