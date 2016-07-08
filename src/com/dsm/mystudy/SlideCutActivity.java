package com.dsm.mystudy;

import java.util.ArrayList;
import java.util.List;

import com.dsm.customview.DsmListView;
import com.dsm.customview.SlideCutListView;
import com.dsm.customview.SlideCutListView.RemoveDirection;
import com.dsm.customview.SlideCutListView.RemoveListener;
import com.dsm.utils.adapter.CommonAdapter;
import com.dsm.utils.adapter.ViewHolder;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class SlideCutActivity extends Activity implements RemoveListener {

	private List<Integer> mLists = new ArrayList<Integer>();
	private CommonAdapter<Integer> mAdapter;
	private SlideCutListView mSlidecut;	
	//private DsmListView mSlidecut;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_slidecut_listview);
		
		initData();
		mSlidecut = (SlideCutListView)findViewById(R.id.slidecut_lv);
		mSlidecut.getBackground().setAlpha(155);
		mAdapter = new CommonAdapter<Integer>(this, mLists, R.layout.slidecut_listview_item) {
			@Override
			public void convertView(ViewHolder holder, Integer item) {
				holder.setText(R.id.tv_title, "SlideCutListView item " + item);
			}
		};
		mSlidecut.setAdapter(mAdapter);
		mSlidecut.setRemoveListener(this);
		mSlidecut.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getApplicationContext(), "click " + mLists.get(position), Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	private void initData() {
		for (int i = 0; i < 30; i ++) {
			mLists.add(i+1);
		}
	}

	@Override
	public void removeItem(RemoveDirection direction, int position) {	
		mLists.remove(mAdapter.getItem(position));
    	mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		mSlidecut.getBackground().setAlpha(255);
		super.onDestroy();
	}

}
