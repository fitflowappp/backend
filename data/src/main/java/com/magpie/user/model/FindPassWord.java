package com.magpie.user.model;

import java.util.Date;

import com.magpie.base.model.BaseModel;

public class FindPassWord extends BaseModel {
	private String id;
	private String userId;
	private String privateKey;
	private Date vaildDate;
	private Date createDate;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public Date getVaildDate() {
		return vaildDate;
	}
	public void setVaildDate(Date vaildDate) {
		this.vaildDate = vaildDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
