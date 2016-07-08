package com.dsm.mystudy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import com.dsm.adapter.CommonAdapterActivity;
import com.dsm.adapter.MultiItemAdapterActivity;
import com.dsm.android.skill.AndroidSkillActivity;
import com.dsm.animation.AnimationActivity;
import com.dsm.animation.AnimationActivity1;
import com.dsm.animation.LayoutTransitionActivity;
import com.dsm.animation.PropertyAnimationActivity;
import com.dsm.asynctask.AsyncTaskActivity;
import com.dsm.broadcast.BroadcastActivity;
import com.dsm.customview.CoordinateActivity;
import com.dsm.customview.VDHLayoutActivity;
import com.dsm.dialog.DialogUtils;
import com.dsm.dialog.MyDialogFragment;
import com.dsm.dialog.MyDialogFragment.OnConfirmListener;
import com.dsm.inject.InjectViewActivity;
import com.dsm.new_v7.DrawerLayoutActivity;
import com.dsm.new_v7.RecyclerViewActivity;
import com.dsm.notification.NotificationActivity;
import com.dsm.regexp.RegExpUtil;
import com.dsm.retrofit.api.GCDevice;
import com.dsm.retrofit.api.LockManager;
import com.dsm.retrofit.api.LockOpendoorLog;
import com.dsm.retrofit.api.Result;
import com.dsm.retrofit.api.Result1;
import com.dsm.retrofit.api.Result2;
import com.dsm.retrofit.api.RetrofitActivity;
import com.dsm.retrofit_okhttp.OkHttpActivity;
import com.dsm.rxjava.RxjavaActivity;
import com.dsm.string_utils.StreamTools;
import com.dsm.utils.adapter.CommonAdapter;
import com.dsm.utils.adapter.ViewHolder;
import com.google.gson.internal.LinkedTreeMap;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

	List<LockOpendoorLog> mOpendoorRecord = new ArrayList<LockOpendoorLog>();
	List<Map<String, String>> mLists = new ArrayList<Map<String, String>>();
	
	private static Context mContext;
	
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        
        initDatas();
        initHeadViews();
        initContentViews();
        
        RegExpUtil.firstFunc();
        RegExpUtil.secondFunc();
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
		mHeadtitle.setText("主界面");
	}
	
    private void initDatas() {
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("Title", "Animation");
    	map.put("Activity", AnimationActivity.class.getName());
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "Animation1");
    	map.put("Activity", AnimationActivity1.class.getName());
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "PropertyAnimation");
    	map.put("Activity", PropertyAnimationActivity.class.getName());
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "LayoutTransition");
    	map.put("Activity", LayoutTransitionActivity.class.getName());
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "RxJava");
    	map.put("Activity", RxjavaActivity.class.getName());
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "FragmentDialog");
    	map.put("Function", "createProgressDialog");
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "Notification");
    	map.put("Activity", NotificationActivity.class.getName());
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "AsyncTask");
    	map.put("Activity", AsyncTaskActivity.class.getName());
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "Broadcast");
    	map.put("Activity", BroadcastActivity.class.getName());
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "Inject View");
    	map.put("Activity", InjectViewActivity.class.getName());
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "DynamicProxy");
    	map.put("Activity", DynamicProxyActivity.class.getName());
    	mLists.add(map);
    	
    	/*map = new HashMap<String, String>();
    	map.put("Title", "Inject Test");
    	map.put("Activity", InjectTestActivity.class.getName());
    	mLists.add(map);*/
    	
    	/*map = new HashMap<String, String>();
    	map.put("Title", "Retrofit");
    	map.put("Function", "queryRecordByRetrofit");
    	mLists.add(map);*/
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "Retrofit");
    	map.put("Activity", RetrofitActivity.class.getName());
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "Adapter");
    	map.put("Activity", MultiItemAdapterActivity.class.getName());
    	mLists.add(map);
    	
		map = new HashMap<String, String>();
    	map.put("Title", "RecycleView");
    	map.put("Activity", RecyclerViewActivity.class.getName());
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "TempTest");
    	map.put("Activity", TempTestActivity.class.getName());
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "Sliding delete");
    	map.put("Activity", SlideCutActivity.class.getName());
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "VDHLayout");
    	map.put("Activity", VDHLayoutActivity.class.getName());
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "DrawerLayout");
    	map.put("Activity", DrawerLayoutActivity.class.getName());
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "Android Coordinate System");
    	map.put("Activity", CoordinateActivity.class.getName());
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "Reflection System");
    	map.put("Activity", ReflectionActivity.class.getName());
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "Study Service");
    	map.put("Activity", ServiceStudyActivity.class.getName());
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "微信分享");
    	map.put("Function", "weixinSocialShare");
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "Android技巧");
    	map.put("Activity", AndroidSkillActivity.class.getName());
    	mLists.add(map);
    	
    	map = new HashMap<String, String>();
    	map.put("Title", "OkHttp");
    	map.put("Activity", OkHttpActivity.class.getName());
    	mLists.add(map);
   
    }
    
    private void initContentViews() {
    	
    	ListView mMainList = (ListView) findViewById(R.id.lv_mainlist);
    	mMainList.setAdapter(new CommonAdapter<Map<String, String>>(this, mLists, android.R.layout.simple_list_item_1) {
			@Override
			public void convertView(ViewHolder holder, final Map<String, String> item) {
				holder.setText(android.R.id.text1, item.get("Title"));
				TextView tv_title = holder.getView(android.R.id.text1);
				tv_title.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String className = item.get("Activity");
						String funcName = item.get("Function");
						if (className == null) {
							if (funcName == null) return;

							try {
								Method method = MainActivity.class.getDeclaredMethod(funcName);
								method.invoke(MainActivity.this);
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							} 
						} else {
							try {
								Class clazz = getClass().forName(className);
								Intent intent = new Intent(MainActivity.this, clazz);
								startActivity(intent);
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
					}
				});
			}
		});
    }
    
    public void createFragmentDialog() {
    	MyDialogFragment dialog = new MyDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putString("title", "锁具信息删除");
		bundle.putString("content", "确定删除该锁具消息？");
		dialog.setArguments(bundle);
		dialog.setOnConfirmListener(new OnConfirmListener() {
			@Override
			public void onConfirm() {
				
			}
		});
		dialog.show(getSupportFragmentManager(), "DeleteMessage");
    }
    
    public void weixinSocialShare() {
    	new ShareAction(this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(_umShareListener)
		.withTitle("小嘀管家下载")
        .withText("http://121.43.225.110:8080/base/appapk/DsmApp.apk")
        .share();
    }
    
    private static UMShareListener _umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
        	if(SHARE_MEDIA.WEIXIN.equals(platform)){
        		Toast.makeText(mContext, "微信分享成功", Toast.LENGTH_SHORT).show();
        	}
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
        	if(SHARE_MEDIA.WEIXIN.equals(platform)){
        		Toast.makeText(mContext, "微信分享失败", Toast.LENGTH_SHORT).show();
        	}
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
        	if(SHARE_MEDIA.WEIXIN.equals(platform)){
        		Toast.makeText(mContext, "微信分享被取消", Toast.LENGTH_SHORT).show();
        	}
        }
    };
    
    
    public void createProgressDialog() {
    	DialogUtils.showProgressDiaog(this, "test Progressbar", true);
    }
    
    public void queryRecordByRetrofit() {
    	LockManager.recordQuery
		.getOpenDoorRecord("E4:38:4E:3A:52:90", new Callback<Result<LockOpendoorLog>>() {
			@Override
			public void success(Result<LockOpendoorLog> result, Response response) {
				LockManager.printResponse(response);
				
				if (result.getStatus().equals("0")) {
					StreamTools.printLogAndTips(getApplicationContext(), "获取开门记录失败!");	
					return;
				}
				
				if (result.getData() != null && result.getData().size() != 0) {
					mOpendoorRecord.clear();
					mOpendoorRecord.addAll(result.getData());
					for (int i = 0; i < mOpendoorRecord.size(); i ++) {
						System.out.println(mOpendoorRecord.get(i).getUserid());
					}
				}
				StreamTools.printLogAndTips(getApplicationContext(), "获取开门记录成功!");
			}
			
			@Override
			public void failure(RetrofitError arg0) {
				StreamTools.printLogAndTips(getApplicationContext(), "获取开门记录失败!");	
			}

		});
		
    	
		/*
		LockManager.recordQuery
		.getOpenDoorRecordRxjava("E4:38:4E:3A:52:90").cache()
		.map(new Func1<Result<LockOpendoorLog> , List<LockOpendoorLog>>() {
			@Override
			public List<LockOpendoorLog> call(Result<LockOpendoorLog> result) {
				return result.getData();
			}
		})
		.flatMap(new Func1<List<LockOpendoorLog>, Observable<LockOpendoorLog>>() {
			@Override
			public Observable<LockOpendoorLog> call(List<LockOpendoorLog> lists) {
				return Observable.from(lists);
			}
		})
		.subscribeOn(Schedulers.io())
		.observeOn(AndroidSchedulers.mainThread())
		.subscribe(new Subscriber<LockOpendoorLog>() {
			@Override
			public void onCompleted() {
				StreamTools.printLogAndTips(getApplicationContext(), "获取开门记录成功!");
			}

			@Override
			public void onError(Throwable e) {
				StreamTools.printLogAndTips(getApplicationContext(), "获取开门记录失败!");	
			}

			@Override
			public void onNext(LockOpendoorLog opendoor) {
				System.out.println(opendoor.getUserid());
			}
		});*/
		
		/*GCDevice gcDevice = new GCDevice();
		gcDevice.gdDevicemac = "fenggedeshijie";
		gcDevice.gdDevicename = "hahaha";
		gcDevice.gdDeviceaddress = "01-18-5";
		gcDevice.gdDeviceusetype = 13;
		
		LockManager.recordQuery
		.addGreenCityDevice(gcDevice, new Callback<Result1>() {
			@Override
			public void failure(RetrofitError arg0) {
				StreamTools.printLogAndTips(getApplicationContext(), "添加绿城设备失败!");
			}

			@Override
			public void success(Result1 arg0, Response arg1) {
				printResponse(arg1);
				if (arg0.getStatus().equals("1")) {
					StreamTools.printLogAndTips(getApplicationContext(), "添加绿城设备成功!");
				} else {
					StreamTools.printLogAndTips(getApplicationContext(), "添加绿城设备失败!");
				}
			}
			
		});*/
		
    	/*Map<String, Object> map = new HashMap<String, Object>();
    	map.put("gdDevicemac", "fenggedeshijie");
    	map.put("gdDevicename", "hahahaha");
    	map.put("gdDeviceaddress", "01-18-4");
    	map.put("gdDeviceusetype", 13);
    	
		LockManager.recordQuery
		.addGreenCityDevice(map, new Callback<Result1>() {
			@Override
			public void failure(RetrofitError arg0) {
				StreamTools.printLogAndTips(getApplicationContext(), "添加绿城设备失败!");
			}

			@Override
			public void success(Result1 arg0, Response arg1) {
				printResponse(arg1);
				if (arg0.getStatus().equals("1")) {
					StreamTools.printLogAndTips(getApplicationContext(), "添加绿城设备成功!");
				} else {
					StreamTools.printLogAndTips(getApplicationContext(), "添加绿城设备失败!");
				}
			}
			
		});*/
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // attention to this below, must add this
        UMShareAPI.get( this ).onActivityResult( requestCode, resultCode, data);
    }
}
