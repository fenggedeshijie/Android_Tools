package com.dsm.mystudy;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/*
 * 以三种不同的方式实现 Splash Screen 效果
 */
@SuppressLint("HandlerLeak")
@TargetApi(Build.VERSION_CODES.KITKAT)
public class SplashActivity extends Activity {

	private static final int workType = 0;
	private Subscription subscription;
	private Handler mHandler = new Handler();
	private Runnable runnable;
	
	private Handler mHandler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
            this.removeMessages(1);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start);
		  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);     // 透明状态栏  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); // 透明导航栏 
        
        switch (workType) {
        case 0:	// 强力推荐这种方式
        	subscription = Observable.timer(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
            .subscribe(new Action1<Long>() {
    			@Override
    			public void call(Long t) {
    				startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
    			}
    		});
        	break;
        case 1:
        	mHandler2.sendMessageDelayed(mHandler2.obtainMessage(1), 3000);
        	break;
        case 2:
        	 mHandler.postDelayed(runnable = new Runnable() {
                 @Override
                 public void run() {
                     startActivity(new Intent(SplashActivity.this, MainActivity.class));
                     finish();
                 }
             }, 3000);
        	break;
        }

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_UP) {
			switch (workType) {
			case 0:
				subscription.unsubscribe();	// 取消订阅
				startActivity(new Intent(SplashActivity.this, MainActivity.class));
	            finish();
				break;
			case 1:
				mHandler2.sendMessage(mHandler2.obtainMessage(1));
				break;
			case 2:
				startActivity(new Intent(SplashActivity.this, MainActivity.class));
	            finish();
	            
	            if (runnable != null) {
	            	mHandler.removeCallbacks(runnable);
	            }
				break;
			}
        }
		return super.onTouchEvent(event);
	}
	
}
