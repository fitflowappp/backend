package com.magpie.yoga.dao;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.yoga.model.AchievementRecord;

@Repository
public class AchievementRecordDao extends BaseMongoRepository<AchievementRecord, Serializable> {

	@Autowired
	public AchievementRecordDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(AchievementRecord.class), mongoOps);
	}

	public List<AchievementRecord> aggregateCount(Date start, Date end) {
		Criteria criteria = new Criteria();
		addDateCriteria(start, end, criteria);

		TypedAggregation<AchievementRecord> aggregation = newAggregation(AchievementRecord.class, match(criteria),
				group("uid", "type").first("type").as("type").first("uid").as("uid"), project("uid", "type"));
		AggregationResults<AchievementRecord> result = getMongoOperations().aggregate(aggregation,
				AchievementRecord.class);
		return result.getMappedResults();
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
