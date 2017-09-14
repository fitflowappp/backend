package com.magpie.yoga.model;

import com.magpie.base.model.BaseModel;

public class UserConfiguration extends BaseModel {
	private String id;
	private String uid;
	// Scheduling in-app notification on? Collected Boolean
	private boolean notification;
	// Scheduling calendar reminder on? Collected Boolean
	private boolean remider;

	// Scheduling days Collected Text
	private String schedulingDays;
	// Scheduling time of day Collected HHMM Time
	private String schedulingTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public boolean isNotification() {
		return notification;
	}

	public void setNotification(boolean notification) {
		this.notification = notification;
	}

	public boolean isRemider() {
		return remider;
	}

	public void setRemider(boolean remider) {
		this.remider = remider;
	}

	public String getSchedulingDays() {
		return schedulingDays;
	}

	public void setSchedulingDays(String schedulingDays) {
		this.schedulingDays = schedulingDays;
	}

	public String getSchedulingTime() {
		return schedulingTime;
	}

	public void setSchedulingTime(String schedulingTime) {
		this.schedulingTime = schedulingTime;
	}

}
