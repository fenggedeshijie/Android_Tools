package com.dsm.customview;


import com.dsm.mystudy.R;
import com.dsm.util.ScreenUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class DsmSlidingContent extends HorizontalScrollView {

	private Scroller mScroller = null ;
	private VelocityTracker mVelocityTracker = null ;
	
	private static int  SNAP_VELOCITY = 100 ;
	private int mTouchSlop = 0 ;
	private float mLastMotionX = 0 ;
	private float mLastMotionY = 0 ;

	private static final int TOUCH_STATE_REST = 0;
	private static final int TOUCH_STATE_SCROLLING = 1;
	private int mTouchState = TOUCH_STATE_REST;
	
	
	private int mScreenWidth;
	private int mContentWidth;
	private int mMenuWidth;
	private int mHalfMenuWidth;
	private int mMenuLeftPadding;
	
	private View mMenu;
	private View mContent;
		
	private boolean once;
	private Status status = Status.Open;
	
	public DsmSlidingContent(Context context) {
		this(context, null, 0);
	}
	
	public DsmSlidingContent(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DsmSlidingContent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScreenWidth = ScreenUtils.getScreenWidth(context);
		mScroller = new Scroller(context);
		
/*//		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
//				R.styleable.SlidingMenu, defStyle, 0);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.MsetSlidingMenu, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
//			case R.styleable.SlidingMenu_rightPadding:
			case R.styleable.MsetSlidingMenu_msetLeftPadding:
				// 默认50
				mMenuLeftPadding = a.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50f,
								getResources().getDisplayMetrics()));// 默认为10DP
				break;
			}
		}
		a.recycle();*/
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		if (!once) {
			LinearLayout wrapper = (LinearLayout) getChildAt(0);
			mMenu = (View)wrapper.getChildAt(0);
			mContent = (View)wrapper.getChildAt(1);

			mMenuWidth = mMenu.getLayoutParams().width;
			mContentWidth = mScreenWidth - mMenuLeftPadding;
			
			mHalfMenuWidth = (mMenuWidth - mMenuLeftPadding) / 2;
			mContent.getLayoutParams().width = mContentWidth;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		
		if (changed) {
			// 将菜单隐藏
			this.scrollTo(mMenuWidth - mMenuLeftPadding, 0);
			mMenu.setTranslationX(mMenuWidth - mMenuLeftPadding);
			//setChildEnable(false);
			once = true;
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);
		
		// 手指触摸位置
		float x = ev.getX();
		
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000);
			
			int velocityX = (int) velocityTracker.getXVelocity() ;
			
			// 滑动速率达到了一个标准, 快速向右滑屏
			if (velocityX > SNAP_VELOCITY) {
				Log.v("DsmSlidingContent", "fast right move!");
				int dx = 0 - getScrollX();
				mScroller.startScroll(getScrollX(), 0, dx, 0, 100);
				//this.smoothScrollTo(0, 0);
				status = Status.Close;
				//setChildEnable(true);
			}
			
			// 滑动速率达到了一个标准, 快速向左滑屏
			else if(velocityX < -SNAP_VELOCITY){
				Log.v("DsmSlidingContent", "fast left move!");
				int dx = mMenuWidth - mMenuLeftPadding - getScrollX();
				mScroller.startScroll(getScrollX(), 0, dx, 0, 100);
				//this.smoothScrollTo(mMenuWidth - mMenuLeftPadding, 0);
				status = Status.Open;
				//setChildEnable(false);
			}
			
			// 缓慢移动的
			else{
				Log.v("DsmSlidingContent", "fast normal move!");
				if (getScrollX() >= mHalfMenuWidth) {
					int dx = mMenuWidth - mMenuLeftPadding - getScrollX();
					mScroller.startScroll(getScrollX(), 0, dx, 0, 100);
					//this.smoothScrollTo(mMenuWidth - mMenuLeftPadding, 0);
					status = Status.Open;
					//setChildEnable(false);
				} else {
					int dx = 0 - getScrollX();
					mScroller.startScroll(getScrollX(), 0, dx, 0, 100);
					//this.smoothScrollTo(0, 0);
					status = Status.Close;
					//setChildEnable(true);
				}
			}
			 //此时需要手动刷新View 否则没效果
		    invalidate();
		    
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			
			mTouchState = TOUCH_STATE_REST ;
			return true;
		}
		return super.onTouchEvent(ev);
	}
	
	@Override
	public void computeScroll() {	
		// 如果返回true，表示动画还没有结束
		// 因为前面startScroll，所以只有在startScroll完成时 才会为false
		if (mScroller.computeScrollOffset()) {
			// 产生了动画效果 每次滚动一点
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			
		    // 刷新View 否则效果可能有误差
			postInvalidate();
		}
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		mMenu.setTranslationX(l);	
	}
	
	public enum Status {
	    Open, Close
	}
	
	public boolean getStatus() {
		if (status == Status.Open) return true;
		else return false;
    }

	public void setStatus(boolean isOpen) {
		if (isOpen == true) {
			int dx = mMenuWidth - mMenuLeftPadding - getScrollX();
			mScroller.startScroll(getScrollX(), 0, dx, 0, 200);
			//this.smoothScrollTo(mMenuWidth - mMenuLeftPadding, 0);
			status = Status.Open;
			//setChildEnable(false);
		} else {
			int dx = 0 - getScrollX();
			mScroller.startScroll(getScrollX(), 0, dx, 0, 200);
			//this.smoothScrollTo(0, 0);
			status = Status.Close;
			//setChildEnable(true);
		}
		invalidate();
	}

	private void setChildEnable(boolean enable) {
		ViewGroup viewGroup = (ViewGroup)mMenu;
		int count = viewGroup.getChildCount();
		for (int i = 0; i < count; i ++) {
			viewGroup.getChildAt(i).setEnabled(enable);
		}
	}
}
