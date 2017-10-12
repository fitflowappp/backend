package com.magpie.user.dao;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.user.model.FaceBookUser;

@Repository
public class FacebookDao extends BaseMongoRepository<FaceBookUser, Serializable> {

	@Autowired
	public FacebookDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(FaceBookUser.class), mongoOps);
	}

	public FaceBookUser findByFacebookUid(String facebookUid) {
		Query query = new Query();
		query.addCriteria(Criteria.where("facebookUid").is(facebookUid));
		return findOneByQuery(query);
	}

	public FaceBookUser findByUid(String uid) {
		Query query = new Query();
		query.addCriteria(Criteria.where("uid").is(uid));
		return findOneByQuery(query);
	}

}
