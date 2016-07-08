package com.dsm.customview;

import com.dsm.mystudy.R;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;

@SuppressLint("ClickableViewAccessibility")
public class DsmListView extends ListView {

	private View mDeleteRename;
	
	// 当前滑动的ListView　position   
    private int slidePosition;
    private int lastPosition = AdapterView.INVALID_POSITION;
    
    // 手指按下时X，Y的坐标 
    private int downX;
    private int downY;
    
    // ListView的item  
    private View itemView;
    
    private boolean isSlide = false;  // 是否响应滑动，默认为不响应
    private boolean isSpecialListView = true;
    
    private int mTouchSlop;  	// 认为是用户滑动的最小距离 
    
	public DsmListView(Context context) {
		this(context, null);
	}
	
	public DsmListView(Context context, AttributeSet attrs) {
		super(context, attrs);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
            downX = (int) event.getX();  
            downY = (int) event.getY();  
  
            slidePosition = pointToPosition(downX, downY);  
  
            // 无效的position, 不做任何处理  
            if (slidePosition == AdapterView.INVALID_POSITION) {  
                return super.dispatchTouchEvent(event);  
            }  
  
            // 获取我们点击的item view  
            itemView = getChildAt(slidePosition - getFirstVisiblePosition());
            mDeleteRename = itemView.findViewById(R.id.id_listview_delete_rename);
            if (mDeleteRename == null) {
            	isSpecialListView = false;
            }
            break;
        case MotionEvent.ACTION_MOVE:
            if (Math.abs(downX  - event.getX()) > mTouchSlop*2 && Math.abs(downY - event.getY())
                    <= Math.abs(downX - event.getX())) {  
                isSlide = true;
            }  
            break;  
		}
       
        return super.dispatchTouchEvent(event);
	}
	
	@Override  
    public boolean onTouchEvent(MotionEvent ev) {
		if (isSpecialListView && isSlide && slidePosition != AdapterView.INVALID_POSITION) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            
            final int action = ev.getAction();  
            switch (action) {  
            case MotionEvent.ACTION_MOVE:
                int deltaX = downX - x;  
                downX = x;
                
                if (Math.abs(downY  - y) > mTouchSlop && Math.abs(downX - y) > Math.abs(deltaX)) {
                	isSlide = false;
                	ev.setAction(MotionEvent.ACTION_DOWN);
					dispatchTouchEvent(ev);
                	break;
                }
                
                if (Math.abs(deltaX) > mTouchSlop) {
                	 if (deltaX > 0) {
                     	goneAllChild();
                     	mDeleteRename.setVisibility(View.VISIBLE);
                     	//mDeleteRename.startAnimation(getInAnimation());
                     	startInAnimation(mDeleteRename);
                     	lastPosition = slidePosition;
                     } else if (lastPosition == slidePosition){
                     	mDeleteRename.setVisibility(View.GONE);
                     	mDeleteRename.startAnimation(getOutAnimation());
                     	lastPosition = AdapterView.INVALID_POSITION;
                     }
                }

                downY = y;
                break;  // 拖动的时候ListView不滚动  
            case MotionEvent.ACTION_UP:  
                isSlide = false;
                break;
            }
            
            return true;
        }  

        // 否则直接交给ListView来处理onTouchEvent事件  
        return super.onTouchEvent(ev);  
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (isSpecialListView && !isSlide && lastPosition != AdapterView.INVALID_POSITION) {
			goneAllChild();
			int firstPosition = getFirstVisiblePosition();
			if (lastPosition - firstPosition >= 0 && lastPosition - firstPosition < getChildCount()) {
				showSingleChildeView(lastPosition - firstPosition);
			}
		}
	}

	private void goneAllChild() {
		for (int i = 0; i < getChildCount(); i ++) {
			goneSingleChildeView(i);
		}
	}

	private void goneSingleChildeView(int position) {
		getChildAt(position).findViewById(R.id.id_listview_delete_rename).setVisibility(View.GONE);
	}
	
	private void showSingleChildeView(int position) {
		getChildAt(position).findViewById(R.id.id_listview_delete_rename).setVisibility(View.VISIBLE);
	}
	
	private Animation getInAnimation() {
		TranslateAnimation translateAnim = new TranslateAnimation(
							Animation.RELATIVE_TO_SELF, 1.0f, 
							Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, 0, 
							Animation.RELATIVE_TO_SELF, 0);
		translateAnim.setDuration(300);
		return translateAnim;
	}
	
	private Animation getOutAnimation() {
		TranslateAnimation translateAnim = new TranslateAnimation(
							Animation.RELATIVE_TO_SELF, 0, 
							Animation.RELATIVE_TO_SELF, 1.0f,
							Animation.RELATIVE_TO_SELF, 0, 
							Animation.RELATIVE_TO_SELF, 0);
		translateAnim.setDuration(300);
		return translateAnim;
	}
	
	private void startInAnimation(View view) {
		mDeleteRename.measure(0, 0);
		ObjectAnimator
		.ofFloat(view, "translationX", mDeleteRename.getMeasuredWidth(), 0)
		.setDuration(300).start();
	}
	
	
}
