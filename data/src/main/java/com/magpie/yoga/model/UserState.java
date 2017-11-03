package com.magpie.yoga.model;

import java.util.List;

import com.magpie.base.model.BaseModel;

public class UserState extends BaseModel {
	private String id;
	private String uid;
	// // Scheduling in-app notification on? Collected Boolean
	// private boolean notification;
	// // Scheduling calendar reminder on? Collected Boolean
	// private boolean remider;
	//
	// // Scheduling days Collected Text
	// private String schedulingDays;
	// // Scheduling time of day Collected HHMM Time
	// private String schedulingTime;

	// Unlocked challenge IDs Collected Integers, comma separated
	private List<String> unlockedChallengeIds;
	// Current challenge ID Collected Integer
	private String currentChallengeId;
	// Number of completed challenges Derived Integer
	private int completedChallengeNum;
	// Started Workout IDs Collected Integers, comma separated
	private List<String> startedWorkoutIds;
	// Current Workout ID Collected Integer
	private String currentWorkoutId;
	// Cumulative duration of videos watched Derived Integer
	private int duration;
	// Number of completed Workouts Derived Integer
	private int completedWorkoutNum;
	// Cumulative number of social shares Derived Integer
	private int socialShareNum;

	private String currentRoutineId;
	// current routine progress seconds
	private int currentRoutineSeconds;

	// record to see if send dialog
	private boolean sendAchieveWorkoutDialog;
	// record to see if send dialog
	private boolean sendAchieveDurationDialog;

	public boolean isSendAchieveWorkoutDialog() {
		return sendAchieveWorkoutDialog;
	}

	public void setSendAchieveWorkoutDialog(boolean sendAchieveWorkoutDialog) {
		this.sendAchieveWorkoutDialog = sendAchieveWorkoutDialog;
	}

	public boolean isSendAchieveDurationDialog() {
		return sendAchieveDurationDialog;
	}

	public void setSendAchieveDurationDialog(boolean sendAchieveDurationDialog) {
		this.sendAchieveDurationDialog = sendAchieveDurationDialog;
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

	public List<String> getUnlockedChallengeIds() {
		return unlockedChallengeIds;
	}

	public void setUnlockedChallengeIds(List<String> unlockedChallengeIds) {
		this.unlockedChallengeIds = unlockedChallengeIds;
	}

	public String getCurrentChallengeId() {
		return currentChallengeId;
	}

	public void setCurrentChallengeId(String currentChallengeId) {
		this.currentChallengeId = currentChallengeId;
	}

	public int getCompletedChallengeNum() {
		return completedChallengeNum;
	}

	public void setCompletedChallengeNum(int completedChallengeNum) {
		this.completedChallengeNum = completedChallengeNum;
	}

	public List<String> getStartedWorkoutIds() {
		return startedWorkoutIds;
	}

	public void setStartedWorkoutIds(List<String> startedWorkoutIds) {
		this.startedWorkoutIds = startedWorkoutIds;
	}

	public String getCurrentWorkoutId() {
		return currentWorkoutId;
	}

	public void setCurrentWorkoutId(String currentWorkoutId) {
		this.currentWorkoutId = currentWorkoutId;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getCompletedWorkoutNum() {
		return completedWorkoutNum;
	}

	public void setCompletedWorkoutNum(int completedWorkoutNum) {
		this.completedWorkoutNum = completedWorkoutNum;
	}

	public int getSocialShareNum() {
		return socialShareNum;
	}

	public void setSocialShareNum(int socialShareNum) {
		this.socialShareNum = socialShareNum;
	}

	/**
	 * @return the currentRoutineId
	 */
	public String getCurrentRoutineId() {
		return currentRoutineId;
	}

	/**
	 * @param currentRoutineId
	 *            the currentRoutineId to set
	 */
	public void setCurrentRoutineId(String currentRoutineId) {
		this.currentRoutineId = currentRoutineId;
	}

	/**
	 * @return the currentRoutineSeconds
	 */
	public int getCurrentRoutineSeconds() {
		return currentRoutineSeconds;
	}

	/**
	 * @param currentRoutineSeconds
	 *            the currentRoutineSeconds to set
	 */
	public void setCurrentRoutineSeconds(int currentRoutineSeconds) {
		this.currentRoutineSeconds = currentRoutineSeconds;
	}

}
