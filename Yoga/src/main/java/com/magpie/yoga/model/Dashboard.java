package com.magpie.yoga.model;

import java.util.Date;

import com.magpie.base.model.BaseModel;

public class Dashboard extends BaseModel {

	private String id;
	private Date date;

	// Number of times Facebook registration submitted
	private long facebookRegSubmitNum;
	// Number of times Facebook registration completed
	private long facebookRegCompleteNum;
	// Number of unique users who have shared on social media
	private long shareNum;

	// Number of challenges started by unique users
	private long challengeStartNum;
	// Number of challenges completed by unique users
	private long challengeCompleteNum;
	// Number of workouts started by unique users
	private long workoutStartNum;
	// Number of workouts completed by unique users
	private long workoutCompleteNum;
	// Number of unique users who have completed a workout
	private long oneWorkoutCompleteUserNum;
	// Cumulative duration of videos watched
	private long totalDuration;

	// Number of unique users who have switched schedule notification on
	private long notificationOnNum;
	// Number of unique users who have switched schedule calendar reminder on
	private long calReminderOnNum;

	// Number of unique users who have received achievements
	private long achievementNum;

	public long getFacebookRegSubmitNum() {
		return facebookRegSubmitNum;
	}

	public void setFacebookRegSubmitNum(long facebookRegSubmitNum) {
		this.facebookRegSubmitNum = facebookRegSubmitNum;
	}

	public long getFacebookRegCompleteNum() {
		return facebookRegCompleteNum;
	}

	public void setFacebookRegCompleteNum(long facebookRegCompleteNum) {
		this.facebookRegCompleteNum = facebookRegCompleteNum;
	}

	public long getChallengeStartNum() {
		return challengeStartNum;
	}

	public void setChallengeStartNum(long challengeStartNum) {
		this.challengeStartNum = challengeStartNum;
	}

	public long getChallengeCompleteNum() {
		return challengeCompleteNum;
	}

	public void setChallengeCompleteNum(long challengeCompleteNum) {
		this.challengeCompleteNum = challengeCompleteNum;
	}

	public long getWorkoutStartNum() {
		return workoutStartNum;
	}

	public void setWorkoutStartNum(long workoutStartNum) {
		this.workoutStartNum = workoutStartNum;
	}

	public long getWorkoutCompleteNum() {
		return workoutCompleteNum;
	}

	public void setWorkoutCompleteNum(long workoutCompleteNum) {
		this.workoutCompleteNum = workoutCompleteNum;
	}

	public long getOneWorkoutCompleteUserNum() {
		return oneWorkoutCompleteUserNum;
	}

	public void setOneWorkoutCompleteUserNum(long oneWorkoutCompleteUserNum) {
		this.oneWorkoutCompleteUserNum = oneWorkoutCompleteUserNum;
	}

	public long getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(long totalDuration) {
		this.totalDuration = totalDuration;
	}

	public long getNotificationOnNum() {
		return notificationOnNum;
	}

	public void setNotificationOnNum(long notificationOnNum) {
		this.notificationOnNum = notificationOnNum;
	}

	public long getCalReminderOnNum() {
		return calReminderOnNum;
	}

	public void setCalReminderOnNum(long calReminderOnNum) {
		this.calReminderOnNum = calReminderOnNum;
	}

	public long getAchievementNum() {
		return achievementNum;
	}

	public void setAchievementNum(long achievementNum) {
		this.achievementNum = achievementNum;
	}

	public long getShareNum() {
		return shareNum;
	}

	public void setShareNum(long shareNum) {
		this.shareNum = shareNum;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

}
