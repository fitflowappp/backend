package com.magpie.user.model;

import java.util.Date;

public class FaceBookUser {

	private String id;
	private String uid;

	// Facebook ID Facebook API Integer
	private String facebookOpenId;//
	// Email Address Facebook API Alphanumeric
	private String email;
	// Profile Picture Facebook API Image
	private String headerImgUrl;// 头像Url
	// Gender Facebook API Text
	private int gender;// 1:男2:女
	// Facebook permissions Facebook API Text
	private String facebookPermissions;
	// Timestamp of App First Opened Collected YYYYMMDDHHMMSS Time
	private Date firstOpenDate;
	// Registered? Derived Boolean
	private boolean unRegistered;// 未注册
	// Timestamp of Facebook Registration submitted Collected YYYYMMDDHHMMSS
	// Time
	private Date submittedDate;
	// Timestamp of Registration Completed Collected YYYYMMDDHHMMSS Time
	private Date registrationCompletedDate;

	public String getFacebookOpenId() {
		return facebookOpenId;
	}

	public void setFacebookOpenId(String facebookOpenId) {
		this.facebookOpenId = facebookOpenId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHeaderImgUrl() {
		return headerImgUrl;
	}

	public void setHeaderImgUrl(String headerImgUrl) {
		this.headerImgUrl = headerImgUrl;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getFacebookPermissions() {
		return facebookPermissions;
	}

	public void setFacebookPermissions(String facebookPermissions) {
		this.facebookPermissions = facebookPermissions;
	}

	public Date getFirstOpenDate() {
		return firstOpenDate;
	}

	public void setFirstOpenDate(Date firstOpenDate) {
		this.firstOpenDate = firstOpenDate;
	}

	public boolean isUnRegistered() {
		return unRegistered;
	}

	public void setUnRegistered(boolean unRegistered) {
		this.unRegistered = unRegistered;
	}

	public Date getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(Date submittedDate) {
		this.submittedDate = submittedDate;
	}

	public Date getRegistrationCompletedDate() {
		return registrationCompletedDate;
	}

	public void setRegistrationCompletedDate(Date registrationCompletedDate) {
		this.registrationCompletedDate = registrationCompletedDate;
	}

}
