package com.dsm.customview;

import com.dsm.mystudy.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.OverScroller;
import android.widget.ScrollView;

/*
 * Being understandable is not enough
 * Remember even your worst day just has 24 hours 
 */
public class StickyNavLayout extends LinearLayout {

	private View mTop;
	private View mNav;
	private ViewPager mViewPager;

	private int mTopViewHeight;
	private ViewGroup mInnerScrollView;
	private boolean isTopHidden = false;

	private OverScroller mScroller;
	private VelocityTracker mVelocityTracker;
	private int mTouchSlop;
	private int mMaximumVelocity, mMinimumVelocity;

	private float mLastY;
	private boolean isInControl = false;	// 是否获得对事件的控件权
	
	public StickyNavLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL);

		mScroller = new OverScroller(context);
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
		mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		
		mTop = findViewById(R.id.id_stickynavlayout_topview);
		mNav = findViewById(R.id.id_stickynavlayout_indicator);
		View view = findViewById(R.id.id_stickynavlayout_viewpager);
		if (!(view instanceof ViewPager)) {
			throw new RuntimeException(
					"id_stickynavlayout_viewpager show used by ViewPager !");
		}
		mViewPager = (ViewPager) view;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
		params.height = getMeasuredHeight() - mNav.getMeasuredHeight();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mTopViewHeight = mTop.getMeasuredHeight();
	}

	private void getCurrentScrollView() {
		int currentItem = mViewPager.getCurrentItem();
		PagerAdapter adapter = mViewPager.getAdapter();
		
		if (adapter instanceof FragmentPagerAdapter) {
			FragmentPagerAdapter fadapter = (FragmentPagerAdapter) adapter;
			Fragment item = (Fragment) fadapter.instantiateItem(mViewPager,	currentItem);
			mInnerScrollView = (ViewGroup) (item.getView()
					.findViewById(R.id.id_stickynavlayout_innerscrollview));
		} else if (adapter instanceof FragmentStatePagerAdapter) {
			FragmentStatePagerAdapter fsAdapter = (FragmentStatePagerAdapter) adapter;
			Fragment item = (Fragment) fsAdapter.instantiateItem(mViewPager, currentItem);
			mInnerScrollView = (ViewGroup) (item.getView()
					.findViewById(R.id.id_stickynavlayout_innerscrollview));
		}
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastY = y;
			Log.v("dispatchTouchEvent", "down ---y = " + y);
			break;
		case MotionEvent.ACTION_MOVE:
			float dy = y - mLastY;
			getCurrentScrollView();
			Log.v("dispatchTouchEvent", "move ---y = " + y + ", isInControl = " + isInControl);
			if (mInnerScrollView instanceof ScrollView) {
				if (mInnerScrollView.getScrollY() == 0 && isTopHidden && dy > 0 && !isInControl) {
					isInControl = true;
					ev.setAction(MotionEvent.ACTION_CANCEL);	// 夺回事件处理权
					MotionEvent ev2 = MotionEvent.obtain(ev);
					dispatchTouchEvent(ev);
					ev2.setAction(MotionEvent.ACTION_DOWN);
					return dispatchTouchEvent(ev2);
				}
			} else if (mInnerScrollView instanceof ListView) {

				ListView lv = (ListView) mInnerScrollView;
				View c = lv.getChildAt(lv.getFirstVisiblePosition());

				if (c != null && c.getTop() == 0 && isTopHidden	&& dy > 0) {
					ev.setAction(MotionEvent.ACTION_CANCEL);
					MotionEvent ev2 = MotionEvent.obtain(ev);
					dispatchTouchEvent(ev);
					ev2.setAction(MotionEvent.ACTION_DOWN);
					return dispatchTouchEvent(ev2);
				}
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		float curY = ev.getY();
		
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastY = curY;
			break;
			
		case MotionEvent.ACTION_MOVE:
			float dy = curY - mLastY;
			getCurrentScrollView();
			Log.e("onInterceptTouchEvent", "move ---y = " + curY);
			if (Math.abs(dy) < mTouchSlop) break;
			
			if (mInnerScrollView instanceof ScrollView) {
				if (!isTopHidden || (mInnerScrollView.getScrollY() == 0	&& isTopHidden && dy > 0)) {
					initVelocityTrackerIfNotExists(ev);
					mLastY = curY;
					return true;
				}
			} else if (mInnerScrollView instanceof ListView) {
				ListView lv = (ListView) mInnerScrollView;
				View c = lv.getChildAt(lv.getFirstVisiblePosition());
				
				if (!isTopHidden ||(c != null && c.getTop() == 0 && isTopHidden && dy > 0)) {
					initVelocityTrackerIfNotExists(ev);
					mLastY = curY;
					return true;
				}
			}
			isInControl = false;	// 不拦截则失去处理事件的权利
			break;
			
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			Log.e("onInterceptTouchEvent", "up ---y = " + curY);
			recycleVelocityTracker();
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		initVelocityTrackerIfNotExists(event);
		int action = event.getAction();
		float curY = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished())
				mScroller.abortAnimation();
			mLastY = curY;
			return true;
		case MotionEvent.ACTION_MOVE:
			float dy = curY - mLastY;
			scrollBy(0, (int)-dy);
			Log.i("onTouchEvent", "move ---y = " + curY);
			if (getScrollY() == mTopViewHeight && dy < 0) {	// 由子控件处理事件
				event.setAction(MotionEvent.ACTION_DOWN); // 转交事件处理权(切换处理事件的控件)
				dispatchTouchEvent(event);
			} 
			mLastY = curY;
			break;	
		case MotionEvent.ACTION_CANCEL:		// 如果该控件被嵌套的话，可能会用到
			recycleVelocityTracker();
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			break;
		case MotionEvent.ACTION_UP:
			mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
			int velocityY = (int) mVelocityTracker.getYVelocity();
			if (Math.abs(velocityY) > mMinimumVelocity) {
				fling(-velocityY);
			}
			recycleVelocityTracker();
			break;
		}
		return super.onTouchEvent(event);
	}

	public void fling(int velocityY) {
		mScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, mTopViewHeight);
		invalidate();
	}
	
	@Override
	public void scrollTo(int x, int y) {
		if (y < 0) {
			y = 0;
		}
		
		if (y > mTopViewHeight) {
			y = mTopViewHeight;
		}
		
		if (y != getScrollY()) {
			super.scrollTo(x, y);
		}

		isTopHidden = (getScrollY() == mTopViewHeight);
	}
	
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(0, mScroller.getCurrY());
			invalidate();
		}
	}
	
	private void initVelocityTrackerIfNotExists(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}
	
	private void recycleVelocityTracker() {
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}
}
