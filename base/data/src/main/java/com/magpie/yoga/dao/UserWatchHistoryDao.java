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
import com.magpie.yoga.constant.HistoryDest;
import com.magpie.yoga.constant.HistoryEvent;
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

	public List<UserWatchHistoryStat> aggregateCountPerUser(String uid) {
		TypedAggregation<UserWatchHistory> aggregation = newAggregation(UserWatchHistory.class,
				match(Criteria.where("uid").is(uid)),
				group("uid", "event", "destType").sum("duration").as("duration").count().as("count").first("uid")
						.as("uid").first("destType").as("destType").first("event").as("event"),
				project("uid", "duration", "count", "destType", "event"), sort(new Sort(Direction.DESC, "duration")));
		AggregationResults<UserWatchHistoryStat> result = getMongoOperations().aggregate(aggregation,
				UserWatchHistoryStat.class);
		return result.getMappedResults();
	}

	public List<UserWatchHistoryStat> aggregateWorkoutWatchHistory(String uid, int event, int destType) {
		TypedAggregation<UserWatchHistory> aggregation = newAggregation(UserWatchHistory.class,
				match(Criteria.where("uid").is(uid).and("event").gte(event).and("destType").lte(destType)),
				group("uid", "workoutId").first("workoutId").as("workoutId").sum("duration").as("duration").count()
						.as("count").first("uid").as("uid").first("destType").as("destType").max("event").as("event"),
				project("uid", "duration", "workoutId", "count", "destType", "event"),
				sort(new Sort(Direction.DESC, "duration")));
		AggregationResults<UserWatchHistoryStat> result = getMongoOperations().aggregate(aggregation,
				UserWatchHistoryStat.class);
		return result.getMappedResults();
	}

	public int aggregateRoutineDuration(String uid) {
		TypedAggregation<UserWatchHistory> aggregation = newAggregation(UserWatchHistory.class,
				match(Criteria.where("uid").is(uid).and("destType").is(HistoryDest.ROUTINE.getCode()).and("event")
						.is(HistoryEvent.COMPLETE.getCode())),
				group("uid").sum("duration").as("duration").first("uid").as("uid"), project("uid", "duration"));
		AggregationResults<UserWatchHistoryStat> result = getMongoOperations().aggregate(aggregation,
				UserWatchHistoryStat.class);
		if (result.getMappedResults() != null && !result.getMappedResults().isEmpty()) {
			return result.getMappedResults().get(0).getDuration();
		} else {
			return 0;
		}
	}

	public List<UserWatchHistoryStat> aggregateWorkoutDuration() {
		TypedAggregation<UserWatchHistory> aggregation = newAggregation(UserWatchHistory.class,
				match(Criteria.where("destType").is(HistoryDest.ROUTINE.getCode()).and("event")
						.is(HistoryEvent.COMPLETE.getCode())),
				group("workoutId").sum("duration").as("duration").first("workoutId").as("workoutId"),
				project("workoutId", "duration"));
		AggregationResults<UserWatchHistoryStat> result = getMongoOperations().aggregate(aggregation,
				UserWatchHistoryStat.class);

		return result.getMappedResults();

	}

	public List<UserWatchHistoryStat> aggregateChallengeWatchHistory(String uid, int event, int destType) {
		TypedAggregation<UserWatchHistory> aggregation = newAggregation(UserWatchHistory.class,
				match(Criteria.where("uid").is(uid).and("event").gte(event).and("destType").lte(destType)),
				group("uid", "challengeId").first("challengeId").as("challengeId").sum("duration").as("duration")
						.count().as("count").first("uid").as("uid").first("destType").as("destType").max("event")
						.as("event"),
				project("uid", "duration", "challengeId", "count", "destType", "event"),
				sort(new Sort(Direction.DESC, "duration")));
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

	public List<UserWatchHistoryStat> aggregateWorkoutUsers(int event, Date start, Date end) {
		Criteria criteria = Criteria.where("event").is(event).and("destType").lte(HistoryDest.WORKOUT.getCode());
		addDateCriteria(start, end, criteria);

		TypedAggregation<UserWatchHistory> aggregation = newAggregation(UserWatchHistory.class, match(criteria),
				group("uid").count().as("count"), project("count"));
		AggregationResults<UserWatchHistoryStat> result = getMongoOperations().aggregate(aggregation,
				UserWatchHistoryStat.class);
		return result.getMappedResults();
	}

	public List<UserWatchHistoryStat> aggregateChallengeCount(Date start, Date end) {

		Criteria criteria = Criteria.where("destType").is(HistoryDest.CHALLENGE.getCode());
		addDateCriteria(start, end, criteria);

		TypedAggregation<UserWatchHistory> aggregation = newAggregation(UserWatchHistory.class, match(criteria),
				group("event", "challengeId").sum("duration").as("duration").count().as("count").first("challengeId")
						.as("challengeId").first("destType").as("destType").first("event").as("event"),
				project("duration", "count", "challengeId", "destType", "event"));
		AggregationResults<UserWatchHistoryStat> result = getMongoOperations().aggregate(aggregation,
				UserWatchHistoryStat.class);
		return result.getMappedResults();
	}

	public List<UserWatchHistoryStat> aggregateWorkoutCount(Date start, Date end) {

		Criteria criteria = Criteria.where("destType").lte(HistoryDest.WORKOUT.getCode());
		addDateCriteria(start, end, criteria);

		TypedAggregation<UserWatchHistory> aggregation = newAggregation(UserWatchHistory.class, match(criteria),
				group("event", "workoutId").sum("duration").as("duration").count().as("count").first("workoutId")
						.as("workoutId").first("destType").as("destType").first("event").as("event"),
				project("duration", "count", "workoutId", "destType", "event"));
		AggregationResults<UserWatchHistoryStat> result = getMongoOperations().aggregate(aggregation,
				UserWatchHistoryStat.class);
		return result.getMappedResults();
	}

	public List<UserWatchHistoryStat> aggregateRoutineCount(Date start, Date end) {

		Criteria criteria = new Criteria();
		addDateCriteria(start, end, criteria);

		TypedAggregation<UserWatchHistory> aggregation = newAggregation(UserWatchHistory.class, match(criteria),
				group("event", "routineId").sum("duration").as("duration").count().as("count").first("routineId")
						.as("routineId").first("destType").as("destType").first("event").as("event"),
				project("duration", "routineId", "count", "destType", "event"));
		AggregationResults<UserWatchHistoryStat> result = getMongoOperations().aggregate(aggregation,
				UserWatchHistoryStat.class);
		return result.getMappedResults();
	}

	public List<UserWatchHistoryStat> aggregateChallengeUsers(Date start, Date end) {
		Criteria criteria = Criteria.where("destType").is(HistoryDest.CHALLENGE.getCode());
		addDateCriteria(start, end, criteria);

		TypedAggregation<UserWatchHistory> aggregation = newAggregation(
				UserWatchHistory.class, match(criteria), group("uid", "challengeId").first("challengeId")
						.as("challengeId").first("uid").as("uid").count().as("count").max("event").as("event"),
				project("count", "uid", "challengeId", "event"));
		AggregationResults<UserWatchHistoryStat> result = getMongoOperations().aggregate(aggregation,
				UserWatchHistoryStat.class);
		return result.getMappedResults();
	}

	public List<UserWatchHistoryStat> aggregateWorkoutUsers(Date start, Date end) {
		Criteria criteria = Criteria.where("destType").lte(HistoryDest.WORKOUT.getCode());
		addDateCriteria(start, end, criteria);

		TypedAggregation<UserWatchHistory> aggregation = newAggregation(
				UserWatchHistory.class, match(criteria), group("uid", "workoutId").first("workoutId").as("workoutId")
						.first("uid").as("uid").count().as("count").max("event").as("event"),
				project("count", "uid", "workoutId", "event"));
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

	public boolean exists(UserWatchHistory history) {
		Query query = new Query();
		query.addCriteria(Criteria.where("uid").is(history.getUid()).and("challengeId").is(history.getChallengeId())
				.and("workoutId").is(history.getWorkoutId()).and("routineId").is(history.getRoutineId()).and("event")
				.is(history.getEvent()).and("destType").is(history.getDestType()));
		return exists(query);
	}

	public int countSkipWorkout(UserWatchHistory history) {
		Query query = new Query();
		query.addCriteria(Criteria.where("uid").is(history.getUid()).and("challengeId").is(history.getChallengeId())
				.and("workoutId").is(history.getWorkoutId()).and("event").is(history.getEvent()));
		return (int) count(query);
	}

}
