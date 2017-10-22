package com.magpie.yoga.model;

import com.magpie.base.model.BaseModel;

public class AchievementRecord extends BaseModel {

	private String id;

	private String uid;

	private int type;// DialogType

	private int no;// 次数

	private int value;// duration minutes or workout count

	public AchievementRecord() {

	}

	public AchievementRecord(String uid, int type, int no, int value) {
		this.uid = uid;
		this.type = type;
		this.no = no;
		this.value = value;
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

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the no
	 */
	public int getNo() {
		return no;
	}

	/**
	 * @param no
	 *            the no to set
	 */
	public void setNo(int no) {
		this.no = no;
	}

}
