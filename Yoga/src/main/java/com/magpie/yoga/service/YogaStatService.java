package com.magpie.yoga.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.magpie.base.utils.DateUtil;
import com.magpie.yoga.constant.HistoryDest;
import com.magpie.yoga.constant.HistoryEvent;
import com.magpie.yoga.dao.AchievementRecordDao;
import com.magpie.yoga.dao.DashboardDao;
import com.magpie.yoga.dao.ShareRecordDao;
import com.magpie.yoga.dao.UserStateDao;
import com.magpie.yoga.dao.UserWatchHistoryDao;
import com.magpie.yoga.model.Dashboard;
import com.magpie.yoga.stat.UserWatchHistoryStat;

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
	@Autowired
	private DashboardDao dashboardDao;

	/**
	 * get dashboard list
	 * 
	 * @return
	 */
	public List<Dashboard> getDashboard() {
		return dashboardDao.findAll(new Sort(Direction.DESC, "crDate"));
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

		dashboard.setAchievementNum(achievementRecordDao.count());

		List<UserWatchHistoryStat> temp = userWatchHistoryDao.aggregateWorkoutCompleteUsers(
				HistoryDest.WORKOUT.getCode(), HistoryEvent.COMPLETE.getCode(), null, null);
		dashboard.setOneWorkoutCompleteUserNum(temp.size());

		dashboard.setTotalDuration(
				userWatchHistoryDao.aggregateTotalDuration(HistoryEvent.COMPLETE.getCode(), null, null).getDuration());

		dashboard.setNotificationOnNum(userStateDao.countNotification(null, null));
		dashboard.setCalReminderOnNum(userStateDao.countReminder(null, null));

		dashboard.setShareNum(shareRecordDao.count());

		return dashboard;

	}

	/**
	 * Generate Dashboard data of yesterday
	 * 
	 * @return
	 */
	@Scheduled(cron = "0 0 1 ? * *")
	public void generatePrevDayDashboard() {

		Dashboard dashboard = generateDashboard(DateUtil.getPrevDayStartTime(), DateUtil.getStartTime());
		dashboard.setDate(DateUtil.getPrevDayStartTime());

		dashboardDao.save(dashboard);

	}

	/**
	 * Generate Dashboard data of today
	 * 
	 * @return
	 */
	public Dashboard generateTodayDashboard() {

		Dashboard dashboard = generateDashboard(DateUtil.getStartTime(), DateUtil.getCurrentDate());
		dashboard.setDate(DateUtil.getPrevDayStartTime());

		return dashboard;
	}

	/**
	 * Generate Dashboard data within the period
	 * 
	 * @return
	 */
	public Dashboard generateDashboard(Date start, Date end) {

		Dashboard dashboard = new Dashboard();

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

		dashboard.setAchievementNum(achievementRecordDao.count(start, end));

		List<UserWatchHistoryStat> temp = userWatchHistoryDao.aggregateWorkoutCompleteUsers(
				HistoryDest.WORKOUT.getCode(), HistoryEvent.COMPLETE.getCode(), start, end);
		dashboard.setOneWorkoutCompleteUserNum(temp.size());

		dashboard.setTotalDuration(
				userWatchHistoryDao.aggregateTotalDuration(HistoryEvent.COMPLETE.getCode(), start, end).getDuration());

		dashboard.setNotificationOnNum(userStateDao.countNotification(start, end));
		dashboard.setCalReminderOnNum(userStateDao.countReminder(start, end));

		dashboard.setShareNum(shareRecordDao.count(start, end));

		return dashboard;

	}
}
