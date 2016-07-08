package com.dsm.mystudy;

import com.dsm.injectview.HyViewInjector;
import com.dsm.injectview.annotation.InjectView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

@InjectView(R.layout.fragment_rxjava_basic)
public class InjectTestActivity extends Activity {

	@InjectView(R.id.tv_logcat)
	public TextView mLogcat;
	@InjectView(R.id.tv_title)
	public TextView mTitle;
	@InjectView(R.id.btn_button)
	public Button mButton;
	
	@Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HyViewInjector.inject(this);
        initViews();
    }
	
	private void initViews() {
		mLogcat.setText("This is my first ViewInject Project!");
		mTitle.setText("My Title");
		mButton.setText("My Button");
	}
}
