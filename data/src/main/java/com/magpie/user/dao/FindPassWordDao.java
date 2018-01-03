package com.magpie.user.dao;

import java.io.Serializable;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.user.model.FindPassWord;

@Repository
public class FindPassWordDao extends BaseMongoRepository<FindPassWord, Serializable> {

	@Autowired
	public FindPassWordDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(FindPassWord.class), mongoOps);
	}
	public FindPassWord findOne(String privateKey){
		return findOneByQuery(new Query().addCriteria(Criteria.where("privateKey").is(privateKey)));
	}
	public FindPassWord findOneByUserId(String userId){
		return findOneByQuery(new Query().addCriteria(Criteria.where("userId").is(userId)));

	}
	public void update(String userId,String privateKey,Date vaildTime) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		Update  update= new Update();
		update.set("privateKey", privateKey);
		update.set("vaildDate", vaildTime);
		
		updateFirst(query,update);
	}
}
