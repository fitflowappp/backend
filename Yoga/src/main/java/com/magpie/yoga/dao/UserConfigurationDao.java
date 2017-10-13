package com.magpie.yoga.dao;

import java.io.Serializable;
import java.util.Date;

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

	public long countReminder(Date start, Date end) {
		Criteria criteria = Criteria.where("reminder").is(true);
		addDateCriteria(start, end, criteria);
		return count(new Query().addCriteria(criteria));
	}

	public long countNotification(Date start, Date end) {
		Criteria criteria = Criteria.where("notification").is(true);
		addDateCriteria(start, end, criteria);
		return count(new Query().addCriteria(criteria));
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

}
