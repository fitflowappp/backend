package com.magpie.yoga.model;

import com.magpie.base.model.BaseModel;

public class TopicSingles extends BaseModel {
	private String id;
	private String topicId;
	private String singlesId;
	private int sort;
	
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public String getSinglesId() {
		return singlesId;
	}
	public void setSinglesId(String singlesId) {
		this.singlesId = singlesId;
	}
	
	
}
