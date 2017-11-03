package com.magpie.yoga.model;

import java.util.Date;

import com.magpie.base.model.BaseModel;
import com.magpie.base.utils.DateUtil;

public class UserConfiguration extends BaseModel {
	private String id;
	private String uid;
	// Scheduling in-app notification on? Collected Boolean
	private boolean notification;
	// Scheduling calendar reminder on? Collected Boolean
	private boolean remider;

	// Scheduling days Collected Text
	private int[] schedulingDays = new int[] { 1, 1, 1, 1, 1, 1, 1 };
	// Scheduling time of day Collected HHMM Time
	private Date schedulingTime = DateUtil.getCurrentDate();

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

	public Date getSchedulingTime() {
		return schedulingTime;
	}

	public void setSchedulingTime(Date schedulingTime) {
		this.schedulingTime = schedulingTime;
	}

	/**
	 * @return the schedulingDays
	 */
	public int[] getSchedulingDays() {
		return schedulingDays;
	}

	/**
	 * @param schedulingDays
	 *            the schedulingDays to set
	 */
	public void setSchedulingDays(int[] schedulingDays) {
		this.schedulingDays = schedulingDays;
	}

}
