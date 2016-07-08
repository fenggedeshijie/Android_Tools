package com.dsm.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.FrameLayout;

/**
 * 通过ViewDragHelper实现的侧滑控件
 * @Author Tony.Cheng
 * @Time Jan 26th, 2016
 */
public class SlidingMenuLayout extends FrameLayout {

	private GestureDetectorCompat mDetectorCompat;
	private ViewDragHelper mDragHelper;
	private DragListener mDragListener;
	
	private View mLeftContent;
	private View mMainContent;
	
	private int mDragRange;		// 可拖动的范围
	private int mMainLeft;		
	private int mHeight;
	private int mWidth;
	
	private Status mStatus = Status.Close;
	
	public SlidingMenuLayout(Context context) {
		this(context, null);
	}
	
	public SlidingMenuLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public SlidingMenuLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mDragHelper = ViewDragHelper.create(this, mCallBack);
		mDetectorCompat = new GestureDetectorCompat(context, mGestureListener);
	}

	private SimpleOnGestureListener mGestureListener = new SimpleOnGestureListener() {
		public boolean onScroll(MotionEvent e1, MotionEvent e2,	float distanceX, float distanceY) {
			if (Math.abs(distanceX) > Math.abs(distanceY)) {	// 处理事件冲突
				return true;
			} else {
				return false;
			}
		}
	};
	
	private ViewDragHelper.Callback mCallBack = new ViewDragHelper.Callback() {
		// 决定child是否可被拖拽，返回true则进行拖拽
		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			return child == mLeftContent || child == mMainContent;
		}
		
		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			if(mMainLeft + dx < 0) {	// 对值进行矫正
				return 0;
			} else if (mMainLeft + dx > mDragRange) {
				return mDragRange;
			}
		    return left;
		}
		
		@Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
			if (changedView == mMainContent) {
				mMainLeft = left;
			} else {
				mMainLeft += dx;	// 此时，left == dx
			}
			
			if (mMainLeft < 0) {	// 进行值的修正
				mMainLeft = 0;
			} else if (mMainLeft > mDragRange) {
				mMainLeft = mDragRange;
			}
			
            if (changedView == mLeftContent) {
       		    mLeftContent.layout(0, 0, mWidth, mHeight);
       		    mMainContent.layout(mMainLeft, 0, mMainLeft + mWidth, mHeight);
            }
            
            dispatchDragEvent(mMainLeft);
        }
		
		@Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
          
            if (xvel > 300) {
				open();
			} else if (xvel > -300 && mMainLeft > mDragRange * 0.5f) {
				open();
			} else {
				close();
			}
        }

		@Override
	    public int getViewHorizontalDragRange(View child) {
	        return mDragRange;
	    }
		
	};
	
	@Override
	public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
		return mDragHelper.shouldInterceptTouchEvent(ev) && mDetectorCompat.onTouchEvent(ev);
	};
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		try {
			mDragHelper.processTouchEvent(event);
	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    }
		return true;
	}
	
	@Override
	public void computeScroll() {
		// 高频率调用，决定是否有下一个变动等待执行
		if (mDragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		mMainContent.layout(mMainLeft, 0, mMainLeft + mWidth, mHeight);
		mLeftContent.layout(0, 0, mWidth, mHeight);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		// 拿到高宽
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
		
		// 设置可拖动范围
		mDragRange = (int) (mWidth * 0.6f);
	}
	
	/**
	 * 填充结束时获得两个子布局的引用
	 */
	@Override
	protected void onFinishInflate() {

		int childCount = getChildCount();
		// 必要的检验
		if (childCount < 2) {
			throw new IllegalStateException(
					"You need two childrens in your content");
		}

		if (!(getChildAt(0) instanceof ViewGroup)
				|| !(getChildAt(1) instanceof ViewGroup)) {
			throw new IllegalArgumentException(
					"Your childrens must be an instance of ViewGroup");
		}

		mLeftContent = getChildAt(0);
		mMainContent = getChildAt(1);
	}
	
	public static enum Status {
		Open, Close, Draging
	}

	public Status getStatus() {
		return mStatus;
	}

	public void setStatus(Status mStatus) {
		this.mStatus = mStatus;
	}
	
	public void close() {
		close(true);
	};

	public void open() {
		open(true);
	}

	public void close(boolean isSmooth) {
		mMainLeft = 0;
		if (isSmooth) {
			// 执行动画，返回true代表有未完成的动画, 需要继续执行
			if (mDragHelper.smoothSlideViewTo(mMainContent, mMainLeft, 0)) {
				// 注意：参数传递根ViewGroup
				ViewCompat.postInvalidateOnAnimation(this);
			}
		} else {
			mMainContent.layout(0, 0, mWidth, mHeight);
			dispatchDragEvent(0);
		}
	}

	public void open(boolean isSmooth) {
		mMainLeft = mDragRange;
		if (isSmooth) {
			if (mDragHelper.smoothSlideViewTo(mMainContent, mMainLeft, 0)) {
				ViewCompat.postInvalidateOnAnimation(this);
			}
		} else {
			mMainContent.layout(mDragRange, 0, mMainLeft + mWidth, mHeight);
			dispatchDragEvent(mDragRange);
		}
	}
	
	@SuppressLint("ClickableViewAccessibility")
	public interface DragListener {
        public void onOpen();
        public void onClose();
        public void onDrag(float percent);
    }

    public void setDragListener(DragListener dragListener) {
    	mDragListener = dragListener;
    }
	
	/**
	 * 每次更新都会调用 根据当前执行的位置计算百分比percent
	 */
	protected void dispatchDragEvent(int mainLeft) {
		if (mDragListener == null) return;
		
		float percent = mainLeft / (float) mDragRange;
		animViews(percent);

		mDragListener.onDrag(percent);

		Status lastStatus = mStatus;
		if (getStatus() != lastStatus && lastStatus == Status.Draging) {
			if (mStatus == Status.Close) {
				mDragListener.onClose();
			} else if (mStatus == Status.Open) {
				mDragListener.onOpen();
			}
		}
	}
	
	/**
	 * 伴随动画：
	 * @param percent
	 */
	private void animViews(float percent) {
		// 主面板：缩放
		mMainContent.setScaleX(1 - percent * 0.2f);
		mMainContent.setScaleY(1 - percent * 0.2f);
		
		// 左面板：缩放、平移、透明度
		mLeftContent.setScaleX(0.5f + 0.5f * percent);
		mLeftContent.setScaleY(0.5f + 0.5f * percent);
		mLeftContent.setTranslationX(-mWidth * 0.5f + mWidth * 0.5f * percent);
		mLeftContent.setAlpha(percent);
		
		// 背景：颜色渐变
		getBackground().setColorFilter(
				evaluate(percent, Color.BLACK, Color.TRANSPARENT),
				PorterDuff.Mode.SRC_OVER);
	}

	private int evaluate(float fraction, int startValue, int endValue) {
		int startInt = (Integer) startValue;
		int startA = (startInt >> 24) & 0xff;
		int startR = (startInt >> 16) & 0xff;
		int startG = (startInt >> 8) & 0xff;
		int startB = startInt & 0xff;

		int endInt = (Integer) endValue;
		int endA = (endInt >> 24) & 0xff;
		int endR = (endInt >> 16) & 0xff;
		int endG = (endInt >> 8) & 0xff;
		int endB = endInt & 0xff;

		return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
				| (int) ((startR + (int) (fraction * (endR - startR))) << 16)
				| (int) ((startG + (int) (fraction * (endG - startG))) << 8)
				| (int) ((startB + (int) (fraction * (endB - startB))));
	}
	
}
