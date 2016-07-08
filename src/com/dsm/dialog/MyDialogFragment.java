package com.dsm.dialog;

import com.dsm.mystudy.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MyDialogFragment extends DialogFragment {

	public OnConfirmListener mOnConfirmListener = null;
	
	public interface OnConfirmListener {
		public void onConfirm();
	}
	
	public void setOnConfirmListener(OnConfirmListener l) {
		mOnConfirmListener = l;
	}
	
	@SuppressLint("InflateParams")
	@Override  
	public Dialog onCreateDialog(Bundle savedInstanceState) {	
		Bundle bundle = getArguments();
		final Dialog mDeleteDialog = new Dialog(getActivity(), R.style.CustomDialog);
		View viewDia = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dialog, null);
		
		TextView title = (TextView)viewDia.findViewById(R.id.tv_title);
		title.setText(bundle.getString("title"));
		TextView content = (TextView)viewDia.findViewById(R.id.tv_content);
		content.setText(bundle.getString("content"));
		
		Button ensure = (Button)viewDia.findViewById(R.id.ensure_exit);
		Button cancel = (Button)viewDia.findViewById(R.id.cancel_exit);
		ensure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mOnConfirmListener != null) {
					mOnConfirmListener.onConfirm();
				}
				mDeleteDialog.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDeleteDialog.dismiss();
			}
		});
		
		mDeleteDialog.setCanceledOnTouchOutside(false);
		mDeleteDialog.setContentView(viewDia);

		return mDeleteDialog;
	}
}
