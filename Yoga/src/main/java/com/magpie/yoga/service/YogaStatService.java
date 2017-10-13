package com.magpie.yoga.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.magpie.base.utils.DateUtil;
import com.magpie.yoga.constant.HistoryDest;
import com.magpie.yoga.constant.HistoryEvent;
import com.magpie.yoga.dao.AchievementRecordDao;
import com.magpie.yoga.dao.DashboardDao;
import com.magpie.yoga.dao.ShareRecordDao;
import com.magpie.yoga.dao.UserConfigurationDao;
import com.magpie.yoga.dao.UserWatchHistoryDao;
import com.magpie.yoga.model.AchievementRecord;
import com.magpie.yoga.model.Dashboard;
import com.magpie.yoga.stat.UserWatchHistoryStat;

@Service
public class YogaStatService {

	@Autowired
	private UserWatchHistoryDao userWatchHistoryDao;
	@Autowired
	private AchievementRecordDao achievementRecordDao;
	@Autowired
	private ShareRecordDao shareRecordDao;
	@Autowired
	private DashboardDao dashboardDao;
	@Autowired
	private UserConfigurationDao userConfigurationDao;

	/**
	 * get dashboard list
	 * 
	 * @return
	 */
	public List<Dashboard> getDashboard() {
		this.generateTodayDashboard();
		return dashboardDao.findAll(new Sort(Direction.DESC, "date"));
	}

	/**
	 * Generate all Dashboard data
	 * 
	 * @return
	 */
	public Dashboard generateDashboard() {

		Dashboard dashboard = new Dashboard();

		int challengeCompleteNum = 0;
		int workoutCompleteNum = 0;
		int challengeStartNum = 0;
		int workoutStartNum = 0;
		for (UserWatchHistoryStat historyStat : userWatchHistoryDao.aggregateStartCompleteNum(null, null)) {

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

		dashboard.setAchievementNum(getAchievementCount(achievementRecordDao.aggregateCount(null, null)));

		List<UserWatchHistoryStat> temp = userWatchHistoryDao.aggregateWorkoutCompleteUsers(
				HistoryDest.WORKOUT.getCode(), HistoryEvent.COMPLETE.getCode(), null, null);
		dashboard.setOneWorkoutCompleteUserNum(temp.size());

		dashboard.setTotalDuration(
				userWatchHistoryDao.aggregateTotalDuration(HistoryEvent.COMPLETE.getCode(), null, null).getDuration());

		dashboard.setNotificationOnNum(userConfigurationDao.countNotification(null, null));
		dashboard.setCalReminderOnNum(userConfigurationDao.countReminder(null, null));

		dashboard.setShareNum(shareRecordDao.count());

		return dashboard;

	}

	/**
	 * Generate Dashboard data of yesterday
	 * 
	 * @return
	 */
	public Dashboard generatePrevDayDashboard() {

		Date date = DateUtil.getPrevDayStartTime();
		return generateDashboard(date);

	}

	/**
	 * Generate Dashboard data of today
	 * 
	 * @return
	 */
	public Dashboard generateTodayDashboard() {

		Date current = Calendar.getInstance().getTime();
		return generateDashboard(current);

	}

	/**
	 * Generate Dashboard data within the period
	 * 
	 * @return
	 */
	public Dashboard generateDashboard(Date date) {

		Date start = DateUtil.getStartTime(date);
		Date end = DateUtil.getEndTime(date);

		Dashboard dashboard = new Dashboard();
		dashboard.setDate(start);

		int challengeCompleteNum = 0;
		int workoutCompleteNum = 0;
		int challengeStartNum = 0;
		int workoutStartNum = 0;
		for (UserWatchHistoryStat historyStat : userWatchHistoryDao.aggregateStartCompleteNum(start, end)) {

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

		dashboard.setAchievementNum(getAchievementCount(achievementRecordDao.aggregateCount(start, end)));

		List<UserWatchHistoryStat> temp = userWatchHistoryDao.aggregateWorkoutCompleteUsers(
				HistoryDest.WORKOUT.getCode(), HistoryEvent.COMPLETE.getCode(), start, end);
		dashboard.setOneWorkoutCompleteUserNum(temp.size());

		dashboard.setTotalDuration(
				userWatchHistoryDao.aggregateTotalDuration(HistoryEvent.COMPLETE.getCode(), start, end).getDuration());

		dashboard.setNotificationOnNum(userConfigurationDao.countNotification(start, end));
		dashboard.setCalReminderOnNum(userConfigurationDao.countReminder(start, end));

		dashboard.setShareNum(shareRecordDao.count(start, end));

		Dashboard dashboardOld = dashboardDao.findDashboard(start);
		if (dashboardOld != null) {
			dashboard.setId(dashboardOld.getId());
		}
		dashboardDao.save(dashboard);

		return dashboard;

	}

	private long getAchievementCount(List<AchievementRecord> achievementStats) {
		long count = 0;
		if (achievementStats != null) {
			count = achievementStats.size();
		}
		return count;
	}
}
