package com.dsm.netty;

import io.netty.channel.ChannelHandlerContext;

import com.dsm.broadcast.BroadcastUtils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class CoreService extends Service{

	private static final String TAG = CoreService.class.getSimpleName();
	private MyBinder mBinder = new MyBinder(); 
	
	private NetUdpReceiver mNetUdpReceiver = new NetUdpReceiver();
	private NetTcpConnector mNetTcpConnector = new NetTcpConnector();
	private NetTcpReceiver mNetTcpReceiver = new NetTcpReceiver();
	
	// 广播接收器
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			 
            // 网络变化的广播
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            	if (!BroadcastUtils.isNetworkAvailable(context)) {
            		Toast.makeText(context, "network disconnected!", Toast.LENGTH_SHORT).show();
            	} else {
            		Toast.makeText(context, "network connected!", Toast.LENGTH_SHORT).show();
            	}
            }
		}
	};
	 
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		mBroadcastReceiver = new BroadcastUtils.SystemBroadcastReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(mBroadcastReceiver, filter);
		
		mNetUdpReceiver.startServer(8100, new UdpFrameHandler());
	}
	
	@Override  
    public int onStartCommand(Intent intent, int flags, int startId) { 
        return super.onStartCommand(intent, flags, startId);
    }  
	
	@Override
	public void onDestroy() {
		unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}
	
	class MyBinder extends Binder {
		public void startDownload() {  
			Log.d(TAG, "startDownload() executed");
	        new Thread(new Runnable() {  
	            @Override  
	            public void run() {  
	                // 执行具体的下载任务  
	            }  
	        }).start();  
	    }  
	}

	// 网络请求处理
	public class UdpFrameHandler extends FrameHandler{
		@Override
		protected void channelRead0(ChannelHandlerContext ctx, FrameData msg) throws Exception {
			String text = "peer_ip:" + msg.peer_address.getHostName();
			System.out.println(text);
			
			Log.d(TAG, "Receive Broadcast to find device");
	    	String host_ip = msg.peer_address.getAddress().getHostAddress();
	    	
	    	//TCPFrameHandler frameHandler = new TCPFrameHandler();
			//frameHandler.setOwner(mMyTcpConnector);
			//mMyTcpConnector.connect(host_ip, 6788, frameHandler);
		}
	    
	    @Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
	    	cause.printStackTrace();
	    	super.exceptionCaught(ctx, cause);
	    }
	}
}
