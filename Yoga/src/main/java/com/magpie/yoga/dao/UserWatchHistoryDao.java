package com.magpie.yoga.dao;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.yoga.model.ChallengeSet;

@Repository
public class UserWatchHistoryDao extends BaseMongoRepository<ChallengeSet, Serializable> {

	@Autowired
	public UserWatchHistoryDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(ChallengeSet.class), mongoOps);
	}

	public ChallengeSet findOneByPhone(String phone) {
		return findOneByQuery(new Query().addCriteria(Criteria.where("phone").is(phone)));
	}
	
}
