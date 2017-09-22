package com.magpie.yoga.view;

import java.util.List;

import com.magpie.share.ResourceRef;

public class ChallengeView {

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
	private List<WorkoutView> workouts;

	private String currentWorkoutId;
	private String currentRoutineId;
	private int seconds;
	private int status;// HistoryEvent
	private boolean avail;

	public String getCurrentWorkoutId() {
		return currentWorkoutId;
	}

	public void setCurrentWorkoutId(String currentWorkoutId) {
		this.currentWorkoutId = currentWorkoutId;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the avail
	 */
	public boolean isAvail() {
		return avail;
	}

	/**
	 * @param avail
	 *            the avail to set
	 */
	public void setAvail(boolean avail) {
		this.avail = avail;
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

	public boolean isUnlocked() {
		return unlocked;
	}

	public void setUnlocked(boolean unlocked) {
		this.unlocked = unlocked;
	}

	public List<WorkoutView> getWorkouts() {
		return workouts;
	}

	public void setWorkouts(List<WorkoutView> workouts) {
		this.workouts = workouts;
	}

	public String getCurrentRoutineId() {
		return currentRoutineId;
	}

	public void setCurrentRoutineId(String currentRoutineId) {
		this.currentRoutineId = currentRoutineId;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

}
