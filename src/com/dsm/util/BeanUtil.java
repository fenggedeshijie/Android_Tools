package com.dsm.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dsm.retrofit.api.Result2;

import android.text.TextUtils;


public class BeanUtil {

	public static Map<String, Class<?>> reflectClassFieldNameAndTypeMap(Object obj){
		Map<String, Class<?>> fieldMap = new HashMap<String, Class<?>>();
		Class<?> clazz = obj.getClass();
		
		Field[] fieldList = clazz.getDeclaredFields();
		for(int i = 0; i < fieldList.length; i ++){
			Field field = fieldList[i];
			fieldMap.put(field.getName(), field.getType());
		}
		return fieldMap;
	}
	
	public static void parseJsonStringToBean(Object result, String args, Object javaBean) 
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ParseException {
		try {
			JSONObject jsonResult = new JSONObject(args);
			Map<String, Class<?>> fieldMap = reflectClassFieldNameAndTypeMap(result);
			
			for (Map.Entry<String, Class<?>> entry : fieldMap.entrySet()) {
				String fieldName = entry.getKey();
				if (jsonResult.toString().contains("\"" + fieldName + "\"")) {
					String methodname = "set" + fieldName.substring(0, 1)
							.toUpperCase(Locale.US) + fieldName.substring(1);
					
					Method method = null;
					Method[] methods = result.getClass().getDeclaredMethods();
					for (int i = 0; i < methods.length; i ++) {
						if (methods[i].getName().equals(methodname)) {
							method = methods[i];
						}
					}
					
					if ("data".equals(fieldName)) {
						String dataString = jsonResult.getString("data");
						if(!TextUtils.isEmpty(dataString) && javaBean != null && result instanceof Result2) {
							if (dataString.contains("[")) {
								ArrayList<Object> lists = new ArrayList<Object>();
								JSONArray jsonarray = new JSONArray(jsonResult.getString(fieldName));
								for (int i = 0; i < jsonarray.length(); i ++) {
									Object array = javaBean.getClass().newInstance(); 
									parseJsonStringToBean(array, jsonarray.getString(i), null);
									lists.add(array);
								}
								method.invoke(result, lists);
							}
						}
					}
					
					else if ("time".equals(fieldName)) {
						SimpleDateFormat format = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
						Date date = format.parse(jsonResult.getString(fieldName));
						method.invoke(result, date);
					}
					
					else if ("opentime".equals(fieldName)) {
						SimpleDateFormat format = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
						Date date = format.parse(jsonResult.getString(fieldName));
						method.invoke(result, date);
					}
					
					else if ("opentimeStr".equals(fieldName)) {
						SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss", Locale.US);
						Date date = format.parse(jsonResult.getString(fieldName));
						method.invoke(result, date);
					}
					
					else if (entry.getValue() == Integer.class) {
						method.invoke(result, jsonResult.getInt(fieldName));
					}
					
					else if (entry.getValue() == String.class) {
						method.invoke(result, jsonResult.getString(fieldName));
					}
					
				}
			}			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> void parseJsonStringToBean2(Object result, String args, T t) 
			throws JSONException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ParseException {
		
		JSONObject jsonResult = new JSONObject(args);
		Map<String, Class<?>> fieldMap = reflectClassFieldNameAndTypeMap(result);
		
		for (Map.Entry<String, Class<?>> entry : fieldMap.entrySet()) {
			String fieldName = entry.getKey();
			if (jsonResult.toString().contains("\"" + fieldName + "\"")) {
				String methodname = "set" + fieldName.substring(0, 1)
						.toUpperCase(Locale.US) + fieldName.substring(1);
				
				Method method = null;
				Method[] methods = result.getClass().getDeclaredMethods();
				for (int i = 0; i < methods.length; i ++) {
					if (methods[i].getName().equals(methodname)) {
						method = methods[i];
					}
				}
				
				if ("data".equals(fieldName)) {
					String dataString = jsonResult.getString("data");
					if(!TextUtils.isEmpty(dataString) && t != null && result instanceof Result2) {
						if (dataString.contains("[")) {
							ArrayList<T> lists = new ArrayList<T>();
							JSONArray jsonarray = new JSONArray(jsonResult.getString(fieldName));
							for (int i = 0; i < jsonarray.length(); i ++) {
								T array = (T) t.getClass().newInstance(); 
								parseJsonStringToBean2(array, jsonarray.getString(i), null);
								lists.add(array);
							}
							method.invoke(result, lists);
						}
					}
				}
				
				else if ("time".equals(fieldName)) {
					SimpleDateFormat format = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
					Date date = format.parse(jsonResult.getString(fieldName));
					method.invoke(result, date);
				}
				
				else if ("opentime".equals(fieldName)) {
					SimpleDateFormat format = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.US);
					Date date = format.parse(jsonResult.getString(fieldName));
					method.invoke(result, date);
				}
				
				else if ("opentimeStr".equals(fieldName)) {
					SimpleDateFormat format = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss", Locale.US);
					Date date = format.parse(jsonResult.getString(fieldName));
					method.invoke(result, date);
				}
				
				else if (entry.getValue() == Integer.class) {
					method.invoke(result, jsonResult.getInt(fieldName));
				}
				
				else if (entry.getValue() == String.class) {
					method.invoke(result, jsonResult.getString(fieldName));
				}
			}
		}
	}
	
}
