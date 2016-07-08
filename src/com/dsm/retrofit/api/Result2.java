package com.dsm.retrofit.api;

import java.util.List;

public class Result2 {

	private String status;
	private List<Object> data;
	private String msg;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
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
		return "Result [status=" + status + ", data=" + data + ", msg=" + msg
				+ "]";
	}
}
