/*    
    // View Touch Event Handler
	* 
	 * Pass the touch screen motion event down to the target view, or this 
	 * view if it is the target. 
	 * 
	 * @param event The motion event to be dispatched. 
	 * @return True if the event was handled by the view, false otherwise. 
	  * *
	public boolean dispatchTouchEvent(MotionEvent event) {  
	  	// Other ...
	  	
	    if (mOnTouchListener != null && (mViewFlags & ENABLED_MASK) == ENABLED &&  
	            mOnTouchListener.onTouch(this, event)) {  
	        return true;  
	    }  
	    return onTouchEvent(event);  
	}
              
   	*
     * Implement this method to handle touch screen motion events. 
     * 
     * @param event The motion event. 
     * @return True if the event was handled, false otherwise. 
      * *
	public boolean onTouchEvent(MotionEvent event) {  
		final int viewFlags = mViewFlags;  
	  
	    if ((viewFlags & ENABLED_MASK) == DISABLED) {  
	    	// A disabled view that is clickable still consumes the touch  
	    	// events, it just doesn't respond to them.  
	        return (((viewFlags & CLICKABLE) == CLICKABLE ||  
	                    (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE));  
	    }  
	  
	    if (mTouchDelegate != null) {  
	        if (mTouchDelegate.onTouchEvent(event)) {  
	            return true;  
	        }  
	    }  
	  
	    if (((viewFlags & CLICKABLE) == CLICKABLE ||  
	                (viewFlags & LONG_CLICKABLE) == LONG_CLICKABLE)) {
	        return true;
	    }
	}
                      

	// ViewGroup Touch Event Handler

	View mTarget = null;	// 保存捕获Touch事件处理的View
	public boolean dispatchTouchEvent(MotionEvent ev) {
	
	    // ....其他处理，在此不管
	    
	    if(ev.getAction()==KeyEvent.ACTION_DOWN){	// down事件只是用于寻找消耗事件的目标view或viewgroup
	        // 每次Down事件，都置为Null
			if (mTarget != null) {  
            	mTarget = null;  
        	}
        
	 		boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0; 
	 		 
	      	if(disallowIntercept || !onInterceptTouchEvent()){
		        View[] views = getChildView();
		        for(int i = views.length -1; i >= 0; i --){
		        	child.getHitRect(frame);  
	                if (frame.contains(scrolledXInt, scrolledYInt)) {
		                if(views[i].dispatchTouchEvent(ev))
			                mTarget = views[i];
			                return true;
		                }
		            }
		        }
	      	}
	    }
	    
	    // 当子View没有捕获down事件时，ViewGroup自身处理。这里处理的Touch事件包含Down、Up和Move
	    if(mTarget == null){
	        return super.dispatchTouchEvent(ev);
	    }
	    
	    // ...其他处理，在此不管
	    if(!disallowIntercept && onInterceptTouchEvent()){
			// 给mTarget分发一个Action_Cancel事件
	    	ev.setAction(MotionEvent.ACTION_CANCEL);  
	        if (!mTarget.dispatchTouchEvent(ev)) {  
	        } 
	        // clear the target  
	        mTarget = null; 
	        
	        // Don't dispatch this event to our own view, because we already  
	        // saw it when intercepting; we just want to give the following  
	        // event to the normal onTouchEvent(). 
	        return true;  
	    }
	    
	    // 这一步在Action_Down中是不会执行到的，只有Move和UP才会执行到。
	    return mTarget.dispatchTouchEvent(ev);
	}               
  
 */


