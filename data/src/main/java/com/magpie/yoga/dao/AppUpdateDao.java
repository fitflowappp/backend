package com.magpie.yoga.dao;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.yoga.model.AppUpdate;
@Repository
public class AppUpdateDao extends BaseMongoRepository<AppUpdate, Serializable> {
	@Autowired
	public AppUpdateDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(AppUpdate.class), mongoOps);
	}
	public AppUpdate findOne(int build,int system){
		Query query=new Query();
		query.addCriteria(Criteria.where("build").is(build).and("system").is(system));
		return findOneByQuery(query);
	}
}
