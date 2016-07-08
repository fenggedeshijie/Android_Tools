package com.dsm.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

public class WorkSheetView extends View {
	
	private static final String TAG = WorkSheetView.class.getSimpleName();
    protected static final int OVERSCROLL_DISTANCE_X = 50;
    protected static final int OVERSCROLL_DISTANCE_Y = 0;
    protected static final int INVALID_POINTER_ID  = -1;

    private int fWorksheetWidth = 2000;
    private int fWorksheetHeight = 0;

    private OverScroller fScroller;
    private VelocityTracker fVelocityTracker = null;
    private int fMinimumVelocity;

    private int fTranslatePointerId = INVALID_POINTER_ID;
    private PointF fTranslateLastTouch = new PointF();

    private Paint mPaint;
    private boolean fInteracting = false;

    public WorkSheetView(Context context, AttributeSet attrs) {
    	this(context, attrs, 0);
    }

    public WorkSheetView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    protected void initView(Context context, AttributeSet attrs) {
        fScroller = new OverScroller(this.getContext());
        setOverScrollMode(OVER_SCROLL_ALWAYS);
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        fMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
    }

    @SuppressLint("ClickableViewAccessibility")
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        addVelocityTracker(event);
        final int action = event.getAction();
        
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {	// 第一个手指按下
                if (!fScroller.isFinished())
                    fScroller.abortAnimation();

                final float x = event.getX();
                final float y = event.getY();

                fTranslateLastTouch.set(x, y);
                fTranslatePointerId = event.getPointerId(0);
                startInteracting();
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndexTranslate = event.findPointerIndex(fTranslatePointerId);
                if (pointerIndexTranslate >= 0) {
                    float translateX = event.getX(pointerIndexTranslate);
                    float translateY = event.getY(pointerIndexTranslate);

                    overScrollBy(
                            (int)(fTranslateLastTouch.x - translateX),
                            (int)(fTranslateLastTouch.y - translateY),
                            getScrollX(),
                            getScrollY(),
                            fWorksheetWidth - getWidth(),
                            fWorksheetHeight - getHeight(),
                            OVERSCROLL_DISTANCE_X,
                            OVERSCROLL_DISTANCE_Y,
                            true);

                    fTranslateLastTouch.set(translateX, translateY);
                    invalidate();
                }
                break;
            }
            
            case MotionEvent.ACTION_UP: {
                int initialXVelocity = getScrollVelocityX();
                int initialYVelocity = getScrollVelocityY();
                
                if ((Math.abs(initialXVelocity) + Math.abs(initialYVelocity) > fMinimumVelocity)) {
                    if (getScrollX() < 0 
                    		|| getScrollX() > fWorksheetWidth - getWidth()) {
                    	if (fScroller.springBack(getScrollX(), getScrollY(), 
                        		0, fWorksheetWidth - getWidth(), 0, fWorksheetHeight - getHeight()))
                            invalidate();
                    } else {
                    	fling(-initialXVelocity, -initialYVelocity);
                    }
                }
                else {
                    if (fScroller.springBack(getScrollX(), getScrollY(),
                    		0, fWorksheetWidth - getWidth(), 0, fWorksheetHeight - getHeight()))
                        invalidate();
                    stopInteracting();
                }

                recycleVelocityTracker();
                fTranslatePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN: {
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (event.getAction() 
                		& MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == fTranslatePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    fTranslateLastTouch.set(event.getX(newPointerIndex), event.getY(newPointerIndex));
                    fTranslatePointerId = event.getPointerId(newPointerIndex);
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                if (fScroller.springBack(getScrollX(), getScrollY(), 
                		0, fWorksheetWidth - getWidth(), 0, fWorksheetHeight - getHeight()))
                    invalidate();

                fTranslatePointerId = INVALID_POINTER_ID;
                break;
            }
        }

        return true;
    }

