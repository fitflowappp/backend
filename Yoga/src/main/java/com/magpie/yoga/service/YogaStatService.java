package com.magpie.yoga.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magpie.yoga.constant.HistoryDest;
import com.magpie.yoga.constant.HistoryEvent;
import com.magpie.yoga.dao.AchievementRecordDao;
import com.magpie.yoga.dao.ShareRecordDao;
import com.magpie.yoga.dao.UserStateDao;
import com.magpie.yoga.dao.UserWatchHistoryDao;
import com.magpie.yoga.stat.UserWatchHistoryStat;
import com.magpie.yoga.view.DashboardView;

@Service
public class YogaStatService {

	@Autowired
	private UserWatchHistoryDao userWatchHistoryDao;
	@Autowired
	private AchievementRecordDao achievementRecordDao;
	@Autowired
	private UserStateDao userStateDao;
	@Autowired
	private ShareRecordDao shareRecordDao;

	/**
	 * Generate Dashboard data
	 * 
	 * @return
	 */
	public DashboardView generateDashboard() {

		DashboardView dashboard = new DashboardView();

		int challengeCompleteNum = 0;
		int workoutCompleteNum = 0;
		int challengeStartNum = 0;
		int workoutStartNum = 0;
		for (UserWatchHistoryStat historyStat : userWatchHistoryDao.aggregateStartCompleteNum()) {

			if (HistoryEvent.COMPLETE.getCode() == historyStat.getEvent()) {
				if (HistoryDest.CHALLENGE.getCode() == historyStat.getDestType()) {
					challengeCompleteNum = historyStat.getCount();
				} else if (HistoryDest.WORKOUT.getCode() == historyStat.getDestType()) {
					workoutCompleteNum = historyStat.getCount();
				}
			}
			if (HistoryEvent.START.getCode() == historyStat.getEvent()) {
				if (HistoryDest.CHALLENGE.getCode() == historyStat.getDestType()) {
					challengeStartNum = historyStat.getCount();
				} else if (HistoryDest.WORKOUT.getCode() == historyStat.getDestType()) {
					workoutStartNum = historyStat.getCount();
				}
			}
		}

		dashboard.setChallengeCompleteNum(challengeCompleteNum);
		dashboard.setChallengeStartNum(challengeStartNum);
		dashboard.setWorkoutCompleteNum(workoutCompleteNum + challengeCompleteNum);
		dashboard.setWorkoutStartNum(workoutStartNum + challengeStartNum);

		dashboard.setAchievementNum(achievementRecordDao.count());

		List<UserWatchHistoryStat> temp = userWatchHistoryDao
				.aggregateWorkoutCompleteUsers(HistoryDest.WORKOUT.getCode(), HistoryEvent.COMPLETE.getCode());
		dashboard.setOneWorkoutCompleteUserNum(temp.size());

		dashboard.setTotalDuration(
				userWatchHistoryDao.aggregateTotalDuration(HistoryEvent.COMPLETE.getCode()).getDuration());

		dashboard.setNotificationOnNum(userStateDao.countNotification());
		dashboard.setCalReminderOnNum(userStateDao.countReminder());

		dashboard.setShareNum(shareRecordDao.count());

		return dashboard;

	}
}
