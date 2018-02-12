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
import org.springframework.util.StringUtils;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.yoga.model.TopicSingles;

@Repository

public class TopicSinglesDao extends BaseMongoRepository<TopicSingles, Serializable> {

	@Autowired
	public TopicSinglesDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(TopicSingles.class), mongoOps);
	}
	
	public List<TopicSingles> findList(String topicId){
		if(StringUtils.isEmpty(topicId)){
			return null;
		}
		Query query=new Query();
		query.addCriteria(Criteria.where("topicId").is(topicId));
		query.with(new Sort(Direction.ASC, "sort"));
		return findByQuery(query);
	}
	
	public TopicSingles findOneBySinglesId(String singlesId){
		if(StringUtils.isEmpty(singlesId)){
			return null;
		}
		Query query=new Query();
		query.addCriteria(Criteria.where("singlesId").is(singlesId));
		return findOneByQuery(query);
	}
	/**
	 * 批量删除topicsingels
	* @Title: deleteSinglesNotInIds  
	* @Description:  topicSinglesIds是topicSingles的id，而不是topicsingels的singelsId
	 */
	public void deleteSinglesNotInIds(List<String> topicSinglesIds,String topicId){
		if(topicSinglesIds!=null&&topicSinglesIds.size()>0){
			Query query=new Query();
			query.addCriteria(Criteria.where("id").nin(topicSinglesIds).and("topicId").is(topicId));
			delete(query);
		}
	}
	public void deleteTopicSinelsByTopicId(String topicId){
		if(StringUtils.isEmpty(topicId)==false){
			Query query=new Query();
			query.addCriteria(Criteria.where("topicId").is(topicId));
			delete(query);
		}
	}

}