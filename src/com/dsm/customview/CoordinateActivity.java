package com.dsm.customview;
import com.dsm.mystudy.R;

import android.animation.ObjectAnimator; 
import android.app.Activity; 
import android.os.Bundle; 
import android.util.Log; 
import android.view.View; 
import android.view.View.OnClickListener; 
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class CoordinateActivity extends Activity implements OnClickListener { 
	private static final String TAG = CoordinateActivity.class.getSimpleName();
	private ImageView mImageView;
	LinearLayout mainLayout;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_coordinate); 
		
		mImageView = (ImageView) findViewById(R.id.iv_icon);
		mImageView.setClickable(true);
		mImageView.setOnClickListener(this);
		
		Button scrollTo = (Button)findViewById(R.id.btn_scrollto);
		scrollTo.setText("ScrollTo");
		scrollTo.setOnClickListener(this);
		Button scrollBy = (Button)findViewById(R.id.btn_scrollby);
		scrollBy.setText("ScrollBy");
		scrollBy.setOnClickListener(this);
		Button spingBack = (Button)findViewById(R.id.btn_spingback);
		spingBack.setText("SpingBack");
		spingBack.setOnClickListener(this);
		
		mainLayout = (LinearLayout)findViewById(R.id.main_layout);
	}
	
	@Override 
	public void onClick(View v) { 
		switch (v.getId()) {
		case R.id.iv_icon:
			Log.d(TAG, "translationX = " + mImageView.getTranslationX());
			Log.d(TAG, "view.getX = " + mImageView.getX());
			Log.d(TAG, "view.getLeft = " + mImageView.getLeft());
			Log.d(TAG, "view.getScrollX = " + mImageView.getScrollX());
			Log.d(TAG, "view.getPaddingLeft = " + mImageView.getPaddingLeft());
			Log.d(TAG, "view.getWidth = " + mImageView.getLayoutParams().width);
			Log.d(TAG, "view.getMeasuredWidth = " + mImageView.getMeasuredWidth());
			//ObjectAnimator.ofFloat(mImageView, "translationX", -60f).setDuration(1000).start(); 
			break;
		case R.id.btn_scrollto:
			mImageView.scrollTo(-50, 0);
			break;
		case R.id.btn_scrollby:
			mImageView.scrollBy(-10, 0);
			break;
		case R.id.btn_spingback:
			//mImageView.spingBack();
			//mainLayout.scrollTo(-50, 0);
			mImageView.offsetLeftAndRight(50);
			break;
		}
		
	} 
}