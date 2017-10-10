package com.magpie.yoga;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.utils.DateUtil;
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
		return new BaseView<>(yogaStatService.generateDashboard(DateUtil.getStartTime(dashboardGenReq.getDate()),
				DateUtil.getEndTime(dashboardGenReq.getDate())));
	}

	@Scheduled(cron = "0 30 0 ? * *")
	public void generateDashboard() {
		yogaStatService.generatePrevDayDashboard();
	}

	@RequestMapping(value = "/allday", method = RequestMethod.POST)
	@ResponseBody
	public void generateAllDashboard() {
		Calendar calendar = Calendar.getInstance();
		for (int i = 1; i <= 10; i++) {
			calendar.set(2017, 10, i);
			yogaStatService.generateDashboard(DateUtil.getStartTime(calendar.getTime()),
					DateUtil.getEndTime(calendar.getTime()));
		}
	}

}
