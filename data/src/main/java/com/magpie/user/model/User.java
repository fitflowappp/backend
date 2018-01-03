package com.magpie.user.model;

import java.util.Date;

import com.magpie.base.model.BaseModel;
import com.magpie.share.ResourceRef;

public class User extends BaseModel {

	private String id;

	private String name;
	private String phone;
	private String password;
	private Date birthday;
	private int age;
	private String gender;

	private boolean unRegistered;

	private String headerImgUrl;// 头像Url
	private ResourceRef headerImg;

	private String signIntro;// 签名简介
	private String intro;// 简介

	private String role;// {#RoleType}

	private Date lastLoginDate;// 上次登录时间
	private Date registerDate;// 注册时间
	private String registerType;// 注册类型
	private Date latestMsgDate;// 最后发送消息的时间

	private String from;// 来源
	private String client;

	private String openId;//
	private String sessionKey;//

	private boolean specialLogin;

	private boolean facebookRegistrationSumbmitted;
	private Date facebookRegistrationSumbmittedDate;
	
	private String email; 
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
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
	 * @param specialLogin
	 *            the specialLogin to set
	 */
	public void setSpecialLogin(boolean specialLogin) {
		this.specialLogin = specialLogin;
	}

	/**
	 * @return the headerImgUrl
	 */
	public String getHeaderImgUrl() {
		return headerImgUrl;
	}

	/**
	 * @param headerImgUrl
	 *            the headerImgUrl to set
	 */
	public void setHeaderImgUrl(String headerImgUrl) {
		this.headerImgUrl = headerImgUrl;
	}

	/**
	 * @return the client
	 */
	public String getClient() {
		return client;
	}

	/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(String client) {
		this.client = client;
	}

	/**
	 * @return the registerType
	 */
	public String getRegisterType() {
		return registerType;
	}

	/**
	 * @param registerType
	 *            the registerType to set
	 */
	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}

	/**
	 * @return the facebookRegistrationSumbmitted
	 */
	public boolean isFacebookRegistrationSumbmitted() {
		return facebookRegistrationSumbmitted;
	}

	/**
	 * @param facebookRegistrationSumbmitted
	 *            the facebookRegistrationSumbmitted to set
	 */
	public void setFacebookRegistrationSumbmitted(boolean facebookRegistrationSumbmitted) {
		this.facebookRegistrationSumbmitted = facebookRegistrationSumbmitted;
	}

	/**
	 * @return the facebookRegistrationSumbmittedDate
	 */
	public Date getFacebookRegistrationSumbmittedDate() {
		return facebookRegistrationSumbmittedDate;
	}

	/**
	 * @param facebookRegistrationSumbmittedDate the facebookRegistrationSumbmittedDate to set
	 */
	public void setFacebookRegistrationSumbmittedDate(Date facebookRegistrationSumbmittedDate) {
		this.facebookRegistrationSumbmittedDate = facebookRegistrationSumbmittedDate;
	}

}
