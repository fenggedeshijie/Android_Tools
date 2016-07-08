package com.dsm.animation;

import com.dsm.mystudy.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class AnimationActivity1 extends Activity {

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
				/*AnimationSet animationSet = new AnimationSet(true);
				//参数1：从哪个旋转角度开始            
				//参数2：转到什么角度            
				//后4个参数用于设置围绕着旋转的圆的圆心在哪里            
				//参数3：确定x轴坐标的类型，有ABSOLUT绝对坐标、RELATIVE_TO_SELF相对于自身坐标、RELATIVE_TO_PARENT相对于父控件的坐标            
				//参数4：x轴的值，0.5f表明是以自身这个控件的一半长度为x轴            
				//参数5：确定y轴坐标的类型            
				//参数6：y轴的值，0.5f表明是以自身这个控件的一半长度为x轴            
				RotateAnimation rotateAnimation = new RotateAnimation(0, 360, 
									Animation.RELATIVE_TO_SELF, 0.5f, 
									Animation.RELATIVE_TO_SELF, 0.5f); 
				rotateAnimation.setDuration(1000); 
				animationSet.addAnimation(rotateAnimation);
				mImage.startAnimation(animationSet);*/
				
				Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anima_rotate);
				mImage.startAnimation(animation);
			}
		});
		
		scale.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AnimationSet animationSet = new AnimationSet(true);
				ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0, 1.0f, 0, 
									Animation.RELATIVE_TO_SELF, 0.5f, 
									Animation.RELATIVE_TO_SELF, 0.5f);
				scaleAnimation.setDuration(1000); 
				animationSet.addAnimation(scaleAnimation); 
				mImage.startAnimation(animationSet);
			}
		});
		
		alpha.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AnimationSet animationSet = new AnimationSet(true);
				AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.5f);
				alphaAnimation.setDuration(1000); 
				animationSet.addAnimation(alphaAnimation); 
				mImage.startAnimation(animationSet);
			}
		});
		
		translate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AnimationSet animationSet = new AnimationSet(true);
				TranslateAnimation translateAnimation = new TranslateAnimation(
									Animation.RELATIVE_TO_SELF, 0, 
									Animation.RELATIVE_TO_SELF, 0.5f, 
									Animation.RELATIVE_TO_SELF, 0, 
									Animation.RELATIVE_TO_SELF, 0.5f);
				translateAnimation.setDuration(1000); 
				animationSet.addAnimation(translateAnimation); 
				mImage.startAnimation(animationSet);
			}
		});
	}

}
