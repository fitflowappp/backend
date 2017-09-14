package com.magpie.user.model;

import java.util.Date;

import com.magpie.base.model.BaseModel;
import com.magpie.share.ResourceRef;

public class User extends BaseModel {
	
//	User ID	System generated	Integer	
	private String id;
//	Facebook ID	Facebook API	Integer	
	private String facebookOpenId;// 
//	Email Address	Facebook API	Alphanumeric	
	private String email;
//	Profile Picture	Facebook API	Image	
	private String headerImgUrl;// 头像Url
//	Gender	Facebook API	Text	
	private int gender;// 1:男2:女
//	Facebook permissions	Facebook API	Text	
	private String facebookPermissions;
//	Timestamp of App First Opened	Collected	YYYYMMDDHHMMSS	Time
	private Date firstOpenDate;
//	Registered?	Derived	Boolean	
	private boolean unRegistered;// 未注册
//	Timestamp of Facebook Registration submitted	Collected	YYYYMMDDHHMMSS	Time
	private Date submittedDate;
//	Timestamp of Registration Completed	Collected	YYYYMMDDHHMMSS	Time
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
	private String name;
	private String phone;
	private String password;
	private Date birthday;
	private int age;

	private ResourceRef headerImg;

	private String signIntro;// 签名简介
	private String intro;// 简介

	private String role;// {#RoleType}
	
	private Date lastLoginDate;// 上次登录时间
	private Date registerDate;// 注册时间
	private Date latestMsgDate;// 最后发送消息的时间

	private String from;// 来源

	private String openId;// 
	private String sessionKey;// 
	
	private boolean specialLogin;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public ResourceRef getHeaderImg() {
		return headerImg;
	}
	public void setHeaderImg(ResourceRef headerImg) {
		this.headerImg = headerImg;
	}
	public String getSignIntro() {
		return signIntro;
	}
	public void setSignIntro(String signIntro) {
		this.signIntro = signIntro;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public Date getLatestMsgDate() {
		return latestMsgDate;
	}
	public void setLatestMsgDate(Date latestMsgDate) {
		this.latestMsgDate = latestMsgDate;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public boolean isUnRegistered() {
		return unRegistered;
	}
	public void setUnRegistered(boolean unRegistered) {
		this.unRegistered = unRegistered;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	/**
	 * @return the specialLogin
	 */
	public boolean isSpecialLogin() {
		return specialLogin;
	}
	/**
	 * @param specialLogin the specialLogin to set
	 */
	public void setSpecialLogin(boolean specialLogin) {
		this.specialLogin = specialLogin;
	}
	
}
