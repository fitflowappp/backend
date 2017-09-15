package com.magpie.yoga.view;

import com.magpie.yoga.model.Challenge;

public class ChallengeView extends Challenge {

	private String currentWorkoutId;

	public String getCurrentWorkoutId() {
		return currentWorkoutId;
	}

	public void setCurrentWorkoutId(String currentWorkoutId) {
		this.currentWorkoutId = currentWorkoutId;
	}

}
