package com.magpie.yoga.service;

import java.util.List;

import com.magpie.yoga.model.Sort;
import com.magpie.yoga.model.Topic;
import com.magpie.yoga.model.TopicSingles;
import com.magpie.yoga.model.TopicSort;

public interface TopicService {
	public List<Topic> findAll();
	public Topic find(String topicId);
	public Topic findOneByChallengeId(String challengeId);
	public List<TopicSingles> findSingles(String topicId);
	
	public Topic save(Topic topic);
	public TopicSingles saveSingles(TopicSingles topicSingles);
	/**
	 * 保存topic的singles列表，同时去除原先从列表去除的singles
	 */
	public void saveSingles(List<? extends TopicSingles> topicSingles,String topicId);
	
	public boolean delete(String topicId);
	public boolean deleteTopicSingles(String topicId);
	
	public boolean sortTopicAndDeleteOther(List<TopicSort> sortList);
	

}
