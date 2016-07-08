package com.dsm.mystudy;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

public class TempTestActivity extends Activity {

	private TextView mLogcat;
	private ScrollView mScroll;
	private Handler mHandler = new Handler();
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_scrollview);
        
        mScroll = (ScrollView) findViewById(R.id.sv_scrollview);
        mLogcat = (TextView) findViewById(R.id.tv_logcat);
        mLogcat.setText("");
        Button add = (Button) findViewById(R.id.btn_addlog);
        add.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				mLogcat.append("Give you the most special gift!\n");
				scrollToBottom(ScrollView.FOCUS_DOWN);
			}
		});
    }
	
	private void scrollToBottom(final int direction) {
		mHandler.post(new Runnable() {  
		    @Override  
		    public void run() {  
		    	mScroll.fullScroll(direction);
		    }  
		});  
	}
}
