package com.dsm.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.OverScroller;

public class MyImageView extends ImageView {

	private OverScroller mScroller;
	
	private float lastX;  
    private float lastY;  
  
    private float startX;  
    private float startY;
	
	public MyImageView(Context context) {
		this(context, null);
	}
	
	public MyImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public MyImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//mScroller = new OverScroller(context, new BounceInterpolator());
		mScroller = new OverScroller(context);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN: 
			lastX = event.getRawX();
			lastY = event.getRawY();
            break;  
        case MotionEvent.ACTION_MOVE:  
            float disX = event.getRawX() - lastX;  
            float disY = event.getRawY() - lastY;  

            offsetLeftAndRight((int) disX);
            offsetTopAndBottom((int) disY);
            
            lastX = event.getRawX();  
            lastY = event.getRawY();  
            break;  
        case MotionEvent.ACTION_UP:  
            mScroller.startScroll((int) getX(), (int) getY(), -(int) (getX() - startX),  
                    -(int) (getY() - startY), 500);  
            invalidate();  
            break;  
		}
		return super.onTouchEvent(event);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		startX = getX();
		startY = getY();
		Log.d("TAG", "startX = " +startX + ", startY = " + startY); 
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {  
            setX(mScroller.getCurrX());  
            setY(mScroller.getCurrY());  
            invalidate();  
        }  
		super.computeScroll();
	}
	
	public void spingBack() {
		 
        if (mScroller.springBack((int) getX(), (int) getY(), 0, (int) getX(), 0,  
                (int) getY() - 100)) { 
        	Log.d("TAG", "getX() = " + getX() + ", getY() = " + getY()); 
            invalidate();
        }  
    }

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		
	}  
}
