package com.magpie.yoga.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.magpie.base.query.PageQuery;
import com.magpie.yoga.model.SinglesSort;
import com.magpie.yoga.model.UserSinglesLock;
import com.magpie.yoga.model.Workout;

public interface WorkoutService {
	public List<Workout> allSinglesList();
	public Workout find(String workoutId);
	public Workout save(Workout workout);
	public void deleteSinglesWorkout(String workoutId);
	public void addSinglesWorkout(String workoutId);
	public void updateSinglesOrder(List<SinglesSort> singlesOrder);
	public void updateSinglesWorkoutSort(String workoutId,int sort);
	
	public Page<Workout> singleWorkoutList(PageQuery pageQuery,boolean lock);
	
	public boolean unLockSingle(String userId,String singlesId);
	public boolean unlockStatus(String userId,String singlesId);
	
	public List<UserSinglesLock> singleLockList(String userId);
	

}
