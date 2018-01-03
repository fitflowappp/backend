package com.magpie.yoga;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.query.PageQuery;
import com.magpie.base.utils.CsvUtils;
import com.magpie.base.view.BaseView;
import com.magpie.base.view.Result;
import com.magpie.cache.yoga.YogaCacheService;
import com.magpie.yoga.dao.ChallengeDao;
import com.magpie.yoga.model.Challenge;

import io.swagger.annotations.ApiOperation;
/**
 * yoga管理后台接口
* @ClassName: ChallengeController  
* @Description: 管理后台走的都是http，如果是给app接口，不要放这里  
* @author jiangming.xia  
* @date 2017年12月29日 下午1:35:09  
*
 */
@RestController
@RequestMapping(value = "/manage/yoga/challenge")
public class ChallengeController {

	@Autowired
	private ChallengeDao challengeDao;
	@Autowired
	private YogaCacheService yogaCacheService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get all challenges", response = Challenge.class, responseContainer = "List")
	public List<Challenge> getChallenge(@ModelAttribute PageQuery pageQuery) {
		return challengeDao.findBySort(pageQuery);
	}
	

	@RequestMapping(method = RequestMethod.GET, value = "/csv")
	@ResponseBody
	@ApiOperation(value = "export csv")
	public void exportCsv(HttpServletResponse response) {
		StringBuffer sb = new StringBuffer();
		sb.append("ID").append(",").append("Code").append(",").append("Title").append(",").append("times started")
				.append(",").append("times completed").append(",").append("unique users started").append(",")
				.append("unique users completed").append("\r\n");

		for (Challenge challenge : challengeDao.findAll()) {
			sb.append(challenge.getId()).append(",").append(challenge.getCode()).append(",")
					.append(challenge.getTitle()).append(",").append(challenge.getStartedTimes()).append(",")
					.append(challenge.getCompletedTimes()).append(",").append(challenge.getStartedUserCount())
					.append(",").append(challenge.getCompletedUserCount()).append("\r\n");
		}
		CsvUtils.download("challenge.csv", sb.toString(), response);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "add one new Challenge")
	public BaseView<Challenge> addWorkout(@RequestBody Challenge challenge) {
		challengeDao.save(challenge);
		yogaCacheService.setChallenge(challenge);
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
		yogaCacheService.setChallenge(challenge);
		return new BaseView<Challenge>(challenge);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value = "delete one Challenge")
	public BaseView<?> removeWorkout(@PathVariable String id) {
		challengeDao.delete(id);
		yogaCacheService.delChallenge(id);
		return new BaseView<Challenge>(Result.SUCCESS);
	}

}
