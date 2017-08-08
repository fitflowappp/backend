package com.magpie.story.manage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.story.manage.req.ReqStoryContent;

@RestController
@RequestMapping(value="/manage/story")
public class StoryManagerApi {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@RequestMapping(value="/{label}/{index}",method = RequestMethod.POST)
	public void addContent(@PathVariable String label,@PathVariable int index, @RequestBody ReqStoryContent storyContent) {
		if(mongoTemplate.exists(new Query().addCriteria(Criteria.where("label").is(label).and("index").is(index)),"AvgStory")) {
			
		}
	
	}
	
	
	@RequestMapping(value="/{index}",method = RequestMethod.PUT)
	public void updateContent(@RequestBody ReqStoryContent storyContent) {
		
	}
	
}
