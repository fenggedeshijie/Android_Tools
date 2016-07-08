package com.dsm.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsm.mystudy.BaseActivity;
import com.dsm.mystudy.R;

public class NotificationActivity extends BaseActivity {

	public static NotificationManager notificationManager;
	
	@Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initHeadViews();
        initContentViews(this);
    }
    
    @TargetApi(Build.VERSION_CODES.KITKAT) 
	private void initHeadViews() {
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);     // 透明状态栏  
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION); // 透明导航栏 
        
		mHeadback = (ImageView) this.findViewById(R.id.im_headback);
		mHeadback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mHeadtitle = (TextView) this.findViewById(R.id.im_headtitle);
		mHeadtitle.setText("消息通知机制");
	}
    
    
    private void initContentViews(final Context context) {
    	
    	Button btn_notify = (Button) findViewById(R.id.btn_notify);
    	btn_notify.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendNotifyInformation(context);
			}
		});
    }
    
    private void sendNotifyInformation(Context context) {
    	// 发出通知
    	notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    	
		// 单击通知后的Intent，此例子单击后还是在当前页面
    	Intent intent = new Intent(context, NotificationActivity.class);
    	Bundle bundle = new Bundle();
    	bundle.putString("PushMessage", "Notification");
    	intent.putExtras(bundle);
    	
		PendingIntent pendIntent = PendingIntent.getActivity(context, 0, intent, 0);

		Notification notify = new Notification.Builder(context)
			.setSmallIcon(R.drawable.ic_launcher)
        	.setTicker("您有新的消息，请注意查收！")  
        	.setContentTitle("Notification Test")  
        	.setContentText("This is a notification test information!") 
        	.setContentIntent(pendIntent)
        	.build();
		
		notify.defaults |= Notification.DEFAULT_ALL;
		notify.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(1, notify);
    }
}
