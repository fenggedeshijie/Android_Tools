package com.dsm.rxjava;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsm.mystudy.BaseActivity;
import com.dsm.mystudy.R;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class RxjavaActivity extends BaseActivity {

	private int count = 0;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xml_for_rxjava);
        
        initHeadViews();
        initContentViews();
    }
	
	@TargetApi(Build.VERSION_CODES.KITKAT)
	private void initHeadViews() {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);     // 透明状态栏  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); // 透明导航栏 
        
        mHeadback = (ImageView) this.findViewById(R.id.im_headback);
		mHeadback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (count <= 1) {
					finish();
				} else {
					getSupportFragmentManager().popBackStack();
					count --;
				}
			}
		});
		mHeadtitle = (TextView) this.findViewById(R.id.im_headtitle);
		mHeadtitle.setText("Rxjava学习");
	}
	
	private void initContentViews() {
		final FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transation =  fm.beginTransaction();
		RxjavaMainFragment mainFragment = new RxjavaMainFragment();
		mainFragment.setFButtonClickListener(new RxjavaMainFragment.FButtonClick() {
			@Override
			public void onButton1Click() {
				FragmentTransaction t =  fm.beginTransaction();
				RxjavaBasicFragment basicFragment = new RxjavaBasicFragment();
				t.addToBackStack(null);
				t.replace(R.id.id_content, basicFragment, "RxjavaBasicFragment");
				t.commit();
				count ++;
			}
			
			@Override
			public void onButton2Click() {
				FragmentTransaction t =  fm.beginTransaction();
				RxjavaOperatorFragment operatorFragment = new RxjavaOperatorFragment();
				t.addToBackStack(null);
				t.replace(R.id.id_content, operatorFragment, "RxjavaOperatorFragment");
				t.commit();
				count ++;
			}
			
			@Override
			public void onButton3Click() {
				FragmentTransaction t =  fm.beginTransaction();
				RxjavaFilterFragment filterFragment = new RxjavaFilterFragment();
				t.addToBackStack(null);
				t.replace(R.id.id_content, filterFragment, "RxjavaFilterFragment");
				t.commit();
				count ++;
			}
		});
		
		Bundle bundle = new Bundle();
		bundle.putString(RxjavaMainFragment.BUTTON_01, "Rxjava的基本知识");
		bundle.putString(RxjavaMainFragment.BUTTON_02, "Rxjava的transform操作符");
		bundle.putString(RxjavaMainFragment.BUTTON_03, "Rxjava的Filtering操作符");
		mainFragment.setArguments(bundle);
		transation.add(R.id.id_content, mainFragment, "RxjavaMainFragment");
		transation.commit();
		count ++;
	}
	
}
