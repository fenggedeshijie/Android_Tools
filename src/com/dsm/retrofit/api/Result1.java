package com.dsm.retrofit.api;


public class Result1 {
	private String status;	// 1 成功，0 失败
	private String data;
	private String msg;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
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
