package com.magpie.yoga.statistics.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magpie.base.query.PageQuery;
import com.magpie.yoga.dao.UserWorkoutDao;
import com.magpie.yoga.dao.WorkoutDao;
import com.magpie.yoga.model.Workout;
import com.magpie.yoga.statistics.model.UserWorkoutAggregate;
import com.magpie.yoga.statistics.service.WorkoutStatisticsService;
import com.magpie.yoga.view.WorkoutStatisticsView;

@Service
public class WorkoutStatisticsServiceImpl implements WorkoutStatisticsService {
	@Autowired
	UserWorkoutDao userWorkoutDao;
	@Autowired
	WorkoutDao workoutDao;
	
	@Override
	public List<WorkoutStatisticsView> getWorkoutList(PageQuery pageQuery) {
		// TODO Auto-generated method stub
		List<Workout> workoutList=null;
		boolean needSort=false;;
		if(pageQuery.getSortKey().equals("userFavCount")||pageQuery.getSortKey().equals("userFavCount")){
			workoutList=workoutDao.findAll();
			needSort=true;
		}else{
			workoutList=workoutDao.findBySort(pageQuery);
		}
			
		List<UserWorkoutAggregate> userWorkouts=userWorkoutDao.aggregateCountPerUser();
		int userWorkoutsSize=userWorkouts.size();
		List<WorkoutStatisticsView> workoutStatisticsViews=new ArrayList<>();
		WorkoutStatisticsView workoutStatisticsView=null;
		UserWorkoutAggregate userWorkout =null;
		for (Workout workout : workoutList) {
			workoutStatisticsView=new WorkoutStatisticsView();
			BeanUtils.copyProperties(workout,workoutStatisticsView);
			workoutStatisticsView.setUserFavCount(0);
			workoutStatisticsView.setUserFavedCount(0);
			for(int i=0;i<userWorkoutsSize;i++){
				userWorkout=userWorkouts.get(i);
				if(userWorkout.getWorkoutId().equals(workoutStatisticsView.getId())){
					workoutStatisticsView.setUserFavedCount(workoutStatisticsView.getUserFavCount()+userWorkout.getCount());
					if(userWorkout.isDelete()==false){
						workoutStatisticsView.setUserFavCount(workoutStatisticsView.getUserFavCount()+userWorkout.getCount());
					}
				}
			}
			workoutStatisticsViews.add(workoutStatisticsView);
		}
		if(needSort){
			int sortType =0,sortDirection=pageQuery.getSortType();
			if(pageQuery.getSortKey().equals("userFavCount")){
				sortType=1;
			}else{
				sortType=2;
			}
			final int finalSortType=sortType;
			final int finalSortDirection=sortDirection;
			Collections.sort(workoutStatisticsViews, new Comparator<WorkoutStatisticsView>(){

				@Override
				public int compare(WorkoutStatisticsView o1, WorkoutStatisticsView o2) {
					// TODO Auto-generated method stub
					int result=0;
					if(finalSortType==1){
						result=(o1.getUserFavCount()-o2.getUserFavCount());
					}else{
						result=(o1.getUserFavedCount()-o2.getUserFavedCount());
					}
					
					return finalSortDirection*result;
				}
				
			});
		}
		return workoutStatisticsViews;
	}
}
