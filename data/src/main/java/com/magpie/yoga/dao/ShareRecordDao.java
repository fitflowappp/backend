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
import com.magpie.yoga.model.ShareRecord;

@Repository
public class ShareRecordDao extends BaseMongoRepository<ShareRecord, Serializable> {

	@Autowired
	public ShareRecordDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(ShareRecord.class), mongoOps);
	}

	public long count(Date start, Date end) {
		Criteria criteria = new Criteria();
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

	public long count(String uid) {
		Query query = new Query();
		query.addCriteria(Criteria.where("uid").is(uid));
		return count(query);
	}
}
