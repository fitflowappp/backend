package com.magpie.fm;

import java.io.Serializable;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.resource.constant.ResourceType;
import com.magpie.resource.model.Resource;

@Repository
public class ResourceDao extends BaseMongoRepository<Resource, Serializable> {

	@Autowired
	public ResourceDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(Resource.class), mongoOps);
	}

	public Resource getResourceByUri(String uri) {
		Query query = new Query().addCriteria(Criteria.where("contentUri").is(uri));
		return findOneByQuery(query);
	}

	public void updateResourceByUri(String uri, boolean alive) {
		Query query = new Query().addCriteria(Criteria.where("contentUri").is(uri));
		updateFirst(query, new Update().set("alive", alive));
	}

	public void updateResourceById(String id, boolean alive) {
		Query query = new Query().addCriteria(Criteria.where("id").is(new ObjectId(id)));
		updateFirst(query, new Update().set("alive", alive));
	}

	public Resource getResourseByOriginalName(String fileName) {
		return getMongoOperations().findOne(
				new Query(Criteria.where("originalFileName").is(fileName).and("type")
						.is(ResourceType.OUTERSOURCE.getCode())), Resource.class);
	}
}
