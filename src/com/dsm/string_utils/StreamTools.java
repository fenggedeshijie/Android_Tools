package com.dsm.string_utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class StreamTools {

	public static String Inputstr2Str_Reader(InputStream in) {
		return Inputstr2Str_Reader(in, null);
	}
	
	public static String Inputstr2Str_Reader(InputStream in, String encode) {
		String str = null;  
		
		if (TextUtils.isEmpty(encode)) {
			encode = "utf-8";  // 默认以utf-8形式  
		}
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, encode));
			StringBuffer sb = new StringBuffer();
			while ((str = reader.readLine()) != null) {  
				sb.append(str).append("\n");  
			}
			str = sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		
       return str;  
	}
	
	public static void printLogAndTips(Context context, String info){
		Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
		Log.d("DSM_SecondLock", info);
	}
}
