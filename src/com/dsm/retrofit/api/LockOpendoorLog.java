package com.dsm.retrofit.api;

public class LockOpendoorLog {
   
    private String id;    
    private String openway;    
    private String lockseq;   
    private String recordtype;  
    private String opentime;    
    private String opentimeStr;    
    private String content;  
    private String userid;   
    private String username;
 
    public String getId() {
        return id;
    }
   
    public void setId(String id) {
        this.id = id;
    }
    
    public String getOpenway() {
        return openway;
    }
   
    public void setOpenway(String openway) {
        this.openway = openway;
    }
   
    public String getLockseq() {
        return lockseq;
    }
    
    public void setLockseq(String lockseq) {
        this.lockseq = lockseq;
    }
    
    public String getRecordtype() {
        return recordtype;
    }
   
    public void setRecordtype(String recordtype) {
        this.recordtype = recordtype;
    }
    
    public String getOpentime() {
        return opentime;
    }
   
    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }
    
    public String getOpentimeStr() {
    	return opentimeStr;
    }
    
    public void setOpentimeStr(String date) {
    	opentimeStr = date;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

	@Override
	public String toString() {
		return "LockOpendoorLog [id=" + id + ", openway=" + openway
				+ ", lockseq=" + lockseq + ", recordtype=" + recordtype
				+ ", opentime=" + opentime + ", content=" + content
				+ ", userid=" + userid + ", username=" + username + "]";
	}
}