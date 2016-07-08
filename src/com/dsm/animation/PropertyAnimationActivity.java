package com.dsm.animation;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.dsm.mystudy.BaseActivity;
import com.dsm.mystudy.R;

public class PropertyAnimationActivity extends BaseActivity {

	private Context mContext;
	private ImageView mImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.android_animation_test_1);
		mContext = this;
		
		Button rotate = (Button) findViewById(R.id.rotateButton);
		Button scale = (Button) findViewById(R.id.scaleButton);
		Button alpha = (Button) findViewById(R.id.alphaButton);
		Button translate = (Button) findViewById(R.id.translateButton);
		mImage = (ImageView) findViewById(R.id.image);
		
		rotate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rotateAnimation(mImage, 2);
			}
		});
		
		alpha.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				alphaAnimation(mImage);
			}
		});
		
		scale.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				scaleAnimation(mImage, 1);
			}
		});
		
		translate.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				translateAnimation(mImage, 1);
			}
		});
	}
	
	private void rotateAnimation(View view, int type) {
		if (type == 1) {
			ObjectAnimator
			.ofFloat(view, "rotationX", 0, 180.0f)
			.setDuration(1000).start();
		} else {
			ObjectAnimator
			.ofFloat(view, "rotationY", 0, 360.0f)
			.setDuration(1000).start();
		}
	}
	
	private void alphaAnimation(View view) {
		ObjectAnimator
		.ofFloat(view, "alpha", 1.0f, 0, 1.0f)
		.setDuration(1000).start();
	}
	
	private void scaleAnimation(View view, int type) {
		if (type == 1) {
			ObjectAnimator
			.ofFloat(view, "scaleX", 1.0f, 0, 1.0f)
			.setDuration(1000).start();
		} else {
			ObjectAnimator
			.ofFloat(view, "scaleY", 1.0f, 0, 1.0f)
			.setDuration(1000).start();
		}
	}
	
	private void translateAnimation(View view, int type) {
		if (type == 1) {
			ObjectAnimator
			.ofFloat(view, "translationX", 0, 50)
			.setDuration(1000).start();
		} else {
			ObjectAnimator
			.ofFloat(view, "translationY", 0, 50)
			.setDuration(1000).start();
		}
	}
}
