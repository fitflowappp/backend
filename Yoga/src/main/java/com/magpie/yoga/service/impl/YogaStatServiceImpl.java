package com.magpie.yoga.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.magpie.base.utils.DateUtil;
import com.magpie.user.dao.FacebookDao;
import com.magpie.user.dao.UserDao;
import com.magpie.yoga.constant.HistoryDest;
import com.magpie.yoga.constant.HistoryEvent;
import com.magpie.yoga.dao.AchievementRecordDao;
import com.magpie.yoga.dao.ChallengeDao;
import com.magpie.yoga.dao.DashboardDao;
import com.magpie.yoga.dao.RoutineDao;
import com.magpie.yoga.dao.ShareRecordDao;
import com.magpie.yoga.dao.UserConfigurationDao;
import com.magpie.yoga.dao.UserWatchHistoryDao;
import com.magpie.yoga.dao.WorkoutDao;
import com.magpie.yoga.model.AchievementRecord;
import com.magpie.yoga.model.Challenge;
import com.magpie.yoga.model.Dashboard;
import com.magpie.yoga.model.Routine;
import com.magpie.yoga.model.Workout;
import com.magpie.yoga.service.YogaStatService;
import com.magpie.yoga.stat.UserWatchHistoryStat;

@Service
public class YogaStatServiceImpl implements YogaStatService {

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
	@Autowired
	private ChallengeDao challengeDao;
	@Autowired
	private WorkoutDao workoutDao;
	@Autowired
	private RoutineDao routineDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private FacebookDao facebookDao;

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
	 * 每天更新Challenge,workout,routine统计数据
	 */
	public void updateCount() {

		Map<String, Challenge> cMap = new HashMap<>();
		Map<String, Workout> wMap = new HashMap<>();
		Map<String, Routine> rMap = new HashMap<>();

		// challenge times
		for (UserWatchHistoryStat stat : userWatchHistoryDao.aggregateChallengeCount(null, null)) {
			Challenge c = null;
			if (cMap.containsKey(stat.getChallengeId())) {
				c = cMap.get(stat.getChallengeId());
			} else {
				c = new Challenge();
				c.setId(stat.getChallengeId());
				cMap.put(stat.getChallengeId(), c);
			}
			if (stat.getEvent() == HistoryEvent.COMPLETE.getCode()) {
				c.setCompletedTimes(stat.getCount());
				c.setStartedTimes(c.getStartedTimes() + stat.getCount());
			} else if (stat.getEvent() == HistoryEvent.START.getCode()) {
				c.setStartedTimes(c.getStartedTimes() + stat.getCount());
			}

		}

		// workout times
		for (UserWatchHistoryStat stat : userWatchHistoryDao.aggregateWorkoutCount(null, null)) {
			Workout w = null;
			if (wMap.containsKey(stat.getWorkoutId())) {
				w = wMap.get(stat.getWorkoutId());
			} else {
				w = new Workout();
				w.setId(stat.getWorkoutId());
				wMap.put(stat.getWorkoutId(), w);
			}
			if (stat.getEvent() == HistoryEvent.COMPLETE.getCode()) {
				w.setCompletedTimes(stat.getCount());
				w.setTotalDuration(stat.getDuration());
				w.setStartedTimes(stat.getCount() + w.getStartedTimes());
			} else if (stat.getEvent() == HistoryEvent.START.getCode()) {
				w.setStartedTimes(stat.getCount() + w.getStartedTimes());
			}

		}

		// routine times
		for (UserWatchHistoryStat stat : userWatchHistoryDao.aggregateRoutineCount(null, null)) {
			Routine r = null;
			if (rMap.containsKey(stat.getRoutineId())) {
				r = rMap.get(stat.getRoutineId());
			} else {
				r = new Routine();
				r.setId(stat.getRoutineId());
				rMap.put(stat.getRoutineId(), r);
			}
			if (stat.getEvent() == HistoryEvent.SKIPPED.getCode()) {
				r.setSkippedTimes(stat.getCount());
			} else if (stat.getEvent() == HistoryEvent.START.getCode()) {
				r.setStartedTimes(r.getStartedTimes() + stat.getCount());
			} else if (stat.getEvent() == HistoryEvent.COMPLETE.getCode()) {
				r.setStartedTimes(r.getStartedTimes() + stat.getCount());
			}

		}

		// challenge users
		for (UserWatchHistoryStat stat : userWatchHistoryDao.aggregateChallengeUsers(null, null)) {
			Challenge c = null;
			if (cMap.containsKey(stat.getChallengeId())) {
				c = cMap.get(stat.getChallengeId());
			} else {
				c = new Challenge();
				c.setId(stat.getChallengeId());
				cMap.put(stat.getChallengeId(), c);
			}
			if (stat.getEvent() == HistoryEvent.COMPLETE.getCode()) {
				c.setCompletedUserCount(c.getCompletedUserCount() + 1);
				c.setStartedUserCount(c.getStartedUserCount() + 1);
			} else {
				c.setStartedUserCount(c.getStartedUserCount() + 1);
			}
		}

		// workout users
		for (UserWatchHistoryStat stat : userWatchHistoryDao.aggregateWorkoutUsers(null, null)) {
			Workout w = null;
			if (wMap.containsKey(stat.getWorkoutId())) {
				w = wMap.get(stat.getWorkoutId());
			} else {
				w = new Workout();
				w.setId(stat.getWorkoutId());
				wMap.put(stat.getWorkoutId(), w);
			}
			if (stat.getEvent() == HistoryEvent.COMPLETE.getCode()) {
				w.setCompletedUserCount(w.getCompletedUserCount() + 1);
				w.setStartedUserCount(w.getStartedUserCount() + 1);
			} else {
				w.setStartedUserCount(w.getStartedUserCount() + 1);
			}

		}

		for (UserWatchHistoryStat stat : userWatchHistoryDao.aggregateWorkoutDuration()) {
			Workout w = null;
			if (wMap.containsKey(stat.getWorkoutId())) {
				w = wMap.get(stat.getWorkoutId());
			} else {
				w = new Workout();
				w.setId(stat.getWorkoutId());
				wMap.put(stat.getWorkoutId(), w);
			}

			w.setTotalDuration(stat.getDuration());
		}

		for (Challenge challenge : cMap.values()) {
			challengeDao.updateStat(challenge);
		}

		for (Workout workout : wMap.values()) {
			workoutDao.updateStat(workout);
		}

		for (Routine routine : rMap.values()) {
			routineDao.updateStat(routine);
		}

	}

