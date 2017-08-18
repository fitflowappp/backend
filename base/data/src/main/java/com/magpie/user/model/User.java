package com.magpie.user.model;

import java.util.Date;

import com.magpie.resource.model.ResourceRef;

public class User {
	
	private String id;
	private String pcode;// 用户手机唯一码
	private String name;
	private String phone;
	private String password;
	private Date birthday;
	private int age;
	private int gender;// 1:男2:女


	private ResourceRef headerImg;
	private String headerImgUri;// 头像Uri

	private ResourceRef fullImg;

	private String signIntro;// 签名简介
	private String intro;// 简介

	private String sig;// qq云通信平台passcode
	private boolean online;// 是否在线

	private String role;// {#RoleType}
	private String weixinNumber;// 微信号
	private String agent;//
	
	private Date lastLoginDate;// 上次登录时间
	private Date registerDate;// 注册时间
	private boolean specialLogin; // 是否采用特殊方式登录
	private Date latestMsgDate;// 最后发送消息的时间

	private String from;// 来源

	private boolean unRegistered;// 未注册
	private boolean active;// 是否激活（用户首次打开app）

	private String idfa;// 苹果手机的idfa或者安卓手机的IMEI号
	private String idfaSource;// idfa来源{#IdfaSource}

	private String client;// 手机系统

	private String openId;// 用户微信openId
	private String sessionKey;// wechat sessionKey
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
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
	public String getHeaderImgUri() {
		return headerImgUri;
	}
	public void setHeaderImgUri(String headerImgUri) {
		this.headerImgUri = headerImgUri;
	}
	public ResourceRef getFullImg() {
		return fullImg;
	}
	public void setFullImg(ResourceRef fullImg) {
		this.fullImg = fullImg;
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
	public String getSig() {
		return sig;
	}
	public void setSig(String sig) {
		this.sig = sig;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getWeixinNumber() {
		return weixinNumber;
	}
	public void setWeixinNumber(String weixinNumber) {
		this.weixinNumber = weixinNumber;
	}
	public String getAgent() {
		return agent;
	}
	public void setAgent(String agent) {
		this.agent = agent;
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
	public boolean isSpecialLogin() {
		return specialLogin;
	}
	public void setSpecialLogin(boolean specialLogin) {
		this.specialLogin = specialLogin;
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
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getIdfa() {
		return idfa;
	}
	public void setIdfa(String idfa) {
		this.idfa = idfa;
	}
	public String getIdfaSource() {
		return idfaSource;
	}
	public void setIdfaSource(String idfaSource) {
		this.idfaSource = idfaSource;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
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
	
}
