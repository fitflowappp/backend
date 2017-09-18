package com.magpie.yoga.view;

import com.magpie.yoga.model.Workout;

public class WorkoutView extends Workout {

	private String currentRoutineId;
	private int seconds;
	private String challengeId;

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

}
