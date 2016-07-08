package com.dsm.retrofit_okhttp;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.dsm.mystudy.R;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

public class OkHttpActivity extends Activity {

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
	private static final String IMGUR_CLIENT_ID = "...";
	private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

	private OkHttpClient mOkHttpClient = new OkHttpClient();
	private TextView mLogcat;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_okhttp);
		
		initViews();
		okHttpGetRequest();
		
		//greenCityDeviceAdd();
		
	}

	private void initViews() {
		mLogcat = (TextView) findViewById(R.id.tv_logcat);
		mLogcat.setMovementMethod(ScrollingMovementMethod.getInstance());
	}
	
	// Http Get Request
	private void okHttpGetRequest() {
		final Request request = new Request.Builder()
			.url("http://dsmzg.com:8080/base/user/login?user.item1=15958006927&user.password=12345678")
			.build();
		
		Call call = mOkHttpClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onResponse(Response response) throws IOException {
				final String str = response.body().string();
				System.out.println("Response: " + str);
				// onResponse执行的线程并不是UI线程
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mLogcat.append(str + "\n");
					}
				});
			}
			
			@Override
			public void onFailure(Request request, IOException e) {
				
			}
		});
	}
	
	// Post方式提交json字符串
	private void okHttpPostJsonRequest(String url, String json, Callback callback) {
		RequestBody requestBody = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url).post(requestBody).build();
		mOkHttpClient.newCall(request).enqueue(callback);
	}
	
	// Post方式提交表单
	private void okHttpPostFormRequest(String url, Map<String, String> map, Callback callback) {
		FormEncodingBuilder buider = new FormEncodingBuilder();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			buider.add(entry.getKey(), entry.getValue());
		}
		RequestBody formBody = buider.build();
		
		Request request = new Request.Builder().url(url).post(formBody).build();
		mOkHttpClient.newCall(request).enqueue(callback);
	}
	
	// Post方式提交String参数
	private void okHttpPostStringRequest(String url, String requestBody, Callback callback) {
		RequestBody strBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, requestBody);
		Request request = new Request.Builder().url(url).post(strBody).build();
		mOkHttpClient.newCall(request).enqueue(callback);
	}
	
	// Post方式提交file
	private void okHttpPostFileRequest(String url, String requestBody, Callback callback) {
		File file = new File("README.txt");
		RequestBody strBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, file);
		Request request = new Request.Builder().url(url).post(strBody).build();
		mOkHttpClient.newCall(request).enqueue(callback);
	}
	
	// Post方式分块提交
	private void okHttpPostMuiltpartRequest(String url, Callback callback) {
		RequestBody requestBody = new MultipartBuilder()
		.type(MultipartBuilder.FORM)
		.addPart(Headers.of("Content-Disposition", "form-data; name=\"title\""), 
				RequestBody.create(null, "Square Logo"))
		.addPart( Headers.of("Content-Disposition", "form-data; name=\"image\""),
	            RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
		.build();
		
		Request request = new Request.Builder()
        .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
        .url(url)
        .post(requestBody)
        .build();
		
		mOkHttpClient.newCall(request).enqueue(callback);
	}
	
	// 超时配置
	private void okHttpConfigureTimeout(String url, Callback callback) {
		OkHttpClient client = mOkHttpClient.clone();
		client.setConnectTimeout(5, TimeUnit.SECONDS);
		client.setWriteTimeout(10, TimeUnit.SECONDS);
		client.setReadTimeout(5, TimeUnit.SECONDS);
		
		Request requst = new Request.Builder().url(url).build();
		client.newCall(requst).enqueue(callback);
	}
	
	private void greenCityDeviceAdd() {
		String url = "http://dsmzg.com:8080/greentown/addGreenTownDevice.action";
		
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("gdDevicemac", "dc-56-14-34-fd-69");
			jsonObject.put("gdDevicename", "长河一号");
			jsonObject.put("gdDeviceaddress", "18-5-6");
			jsonObject.put("gdDeviceusetype", "3");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		okHttpPostJsonRequest(url, jsonObject.toString(), new Callback() {
			@Override
			public void onResponse(Response response) throws IOException {
				final String str = response.body().string();
				System.out.println("Respone: " + str);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(OkHttpActivity.this, "添加设备成功！", Toast.LENGTH_SHORT).show();
						mLogcat.append(str + "\n");
					}
				});
			}
			
			@Override
			public void onFailure(Request request, IOException e) {
			}
		});
		
	}
}
