package com.magpie.yoga.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.magpie.yoga.dao.TopicDao;
import com.magpie.yoga.dao.TopicSinglesDao;
import com.magpie.yoga.model.Sort;
import com.magpie.yoga.model.Topic;
import com.magpie.yoga.model.TopicSingles;
import com.magpie.yoga.service.TopicService;
@Service
public class TopicServiceImpl implements TopicService {
	@Autowired
	TopicDao topicDao;
	@Autowired
	TopicSinglesDao topicSinglesDao;

	@Override
	public List<Topic> findAll() {
		// TODO Auto-generated method stub
		
		return topicDao.findAll();
	}

	@Override
	public List<TopicSingles> findSingles(String topicId) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(topicId)){
			return null;
		}
		return topicSinglesDao.findList(topicId);
	}

	@Override
	public Topic save(Topic topic) {
		// TODO Auto-generated method stub
		return topicDao.save(topic);
	}

	@Override
	public TopicSingles saveSingles(TopicSingles topicSingles) {
		// TODO Auto-generated method stub
		TopicSingles singles=topicSinglesDao.findOneBySinglesId(topicSingles.getSinglesId());
		if(singles==null){
			return topicSinglesDao.save(topicSingles);
		}
		return singles;
	}

	@Override
	public void saveSingles(List<? extends TopicSingles> topicSingles,String topicId) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(topicId)){
			return;
		}
		if(topicSingles!=null&&topicSingles.size()>0){
			List<String> topicSinglesIds=new ArrayList<>();
			TopicSingles item=null;
			for (int i=0;i<topicSingles.size();i++) {
				item=topicSingles.get(i);
				item.setTopicId(topicId);//补充topicId，新增的可能没有topicId
				item.setSort(i);
				saveSingles(item);
				topicSinglesIds.add(item.getId());
			}
			topicSinglesDao.deleteSinglesNotInIds(topicSinglesIds, topicId);
		}
		
		
	}

	@Override
	public Topic find(String topicId) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(topicId)){
			return null;
		}
		return topicDao.findOne(topicId);
	}

	@Override
	public boolean delete(String topicId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteTopicSingles(String topicId) {
		// TODO Auto-generated method stub
		if(!StringUtils.isEmpty(topicId)){
			topicDao.delete(topicId);
			topicSinglesDao.deleteTopicSinelsByTopicId(topicId);
			return true;
		}
		return false;
	}

	@Override
	public boolean sortTopicAndDeleteOther(List<Sort> sortList) {
		// TODO Auto-generated method stub
		if(sortList!=null&&sortList.size()>0){
			List<String> topicIdList=new ArrayList<>();
			for (Sort sort : sortList) {
				Topic topic=topicDao.findOne(sort.getId());
				if(topic!=null){
					topic.setSort(sort.getSort());
					topicDao.save(topic);
				}
				topicIdList.add(sort.getId());
			}
			topicDao.deleteOtherTopic(topicIdList);
		}
		return false;
	}

}
