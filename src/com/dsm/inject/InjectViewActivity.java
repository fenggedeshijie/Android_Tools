package com.dsm.inject;

import com.dsm.inject.annotation.ContentView;
import com.dsm.inject.annotation.OnClick;
import com.dsm.inject.annotation.ViewInject;
import com.dsm.mystudy.BaseActivity;
import com.dsm.mystudy.R;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@ContentView(value = R.layout.activity_main)
public class InjectViewActivity extends BaseActivity {

	/*@ViewInject(R.id.anim_object)
	private Button mAnimation;
	
	@ViewInject(R.id.layout_trans)
	private Button mTransition;
	
	@ViewInject(R.id.rxjava)
	private Button mRxjava;
	
	@ViewInject(R.id.dialog)
	private Button mFragmentDialog;
	
	@ViewInject(R.id.notification)
	private Button mNotification;
	
	@ViewInject(R.id.asynctask)
	private Button mAsynctask;
	
	@ViewInject(R.id.broadcast)
	private Button mBroadcast;*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewInjectUtils.inject(this);

		initHeadViews();
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
		mHeadtitle.setText("Inject View");
	}

	/*@OnClick({ R.id.anim_object, R.id.rxjava })
	public void clickBtnInvoked(View v) {
		switch (v.getId()) {
		case R.id.anim_object:
			Toast.makeText(InjectViewActivity.this, "Why do you click me ?",
					Toast.LENGTH_SHORT).show();
			break;

		case R.id.rxjava:
			Toast.makeText(InjectViewActivity.this, "I am sleeping !!!",
					Toast.LENGTH_SHORT).show();
			break;
		}
	}*/
}
