package com.dsm.mystudy;

import com.dsm.service.CoreService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class ServiceStudyActivity extends Activity implements OnClickListener{

	private CoreService.MyBinder myBinder;
	
	private ServiceConnection sConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			 myBinder = (CoreService.MyBinder) service;  
	         myBinder.startDownload();  
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {}	
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.service_test);
		
		initViews();
	}
	
	private void initViews() {
		Button button_01 = (Button)findViewById(R.id.button_01);
		button_01.setText("启动服务");
		button_01.setOnClickListener(this);
		
		Button button_02 = (Button)findViewById(R.id.button_02);
		button_02.setText("停止服务");
		button_02.setOnClickListener(this);
		
		Button button_03 = (Button)findViewById(R.id.button_03);
		button_03.setText("绑定服务");
		button_03.setOnClickListener(this);
		
		Button button_04 = (Button)findViewById(R.id.button_04);
		button_04.setText("解绑服务");
		button_04.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_01:
			Intent start = new Intent(this, CoreService.class);
			startService(start);
			System.out.println("start service in ServiceStudyActivity");
			break;
		case R.id.button_02:
			Intent stop = new Intent(this, CoreService.class);
			stopService(stop);
			break;
		case R.id.button_03:
			Intent bindservice = new Intent(this, CoreService.class);
			bindService(bindservice, sConnection, BIND_AUTO_CREATE);
			break;
		case R.id.button_04:
			if (myBinder.isServiceRunning()) {
				unbindService(sConnection);
			} else {
				System.out.println("The Service is not Running!");
			}
			
			break;
		}
	}

}
