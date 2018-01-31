package com.magpie.yoga.manage.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.view.BaseView;
import com.magpie.base.view.Result;
import com.magpie.yoga.dao.ChallengeSetDao;
import com.magpie.yoga.model.ChallengeSet;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/manage/yoga/challengeset")
public class ChallengeSetController {

	@Autowired
	private ChallengeSetDao challengeSetDao;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get all challengesets", response = ChallengeSet.class, responseContainer = "List")
	public List<ChallengeSet> getChallenge() {
		return challengeSetDao.findAll();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get one ChallengeSet")
	public BaseView<ChallengeSet> getChallengeSet(@PathVariable String id) {
		return new BaseView<ChallengeSet>(challengeSetDao.findOne(id));
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "add one new ChallengeSet")
	public BaseView<ChallengeSet> addChallengeSet(@RequestBody ChallengeSet challengeSet) {
		challengeSetDao.save(challengeSet);
		return new BaseView<ChallengeSet>(challengeSet);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "edit one ChallengeSet")
	public BaseView<ChallengeSet> editChallengeSet(@PathVariable String id, @RequestBody ChallengeSet challengeSet) {
		challengeSet.setId(id);
		challengeSetDao.save(challengeSet);
		return new BaseView<ChallengeSet>(challengeSet);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value = "delete one ChallengeSet")
	public BaseView<?> removeChallengeSet(@PathVariable String id) {
		challengeSetDao.delete(id);
		return new BaseView<ChallengeSet>(Result.SUCCESS);
	}

}
