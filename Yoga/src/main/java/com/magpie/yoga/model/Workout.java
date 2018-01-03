package com.magpie.yoga.model;

import java.util.List;

import com.magpie.base.model.BaseModel;
import com.magpie.share.ResourceRef;

public class Workout extends BaseModel {
	// Workout ID System generated Integer
	private String id;
	// Workout Code Admin create new or edit Alphanumeric
	private String code;
	// Workout Title Admin create new or edit Text
	private String title;
	// Workout Image Admin create new or edit png, jpg, gif
	private ResourceRef coverImg;
	// Workout Description Admin create new or edit Text
	private String description;
	// Workout Duration Admin create new or edit Integer Minutes
	private int duration;
	// Completion message Admin create new or edit Text
	private String message;
	// Associated Routine IDs, in sequence Admin create new or edit Custom
	private List<Routine> routines;

	// Number of times started Collected Integer
	// Number of times completed Collected Integer
	// Number of unique users started Derived, from individual user's started
	// routine ID Integer
	// Number of unique users completed Derived, from individual user's
	// completed routine ID Integer
	// Total duration of being watched Collected HHMMSS
	private long startedTimes;
	private long completedTimes;
	private long startedUserCount;
	private long completedUserCount;
	private long totalDuration;
	private boolean isSingle=false;
	
	

	public boolean isSingle() {
		return isSingle;
	}

	public void setSingle(boolean isSingle) {
		this.isSingle = isSingle;
	}

	public long getStartedTimes() {
		return startedTimes;
	}

	public void setStartedTimes(long startedTimes) {
		this.startedTimes = startedTimes;
	}

	public long getCompletedTimes() {
		return completedTimes;
	}

	public void setCompletedTimes(long completedTimes) {
		this.completedTimes = completedTimes;
	}

	public long getStartedUserCount() {
		return startedUserCount;
	}

	public void setStartedUserCount(long startedUserCount) {
		this.startedUserCount = startedUserCount;
	}

	public long getCompletedUserCount() {
		return completedUserCount;
	}

	public void setCompletedUserCount(long completedUserCount) {
		this.completedUserCount = completedUserCount;
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

	public ResourceRef getCoverImg() {
		return coverImg;
	}

	public void setCoverImg(ResourceRef coverImg) {
		this.coverImg = coverImg;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Routine> getRoutines() {
		return routines;
	}

	public void setRoutines(List<Routine> routines) {
		this.routines = routines;
	}

	/**
	 * @return the totalDuration
	 */
	public long getTotalDuration() {
		return totalDuration;
	}

	/**
	 * @param totalDuration the totalDuration to set
	 */
	public void setTotalDuration(long totalDuration) {
		this.totalDuration = totalDuration;
	}

}
