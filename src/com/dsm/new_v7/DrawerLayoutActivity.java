package com.dsm.new_v7;

import java.util.ArrayList;
import java.util.List;

import com.dsm.mystudy.BaseActivity;
import com.dsm.mystudy.R;
import com.dsm.utils.adapter.CommonAdapter;
import com.dsm.utils.adapter.ViewHolder;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class DrawerLayoutActivity extends BaseActivity {

	private DrawerLayout mDrawerLayout;
	private View mContentView;
	private ListView mSetList;
	private ImageView mLogo;
	private ImageView mIcon;
	private List<String> mLists = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawerlayout);
		
		initDatas();
		initViews();
	}

	private void initDatas() {
		mLists.add("我的设备");
		mLists.add("设备");
		mLists.add("添加设备");
		mLists.add("设备管理");
		mLists.add("报警管理");
		mLists.add("信息中心");
		mLists.add("帮助");
		mLists.add("关于");
	}
	
	private void initViews() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
		mContentView = findViewById(R.id.drawerview);
		
		mSetList = (ListView) findViewById(R.id.lv_setting);
		mSetList.setAdapter(new CommonAdapter<String>(this, mLists, R.layout.slidecut_listview_item ) {
			@Override
			public void convertView(ViewHolder holder, String item) {
				holder.setText(R.id.tv_title, item);
			}
		});
		
		mLogo = (ImageView) findViewById(R.id.iv_logo);
		mLogo.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (mDrawerLayout.isDrawerOpen(mContentView)) {
					mDrawerLayout.closeDrawer(mContentView);
				} else {
					mDrawerLayout.openDrawer(mContentView);
				}
			}
		});

		mIcon = (ImageView) findViewById(R.id.iv_icon);
		mIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ObjectAnimator anim = ObjectAnimator.ofFloat(mIcon, "cxc", 1.0f, 1.2f, 1.0f);
		        anim.setDuration(500);
		        anim.start();
		        anim.addUpdateListener(new AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						float cVal = (Float) animation.getAnimatedValue();  
						mIcon.setScaleX(cVal);  
						mIcon.setScaleY(cVal); 
					}
				});
			}
		});

	}
}
