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
import com.magpie.yoga.model.UserSinglesLock;
@Repository
public class UserSinglesLockDao extends BaseMongoRepository<UserSinglesLock, Serializable> {
	@Autowired
	public UserSinglesLockDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(UserSinglesLock.class), mongoOps);
	}
	public List<UserSinglesLock> findList(String userId){
		Query query=new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		return findByQuery(query);
	}
	public boolean findUserStatus(String userId,String singles){
		Query query=new Query();
		query.addCriteria(Criteria.where("userId").is(userId).and("singlesId").is(singles));
		return findOneByQuery(query)==null?false:true;
	}
}
