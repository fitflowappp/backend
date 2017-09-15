package com.magpie.yoga.dao;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.yoga.model.UserWatchHistory;

@Repository
public class UserWatchHistoryDao extends BaseMongoRepository<UserWatchHistory, Serializable> {

	@Autowired
	public UserWatchHistoryDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(UserWatchHistory.class), mongoOps);
	}

	public UserWatchHistory findLastHistory(String uid) {
		Query query = new Query();
		query.addCriteria(Criteria.where("uid").is(uid));
		query.with(new Sort(Direction.DESC, "crDate"));
		return findOneByQuery(query);
	}

	public UserWatchHistory findLastHistory(String uid, String workoutId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("uid").is(uid).and("workoutId").is(workoutId));
		query.with(new Sort(Direction.DESC, "crDate"));
		return findOneByQuery(query);
	}
}
