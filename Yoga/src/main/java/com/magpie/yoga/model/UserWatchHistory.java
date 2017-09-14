package com.magpie.yoga.model;

import com.magpie.base.model.BaseModel;

public class UserWatchHistory extends BaseModel {

	private String id;
	private String uid;
	private String routineId;
	private String workoutId;
	private String challengeId;
	
	private int event;//@see HistoryEvent

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
	
}
