package com.magpie.yoga.dao;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.yoga.model.UserConfiguration;

@Repository
public class UserConfigurationDao extends BaseMongoRepository<UserConfiguration, Serializable> {

	@Autowired
	public UserConfigurationDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(UserConfiguration.class), mongoOps);
	}

	public UserConfiguration findOneByUid(String uid) {
		return findOneByQuery(new Query().addCriteria(Criteria.where("uid").is(uid)));
	}

}
