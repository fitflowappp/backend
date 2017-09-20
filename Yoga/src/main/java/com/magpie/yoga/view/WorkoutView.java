package com.magpie.yoga.view;

import java.util.List;

import com.magpie.yoga.model.Workout;

public class WorkoutView extends Workout {

	private String currentRoutineId;
	private int seconds;
	private String challengeId;

	private int status;// HistoryEvent

	private List<Integer> statuses;// routine status list

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
	 * @return the statuses
	 */
	public List<Integer> getStatuses() {
		return statuses;
	}

	/**
	 * @param statuses
	 *            the statuses to set
	 */
	public void setStatuses(List<Integer> statuses) {
		this.statuses = statuses;
	}

}
