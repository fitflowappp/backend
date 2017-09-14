package com.magpie.yoga.dao;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.yoga.model.Routine;

@Repository
public class RoutineDao extends BaseMongoRepository<Routine, Serializable> {

	@Autowired
	public RoutineDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(Routine.class), mongoOps);
	}

}
