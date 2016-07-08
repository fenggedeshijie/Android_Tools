package com.dsm.utils.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context mContext;
	protected List<T> mDatas;
	protected final int mItemLayoutId;
	
	public CommonAdapter(Context context, List<T> data, int layoutid) {
		mContext = context;
		mDatas = data;
		mItemLayoutId = layoutid;
	}
	
	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
		convertView(holder, getItem(position));
		return holder.getConvertView();
	}
	
	public abstract void convertView(ViewHolder holder, T item);
	
}
