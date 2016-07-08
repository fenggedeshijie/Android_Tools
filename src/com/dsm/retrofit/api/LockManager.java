package com.dsm.retrofit.api;

import java.io.IOException;
import java.util.Map;

import com.dsm.string_utils.StreamTools;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

public class LockManager {

	private static final String ENDPOINT = "http://121.43.225.110:8080";
	 
	/**
	 * 锁具记录查询的服务接口
	 */
	public interface RecordQueryService {
		// 获取开门记录
	    @GET("/lock/getOpenDoorRecordByLockSeq.action")
	    void getOpenDoorRecord(@Query("content") String lockseq, Callback<Result<LockOpendoorLog>> callback);
	    
	    // 获取开门记录
	    @GET("/lock/getOpenDoorRecordByLockSeq.action")
	    void getOpenDoorRecord2(@Query("content") String lockseq, Callback<Result2> callback);
	    
	    // 获取报警记录
	    @GET("/lock/getalarmInfoByLockSeq.action")
	    void getOpenAlarmRecord(@Query("content") String lockseq, Callback<Object> callback);
	    
	    // 获取亲情记录
	    @GET("/lock/getLoveAccountInfoByLockSeq.action")
	    void getOpenLoveRecord(@Query("content") String lockseq, Callback<Object> callback);
	    
	    // 获取开门记录
	    @GET("/lock/getOpenDoorRecordByLockSeq.action")
	    Observable<Result<LockOpendoorLog>> getOpenDoorRecordRxjava(@Query("content") String lockseq);
	    
	    // 绿城添加用户
	    @POST("/greentown/addGreenTownDevice.action")
	    void addGreenCityDevice(@Body GCDevice device, Callback<Result1> callback);
	    
	    // 绿城添加用户
	    @FormUrlEncoded
	    @POST("/greentown/addGreenTownDevice.action")
	    void addGreenCityDevice(@FieldMap Map<String, Object> map, Callback<Result1> callback);
	    
	    // 验证登录密码是否正确
	    @GET("/base/user/login")
	    void checkPasswordIsRight(@Query("user.password") String passwd, @Query("user.item1") String mobile,
	    		Callback<Result<Object>> callback);
	}
	
	private static RestAdapter restAdapter = new RestAdapter.Builder()
			.setEndpoint(ENDPOINT).setLogLevel(RestAdapter.LogLevel.FULL).build();
	
	public static RecordQueryService recordQuery = restAdapter.create(RecordQueryService.class);
	
	public static String printResponse(Response response) {
    	String body = "";
    	try {
			body = StreamTools.Inputstr2Str_Reader(response.getBody().in());
			System.out.println(body);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return body;
    }
}
