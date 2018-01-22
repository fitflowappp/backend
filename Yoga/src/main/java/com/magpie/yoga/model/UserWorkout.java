package com.magpie.yoga.model;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.magpie.base.model.BaseModel;

public class UserWorkout extends BaseModel {
	public static int SYSTEM=10001;
	public static int USER=SYSTEM+1;

	private String id;

	private String uid;
	private boolean isDelete = false;
	private String workoutId;
	private int from = SYSTEM;// workout的来源，目前是系统默认添加的，还有一种是用户自己添加的，第一种是0，第二种是1
	@DBRef
	private Workout workout;

	

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

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
