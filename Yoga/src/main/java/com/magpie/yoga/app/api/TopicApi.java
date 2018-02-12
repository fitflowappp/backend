package com.magpie.yoga.app.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.yoga.model.Topic;
import com.magpie.yoga.service.TopicService;

import io.swagger.annotations.ApiOperation;
@RestController
@RequestMapping(value = "/yoga")
public class TopicApi {
	@Autowired
	TopicService topicService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/challenge/getTopical")
	@ResponseBody
	@ApiOperation(value = "get all topical")
	public List<Topic> getTopical() {
		return topicService.findAll();
	}
}
