package com.magpie.yoga.dao;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.yoga.model.AppUpdateSetting;
@Repository
public class AppUpdateSettingDao extends BaseMongoRepository<AppUpdateSetting, Serializable> {
	@Autowired
	public AppUpdateSettingDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(AppUpdateSetting.class), mongoOps);
	}
	public AppUpdateSetting findOne(int system){
		Query query=new Query();
		query.addCriteria(Criteria.where("system").is(system));
		return findOneByQuery(query);
	}
}
