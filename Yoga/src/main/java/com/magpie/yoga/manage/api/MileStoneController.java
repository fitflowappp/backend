package com.magpie.yoga.manage.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.magpie.base.view.BaseView;
import com.magpie.yoga.dao.MilestoneDao;
import com.magpie.yoga.model.Milestone;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/manage/yoga/milestone")
public class MileStoneController {

	@Autowired
	private MilestoneDao milestoneDao;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get milestone configuration")
	public BaseView<Milestone> getMilestone() {
		Milestone milestone = milestoneDao.findOne();
		if (milestone == null) {
			milestoneDao.save(new Milestone());
		}
		return new BaseView<Milestone>(milestoneDao.findOne());
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "edit milestone configuration")
	public BaseView<Milestone> editMilestone(@PathVariable String id, @RequestBody Milestone milestone) {
		milestone.setId(id);
		milestoneDao.save(milestone);
		return new BaseView<Milestone>(milestone);
	}
}
