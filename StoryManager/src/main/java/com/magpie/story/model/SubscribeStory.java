package com.magpie.story.model;

import com.magpie.base.model.BaseModel;

public class SubscribeStory extends BaseModel {

	private String id;
	private String storyId;
	private String storySeriesId;
	private int no;
	private String uid;
	private String from;

	public SubscribeStory() {
	}

	public SubscribeStory(String uid, Story story, String from) {
		this.storyId = story.getId();
		this.uid = uid;
		this.from = from;
		this.storySeriesId = story.getStorySeriesId();
		this.no = story.getNo();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStoryId() {
		return storyId;
	}

	public void setStoryId(String storyId) {
		this.storyId = storyId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from
	 *            the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	public String getStorySeriesId() {
		return storySeriesId;
	}

	public void setStorySeriesId(String storySeriesId) {
		this.storySeriesId = storySeriesId;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

}
