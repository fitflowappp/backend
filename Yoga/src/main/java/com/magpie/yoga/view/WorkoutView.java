package com.magpie.yoga.view;

import java.util.List;

import com.magpie.share.ResourceRef;

public class WorkoutView {

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
	private List<RoutineView> routines;

	private String currentRoutineId;
	private int seconds;
	private String challengeId;

	private int status;// HistoryEvent
	private boolean avail;
	private boolean isSingle=false;
	
	private String singleChallengeId;

	private boolean isfollow=false;
	
	private String shareUrl;
	
	private boolean singlesLock=false;

	public boolean isSinglesLock() {
		return singlesLock;
	}

	public void setSinglesLock(boolean singlesLock) {
		this.singlesLock = singlesLock;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public boolean isIsfollow() {
		return isfollow;
	}

	public void setIsfollow(boolean isfollow) {
		this.isfollow = isfollow;
	}
	public String getSingleChallengeId() {
		return singleChallengeId;
	}

	public void setSingleChallengeId(String singleChallengeId) {
		this.singleChallengeId = singleChallengeId;
	}

	public boolean isSingle() {
		return isSingle;
	}

	public void setSingle(boolean isSingle) {
		this.isSingle = isSingle;
	}

	public String getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(String challengeId) {
		this.challengeId = challengeId;
	}

	public String getCurrentRoutineId() {
		return currentRoutineId;
	}

	public void setCurrentRoutineId(String currentRoutineId) {
		this.currentRoutineId = currentRoutineId;
	}

	/**
	 * @return the seconds
	 */
	public int getSeconds() {
		return seconds;
	}

	/**
	 * @param seconds
	 *            the seconds to set
	 */
	public void setSeconds(int seconds) {
		this.seconds = seconds;
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

	public List<RoutineView> getRoutines() {
		return routines;
	}

	public void setRoutines(List<RoutineView> routines) {
		this.routines = routines;
	}

}