/*
 	public boolean dispatchTouchEvent(MotionEvent ev) {  
    final int action = ev.getAction();  
    final float xf = ev.getX();  
    final float yf = ev.getY();  
    final float scrolledXFloat = xf + mScrollX;  
    final float scrolledYFloat = yf + mScrollY;  
    final Rect frame = mTempRect;  
    boolean disallowIntercept = (mGroupFlags & FLAG_DISALLOW_INTERCEPT) != 0;
    if (action == MotionEvent.ACTION_DOWN) {  
        if (mMotionTarget != null) {  
            mMotionTarget = null;  
        }  
        if (disallowIntercept || !onInterceptTouchEvent(ev)) {  
            ev.setAction(MotionEvent.ACTION_DOWN);  
            final int scrolledXInt = (int) scrolledXFloat;  
            final int scrolledYInt = (int) scrolledYFloat;  
            final View[] children = mChildren;  
            final int count = mChildrenCount;  
            for (int i = count - 1; i >= 0; i--) {  
                final View child = children[i];  
                if ((child.mViewFlags & VISIBILITY_MASK) == VISIBLE  
                        || child.getAnimation() != null) {  
                    child.getHitRect(frame);  
                    if (frame.contains(scrolledXInt, scrolledYInt)) {  
                        final float xc = scrolledXFloat - child.mLeft;  
                        final float yc = scrolledYFloat - child.mTop;  
                        ev.setLocation(xc, yc);  
                        child.mPrivateFlags &= ~CANCEL_NEXT_UP_EVENT;  
                        if (child.dispatchTouchEvent(ev))  {  
                            mMotionTarget = child;  
                            return true;  
                        }  
                    }  
                }  
            }  
        }  
    }  
    boolean isUpOrCancel = (action == MotionEvent.ACTION_UP) ||  
            (action == MotionEvent.ACTION_CANCEL);  
    if (isUpOrCancel) {  
        mGroupFlags &= ~FLAG_DISALLOW_INTERCEPT;  
    }  
    final View target = mMotionTarget;  
    if (target == null) {  
        ev.setLocation(xf, yf);  
        if ((mPrivateFlags & CANCEL_NEXT_UP_EVENT) != 0) {  
            ev.setAction(MotionEvent.ACTION_CANCEL);  
            mPrivateFlags &= ~CANCEL_NEXT_UP_EVENT;  
        }  
        return super.dispatchTouchEvent(ev);  
    }  
    if (!disallowIntercept && onInterceptTouchEvent(ev)) {  
        final float xc = scrolledXFloat - (float) target.mLeft;  
        final float yc = scrolledYFloat - (float) target.mTop;  
        mPrivateFlags &= ~CANCEL_NEXT_UP_EVENT;  
        ev.setAction(MotionEvent.ACTION_CANCEL);  
        ev.setLocation(xc, yc);  
        if (!target.dispatchTouchEvent(ev)) {  
        }  
        mMotionTarget = null;  
        return true;  
    }  
    if (isUpOrCancel) {  
        mMotionTarget = null;  
    }  
    final float xc = scrolledXFloat - (float) target.mLeft;  
    final float yc = scrolledYFloat - (float) target.mTop;  
    ev.setLocation(xc, yc);  
    if ((target.mPrivateFlags & CANCEL_NEXT_UP_EVENT) != 0) {  
        ev.setAction(MotionEvent.ACTION_CANCEL);  
        target.mPrivateFlags &= ~CANCEL_NEXT_UP_EVENT;  
        mMotionTarget = null;  
    }  
    return target.dispatchTouchEvent(ev);  
}  
 */

package com.dsm.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by zhy on 15/6/3.
 */
public class VDHLayout extends LinearLayout {
	private static final String TAG = "VDHLayout";
    private ViewDragHelper mDragger;

    private View mDragView;
    private View mAutoBackView;
    private View mEdgeTrackerView;

    private Point mAutoBackOriginPos = new Point();

