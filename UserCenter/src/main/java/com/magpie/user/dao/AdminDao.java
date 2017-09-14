package com.magpie.user.dao;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.user.model.Admin;

@Repository
public class AdminDao extends BaseMongoRepository<Admin, Serializable> {

	@Autowired
	public AdminDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(Admin.class), mongoOps);
	}

	public Admin findByNameAndPasswd(String name, String password) {
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is(name).and("password").is(password));
		return findOneByQuery(query);
	}

}
