package com.dsm.customview;

import java.util.ArrayList;
import java.util.List;

import com.dsm.customview.SlidingMenuLayout.DragListener;
import com.dsm.mystudy.R;
import com.dsm.utils.adapter.CommonAdapter;
import com.dsm.utils.adapter.ViewHolder;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

/*
 * 细雨斜风作晓寒，淡烟疏柳媚晴滩。入淮清洛渐漫漫，雪沫乳花浮午盏。蓼茸蒿笋试春盘，人间有味是清欢。--浣溪沙 苏轼
 */
public class VDHLayoutActivity extends Activity {

	private SlidingMenuLayout mSlideLayout;
	private ListView mSetList;
	private ImageView mLogo;
	private ImageView mIcon;
	private List<String> mLists = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_vdh_layout);
		
		initDatas();
		initViews();
		initDragLayout();
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
				if (mSlideLayout.getStatus() == SlidingMenuLayout.Status.Close) {
					mSlideLayout.open();
				} else {
					mSlideLayout.close();
				}
			}
		});
		
		mIcon = (ImageView) findViewById(R.id.iv_icon);
		mIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ObjectAnimator anim = ObjectAnimator.ofFloat(mIcon, "T.C", 1.0f, 1.2f, 1.0f);
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
	
	private void initDragLayout() {
		mSlideLayout = (SlidingMenuLayout) findViewById(R.id.slidemenu);
		mSlideLayout.setDragListener(new DragListener() {
			@Override
			public void onOpen() {
			}

			@Override
			public void onClose() {
			}

			@Override
			public void onDrag(float percent) {
				mLogo.setAlpha(1 - percent);
			}
		});
	}
	
	/*
	 * badminton
	 * January, February, March, April, May, june
	 * The icy wind blew hard.
	 * 
	 * Be carefully, tomorrow will be icy clod.
	 * It was a beautiful frosty morning.
	 * He breathed in the frosty air.
	 * It's freezing in the house. Can I trun on the heating?
	 * It was freezing out there.
	 * 阴冷，湿冷；冰冷；霜冻；冷冻的，冻结的
	 * Icy, Frosty, Freeze, Freezing
	 * Yesterday was very chilly.
	 * I'm a bit chilly.
	 */
	
}
