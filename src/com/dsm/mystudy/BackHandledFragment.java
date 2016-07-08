package com.dsm.mystudy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

public abstract class BackHandledFragment extends Fragment {
	
	private final static String TAG = BackHandledFragment.class.getSimpleName();
	protected BackHandledInterface mBackHandledInterface;
	
	public interface BackHandledInterface {
		public abstract void setSelectedFragment(BackHandledFragment selectedFragment);  
	}
	
	public abstract boolean onBackPressed();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "getActivity()=" + getActivity());
		
		if (!(getActivity() instanceof BackHandledInterface)) {
			throw new ClassCastException("Hosting activity must implements BackHandledInterface");
		} else {
			this.mBackHandledInterface = (BackHandledInterface)getActivity();
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		
		// 告诉FragmentActivity, 当前Fragment在栈顶
		mBackHandledInterface.setSelectedFragment(this);
	}
	
}
