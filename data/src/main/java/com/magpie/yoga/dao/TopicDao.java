package com.magpie.yoga.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.yoga.model.Topic;

@Repository

public class TopicDao extends BaseMongoRepository<Topic, Serializable> {

	@Autowired
	public TopicDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(Topic.class), mongoOps);
	}
	public List<Topic> findAll(){
		
		return findAll(new Sort(Direction.ASC, "sort"));
	}
	public void deleteOtherTopic(List<String> topicIdList){
		Query query=new Query();
		query.addCriteria(Criteria.where("id").nin(topicIdList));
		delete(query);
	}

	
}
