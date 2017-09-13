package com.magpie.story.manage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.magpie.base.view.BaseView;
import com.magpie.session.ActiveUser;
import com.magpie.share.UserRef;
import com.magpie.story.manage.req.ReqStoryContent;

@RestController
@RequestMapping(value = "/manage/story")
public class StoryManagerApi {

	@Autowired
	private MongoTemplate mongoTemplate;

	@RequestMapping(value = "/{label}/{index}", method = RequestMethod.POST)
	@ResponseBody
	public void addContent(@PathVariable String label, @PathVariable int index,
			@RequestBody ReqStoryContent storyContent) {
		if (mongoTemplate.exists(new Query().addCriteria(Criteria.where("label").is(label).and("index").is(index)),
				"AvgStory")) {

		}

	}

	@RequestMapping(value = "/{index}", method = RequestMethod.PUT)
	@ResponseBody
	public void updateContent(@RequestBody ReqStoryContent storyContent) {

	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public BaseView<?> getContent(@ActiveUser UserRef userRef) {
		System.out.println(JSON.toJSONString(userRef));
		return new BaseView<>();
	}

}
