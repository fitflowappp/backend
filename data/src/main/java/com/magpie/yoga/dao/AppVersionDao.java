package com.magpie.yoga.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.yoga.model.AppVersion;
@Repository
public class AppVersionDao extends BaseMongoRepository<AppVersion, Serializable> {
	@Autowired
	public AppVersionDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(AppVersion.class), mongoOps);
	}
	public AppVersion findOne(int system,String version,int build){
		Query query=new Query();
		query.addCriteria(Criteria.where("system").is(system).
				and("version").is(version).
				and("build").is(build));
		return findOneByQuery(query);
	}
	
	public List<AppVersion> findList(int system){
		Query query=new Query();
		query.addCriteria(Criteria.where("system").is(system));
		return findByQuery(query);
	}
	
}
