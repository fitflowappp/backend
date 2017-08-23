package com.magpie.base.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

@Configuration
public class DBConfig {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Bean
	public MongoRepositoryFactory createFactory() {
		return new MongoRepositoryFactory(mongoTemplate);
	}
}
