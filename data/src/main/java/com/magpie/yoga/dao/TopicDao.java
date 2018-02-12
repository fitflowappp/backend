package com.magpie.yoga.dao;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.yoga.model.Topic;

@Repository

public class TopicDao extends BaseMongoRepository<Topic, Serializable> {
	private Logger logger = LoggerFactory.getLogger(getClass());

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
	public Topic findDefault(){
		Query query=new Query();
		query.addCriteria(Criteria.where("isDefault").is(true));
		return findOneByQuery(query);

	}
	public Topic findOne(){
		return findOneByQuery(new Query());
	}
	public String findDefaultTopicChallengeId(){
		Topic topic=null;
		try {
			topic=findDefault();
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("find default topic error:{}", e);
		}
		if(topic!=null){
			return topic.getChallengeId();
		}
		return null;
		
	}
	public Topic findOneByChallengeId(String challengeId){
		if(StringUtils.isEmpty(challengeId)==false){
			Query query=new Query();
			query.addCriteria(Criteria.where("challengeId").is(challengeId));
			return findOneByQuery(query);
		}
		return null;
	}

	
}
