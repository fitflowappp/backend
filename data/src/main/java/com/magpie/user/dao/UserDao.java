package com.magpie.user.dao;

import java.io.Serializable;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;
import org.springframework.stereotype.Repository;

import com.magpie.base.dao.BaseMongoRepository;
import com.magpie.base.query.PageQuery;
import com.magpie.user.model.User;

@Repository
public class UserDao extends BaseMongoRepository<User, Serializable> {

	@Autowired
	public UserDao(MongoRepositoryFactory mongoRepositoryFactory, MongoOperations mongoOps) {
		super(mongoRepositoryFactory.getEntityInformation(User.class), mongoOps);
	}

	public User findOneByPhone(String phone) {
		return findOneByQuery(new Query().addCriteria(Criteria.where("phone").is(phone)));
	}
	public User findOneByEmail(String email) {
		return findOneByQuery(new Query().addCriteria(Criteria.where("email").is(email)));
	}

	public void updateFacebookRegSubmit(String id, boolean submitted, Date submittedDate) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(new ObjectId(id)));
		Update update = new Update();
		update.set("facebookRegistrationSumbmitted", submitted).set("facebookRegistrationSumbmittedDate",
				submittedDate);
		updateFirst(query, update);
	}
	public void updatePassword(String id ,String password){
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(new ObjectId(id)));
		Update update = new Update();
		update.set("password", password);
		updateFirst(query, update);
	}
	public void updateChallenge(String id ,String challengeId){
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(new ObjectId(id)));
		Update update = new Update();
		update.set("challengeId", challengeId);
		updateFirst(query, update);
	}

	public void updateUserRole(String uid, String role) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(new ObjectId(uid)));
		updateFirst(query, new Update().set("role", role));
	}

	public Page<User> findPage(PageQuery pageQuery) {
		Query query = new Query();
		query.with(new Sort(Direction.DESC, "crDate"));
		return findByQueryAndSort(query, pageQuery);
	}

	private void addDateCriteria(Date start, Date end, Criteria criteria) {
		if (start != null && end != null) {
			criteria.andOperator(Criteria.where("crDate").gte(start), Criteria.where("crDate").lt(end));
		} else if (start != null && end == null) {
			criteria.and("crDate").gte(start);
		} else if (start == null && end != null) {
			criteria.and("crDate").lt(end);
		}
	}

	public long count(boolean registrationSubmitted, Date start, Date end) {
		Criteria criteria = Criteria.where("facebookRegistrationSumbmitted").is(registrationSubmitted);
		addDateCriteria(start, end, criteria);
		Query query = new Query();
		query.addCriteria(criteria);
		return count(query);
	}

}
