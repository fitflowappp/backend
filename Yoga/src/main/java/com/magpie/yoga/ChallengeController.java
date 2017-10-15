package com.magpie.yoga;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.query.PageQuery;
import com.magpie.base.view.BaseView;
import com.magpie.base.view.Result;
import com.magpie.yoga.dao.ChallengeDao;
import com.magpie.yoga.model.Challenge;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/manage/yoga/challenge")
public class ChallengeController {

	@Autowired
	private ChallengeDao challengeDao;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get all challenges", response = Challenge.class, responseContainer = "List")
	public List<Challenge> getChallenge(@ModelAttribute PageQuery pageQuery) {
		return challengeDao.findBySort(pageQuery);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "add one new Challenge")
	public BaseView<Challenge> addWorkout(@RequestBody Challenge challenge) {
		challengeDao.save(challenge);
		return new BaseView<Challenge>(challenge);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get one Challenge")
	public BaseView<Challenge> getChallenge(@PathVariable String id) {
		return new BaseView<Challenge>(challengeDao.findOne(id));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "edit one Challenge")
	public BaseView<Challenge> editWorkout(@PathVariable String id, @RequestBody Challenge challenge) {
		challenge.setId(id);
		challengeDao.save(challenge);
		return new BaseView<Challenge>(challenge);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value = "delete one Challenge")
	public BaseView<?> removeWorkout(@PathVariable String id) {
		challengeDao.delete(id);
		return new BaseView<Challenge>(Result.SUCCESS);
	}

}
