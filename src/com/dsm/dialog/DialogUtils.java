package com.dsm.dialog;

import com.dsm.mystudy.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

public class DialogUtils {

	public static class DsmProgressDialog extends Dialog{
		
		public DsmProgressDialog(Context context, String strMessage) {  
	        this(context, R.style.DsmCustomProgressDialog, strMessage);  
	    }  
	  
	    public DsmProgressDialog(Context context, int theme, String strMessage) {  
	        super(context, theme);  
	        this.setContentView(R.layout.app_layout_normal_progress_dialog);  
	        this.getWindow().getAttributes().gravity = Gravity.CENTER;  
	        TextView tvMsg = (TextView) this.findViewById(R.id.id_tv_loadingmsg);  
	        if (tvMsg != null) {  
	            tvMsg.setText(strMessage);  
	        }  
	    }  
	  
	    @Override  
	    public void onWindowFocusChanged(boolean hasFocus) {
	        if (!hasFocus) {  
	            dismiss();  
	        }  
	    }
	}
	
	public static Dialog showProgressDiaog(Context context, String title, boolean cancelable) {  
        Dialog dialog = new DsmProgressDialog(context, title); 
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }
}
