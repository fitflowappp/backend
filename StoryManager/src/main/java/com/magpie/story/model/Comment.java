package com.magpie.story.model;

import com.magpie.base.model.BaseModel;
import com.magpie.share.ResourceRef;

public class Comment extends BaseModel {

	private String id;
	private String uid;
	private int type;// #CommentDestType
	private String destId;// 目标
	private String content;
	private ResourceRef imgRef;
	private int agreeCount;

	private boolean available;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ResourceRef getImgRef() {
		return imgRef;
	}

	public void setImgRef(ResourceRef imgRef) {
		this.imgRef = imgRef;
	}

	/**
	 * @return the available
	 */
	public boolean isAvailable() {
		return available;
	}

	/**
	 * @param available
	 *            the available to set
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}

	/**
	 * @return the agreeCount
	 */
	public int getAgreeCount() {
		return agreeCount;
	}

	/**
	 * @param agreeCount
	 *            the agreeCount to set
	 */
	public void setAgreeCount(int agreeCount) {
		this.agreeCount = agreeCount;
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

}
