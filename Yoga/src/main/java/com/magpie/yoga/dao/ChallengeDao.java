package com.magpie.yoga.dao;

import java.io.Serializable;
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
import com.magpie.yoga.model.Challenge;

@Repository
public class ChallengeDao extends BaseMongoRepository<Challenge, Serializable> {

	@Autowired
	public ChallengeDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(Challenge.class), mongoOps);
	}

	public Challenge findRandomOne() {
		return findOneByQuery(new Query());
	}

	public void updateStat(Challenge challenge) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(new ObjectId(challenge.getId())));

		Update update = new Update();
		update.set("startedTimes", challenge.getStartedTimes()).set("completedTimes", challenge.getCompletedTimes())
				.set("startedUserCount", challenge.getStartedUserCount())
				.set("completedUserCount", challenge.getCompletedUserCount());

		updateFirst(query, update);
	}

	public List<Challenge> findBySort(PageQuery pageQuery) {
		Query query = new Query();
		query.with(getSort(pageQuery));
		return findByQuery(query);
	}

}
