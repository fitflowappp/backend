package com.magpie.share;

public class UserRef {
	private String id;
	private String name;
	private String sessionId;
	private String role;
	private String picture;
	private String phone;
	private boolean loginLimit; // 登录限制
	private boolean checkLimit; // 审核限制

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public boolean isLoginLimit() {
		return loginLimit;
	}

	public void setLoginLimit(boolean loginLimit) {
		this.loginLimit = loginLimit;
	}

	public boolean isCheckLimit() {
		return checkLimit;
	}

	public void setCheckLimit(boolean checkLimit) {
		this.checkLimit = checkLimit;
	}

}
