package com.magpie.yoga.dao;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.yoga.model.UserState;

@Repository
public class UserStateDao extends BaseMongoRepository<UserState, Serializable> {

	@Autowired
	public UserStateDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(UserState.class), mongoOps);
	}

	public UserState findUserState(String uid) {
		return findOneByQuery(new Query().addCriteria(Criteria.where("uid").is(uid)));
	}

	public void updateCurrentState(String uid, String currentChallengeId, String currentWorkoutId,
			String currentRoutineId) {
		Query query = new Query().addCriteria(Criteria.where("uid").is(uid));
		updateFirst(query, new Update().set("currentChallengeId", currentChallengeId)
				.set("currentWorkoutId", currentWorkoutId).set("currentRoutineId", currentRoutineId));
	}

	public void updateCurrentState(String uid, String currentChallengeId, String currentWorkoutId,
			String currentRoutineId, int seconds) {
		Query query = new Query().addCriteria(Criteria.where("uid").is(uid));
		updateFirst(query,
				new Update().set("currentChallengeId", currentChallengeId).set("currentWorkoutId", currentWorkoutId)
						.set("currentRoutineId", currentRoutineId).set("currentRoutineSeconds", seconds));
	}

	public long countReminder() {
		return count(new Query().addCriteria(Criteria.where("reminder").is(true)));
	}

	public long countNotification() {
		return count(new Query().addCriteria(Criteria.where("notification").is(true)));
	}
}
