package com.magpie.yoga.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magpie.yoga.dao.WorkoutDao;
import com.magpie.yoga.model.Workout;

@Service
public class WorkoutService {
	@Autowired
	WorkoutDao workoutDao;
	
	
	public List<Workout> allSinglesList(){
		return workoutDao.findsginleList();
	}
	public Workout find(String workoutId){
		return workoutDao.findOne(workoutId);
	}
	public Workout save(Workout workout){
		return workoutDao.save(workout);
	}
	public void deleteSinglesWorkout(String workoutId){
		workoutDao.updateSingle(workoutId, false);
	}
	public void addSinglesWorkout(String workoutId){
		Workout workout=workoutDao.minSortSingleWorkout();
		if(workout==null){
			
			workoutDao.updateSingle(workoutId, true);
		}else{
			workoutDao.updateSingle(workoutId, true, workout.getSinglesSort()-1);
		}
	}
	public void updateSinglesOrder(List<String> singlesOrder){
		if(singlesOrder!=null&&singlesOrder.size()>0){
			Workout workout=null;
			int size=singlesOrder.size();
			for(int i=0;i<size;i++){
				workout=workoutDao.findOne(singlesOrder.get(i));
				if(workout!=null){
					workout.setSingle(true);
					workout.setSinglesSort(10000+size-i);
					workoutDao.save(workout);
				}
			}
		}
	}
	public void updateSinglesWorkoutSort(String workoutId,int sort){
		
		workoutDao.updateSingleSortl(sort);
		
		Workout workout=workoutDao.findOne(workoutId);
		workout.setSinglesSort(sort);
		workoutDao.save(workout);
		
	}
}
