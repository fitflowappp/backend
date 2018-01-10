package com.magpie.yoga.model;

import com.magpie.base.model.BaseModel;
import com.magpie.share.ResourceRef;

public class Routine extends BaseModel {
	// Routine ID System generated Number
	private String id;
	// Routine Code Admin create or edit Alphanumeric
	private String code;
	// Routine Title Admin create or edit Text
	private String title;
	// Routine Duration Admin create or edit Integer Minutes
	private int duration;
	// Routine Video Admin create or edit
	private ResourceRef video;
	// Display in Workout detail page? Admin create new or edit Boolean
	private boolean display;

	// for statistic
	// Number of times started Collected Integer
	private int startedTimes;
	// Number of times skipped Collected Integer
	private int skippedTimes;
	
	// Routine Image Admin create new or edit png, jpg, gif
	private ResourceRef coverImg;
	
	

	public ResourceRef getCoverImg() {
		return coverImg;
	}

	public void setCoverImg(ResourceRef coverImg) {
		this.coverImg = coverImg;
	}

	public int getStartedTimes() {
		return startedTimes;
	}

	public void setStartedTimes(int startedTimes) {
		this.startedTimes = startedTimes;
	}

	public int getSkippedTimes() {
		return skippedTimes;
	}

	public void setSkippedTimes(int skippedTimes) {
		this.skippedTimes = skippedTimes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public ResourceRef getVideo() {
		return video;
	}

	public void setVideo(ResourceRef video) {
		this.video = video;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

}
