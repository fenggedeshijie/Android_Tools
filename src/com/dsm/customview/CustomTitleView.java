package com.dsm.customview;

import com.dsm.mystudy.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class CustomTitleView extends View {

	private String mTitleText;
	private int mTextColor;
	private int mTextSize;
	
	// 绘制时控制文本绘制的范围
    private Rect mBound;  
    private Paint mPaint;
	
	public CustomTitleView(Context context) {
		super(context, null);
	}
	
	public CustomTitleView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}
	
	public CustomTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		TypedArray attrArray = context.getTheme()
				.obtainStyledAttributes(attrs, R.styleable.CustomTitleView, defStyleAttr, 0);
		for (int i = 0; i < attrArray.getIndexCount(); i ++) {
			int attr = attrArray.getIndex(i);
			
			switch (attr) {
			case R.styleable.CustomTitleView_titleText:
				mTitleText = attrArray.getString(attr);
				break;
			case R.styleable.CustomTitleView_titleTextColor:
				// 如果未设置该属性，则默认颜色为黑色  
				mTextColor = attrArray.getColor(attr, Color.BLACK);
				break;
			case R.styleable.CustomTitleView_titleTextSize:
				// 默认设置为16sp，TypeValue也可以把sp转化为px 
				mTextSize = attrArray.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(  
                        TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
				break;
			}
		}
		attrArray.recycle();
		
		this.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				
			}
		});
		
		// 获得绘制文本的宽和高 
        mPaint = new Paint();  
        mBound = new Rect();
        mPaint.setTextSize(mTextSize);  
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound); 
	}

	@Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);  
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);  
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
	    int width, height ;  
	    
	    if (widthMode == MeasureSpec.EXACTLY) {  
	        width = widthSize;  
	    } else {  
	        float textWidth = mBound.width();  
	        int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());  
	        width = desired;  
	    }  
		
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {  
        mPaint.setColor(Color.YELLOW);  
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);  
  
        mPaint.setColor(mTextColor);  
        canvas.drawText(mTitleText, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);  
    }  
}
