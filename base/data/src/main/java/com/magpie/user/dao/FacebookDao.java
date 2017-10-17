package com.magpie.user.dao;

import java.io.Serializable;
import java.util.Date;

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

	private void addDateCriteria(Date start, Date end, Criteria criteria) {
		if (start != null && end != null) {
			criteria.andOperator(Criteria.where("crDate").gte(start), Criteria.where("crDate").lt(end));
		} else if (start != null && end == null) {
			criteria.and("crDate").gte(start);
		} else if (start == null && end != null) {
			criteria.and("crDate").lt(end);
		}
	}

	public long count(Date start, Date end) {
		Criteria criteria = new Criteria();
		addDateCriteria(start, end, criteria);
		Query query = new Query();
		query.addCriteria(criteria);
		return count(query);
	}

}
