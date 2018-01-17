package com.magpie.user.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.user.model.UserBackgroundMusic;


@Repository
public class UserBackgroundMusicDao extends BaseMongoRepository<UserBackgroundMusic, Serializable> {
	@Autowired
	public UserBackgroundMusicDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(UserBackgroundMusic.class), mongoOps);
	}

	public UserBackgroundMusic findOneByUserId(String userId){
		if(StringUtils.isEmpty(userId)){
			return null;
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		return findOneByQuery(query);
	}
	public List<UserBackgroundMusic> findList(List<String> userIdList){
		if(userIdList==null||userIdList.size()==0){
			return null;
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").in(userIdList));
		return findByQuery(query);
	}
	
	
}