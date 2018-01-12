package com.magpie.yoga.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.yoga.model.UserWorkout;
import com.magpie.yoga.model.Workout;
@Repository
public class UserWorkoutDao extends BaseMongoRepository<UserWorkout, Serializable> {
	@Autowired
	public UserWorkoutDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(UserWorkout.class), mongoOps);
	}

	public List<Workout> findWorkoutSortByDate(String uid){
		List<UserWorkout> userWorkoutList=findSortByDate(uid);
		List<Workout> workoutList=new ArrayList<>();
		for (UserWorkout userWorkout : userWorkoutList) {
			workoutList.add(userWorkout.getWorkout());
		}
		return workoutList;
	}
	public List<UserWorkout> findSortByDate(String uid){
		return findSortByDate(uid, null);
	}
	public List<UserWorkout> findSortByDate(String uid,String workoutId){
		return findSortByDate(uid, workoutId, false);
	}
	public List<UserWorkout> findSortByDate(String uid,String workoutId,boolean isAll){
		Query query = new Query();
		query.addCriteria(Criteria.where("uid").in(uid));
		if(!isAll){
			query.addCriteria(Criteria.where("isDelete").in(false));
		}
		if(workoutId!=null&&workoutId.length()>0){
			query.addCriteria(Criteria.where("workoutId").in(workoutId));
		}
		
		return findByQuery(query);
	}
	
	public void updateDeleteStatus(String uid,String workoutId){
		Query query = new Query();
		query.addCriteria(Criteria.where("uid").is(uid));
		query.addCriteria(Criteria.where("workoutId").is(workoutId));
		Update update = new Update();
		update.set("isDelete", true);
		updateMulti(query, update);
	}
	
	

	
	
}