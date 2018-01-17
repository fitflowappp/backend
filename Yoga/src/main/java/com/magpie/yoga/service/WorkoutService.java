package com.magpie.yoga.service;

import java.util.List;

import com.magpie.yoga.model.Workout;

public interface WorkoutService {
	public List<Workout> allSinglesList();
	public Workout find(String workoutId);
	public Workout save(Workout workout);
	public void deleteSinglesWorkout(String workoutId);
	public void addSinglesWorkout(String workoutId);
	public void updateSinglesOrder(List<String> singlesOrder);
	public void updateSinglesWorkoutSort(String workoutId,int sort);
}
