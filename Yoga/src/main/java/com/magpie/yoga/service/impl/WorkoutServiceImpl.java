package com.magpie.yoga.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.magpie.base.query.PageQuery;
import com.magpie.yoga.dao.UserSinglesLockDao;
import com.magpie.yoga.dao.WorkoutDao;
import com.magpie.yoga.model.SinglesSort;
import com.magpie.yoga.model.UserSinglesLock;
import com.magpie.yoga.model.Workout;
import com.magpie.yoga.service.WorkoutService;
import com.magpie.yoga.view.SinglesWorkoutView;
import com.sun.corba.se.spi.orbutil.threadpool.Work;

@Service
public class WorkoutServiceImpl implements WorkoutService {
	@Autowired
	WorkoutDao workoutDao;
	@Autowired 
	UserSinglesLockDao userSinglesLockDao;
	
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
	public void updateSinglesOrder(List<SinglesSort> singlesOrder){
		if(singlesOrder!=null&&singlesOrder.size()>0){
			Workout workout=null;
			int size=singlesOrder.size();
			SinglesSort item=null;
			for(int i=0;i<size;i++){
				item=singlesOrder.get(i);
				if(item!=null){
					workout=workoutDao.findOne(item.getSinglesId());
					if(workout!=null){
						workout.setSingle(true);
						workout.setSinglesLock(item.isSinglesLock());
						workout.setSinglesSort(10000+size-i);
						workoutDao.save(workout);
					}
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
	@Override
	public Page<Workout> singleWorkoutList( PageQuery pageQuery,boolean lock) {
		// TODO Auto-generated method stub
		Page<Workout> page=workoutDao.findSinglesBySort(pageQuery, lock);
		if(page==null||page.getContent()==null||page.getContent().size()==0){
			List<Workout> list=allSinglesList();
			if(list!=null&&list.size()>0){
				for (Workout workout : list) {
					workout.setSinglesLock(true);
					save(workout);
				}
			}
		}
		return workoutDao.findSinglesBySort(pageQuery, lock);
	}
	@Override
	public List<UserSinglesLock> singleLockList(String userId) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(userId)){
			return null;
		}
		return userSinglesLockDao.findList(userId);
	}
	@Override
	public boolean unLockSingle(String userId, String singlesId) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(userId)){
			return false;
		}
		Workout workout=workoutDao.findOne(singlesId);
		if(workout!=null&&workout.isSinglesLock()&&
				userSinglesLockDao.findUserStatus(userId, singlesId)){
			UserSinglesLock userSinglesLock=new UserSinglesLock();
			userSinglesLock.setUserId(userId);
			userSinglesLock.setSinglesId(singlesId);
			userSinglesLockDao.save(userSinglesLock);
			return true;
			
		}
		return false;
	}
	@Override
	public boolean unlockStatus(String userId, String singlesId) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(userId)){
			return false;
		}
		Workout workout=workoutDao.findOne(singlesId);
		if(workout!=null&&workout.isSinglesLock()&&
				userSinglesLockDao.findUserStatus(userId, singlesId)){
			return true;
		}
		return false;
	}
}
