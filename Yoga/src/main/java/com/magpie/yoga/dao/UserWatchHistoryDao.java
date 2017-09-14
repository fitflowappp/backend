package com.magpie.yoga.dao;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
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

}
