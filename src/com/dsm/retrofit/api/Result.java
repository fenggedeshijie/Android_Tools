package com.dsm.retrofit.api;

import java.util.List;

/*
 *	   服务器返回的数据格式
 *	 {
 *	 	"status": 1,
 *	    "data": [{},{},...],
 *	    "msg": "请求成功"
 *	 }
 */
public class Result<T> {
	
	private String status;	// 1 成功，0 失败
	private List<T> data;
	private String msg;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "Result [status=" + status + ", data=" + data + ", msg=" + msg + "]";
	}
}