	/**
	 * Generate all Dashboard data
	 * 
	 * @return
	 */
	public Dashboard generateDashboard() {

		Dashboard dashboard = new Dashboard();

		long challengeStartNum = 0;
		long challengeCompleteNum = 0;

		long workoutStartNum = 0;
		long workoutCompleteNum = 0;
		long workoutCompleteDuration = 0;
		for (UserWatchHistoryStat historyStat : userWatchHistoryDao.aggregateStartCompleteNum(null, null)) {

			if (HistoryEvent.COMPLETE.getCode() == historyStat.getEvent()) {
				if (HistoryDest.CHALLENGE.getCode() == historyStat.getDestType()) {
					challengeCompleteNum = historyStat.getCount();
				} else if (HistoryDest.WORKOUT.getCode() == historyStat.getDestType()) {
					workoutCompleteNum = historyStat.getCount();
					workoutCompleteDuration = historyStat.getDuration();
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
		dashboard.setFacebookRegCompleteNum(facebookDao.count(null, null));
		dashboard.setFacebookRegSubmitNum(userDao.count(true, null, null));

		// dashboard.setAchievementNum(getAchievementCount(achievementRecordDao.aggregateCount(null,
		// null)));
		dashboard.setAchievementNum(achievementRecordDao.count(null, null));

		List<UserWatchHistoryStat> temp = userWatchHistoryDao.aggregateWorkoutUsers(HistoryEvent.COMPLETE.getCode(),
				null, null);
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

		// dashboard.setAchievementNum(getAchievementCount(achievementRecordDao.aggregateCount(start,
		// end)));
		dashboard.setAchievementNum(achievementRecordDao.count(start, end));

		List<UserWatchHistoryStat> temp = userWatchHistoryDao.aggregateWorkoutUsers(HistoryEvent.COMPLETE.getCode(),
				start, end);
		dashboard.setOneWorkoutCompleteUserNum(temp.size());
		dashboard.setFacebookRegCompleteNum(facebookDao.count(start, end));
		dashboard.setFacebookRegSubmitNum(userDao.count(true, start, end));

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
		Map<String, AchievementRecord> aMap = new HashMap<>();
		if (achievementStats != null) {
			for (AchievementRecord achievementRecord : achievementStats) {
				aMap.put(achievementRecord.getUid(), achievementRecord);
			}
			return aMap.values().size();
		}
		return count;
	}
}
