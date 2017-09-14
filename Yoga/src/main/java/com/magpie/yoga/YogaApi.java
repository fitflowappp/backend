package com.magpie.yoga;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.view.BaseView;
import com.magpie.yoga.dao.ChallengeSetDao;
import com.magpie.yoga.model.ChallengeSet;
import com.magpie.yoga.view.ChallengeSetView;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/yoga")
public class YogaApi {

	@Autowired
	private ChallengeSetDao challengeSetDao;

	@RequestMapping(value = "/home/challenge", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get primary challenge set of homepage", response = BaseView.class)
	public BaseView<ChallengeSetView> getChallengeSet() {
		ChallengeSet challengeSet = challengeSetDao.findOneByPrimary(true);
		ChallengeSetView view = new ChallengeSetView();
		BeanUtils.copyProperties(challengeSet, view);
		return new BaseView<ChallengeSetView>(view);
	}

}
