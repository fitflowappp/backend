package com.magpie.dao;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.user.model.User;

@Repository
public class UserDao extends BaseMongoRepository<User, Serializable> {

	@Autowired
	public UserDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(User.class), mongoOps);
	}

	public User findOneByPhone(String phone) {
		return findOneByQuery(new Query().addCriteria(Criteria.where("phone").is(phone)));
	}
	
}
