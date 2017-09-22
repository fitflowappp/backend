package com.magpie.yoga;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.yoga.model.Dashboard;
import com.magpie.yoga.service.YogaStatService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/manage/yoga/dashboard")
public class DashboardController {

	@Autowired
	private YogaStatService yogaStatService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get dashboard statistic data")
	public List<Dashboard> getDashboard() {
		return yogaStatService.getDashboard();
	}
}
