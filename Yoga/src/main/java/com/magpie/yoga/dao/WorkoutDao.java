package com.magpie.yoga.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.base.query.PageQuery;
import com.magpie.yoga.model.Workout;

@Repository
public class WorkoutDao extends BaseMongoRepository<Workout, Serializable> {

	@Autowired
	public WorkoutDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(Workout.class), mongoOps);
	}

	public List<Workout> findWorkouts(List<Workout> workouts) {

		List<ObjectId> oRids = new ArrayList<>();
		for (Workout w : workouts) {
			oRids.add(new ObjectId(w.getId()));
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("id").in(oRids));
		return findByQuery(query);
	}

	public List<Workout> findsginleList() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isSingle").in(true));
		query.with(new Sort(new Order(Direction.DESC, "singlesSort")));
		return findByQuery(query);
	}

	public List<Workout> findBySort(PageQuery pageQuery) {
		Query query = new Query();
		query.with(getSort(pageQuery));
		return findByQuery(query);
	}

	public Page<Workout> findSinglesBySort(PageQuery pageQuery, boolean lock) {
		Query query = new Query();
		Criteria criteria = Criteria.where("isSingle").is(true);
		query.with(new Sort(new Order(Direction.DESC, "singlesSort")));
		if (lock) {
			criteria=criteria.and("singlesLock").is(lock);
		}
		query.addCriteria(criteria);

		return findByQuery(query, pageQuery);
	}

	public void updateStat(Workout workout) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(new ObjectId(workout.getId())));

		Update update = new Update();
		update.set("startedTimes", workout.getStartedTimes()).set("completedTimes", workout.getCompletedTimes())
				.set("startedUserCount", workout.getStartedUserCount())
				.set("completedUserCount", workout.getCompletedUserCount())
				.set("totalDuration", workout.getTotalDuration());

		updateFirst(query, update);
	}

	public void updateSingle(Workout workout) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(new ObjectId(workout.getId())));

		Update update = new Update();
		update.set("isSingle", workout.isSingle());
		update.set("singlesSort", workout.getSinglesSort());

		updateFirst(query, update);
	}

	public void updateSingle(String workoutId, boolean isSingles) {
		updateSingle(workoutId, isSingles, 0);
	}

	public void updateSingle(String workoutId, boolean isSingles, int sort) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(new ObjectId(workoutId)));

		Update update = new Update();
		update.set("isSingle", isSingles);
		update.set("singlesSort", sort);
		updateFirst(query, update);
	}

	public Workout minSortSingleWorkout() {
		Query query = new Query();
		query.addCriteria(Criteria.where("isSingle").is(true));
		query.with(new Sort(new Order(Direction.ASC, "singlesSort")));
		query.limit(1);
		return findOneByQuery(query);

	}

	/**
	 * 更新singles 排序 @Title: updateSingleSortl @Description:
	 * ，比sort等于或者小的减少1个，比sort大的增加一个 @param @param sort 设定文件 @return void
	 * 返回类型 @throws
	 */
	public void updateSingleSortl(int sort) {
		Query query = new Query();
		query.addCriteria(Criteria.where("singlesSort").lte(sort));
		query.addCriteria(Criteria.where("isSingle").is(true));
		Update update = new Update();
		update.inc("singlesSort", -1);

		updateMulti(query, update);

		query = new Query();
		query.addCriteria(Criteria.where("singlesSort").gt(sort));
		query.addCriteria(Criteria.where("isSingle").is(true));
		update = new Update();
		update.inc("singlesSort", 1);
		updateMulti(query, update);
	}

}
