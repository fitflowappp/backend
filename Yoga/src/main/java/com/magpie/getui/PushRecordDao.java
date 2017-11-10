package com.magpie.getui;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;

@Repository
public class PushRecordDao extends BaseMongoRepository<PushRecord, Serializable> {

	@Autowired
	public PushRecordDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(PushRecord.class), mongoOps);
	}

}
