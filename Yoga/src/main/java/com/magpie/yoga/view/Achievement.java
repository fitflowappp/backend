package com.magpie.yoga.view;

public class Achievement {

	private int completedWorkoutCount;

	private int completedChallengeCount;

	private int days;

	public int getCompletedWorkoutCount() {
		return completedWorkoutCount;
	}

	public void setCompletedWorkoutCount(int completedWorkoutCount) {
		this.completedWorkoutCount = completedWorkoutCount;
	}

	public int getCompletedChallengeCount() {
		return completedChallengeCount;
	}

	public void setCompletedChallengeCount(int completedChallengeCount) {
		this.completedChallengeCount = completedChallengeCount;
	}

	/**
	 * @return the days
	 */
	public int getDays() {
		return days;
	}

	/**
	 * @param days the days to set
	 */
	public void setDays(int days) {
		this.days = days;
	}

}
