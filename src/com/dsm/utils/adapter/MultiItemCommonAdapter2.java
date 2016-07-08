package com.dsm.utils.adapter;

import java.util.List;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;

public abstract class MultiItemCommonAdapter2<T> extends CommonAdapter<T> {
	protected SparseIntArray mTypes = null;  
	public MultiItemCommonAdapter2(Context context, List<T> data) {
		super(context, data, -1);
		mTypes = new SparseIntArray();
	}

	@Override
	public abstract int getViewTypeCount();

	@Override
	public abstract int getItemViewType(int position);
	
	public abstract int getLayoutId(int position, T item);
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int layoutId = getLayoutId(position, getItem(position));
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent, layoutId, position);
		convertView(viewHolder, getItem(position));
		return viewHolder.getConvertView();
	}
}
