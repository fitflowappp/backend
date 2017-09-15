package com.magpie.yoga.view;

import com.magpie.yoga.model.Workout;

public class WorkoutView extends Workout {

	private String currentRoutineId;

	public String getCurrentRoutineId() {
		return currentRoutineId;
	}

	public void setCurrentRoutineId(String currentRoutineId) {
		this.currentRoutineId = currentRoutineId;
	}

}
