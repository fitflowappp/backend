package com.magpie.yoga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.magpie.yoga.service.YogaStatService;

@Component
@EnableScheduling
public class TaskComponent {
	@Autowired
	private YogaStatService yogaStatService;

	@Scheduled(cron = "0 30 0 * * *")
	public void generateDashboard() {
		yogaStatService.generatePrevDayDashboard();
	}

	// @Scheduled(cron = "0 0 0/4 * * *")
	@Scheduled(cron = "0 0/5 * * * *")
	// Challenge,Workout,Routine统计数据
	public void generateCWRStatistics() {
		yogaStatService.updateCount();
	}

}
