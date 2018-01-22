package com.magpie.yoga.dao;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.yoga.model.UserWorkout;
import com.magpie.yoga.model.Workout;
import com.magpie.yoga.statistics.model.UserWorkoutAggregate;
@Repository
public class UserWorkoutDao extends BaseMongoRepository<UserWorkout, Serializable> {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	
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
	public UserWorkout findOne(String uid,String workoutId){
		Query query = new Query();
		query.addCriteria(Criteria.where("uid").in(uid));
		
		if(workoutId!=null&&workoutId.length()>0){
			query.addCriteria(Criteria.where("workoutId").in(workoutId));
		}
		
		return findOneByQuery(query);
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
		updateDeleteStatus(uid, workoutId, true);
	}
	public void updateDeleteStatus(String uid,String workoutId,boolean isDelete){
		Query query = new Query();
		query.addCriteria(Criteria.where("uid").is(uid));
		query.addCriteria(Criteria.where("workoutId").is(workoutId));
		Update update = new Update();
		update.set("isDelete", isDelete);
		updateMulti(query, update);
	}
	public void updateDeleteStatus(String uid,String workoutId,int from,boolean isDelete){
		Query query = new Query();
		query.addCriteria(Criteria.where("uid").is(uid));
		query.addCriteria(Criteria.where("workoutId").is(workoutId));
		Update update = new Update();
		update.set("isDelete", isDelete);
		if(from>0){
			update.set("from", from);
		}
		updateMulti(query, update);
	}
	
	public List<UserWorkoutAggregate> aggregateCountPerUser() {
		TypedAggregation<UserWorkout> aggregation = newAggregation(UserWorkout.class,
	            match(Criteria.where("from").is(UserWorkout.USER)),
				group("workoutId","isDelete").count().as("count"),
				project("workoutId", "isDelete","count" ));
		AggregationResults<UserWorkoutAggregate> result = getMongoOperations().aggregate(aggregation,
				UserWorkoutAggregate.class);
		logger.info("result:"+result.getServerUsed());
		return result.getMappedResults();
	}
	
	
}
