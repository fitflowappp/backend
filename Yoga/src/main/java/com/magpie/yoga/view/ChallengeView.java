package com.magpie.yoga.view;

import java.util.List;

import com.magpie.yoga.model.Challenge;

public class ChallengeView extends Challenge {

	private String currentWorkoutId;

	private int status;// HistoryEvent

	private List<Integer> statuses;// workout status list

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