    public VDHLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback()
        {
        	@Override
        	public int getOrderedChildIndex(int index) {
        		Log.d("Callback", "->getOrderedChildIndex");
                return index;
            }
        	 
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
            	Log.d("Callback", "->tryCaptureView");
                // mEdgeTrackerView禁止直接移动
                return child == mDragView || child == mAutoBackView;
            }
            
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
            	Log.d("Callback", "->clampViewPositionHorizontal");
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
            	Log.d("Callback", "->clampViewPositionVertical");
                return top;
            }

            //手指释放的时候回调
            @Override	// 通知消息
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
            	Log.d("Callback", "->onViewReleased");
                //mAutoBackView手指释放时可以自动回去
                if (releasedChild == mAutoBackView) {
                    mDragger.settleCapturedViewAt(mAutoBackOriginPos.x, mAutoBackOriginPos.y);
                    invalidate();
                }
            }
            
            @Override	// 通知消息
            public void onViewCaptured (View capturedChild, int activePointerId) {
            	Log.d("Callback", "->onViewCaptured");
            }
            
            @Override   // 通知消息
            public void onEdgeTouched (int edgeFlags, int pointerId) {
            	Log.d("Callback", "->onEdgeTouched");
            }
            
            //在边界拖动时回调
            @Override	// 通知消息
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            	Log.d("Callback", "->onEdgeDragStarted");
                mDragger.captureChildView(mEdgeTrackerView, pointerId);
            }
            
            @Override	// 通知消息
            public void onViewDragStateChanged (int state) {
            	Log.d("Callback", "->onViewDragStateChanged");
            	/*
            	 * 三种拖动状态
            	 * STATE_IDLE 
            	 * STATE_DRAGGING 
            	 * STATE_SETTLING
            	 */
            }
            
            @Override	// 通知消息
            public void onViewPositionChanged (View changedView, int left, int top, int dx, int dy) {
            	Log.d("Callback", "->onViewPositionChanged");
            }
            
            @Override
            public int getViewHorizontalDragRange(View child) {
            	Log.d("Callback", "->getViewHorizontalDragRange");
                return getMeasuredWidth()- child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(View child) {
            	Log.d("Callback", "->getViewVerticalDragRange");
                return getMeasuredHeight()- child.getMeasuredHeight();
            }
        });
        mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
    	//Log.d(TAG, "dispatchTouchEvent");
    	return super.dispatchTouchEvent(event);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
    	boolean ret = false;
    	
        switch(event.getAction()) {
        case MotionEvent.ACTION_DOWN:
             Log.d(TAG, "onInterceptTouchEvent action:ACTION_DOWN");
             break;
        case MotionEvent.ACTION_MOVE:
             Log.d(TAG, "onInterceptTouchEvent action:ACTION_MOVE");
             break;
        case MotionEvent.ACTION_UP:
             Log.d(TAG, "onInterceptTouchEvent action:ACTION_UP");
             break;
        case MotionEvent.ACTION_CANCEL:
             Log.d(TAG, "onInterceptTouchEvent action:ACTION_CANCEL");
             break;
        }
        
        Log.d("onInterceptTouchEvent", "DragState: " + mDragger.getViewDragState());
        ret = mDragger.shouldInterceptTouchEvent(event);
        Log.d("onInterceptTouchEvent", "DragState: " + mDragger.getViewDragState());
        return ret;
    }

    @SuppressLint("ClickableViewAccessibility")
	@Override
    public boolean onTouchEvent(MotionEvent event) {
    	int action = event.getAction();
        switch(action) {
        case MotionEvent.ACTION_DOWN:
             Log.d(TAG, "onTouchEvent action:ACTION_DOWN");
             break;
        case MotionEvent.ACTION_MOVE:
             Log.d(TAG, "onTouchEvent action:ACTION_MOVE");
             break;
        case MotionEvent.ACTION_UP:
             Log.d(TAG, "onTouchEvent action:ACTION_UP");
             break;
        case MotionEvent.ACTION_CANCEL:
             Log.d(TAG, "onTouchEvent action:ACTION_CANCEL");
             break;
        }
        Log.d("onTouchEvent", "DragState: " + mDragger.getViewDragState());
        mDragger.processTouchEvent(event);
        Log.d("onTouchEvent", "DragState: " + mDragger.getViewDragState());
        return true;
    }

    /**
     * 
     * Move the captured settling view by the appropriate amount for the current time.
     * If <code>continueSettling</code> returns true, the caller should call it again
     * on the next frame to continue.
     *
     * @param deferCallbacks true if state callbacks should be deferred via posted message.
     *                       Set this to true if you are calling this method from
     *                       {@link android.view.View#computeScroll()} or similar methods
     *                       invoked as part of layout or drawing.
     * @return true if settle is still in progress
     */
    /*
    public boolean continueSettling(boolean deferCallbacks) {
        if (mDragState == STATE_SETTLING) {
            boolean keepGoing = mScroller.computeScrollOffset();
            final int x = mScroller.getCurrX();
            final int y = mScroller.getCurrY();
            final int dx = x - mCapturedView.getLeft();
            final int dy = y - mCapturedView.getTop();
     
            if (dx != 0) {
                mCapturedView.offsetLeftAndRight(dx);
            }
            if (dy != 0) {
                mCapturedView.offsetTopAndBottom(dy);
            }
     
            if (dx != 0 || dy != 0) {
                mCallback.onViewPositionChanged(mCapturedView, x, y, dx, dy);
            }
     
            if (keepGoing && x == mScroller.getFinalX() && y == mScroller.getFinalY()) {
                // Close enough. The interpolator/scroller might think we're still moving
                // but the user sure doesn't.
                mScroller.abortAnimation();
                keepGoing = mScroller.isFinished();
            }
     
            if (!keepGoing) {
                if (deferCallbacks) {
                    mParentView.post(mSetIdleRunnable);
                } else {
                    setDragState(STATE_IDLE);
                }
            }
        }
     
        return mDragState == STATE_SETTLING;
    }
    */
    
    @Override
    public void computeScroll()
    {
        if(mDragger.continueSettling(true))
        {
        	ViewCompat.postInvalidateOnAnimation(this);
            //invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        mAutoBackOriginPos.x = mAutoBackView.getLeft();
        mAutoBackOriginPos.y = mAutoBackView.getTop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mDragView = getChildAt(0);
        mAutoBackView = getChildAt(1);
        mEdgeTrackerView = getChildAt(2);
        /*
        mAutoBackView.setOnTouchListener(new OnTouchListener(){

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
		        switch(action) {
		        case MotionEvent.ACTION_DOWN:
		             Log.d("CustomImageView", "onTouch_Listener action:ACTION_DOWN");
		             break;
		        case MotionEvent.ACTION_MOVE:
		             Log.d("CustomImageView", "onTouch_Listener action:ACTION_MOVE");
		             break;
		        case MotionEvent.ACTION_UP:
		             Log.d("CustomImageView", "onTouch_Listener action:ACTION_UP");
		             break;
		        case MotionEvent.ACTION_CANCEL:
		             Log.d("CustomImageView", "onTouch_Listener action:ACTION_CANCEL");
		             break;
		        }
		        return false;
			}
        });
		*/
        mAutoBackView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "ImageView Click Event", Toast.LENGTH_SHORT).show();
			}
			
        });
		
        mEdgeTrackerView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "Click Event", Toast.LENGTH_SHORT).show();
			}        	
        });
    }
}
