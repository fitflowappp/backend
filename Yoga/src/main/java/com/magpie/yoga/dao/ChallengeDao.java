package com.magpie.yoga.dao;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
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

}
