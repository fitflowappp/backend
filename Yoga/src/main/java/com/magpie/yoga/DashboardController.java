package com.magpie.yoga;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.utils.CsvUtils;
import com.magpie.base.view.BaseView;
import com.magpie.yoga.model.Dashboard;
import com.magpie.yoga.req.DashboardGenReq;
import com.magpie.yoga.service.impl.YogaStatService;

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

	@RequestMapping(method = RequestMethod.GET, value = "/csv")
	@ResponseBody
	@ApiOperation(value = "export csv")
	public void exportCsv(HttpServletResponse response) {

		StringBuffer sb = new StringBuffer();
		sb.append("date").append(",").append("Facebook registration submitted").append(",")
				.append("Facebook registration completed").append(",").append("Challenges started by unique users")
				.append(",").append("Challenges completed by unique users").append(",")
				.append("Workouts started by unique users").append(",").append("Workouts completed by unique users")
				.append(",").append("Unique users who completed a Workout").append(",")
				.append("Cumulative duration of videos watched").append(",")
				.append("Unique users who turned scheduling on").append(",")
				.append("Unique users who have received achievements").append(",")
				.append("Unique users who have shared on social media").append("\r\n");

		for (Dashboard board : yogaStatService.getDashboard()) {
			sb.append(board.getDate()).append(",").append(board.getFacebookRegSubmitNum()).append(",")
					.append(board.getFacebookRegCompleteNum()).append(",").append(board.getChallengeStartNum())
					.append(",").append(board.getChallengeCompleteNum()).append(",").append(board.getWorkoutStartNum())
					.append(",").append(board.getWorkoutCompleteNum()).append(",")
					.append(board.getOneWorkoutCompleteUserNum()).append(",").append(board.getTotalDuration())
					.append(",").append(board.getNotificationOnNum()).append(",").append(board.getAchievementNum())
					.append(",").append(board.getShareNum()).append("\r\n");
		}
		CsvUtils.download("dashboard.csv", sb.toString(), response);
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
