package com.magpie.yoga.model;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.magpie.base.model.BaseModel;

public class UserWorkout extends BaseModel {
	private String id;
	
	private String uid;
	private String workoutId;
	@DBRef
	private Workout workout;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Workout getWorkout() {
		return workout;
	}
	public void setWorkout(Workout workout) {
		this.workout = workout;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getWorkoutId() {
		return workoutId;
	}
	public void setWorkoutId(String workoutId) {
		this.workoutId = workoutId;
	}
	
	
}
