package com.dsm.utils.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {
	private SparseArray<View> mViews = null;  
	private View mConvertView;
	private int mPosition;
	private int mLayoutId;
	
	public ViewHolder(Context context, ViewGroup parent, int layout_id, int position) {
		mViews = new SparseArray<View>();
		mLayoutId = layout_id;
		mPosition = position;
		mConvertView = LayoutInflater.from(context).inflate(layout_id, parent, false);
		mConvertView.setTag(this);
	}
	
	/** 
     * 拿到一个ViewHolder对象 
     */  
	public static ViewHolder get(Context context, View convertView,  
            ViewGroup parent, int layoutId, int position) {  
        if (convertView == null) {  
            return new ViewHolder(context, parent, layoutId, position);  
        }  
        return (ViewHolder) convertView.getTag();  
    }
	
	/** 
	 * 通过控件的Id获取对于的控件，如果没有则加入views 
	 */  
    @SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId) {    
        View view = mViews.get(viewId);  
        if (view == null) {  
            view = mConvertView.findViewById(viewId);  
            mViews.put(viewId, view);  
        }  
        return (T) view;  
    }  
  
    public View getConvertView() {  
        return mConvertView;  
    }
    
    public int getPosition() {
		return mPosition;
	}

	public int getLayoutId(){
		return mLayoutId;
	}
	
	
    /** 
     * 为TextView设置字符串 
     */  
    public ViewHolder setText(int viewId, String text) {  
        TextView view = getView(viewId);  
        view.setText(text);  
        return this;  
    }
    
    /** 
     * 为Button设置字符串 
     */
    public ViewHolder setTextForButton(int viewId, String text) {
    	Button view = getView(viewId);
    	view.setText(text);
    	return this;
    }
    
    /** 
     * 为EditText设置字符串 
     */
    public ViewHolder setTextForEditText(int viewId, String text) {
    	EditText view = getView(viewId);
    	view.setText(text);
    	return this;
    }
    
    public ViewHolder setImageResource(int viewId, int resId) {
		ImageView view = getView(viewId);
		view.setImageResource(resId);
		return this;
	}
    
    /** 
     * 设置click事件
     */  
    public ViewHolder setOnClickListener(int viewId, OnClickListener l) {    
        View view = getView(viewId);
        view.setOnClickListener(l);
        return this;  
    }
}
