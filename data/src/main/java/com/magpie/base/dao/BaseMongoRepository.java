package com.magpie.base.dao;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.SkipOperation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.util.StringUtils;

import com.magpie.base.query.PageQuery;
import com.magpie.base.utils.DateUtil;


public class BaseMongoRepository<T, ID extends Serializable> extends SimpleMongoRepository<T, Serializable> {

	private MongoOperations mongoOps;
	private MongoEntityInformation<T, Serializable> entityInformation;

	public BaseMongoRepository(MongoEntityInformation<T, Serializable> entityInformation, MongoOperations mongoOps) {
		super(entityInformation, mongoOps);
		this.mongoOps = mongoOps;
		this.entityInformation = entityInformation;
	}

	public MongoOperations getMongoOperations() {
		return mongoOps;
	}

	public MongoEntityInformation<T, Serializable> getEntityInformation() {
		return entityInformation;
	}

	/**
	 * 根据查询条件取数据
	 * 
	 * @param query
	 * @return
	 */
	protected List<T> findByQuery(Query query) {

		if (query == null) {
			return Collections.emptyList();
		}

		return getMongoOperations().find(query, getEntityInformation().getJavaType(),
				getEntityInformation().getCollectionName());
	}

	/**
	 * 根据查询条件取分页数据
	 * 
	 * @param query
	 * @param pageable
	 * @return
	 */
	protected Page<T> findByQuery(Query query, final Pageable pageable) {
		if (query == null) {
			return new PageImpl<T>(null, pageable, 0);
		}
		Long count = count(query);

		List<T> list = findByQuery(query.with(pageable));

		return new PageImpl<T>(list, pageable, count);
	}

	/**
	 * 根据查询条件取得数据条数
	 * 
	 * @param query
	 * @return
	 */
	protected long count(Query query) {

		if (query == null) {
			return 0l;
		}

		return getMongoOperations().count(query, this.getEntityInformation().getJavaType());
	}

	/**
	 * 查询一条记录
	 * 
	 * @param query
	 * @return
	 */
	public T findOneByQuery(Query query) {
		List<T> ls = this.findByQuery(query.limit(1));
		return ls == null || ls.isEmpty() ? null : ls.get(0);
	}

	protected Page<T> findByQuery(Query query, PageQuery pageQuery) {
		final Pageable pageable = new PageRequest(pageQuery.getNumber(), pageQuery.getSize());
		if (query == null) {
			return new PageImpl<T>(null, pageable, 0);
		}
		Long count = count(query);

		List<T> list = findByQuery(query.with(pageable));

		return new PageImpl<T>(list, pageable, count);
	}

	protected Page<T> findByQueryAndSort(Query query, PageQuery pageQuery) {
		final Pageable pageable = new PageRequest(pageQuery.getNumber(), pageQuery.getSize(), getSort(pageQuery));
		if (query == null) {
			return new PageImpl<T>(null, pageable, 0);
		}
		Long count = count(query);

		List<T> list = findByQuery(query.with(pageable));

		return new PageImpl<T>(list, pageable, count);
	}

	protected Page<T> aggregateByQuery(Query query, final Pageable pageable, TypedAggregation<T> aggregation) {
		if (query == null) {
			return new PageImpl<T>(null, pageable, 0);
		}
		// Long count = count(query);

		AggregationResults<T> result = getMongoOperations().aggregate(aggregation, aggregation.getInputType());

		List<T> list = result.getMappedResults();

		return new PageImpl<T>(list, pageable, list.size());
	}

	protected Page<T> aggregateByQuery(final Pageable pageable, Class<T> type, List<AggregationOperation> operations) {

		TypedAggregation<T> aggregationOne = newAggregation(type, operations);

		AggregationResults<T> resultOne = getMongoOperations().aggregate(aggregationOne, aggregationOne.getInputType());

		Long count = (long) resultOne.getMappedResults().size();

		SkipOperation skipOperation = Aggregation.skip(pageable.getOffset());
		LimitOperation limitOperation = Aggregation.limit(pageable.getPageSize());

		operations.add(skipOperation);
		operations.add(limitOperation);

		TypedAggregation<T> agg = newAggregation(type, operations);

		AggregationResults<T> result = getMongoOperations().aggregate(agg, type);

		List<T> list = result.getMappedResults();

		return new PageImpl<T>(list, pageable, count);
	}

	protected List<T> aggregateByQuery(Query query, TypedAggregation<T> aggregation) {
		if (query == null) {
			return Collections.emptyList();
		}

		AggregationResults<T> result = getMongoOperations().aggregate(aggregation, aggregation.getInputType());

		return result.getMappedResults();
	}

	public boolean exists(Query query) {
		return getMongoOperations().exists(query, getEntityInformation().getJavaType(),
				getEntityInformation().getCollectionName());
	}

	public void updateFirst(Query query, Update update) {
		this.updateBasicModelInfo(update);
		getMongoOperations().updateFirst(query, update, getEntityInformation().getJavaType(),
				getEntityInformation().getCollectionName());
	}

	public void updateMulti(Query query, Update update) {
		this.updateBasicModelInfo(update);
		getMongoOperations().updateMulti(query, update, getEntityInformation().getJavaType(),
				getEntityInformation().getCollectionName());
	}

	private void updateBasicModelInfo(Update update) {
		if (update != null) {
			update.set("mdDate", DateUtil.getCurrentDate());
		}
	}

	public Sort getSort(PageQuery pageQuery) {
		if (!StringUtils.isEmpty(pageQuery.getSortKey())) {
			Direction direction = Direction.ASC;
			if (pageQuery.getSortType() == 1) {//
				direction = Direction.ASC;
			} else if (pageQuery.getSortType() == -1) {
				direction = Direction.DESC;
			}
			return new Sort(direction, pageQuery.getSortKey());

		} else {
			return null;
		}
	}

	@Override
	public <S extends T> S insert(S entity) {
		return super.insert(entity);
	}

	@Override
	public <S extends T> S save(S entity) {
		return super.save(entity);
	}

	public void delete(Query query) {
		getMongoOperations().remove(query, getEntityInformation().getJavaType(),
				getEntityInformation().getCollectionName());
	}

	@Override
	public void delete(Serializable id) {
		T entity = findOne(id);
		getMongoOperations().save(new Duplicate(getEntityInformation().getCollectionName(), entity));
		super.delete(id);
	}

	@Override
	public void delete(T entity) {
		this.delete(entityInformation.getId(entity));
	}

}

