package com.magpie.story.model;

import com.magpie.base.model.BaseModel;

public class AgreeRecord extends BaseModel {

	private String id;
	private String uid;
	private int type;// #CommentDestType
	private String destId;// 目标
	private boolean agree;

	public AgreeRecord() {

	}

	public AgreeRecord(String uid, int type, String destId, boolean agree) {
		this.uid = uid;
		this.type = type;
		this.destId = destId;
		this.agree = agree;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDestId() {
		return destId;
	}

	public void setDestId(String destId) {
		this.destId = destId;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid
	 *            the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the agree
	 */
	public boolean isAgree() {
		return agree;
	}

	/**
	 * @param agree
	 *            the agree to set
	 */
	public void setAgree(boolean agree) {
		this.agree = agree;
	}

}
