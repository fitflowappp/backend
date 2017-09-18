package com.magpie.yoga.model;

import com.magpie.base.model.BaseModel;

public class UserWatchHistory extends BaseModel {

	private String id;
	private String uid;
	private String routineId;
	private String workoutId;
	private String challengeId;
	// process seconds
	private int seconds;

	private int event;// @see HistoryEvent
	private boolean workout;//
	private boolean challenge;
	private boolean routine;

	public boolean isWorkout() {
		return workout;
	}

	public void setWorkout(boolean workout) {
		this.workout = workout;
	}

	public boolean isChallenge() {
		return challenge;
	}

	public void setChallenge(boolean challenge) {
		this.challenge = challenge;
	}

	public UserWatchHistory() {

	}

	public UserWatchHistory(String challengeId, String workoutId, String routineId) {
		this.challengeId = challengeId;
		this.workoutId = workoutId;
		this.routineId = workoutId;
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

	public String getRoutineId() {
		return routineId;
	}

	public void setRoutineId(String routineId) {
		this.routineId = routineId;
	}

	public String getWorkoutId() {
		return workoutId;
	}

	public void setWorkoutId(String workoutId) {
		this.workoutId = workoutId;
	}

	public String getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(String challengeId) {
		this.challengeId = challengeId;
	}

	public int getEvent() {
		return event;
	}

	public void setEvent(int event) {
		this.event = event;
	}

	/**
	 * @return the routine
	 */
	public boolean isRoutine() {
		return routine;
	}

	/**
	 * @param routine
	 *            the routine to set
	 */
	public void setRoutine(boolean routine) {
		this.routine = routine;
	}

	/**
	 * @return the seconds
	 */
	public int getSeconds() {
		return seconds;
	}

	/**
	 * @param seconds the seconds to set
	 */
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

}
