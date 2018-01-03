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
import com.magpie.base.query.PageQuery;
import com.magpie.yoga.model.Workout;

@Repository
public class WorkoutDao extends BaseMongoRepository<Workout, Serializable> {

	@Autowired
	public WorkoutDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(Workout.class), mongoOps);
	}

	public List<Workout> findWorkouts(List<Workout> workouts) {

		List<ObjectId> oRids = new ArrayList<>();
		for (Workout w : workouts) {
			oRids.add(new ObjectId(w.getId()));
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("id").in(oRids));
		return findByQuery(query);
	}

	public List<Workout> findsginleList() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isSingle").in(true));

		return findByQuery(query);
	}
	public List<Workout> findBySort(PageQuery pageQuery) {
		Query query = new Query();
		query.with(getSort(pageQuery));
		return findByQuery(query);
	}

	public void updateStat(Workout workout) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(new ObjectId(workout.getId())));

		Update update = new Update();
		update.set("startedTimes", workout.getStartedTimes()).set("completedTimes", workout.getCompletedTimes())
				.set("startedUserCount", workout.getStartedUserCount())
				.set("completedUserCount", workout.getCompletedUserCount())
				.set("totalDuration", workout.getTotalDuration());

		updateFirst(query, update);
	}
	public void updateSingle(Workout workout){
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(new ObjectId(workout.getId())));

		Update update = new Update();
		update.set("isSingle",workout.isSingle());

		updateFirst(query, update);
	}
}
