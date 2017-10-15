package com.magpie.yoga;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.view.BaseView;
import com.magpie.yoga.model.Dashboard;
import com.magpie.yoga.req.DashboardGenReq;
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

	@RequestMapping(value = "/day", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "generate one day dashboard data")
	public BaseView<Dashboard> generateDashboard(@RequestBody DashboardGenReq dashboardGenReq) {
		return new BaseView<>(yogaStatService.generateDashboard(dashboardGenReq.getDate()));
	}

	@RequestMapping(value = "/allday", method = RequestMethod.POST)
	@ResponseBody
	public void generateAllDashboard() {
		Calendar calendar = Calendar.getInstance();
		for (int i = 1; i <= 15; i++) {
			calendar.set(2017, 9, i);
			yogaStatService.generateDashboard(calendar.getTime());
		}
	}

}