    private void fling(int velocityX, int velocityY) {
        int x = getScrollX();
        int y = getScrollY();

        startInteracting();
        fScroller.fling(x, y, velocityX, velocityY, 
        		0, fWorksheetWidth - getWidth(), 
        		0, fWorksheetHeight - getHeight());

        invalidate();
    }

    private void startInteracting() {
        fInteracting = true;
    }

    private void stopInteracting() {
        fInteracting = false;
    }

    @Override
    public void computeScroll() {
        if (fScroller != null && fScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = fScroller.getCurrX();
            int y = fScroller.getCurrY();

            if (oldX != x || oldY != y) {
                overScrollBy(
                        x - oldX,
                        y - oldY,
                        oldX,
                        oldY,
                        fWorksheetWidth - getWidth(),
                        fWorksheetHeight - getHeight(),
                        OVERSCROLL_DISTANCE_X,
                        OVERSCROLL_DISTANCE_Y,
                        false);
            }

            if (fScroller.isFinished())
                stopInteracting();

            postInvalidate();
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        scrollTo(scrollX, scrollY);
        awakenScrollBars();
    }

    @Override
    protected int computeHorizontalScrollExtent() {
        //return getWidth();
        return getWidth() * getWidth() / fWorksheetWidth;
    }

    @Override
    protected int computeHorizontalScrollRange() {
        //return fWorksheetWidth;
    	return getWidth() - computeHorizontalScrollExtent();
    }

    @Override
    protected int computeHorizontalScrollOffset() {
        //return getScrollX();
    	return getScrollX() * computeHorizontalScrollRange() / (fWorksheetWidth - getWidth());
    }

    @Override
    protected int computeVerticalScrollExtent() {
        return getHeight();
    }

    @Override
    protected int computeVerticalScrollRange() {
        return fWorksheetHeight;
    }

    @Override
    protected int computeVerticalScrollOffset() {
        return getScrollY( );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GRAY);
       
        Paint paint = getPaint();
        
        if (fInteracting) ;

        paint.setColor(Color.GREEN);
        canvas.drawRect(0, 0, fWorksheetWidth, fWorksheetHeight, paint);
        Log.d(TAG, "onDraw executed scrollX is " + getScrollX());
        paint.setColor(Color.RED);
        for (int i = 0; i <= 500; i += 50) {
            canvas.drawLine(i, 0, i, 500, paint);
            canvas.drawLine(0, i, 500, i, paint);
        }

        canvas.drawRect(fWorksheetWidth - 50, 0, fWorksheetWidth, fWorksheetHeight, paint);
    }
    
    private Paint getPaint() {
    	if (mPaint == null) {
    		mPaint = new Paint();
    	}
    	return mPaint;
    }
    
    @Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		fWorksheetHeight = getHeight();
	}
    
    /** 
     * 添加用户的速度跟踪器 
     */  
    private void addVelocityTracker(MotionEvent event) {
        if (fVelocityTracker == null) {  
        	fVelocityTracker = VelocityTracker.obtain();  
        }  
        fVelocityTracker.addMovement(event);  
    }  
  
    /** 
     * 移除用户速度跟踪器 
     */  
    private void recycleVelocityTracker() {
        if (fVelocityTracker != null) {  
        	fVelocityTracker.recycle();  
        	fVelocityTracker = null;  
        }  
    }
    
    /** 
     * 获取X方向的滑动速度,大于0向右滑动，反之向左 
     */  
    private int getScrollVelocityX() {
    	fVelocityTracker.computeCurrentVelocity(1000);  
        int velocity = (int) fVelocityTracker.getXVelocity();  
        return velocity;  
    }
    
    /** 
     * 获取Y方向的滑动速度,大于0向上滑动，反之向下 
     */  
    private int getScrollVelocityY() {
    	fVelocityTracker.computeCurrentVelocity(1000);  
        int velocity = (int) fVelocityTracker.getYVelocity();  
        return velocity;  
    }  
}
