package com.magpie.yoga.model;

import com.magpie.base.model.BaseModel;

public class UserSinglesLock extends BaseModel {
	private String id;
	private String userId;
	private String singlesId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSinglesId() {
		return singlesId;
	}
	public void setSinglesId(String singlesId) {
		this.singlesId = singlesId;
	}
	
	
}
