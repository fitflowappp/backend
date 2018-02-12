package com.magpie.yoga.manage.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.view.BaseView;
import com.magpie.base.view.Result;
import com.magpie.cache.yoga.YogaCacheService;
import com.magpie.yoga.def.UserWorkDef;
import com.magpie.yoga.model.Challenge;
import com.magpie.yoga.model.Topic;
import com.magpie.yoga.model.TopicSingles;
import com.magpie.yoga.model.TopicSort;
import com.magpie.yoga.model.Workout;
import com.magpie.yoga.service.TopicService;
import com.magpie.yoga.view.TopicSinglesView;
import com.magpie.yoga.view.TopicView;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/manage/yoga/topic")
public class TopicController {
	@Autowired
	TopicService topicService;
	@Autowired
	YogaCacheService yogaCacheService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get all topic", response = Topic.class, responseContainer = "List")
	public List<TopicView> getTopicList() {
		List<Topic> topic = topicService.findAll();
		List<TopicView> topicViewList = new ArrayList<>();
		Topic item = null;
		TopicView topicView = null;
		for (int i = 0; i < topic.size(); i++) {
			item = topic.get(i);
			topicView = new TopicView();
			BeanUtils.copyProperties(item, topicView);

			topicView.setChallenge(getChallenge(item.getChallengeId()));
			topicViewList.add(topicView);
		}
		return topicViewList;
	}

	private Challenge getChallenge(String id) {
		Challenge challenge = yogaCacheService.getChallenge(id);
		if (challenge != null) {// 去除workout，节省流量加快速度
			challenge.setWorkouts(null);
		}
		return challenge;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "add one new topic")
	public BaseView<Result> addTopic(@RequestBody TopicView topicView) {
		Topic existedTopic = topicService.findOneByChallengeId(topicView.getChallengeId());
		if (existedTopic != null && existedTopic.getId().equals(topicView.getId()) == false) {// 不是同一个topic的challengeId不能重复
			return new BaseView<>(new Result(Result.CODE_FAILURE,
					"This Challenge is already associated with an existing Topic, please select another."));
		}

		Topic topic = new Topic();
		topic.setId(topicView.getId());
		topic.setChallengeId(topicView.getChallengeId());
		topic.setTitle(topicView.getTitle());
		topic.setDefault(topicView.isDefault());
		topic.setSort(topicView.getSort());
		topic = topicService.save(topic);
		if (StringUtils.isEmpty(topic.getId()) == false) {
			List<TopicSinglesView> singles = topicView.getSingles();

			topicService.saveSingles(singles, topic.getId());
		}
		// 需要检查下是否是默认的topic改变了challenge，如果是改变的话，需要更新redis的数据
		if (topic.isDefault() && StringUtils.isEmpty(topic.getId()) == false) {
			yogaCacheService.setDefaultChallengeId(topic.getChallengeId());
			yogaCacheService.setDefaultTopicId(topic.getId());
		}

		return new BaseView<>(Result.SUCCESS);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	@ResponseBody
	@ApiOperation(value = "delete topic")
	public List<TopicView> deleteTopic(@PathVariable String id) {
		if (!StringUtils.isEmpty(id)) {
			Topic topic = topicService.find(id);
			if (topic != null) {
				topicService.deleteTopicSingles(id);
			}
		}

		return getTopicList();
	}

	@RequestMapping(value = "/sort", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "sort topic")
	public List<TopicView> topicSort(@RequestBody HashMap<Integer, TopicSort> sortMap) {
		List<TopicSort> sortList = new ArrayList<>();
		TopicSort sort = null;
		for (int i = 0; i < sortMap.size(); i++) {
			sort = sortMap.get((i));
			sort.setSort(i);
			sortList.add(sort);
			if (sort.isSelectDefault()) {// 缓存那边，topic更改配置
				Topic topic = topicService.find(sort.getId());
				if (topic != null) {
					yogaCacheService.setDefaultChallengeId(topic.getChallengeId());
					yogaCacheService.setDefaultTopicId(sort.getId());
				}
			}
		}
		logger.debug("sort list:{}", sortList);
		topicService.sortTopicAndDeleteOther(sortList);
		return getTopicList();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get one topic")
	public TopicView getTopic(@PathVariable String id) {
		TopicView topicView = new TopicView();
		Topic topic = topicService.find(id);
		if (topic != null) {
			BeanUtils.copyProperties(topic, topicView);
			topicView.setChallenge(getChallenge(topicView.getChallengeId()));
			List<TopicSingles> topicSingles = topicService.findSingles(topic.getId());
			logger.trace("one topic's topicsingles:{}", topicSingles);
			List<TopicSinglesView> topicSingelsViews = new ArrayList<>();
			TopicSinglesView topicSinglesView = null;
			TopicSingles topicSinglesItem = null;
			for (int i = 0; i < topicSingles.size(); i++) {
				topicSinglesItem = topicSingles.get(i);
				topicSinglesView = new TopicSinglesView();
				BeanUtils.copyProperties(topicSinglesItem, topicSinglesView);
				Workout workout = yogaCacheService.getWorkout(topicSinglesItem.getSinglesId());
				if (workout != null) {
					workout.setRoutines(null);
				}
				topicSinglesView.setSingles(workout);
				topicSingelsViews.add(topicSinglesView);
			}
			logger.trace("one topic's topicsingles:{}", topicSingles);
			topicView.setSingles(topicSingelsViews);
		}

		return topicView;
	}

	@RequestMapping(value = "/init", method = RequestMethod.GET)
	@ResponseBody
	public BaseView<Result> initTopic() {
		TopicSingles topicSingles = null;
		for (int i = 0; i < 10000; i++) {
			String singlesId = ""+System.currentTimeMillis();
			topicSingles = new TopicSingles();
			topicSingles.setSinglesId(singlesId);
			topicSingles.setTopicId("78");
			TopicSingles  saved=topicService.saveSingles(topicSingles);
					
			logger.trace("write error:{},savedId:{}", singlesId,saved!=null?saved.getId():"");;
					
		
		}

		return new BaseView<>(Result.SUCCESS);
	}
}
