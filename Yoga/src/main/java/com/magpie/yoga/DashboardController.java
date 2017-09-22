package com.magpie.yoga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.view.BaseView;
import com.magpie.yoga.service.YogaStatService;
import com.magpie.yoga.view.DashboardView;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/manage/yoga/dashboard")
public class DashboardController {

	@Autowired
	private YogaStatService yogaStatService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get dashboard statistic data")
	public BaseView<DashboardView> getDashboard() {
		return new BaseView<DashboardView>(yogaStatService.generateDashboard());
	}
}
