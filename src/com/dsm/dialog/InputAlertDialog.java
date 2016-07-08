package com.dsm.dialog;

import com.dsm.mystudy.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class InputAlertDialog extends DialogFragment {

public OnDialogListener mOnConfirmListener = null;
	
	private EditText mPasswd;
	public interface OnDialogListener {
		public void onConfirm(String result);
	}
	
	public void setOnConfirmListener(OnDialogListener l) {
		mOnConfirmListener = l;
	}
	
	class MyDialog extends Dialog {
		protected EditText mEtPasswd;
		
		public MyDialog(Context context, int themeResId) {
			super(context, themeResId);
		}

		@Override
		public void cancel() {
			InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE );
			imm.hideSoftInputFromWindow(mEtPasswd.getApplicationWindowToken(), 0);
			super.cancel();
		}
	}
	
	@SuppressLint("InflateParams")
	@Override  
	public Dialog onCreateDialog(Bundle savedInstanceState) {	
		final MyDialog mInputDialog = new MyDialog(getActivity(), R.style.CustomDialog);
		View viewDia = LayoutInflater.from(getActivity()).inflate(R.layout.app_mobile_openlock_input_dialog, null);
		TextView title = (TextView)viewDia.findViewById(R.id.tv_title);
		TextView content = (TextView)viewDia.findViewById(R.id.tv_content);
		
		Bundle bundle = getArguments();
		title.setText(bundle.getString("title"));
		content.setText(bundle.getString("content"));
		
		mInputDialog.mEtPasswd = mPasswd = (EditText)viewDia.findViewById(R.id.et_openlockpwd);
		mPasswd.setRawInputType(InputType.TYPE_CLASS_NUMBER);
		mPasswd.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String passwd = mPasswd.getText().toString().trim();
				if (passwd.length() >= 6) {
					HideKeyboard(mPasswd);
					mInputDialog.dismiss();
					if (mOnConfirmListener != null) {	
						mOnConfirmListener.onConfirm(passwd);
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		mInputDialog.setCanceledOnTouchOutside(true);
		mInputDialog.setContentView(viewDia);
		
		return mInputDialog;
	}
	
	// 隐藏虚拟键盘
    public static void HideKeyboard(View view) {
    	InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE );     
    	imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }
    
    // 显示虚拟键盘
    public static void ShowKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE );
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

	@Override
	public void onResume() {
		super.onResume();
		
		new Handler().postDelayed(new Runnable() { // Open the input method after the dialog be showed
			public void run() { 
				ShowKeyboard(mPasswd); 
			} 
		}, 50); 
	}

}
