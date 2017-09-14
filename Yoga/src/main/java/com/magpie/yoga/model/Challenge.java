package com.magpie.yoga.model;

import java.util.List;

import com.magpie.base.model.BaseModel;
import com.magpie.share.ResourceRef;

public class Challenge extends BaseModel {
	// Challenge ID System generated Integer
	private String id;
	// Challenge Code Admin create new or edit Alphanumeric
	private String code;
	// Challenge Title Admin create new or edit Text
	private String title;
	// Challenge Subtitle Admin create new or edit Text
	private String subTitle;
	// Challenge Image Admin create new or edit png, jpg, gif
	private ResourceRef coverImg;
	// Challenge Description Admin create new or edit Text
	private String description;
	// Sequence in Challenge Homepage Admin create new or edit Integer
	// @see HomePage
	// Challenge IDs that need to be completed to unlock Admin create new or
	// edit Integer
	private boolean unlocked;
	// Associated Workout IDs, in sequence Admin create new or edit Custom
	private List<Workout> workouts;
	// Number of times started Collected Integer
	// Number of times completed Collected Integer
	// Number of unique users started Derived, from individual user's started
	// challenge ID Integer
	// Number of unique users completed Derived, from individual user's
	// completed challenge ID Integer

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

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
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

	public List<Workout> getWorkouts() {
		return workouts;
	}

	public void setWorkouts(List<Workout> workouts) {
		this.workouts = workouts;
	}

	/**
	 * @return the unlocked
	 */
	public boolean isUnlocked() {
		return unlocked;
	}

	/**
	 * @param unlocked
	 *            the unlocked to set
	 */
	public void setUnlocked(boolean unlocked) {
		this.unlocked = unlocked;
	}

}
