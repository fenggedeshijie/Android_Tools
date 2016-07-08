package com.dsm.mystudy;

import java.lang.reflect.Type;

import com.dsm.util.ViewUtil;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ReflectionActivity extends Activity {

	@Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_test);
        
        initViews();
        
        String str = new String();
        testType(str.getClass());
    }

	public void initViews() {
		final Button test = (Button)findViewById(R.id.button_01);
		test.setText("消抖测试");
		test.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/simkai.ttf"));
		test.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "您老悠着点儿！", Toast.LENGTH_SHORT).show();
				ViewUtil.noMoreClick(test, 1000);
			}
		});
		
		Button test2 = (Button)findViewById(R.id.button_02);
		test2.setText("消抖测试");
		test2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/stsong.ttf"));
	}
	
	public void testType(Type type) {
		Class<?> clazz = (Class<?>) type;
		
		System.out.println("The \"Type\" is " + clazz.getSimpleName());
	}
}
