package com.magpie.yoga.dao;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.yoga.model.UserWatchHistory;
import com.magpie.yoga.stat.UserWatchHistoryStat;

@Repository
public class UserWatchHistoryDao extends BaseMongoRepository<UserWatchHistory, Serializable> {

	@Autowired
	public UserWatchHistoryDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(UserWatchHistory.class), mongoOps);
	}

	public UserWatchHistory findLastHistory(String uid) {
		Query query = new Query();
		query.addCriteria(Criteria.where("uid").is(uid));
		query.with(new Sort(Direction.DESC, "crDate"));
		return findOneByQuery(query);
	}

	public UserWatchHistory findUserHistory(String uid, String cid, int destType) {
		Query query = new Query();
		query.addCriteria(Criteria.where("uid").is(uid).and("challengeId").is(cid).and("destType").is(destType));
		query.with(new Sort(Direction.DESC, "event"));
		return findOneByQuery(query);
	}

	public UserWatchHistory findUserHistory(String uid, String cid, String wid, int destType) {
		Query query = new Query();
		query.addCriteria(Criteria.where("uid").is(uid).and("challengeId").is(cid).and("workoutId").is(wid)
				.and("destType").lte(destType));
		query.with(new Sort(Direction.DESC, "event"));
		return findOneByQuery(query);
	}

	public UserWatchHistory findUserHistory(String uid, String cid, String wid, String rid) {
		Query query = new Query();
		query.addCriteria(Criteria.where("uid").is(uid).and("challengeId").is(cid).and("workoutId").is(wid)
				.and("routineId").is(rid));
		query.with(new Sort(Direction.DESC, "event"));
		return findOneByQuery(query);
	}

	public List<UserWatchHistoryStat> aggregateUserWatchHistory(String uid, int event) {
		TypedAggregation<UserWatchHistory> aggregation = newAggregation(UserWatchHistory.class,
				match(Criteria.where("uid").is(uid).and("event").is(event)),
				group("uid", "destType").sum("duration").as("duration").count().as("count").first("uid").as("uid")
						.first("destType").as("destType"),
				project("uid", "duration", "count", "destType"), sort(new Sort(Direction.DESC, "duration")));
		AggregationResults<UserWatchHistoryStat> result = getMongoOperations().aggregate(aggregation,
				UserWatchHistoryStat.class);
		return result.getMappedResults();
	}

	public List<UserWatchHistoryStat> aggregateStartCompleteNum(Date start, Date end) {

		Criteria criteria = new Criteria();
		addDateCriteria(start, end, criteria);

		TypedAggregation<UserWatchHistory> aggregation = newAggregation(
				UserWatchHistory.class, match(criteria), group("event", "destType").sum("duration").as("duration")
						.count().as("count").first("destType").as("destType").first("event").as("event"),
				project("duration", "count", "destType", "event"));
		AggregationResults<UserWatchHistoryStat> result = getMongoOperations().aggregate(aggregation,
				UserWatchHistoryStat.class);
		return result.getMappedResults();
	}

	public List<UserWatchHistoryStat> aggregateWorkoutCompleteUsers(int destType, int event, Date start, Date end) {
		Criteria criteria = Criteria.where("event").is(event).and("destType").lte(destType);
		addDateCriteria(start, end, criteria);

		TypedAggregation<UserWatchHistory> aggregation = newAggregation(UserWatchHistory.class, match(criteria),
				group("uid").count().as("count"), project("count"));
		AggregationResults<UserWatchHistoryStat> result = getMongoOperations().aggregate(aggregation,
				UserWatchHistoryStat.class);
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

	public UserWatchHistoryStat aggregateTotalDuration(int event, Date start, Date end) {
		Criteria criteria = Criteria.where("event").is(event);
		addDateCriteria(start, end, criteria);

		TypedAggregation<UserWatchHistory> aggregation = newAggregation(UserWatchHistory.class, match(criteria),
				group().sum("duration").as("duration"), project("duration"));
		AggregationResults<UserWatchHistoryStat> result = getMongoOperations().aggregate(aggregation,
				UserWatchHistoryStat.class);
		if (result.getMappedResults() != null && !result.getMappedResults().isEmpty()) {
			return result.getMappedResults().get(0);
		} else {
			return new UserWatchHistoryStat();
		}
	}

}
