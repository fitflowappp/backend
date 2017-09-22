package com.magpie.yoga.model;

import com.magpie.base.model.BaseModel;

public class AchievementRecord extends BaseModel {

	private String id;

	private String uid;

	private int type;// DialogType

	public AchievementRecord() {

	}

	public AchievementRecord(String uid, int type) {
		this.uid = uid;
		this.type = type;
	}

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
