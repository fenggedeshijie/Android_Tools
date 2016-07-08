package com.dsm.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dsm.mystudy.App;
import com.dsm.retrofit.api.Result2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;


public class HttpRequestUtil {

	public interface HttpRequestCallBack{
		public void requestSuccess(Object obj);
		public void requestFailure(String error);
	}
	
	public static boolean checkNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						NetworkInfo netWorkInfo = info[i];
						if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
							return true;
						} else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private static void sendHttpRequestGetStringRequest(final Context context, String url
			, final HttpRequestCallBack callback, final Object destObj) {

		if(!checkNetworkAvailable(context)){
			//Util.printLogAndTips(context, "无网络,请检查手机网络连接");
			callback.requestFailure(null);
			return;
		}

		StringRequest request = new StringRequest(Method.GET, url, 
				new Response.Listener<String>() {
					@Override
					public void onResponse(String arg) {
						if (!arg.isEmpty() && arg != null) {
							LogUtil.d(arg);
							Result2 result = new Result2();
							try {
								BeanUtil.parseJsonStringToBean2(result, arg, destObj);
							}catch (java.text.ParseException e) {
								e.printStackTrace();
							}catch (IllegalAccessException e) {
								e.printStackTrace();
								callback.requestFailure(e.getMessage());
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
								callback.requestFailure(e.getMessage());
							} catch (InvocationTargetException e) {
								e.printStackTrace();
								callback.requestFailure(e.getMessage());
							} catch (InstantiationException e) {
								e.printStackTrace();
								callback.requestFailure(e.getMessage());
							} catch (JSONException e) {
								callback.requestFailure(e.getMessage());
								e.printStackTrace();
							} catch (ParseException e) {
								callback.requestFailure(e.getMessage());
								e.printStackTrace();
							}
							
							if(!result.getStatus().equals("1")){
								String ps = "request failure, return status:" + result.getStatus();
								callback.requestFailure(ps);
								return;
							}
							
							List<Object> objList = result.getData();
							if(objList == null || objList.size() == 0){
								LogUtil.d("data is null");
								callback.requestSuccess(null);
								return;
							}
							callback.requestSuccess(objList);
						}
					}
				}, 
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError e) {
						e.printStackTrace();
						callback.requestFailure(e.getMessage());
					}
				});
		
		if (App.variables.mRequestQueue == null) {
			App.variables.mRequestQueue = Volley.newRequestQueue(context);
		}
		App.variables.mRequestQueue.add(request);
	}
	
	private static void sendHttpRequestPostStringRequest(final Context context, String url
			, JSONObject args, final HttpRequestCallBack callback, final Object destObj) {

		if(!checkNetworkAvailable(context)){
			//Util.printLogAndTips(context, "无网络,请检查手机网络连接");
			callback.requestFailure(null);
			return;
		}
		
		JsonObjectRequest request = new JsonObjectRequest(Method.POST, url, args,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject arg) {
						String str = arg.toString();
						LogUtil.d(str);
						Result2 response = new Result2();
						try {
							BeanUtil.parseJsonStringToBean2(response, str, destObj);
						}catch (java.text.ParseException e) {
							e.printStackTrace();
						}catch (IllegalAccessException e) {
							e.printStackTrace();
							callback.requestFailure(e.getMessage());
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
							callback.requestFailure(e.getMessage());
						} catch (InvocationTargetException e) {
							e.printStackTrace();
							callback.requestFailure(e.getMessage());
						} catch (InstantiationException e) {
							e.printStackTrace();
							callback.requestFailure(e.getMessage());
						} catch (JSONException e) {
							callback.requestFailure(e.getMessage());
							e.printStackTrace();
						} catch (ParseException e) {
							callback.requestFailure(e.getMessage());
							e.printStackTrace();
						}
						
						if(!response.getStatus().equals("1")){
							String ps = "request failure, return status:" + response.getStatus();
							callback.requestFailure(ps);
							return;
						}
						
						List<Object> objList = response.getData();
						if(objList == null || objList.size() == 0){
							LogUtil.d("data is null");
							callback.requestSuccess(null);
							return;
						}
						callback.requestSuccess(objList);
					}
				}, 
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError e) {
						e.printStackTrace();
						callback.requestFailure(e.getMessage());
					}
				}) {
			
			@Override
		    public Map<String, String> getHeaders() {
		        HashMap<String, String> headers = new HashMap<String, String>();
		        headers.put("Accept", "application/json");
		        headers.put("Content-Type", "application/json; charset=UTF-8");
		        return headers;
		    }
		};

		if (App.variables.mRequestQueue == null) {
			App.variables.mRequestQueue = Volley.newRequestQueue(context);
		}
		App.variables.mRequestQueue.add(request);
	}
}
