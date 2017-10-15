package com.magpie.yoga.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.base.query.PageQuery;
import com.magpie.yoga.model.Routine;

@Repository
public class RoutineDao extends BaseMongoRepository<Routine, Serializable> {

	@Autowired
	public RoutineDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(Routine.class), mongoOps);
	}

	public List<Routine> findRoutines(List<Routine> rids) {

		List<ObjectId> oRids = new ArrayList<>();
		for (Routine r : rids) {
			oRids.add(new ObjectId(r.getId()));
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("id").in(oRids));
		return findByQuery(query);
	}

	public List<Routine> findBySort(PageQuery pageQuery) {
		Query query = new Query();
		query.with(getSort(pageQuery));
		return findByQuery(query);
	}

	public void updateStat(Routine routine) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(new ObjectId(routine.getId())));

		Update update = new Update();
		update.set("startedTimes", routine.getStartedTimes()).set("skippedTimes", routine.getSkippedTimes());

		updateFirst(query, update);
	}

}
