package com.dsm.animation;

import com.dsm.mystudy.R;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class AnimationActivity extends Activity {

	private static int X_DIRECTION = 1;
	private static int Y_DIRECTION = 2;
	
	private ImageView mImage;
	private ImageView mBall;
	private final int mDuration = 3;	// 动画持续的时间
	
	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.xml_for_anim); 
        
        initViews();
    }  
  
	private void initViews() {
		mImage = (ImageView) findViewById(R.id.id_image);
		mImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rotateyAnimRun(v, AnimationActivity.X_DIRECTION);
			}
		});
        mBall = (ImageView) findViewById(R.id.id_ball);
	}
	
    public void rotateyAnimRun(final View view, final int way) { 
    	if (way == AnimationActivity.X_DIRECTION) {
    		ObjectAnimator  // 沿X轴旋转
            .ofFloat(view, "rotationX", 0.0F, 360.0F)  
            .setDuration(500) 
            .start();
    	} else if (way == AnimationActivity.Y_DIRECTION) {
    		ObjectAnimator  // 沿X轴旋转
            .ofFloat(view, "rotationY", 0.0F, 360.0F)  
            .setDuration(500) 
            .start();
    	}
    }
    
    private void animObject(final View view) {
    	ObjectAnimator anim = ObjectAnimator.ofFloat(view, "cxc", 1.0F, 0.3F);
        anim.setDuration(500);
        anim.start();
        anim.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float cVal = (Float) animation.getAnimatedValue();  
	            view.setAlpha(cVal);  
	            view.setScaleX(cVal);  
	            view.setScaleY(cVal); 
			}
		});
    }
    
    private void animByHolder(View view) {
    	PropertyValuesHolder pvhA = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f, 1.0f);
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.0f, 1.0f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.0f, 1.0f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhA, pvhX, pvhY).setDuration(1000).start();
    }
    
    private void animPiaoWuXian() {
    	ValueAnimator anim = new ValueAnimator();
        anim.setDuration(mDuration * 1000);
        anim.setObjectValues(new PointF(0, 0));
        anim.setInterpolator(new LinearInterpolator());
        anim.setEvaluator(new TypeEvaluator<PointF>() {
        	
        	// fraction = t / duration 
        	@Override
			public PointF evaluate(float fraction, PointF startValue,
					PointF endValue) {
				PointF point = new PointF();
				point.x = 200 * fraction * mDuration;  
                point.y = 0.5f * 200 * (fraction * mDuration) * (fraction * mDuration);
				return point;
			}
		});
        
		anim.addListener(new AnimatorListener() { 
			@Override
			public void onAnimationStart(Animator animation) {
				 Log.i("ValueAnimator", "onAnimationStart"); 
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				Log.i("ValueAnimator", "onAnimationRepeat"); 
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				Log.i("ValueAnimator", "onAnimationEnd"); 
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				Log.i("ValueAnimator", "onAnimationCancel"); 
			}
		});
		
		anim.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				Log.i("ValueAnimator", "onAnimationEnd2"); 
			}
		});
		
        anim.start();  
        anim.addUpdateListener(new AnimatorUpdateListener() {  
            @Override  
            public void onAnimationUpdate(ValueAnimator animation) {  
                PointF point = (PointF) animation.getAnimatedValue();  
                mBall.setX(point.x);  
                mBall.setY(point.y);  
            }  
        });
    }
    
    private void animByViewMethod() {
    	 mBall.animate().alpha(0.5f)		  // need API 11
         .y(500).setDuration(1000)  
         .withStartAction(new Runnable() { // need API 12
             @Override  
             public void run() {  
                 Log.i("View.animate()", "START");  
             }  
         })
         .withEndAction(new Runnable() { // need API 16  
             @Override  
             public void run() {  
             	Log.i("View.animate()", "END");  
                 runOnUiThread(new Runnable() {  
                     @Override  
                     public void run() {  
                         mBall.setY(0);  
                         mBall.setAlpha(1.0f);  
                     }  
                 });  
             }  
         }).start();  
    }
    
    class PointF {
    	private float x;
    	private float y;
    	
    	public PointF() {
    		this.x = 0.0f;
    		this.y = 0.0f;
    	}
    	
		public PointF(float x, float y) {
    		this.x = x;
    		this.y = y;
    	}
    	
    	public float getX() {
    		return x;
    	}
    	
    	public void setX(float x) {
    		this.x = x;
    	}
    	
    	public float getY() {
    		return y;
    	}
    	
    	public void setY(float y) {
    		this.y = y;
    	}
    }
}
