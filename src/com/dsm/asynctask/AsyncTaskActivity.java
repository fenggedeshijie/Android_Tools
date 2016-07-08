package com.dsm.asynctask;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dsm.mystudy.BaseActivity;
import com.dsm.mystudy.R;

public class AsyncTaskActivity extends BaseActivity{

	private static final String TAG = AsyncTaskActivity.class.getSimpleName();
	private TextView mTextView;
	private ProgressBar mProgressBar;
	private Button mBtnExec;
	private Button mBtnCancel;
	private MyAsyncTask mTask;
	private Subscription mSubscription;
	
	@Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);

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
		mHeadtitle.setText("AsyncTask");

	}
    
    
    private void initContentViews(final Context context) {
    	mBtnExec = (Button) findViewById(R.id.btn_execu);
    	mBtnExec.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//mTask = new MyAsyncTask();
				//mTask.execute();
				mSubscription = startTaskByObservable();
			}
		});
    	
    	mBtnCancel = (Button) findViewById(R.id.btn_cancel);
    	mBtnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//mTask.cancel(true);
				//mTask = null;
				if (!mSubscription.isUnsubscribed()) {
					mSubscription.unsubscribe();
				}
			}
		});
    	
    	
    	Button result = (Button) findViewById(R.id.btn_result);
    	result.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mSubscription.isUnsubscribed()) {
					System.out.println("Has subscribed!");
				} else {
					System.out.println("Has not subscribed!");
				}
			}
		});
    	
    	mTextView = (TextView)findViewById(R.id.tv_async);
		mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);
    }
    
    private class MyAsyncTask extends AsyncTask<Void, Integer, String> {

    	// onPreExecute方法用于在执行后台任务前做一些UI操作  
        @Override  
        protected void onPreExecute() {  
            Log.i(TAG, "onPreExecute() called");  
            mTextView.setText("loading...");  
        }  
          
        // doInBackground方法内部执行后台任务,不可在此方法内修改UI  
		@Override
		protected String doInBackground(Void... params) {
			int count = 100;
			while(count > 0) {
				try {
					publishProgress(100 - count);
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				count --;
			}
			return "Load complete";
		}

		// onProgressUpdate方法用于更新进度信息  
        @Override  
        protected void onProgressUpdate(Integer... progresses) {  
            Log.i(TAG, "onProgressUpdate(Progress... progresses) called");  
            mProgressBar.setProgress(progresses[0]);  
            mTextView.setText("loading..." + progresses[0] + "%");  
        }  
          
        // onPostExecute方法用于在执行完后台任务后更新UI,显示结果  
        @Override  
        protected void onPostExecute(String result) {  
            Log.i(TAG, "onPostExecute(Result result) called");  
            mTextView.setText(result);  
              
            mBtnExec.setEnabled(true);  
            mBtnCancel.setEnabled(false);  
        }  
          
        // onCancelled方法用于在取消执行中的任务时更改UI  
        @Override  
        protected void onCancelled() {  
            Log.i(TAG, "onCancelled() called");  
            mTextView.setText("cancelled");  
            mProgressBar.setProgress(0);  
              
            mBtnExec.setEnabled(true);  
            mBtnCancel.setEnabled(false);  
        }  
    	
    }

    private Subscription startTaskByObservable() {
    	return Observable.create(new OnSubscribe<Integer>() {
			@Override
			public void call(Subscriber<? super Integer> sub) {
				int count = 1;
				while (count <= 100) {
					try {
						sub.onNext(count);
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
						sub.onError(e);
					}
					count ++;
				}
				sub.onCompleted();
			}
		})
		.subscribeOn(Schedulers.computation())
		.doOnSubscribe(new Action0() {
			@Override
			public void call() {
				 mTextView.setText("Starting Task!");
				 mBtnExec.setEnabled(false);  
		         mBtnCancel.setEnabled(true);
			}
		})
		.doOnUnsubscribe(new Action0() {
			@Override
			public void call() {
				mTextView.setText("Cancel Task!");
				mBtnExec.setEnabled(true);  
	            mBtnCancel.setEnabled(false);
			}
		})
		.subscribeOn(AndroidSchedulers.mainThread())
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe(new Subscriber<Integer>() {
			@Override
			public void onCompleted() {
				mTextView.setText("Ending Task!");   
			}

			@Override
			public void onError(Throwable arg0) {
				 mTextView.setText("error!");
			}

			@Override
			public void onNext(Integer count) {
				mProgressBar.setProgress(count);  
		        mTextView.setText("loading... " + count + "%");
			}
			
		});

    }
}
