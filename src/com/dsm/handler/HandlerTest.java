package com.dsm.handler;

import java.util.Timer;
import java.util.TimerTask;

import com.dsm.mystudy.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/*
 * 深入理解Handler 、 Looper 、Message三者的关系
 * http://blog.csdn.net/lmj623565791/article/details/38377229
 */
public class HandlerTest extends Activity {

	private static final int TASK_1 = 1;
	private static final int TASK_2 = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		
		new Thread(new MyThread()).start();
		timer.schedule(task, 10000);
	}
	
	Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case HandlerTest.TASK_1:
				break;
			case HandlerTest.TASK_2:
				setTitle("Hello, Handler!");
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	class MyThread implements Runnable {
		@Override
		public void run() {
			 while (!Thread.currentThread().isInterrupted()) {    
                 
                 Message message = new Message();   
                 message.what = HandlerTest.TASK_1;   
                   
                 HandlerTest.this.myHandler.sendMessage(message);   
                 myHandler.sendMessageAtTime(message, 10000);
                 try {   
                      Thread.sleep(100);    
                 } catch (InterruptedException e) {   
                      Thread.currentThread().interrupt();   
                 }   
            }   
		}
	}
	
	Timer timer = new Timer();  
	TimerTask task = new TimerTask(){    
        public void run() {  
            Message message = new Message();      
            message.what = HandlerTest.TASK_2;      
            myHandler.sendMessage(message);    
        }            
    }; 
    
    public void handlerTest() {
    	myHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// do something
			}
		}, 2000);
    }
}
