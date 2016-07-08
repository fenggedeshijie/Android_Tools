package com.dsm.rxjava;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.dsm.mystudy.R;

public class RxjavaMainFragment extends Fragment implements OnClickListener{

	public static final String BUTTON_01 = "button_01";
	public static final String BUTTON_02 = "button_02";
	public static final String BUTTON_03 = "button_03";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_rxjava_main, container, false);
		
		initViews(view);
		
		return view;
	}
	
	private void initViews(View view) {
		Button button_1 = (Button)view.findViewById(R.id.button_1);
		Button button_2 = (Button)view.findViewById(R.id.button_2);
		Button button_3 = (Button)view.findViewById(R.id.button_3);
		
		Bundle bundle = getArguments();
		if (bundle != null) {
			String button1 = bundle.getString(BUTTON_01);
			if (button1 != null) {
				button_1.setText(button1);
			}
			
			String button2 = bundle.getString(BUTTON_02);
			if (button2 != null) {
				button_2.setText(button2);
			}
			
			String button3 = bundle.getString(BUTTON_03);
			if (button3 != null) {
				button_3.setText(button3);
			}
		}
		
		button_1.setOnClickListener(this);
		button_2.setOnClickListener(this);
		button_3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_1:
			if (mButtonListener != null) {
				mButtonListener.onButton1Click();
			}
			break;
		case R.id.button_2:
			if (mButtonListener != null) {
				mButtonListener.onButton2Click();
			}
			break;
		case R.id.button_3:
			if (mButtonListener != null) {
				mButtonListener.onButton3Click();
			}
			break;
		}
	}
	
	private FButtonClickListener mButtonListener = null;
	public interface FButtonClickListener {
		public void onButton1Click();
		public void onButton2Click();
		public void onButton3Click();
	}
	
	public void setFButtonClickListener(FButtonClickListener l) {
		mButtonListener = l;
	}
	
	static class FButtonClick implements FButtonClickListener {
		@Override
		public void onButton1Click() {}

		@Override
		public void onButton2Click() {}

		@Override
		public void onButton3Click() {}
	}
}
