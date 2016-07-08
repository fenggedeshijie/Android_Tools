package com.dsm.retrofit.api;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.dsm.mystudy.R;
import com.dsm.string_utils.StreamTools;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class RetrofitActivity extends Activity {

	List<LockOpendoorLog> mOpendoorRecord = new ArrayList<LockOpendoorLog>();
	
	@Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        queryRecordByRetrofit();
        
        init();
    }
	
	public void init() {
		
	}
	
	public void queryRecordByRetrofit() {
    	/*LockManager.recordQuery
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

		});*/
		
    	LockManager.recordQuery
    	.checkPasswordIsRight("6532417854", "15958006927", new Callback<Result<Object>>() {
			
			@Override
			public void success(Result<Object> result, Response response) {
				System.out.println("The msg is: " + result.getMsg());
			}
			
			@Override
			public void failure(RetrofitError error) {
				
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
}
