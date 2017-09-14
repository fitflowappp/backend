package com.magpie.yoga.model;

import com.magpie.base.model.BaseModel;

public class Milestone extends BaseModel {

	private String id;
	// Frequency of total exercise minutes completed with which to give
	// achievement Admin create new or edit Integer Minutes 30 300 60
	private int achievementMinutes;
	// Frequency of total workout number completed with which to give
	// achievement Admin create new or edit Integer Workouts 1 20 5
	private int achievementWorkoutNum;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getAchievementMinutes() {
		return achievementMinutes;
	}

	public void setAchievementMinutes(int achievementMinutes) {
		this.achievementMinutes = achievementMinutes;
	}

	public int getAchievementWorkoutNum() {
		return achievementWorkoutNum;
	}

	public void setAchievementWorkoutNum(int achievementWorkoutNum) {
		this.achievementWorkoutNum = achievementWorkoutNum;
	}

}
