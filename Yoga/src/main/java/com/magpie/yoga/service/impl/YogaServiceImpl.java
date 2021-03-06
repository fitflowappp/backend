package com.magpie.yoga.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.magpie.base.utils.DateUtil;
import com.magpie.cache.yoga.YogaCacheService;
import com.magpie.share.UserRef;
import com.magpie.user.dao.UserDao;
import com.magpie.user.model.User;
import com.magpie.yoga.constant.DialogType;
import com.magpie.yoga.constant.HistoryDest;
import com.magpie.yoga.constant.HistoryEvent;
import com.magpie.yoga.dao.AchievementRecordDao;
import com.magpie.yoga.dao.ChallengeSetDao;
import com.magpie.yoga.dao.MilestoneDao;
import com.magpie.yoga.dao.TopicDao;
import com.magpie.yoga.dao.TopicSinglesDao;
import com.magpie.yoga.dao.UserStateDao;
import com.magpie.yoga.dao.UserWatchHistoryDao;
import com.magpie.yoga.dao.UserWorkoutDao;
import com.magpie.yoga.dao.WorkoutDao;
import com.magpie.yoga.def.UserWorkDef;
import com.magpie.yoga.model.AchievementRecord;
import com.magpie.yoga.model.Challenge;
import com.magpie.yoga.model.ChallengeSet;
import com.magpie.yoga.model.Milestone;
import com.magpie.yoga.model.Routine;
import com.magpie.yoga.model.Topic;
import com.magpie.yoga.model.TopicSingles;
import com.magpie.yoga.model.UserState;
import com.magpie.yoga.model.UserWatchHistory;
import com.magpie.yoga.model.UserWorkout;
import com.magpie.yoga.model.Workout;
import com.magpie.yoga.service.YogaService;
import com.magpie.yoga.stat.UserWatchHistoryStat;
import com.magpie.yoga.view.Achievement;
import com.magpie.yoga.view.ActView;
import com.magpie.yoga.view.ChallengeSetView;
import com.magpie.yoga.view.ChallengeView;
import com.magpie.yoga.view.RoutineView;
import com.magpie.yoga.view.SinglesWorkoutView;
import com.magpie.yoga.view.UserWorkoutStat;
import com.magpie.yoga.view.WorkoutView;

@Service
public class YogaServiceImpl implements YogaService {

	@Autowired
	private UserStateDao userStateDao;
	@Autowired
	private ChallengeSetDao challengeSetDao;
	@Autowired
	private UserWatchHistoryDao userWatchHistoryDao;
	@Autowired
	private MilestoneDao milestoneDao;
	@Autowired
	private AchievementRecordDao achievementRecordDao;
	@Autowired
	private YogaCacheService yogaCacheService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserWorkoutDao userWorkoutDao;
	@Autowired
	private WorkoutDao workoutDao;
	@Autowired
	private TopicSinglesDao topicSinglesDao;
	@Autowired
	private TopicDao topicDao;


	private Logger logger = LoggerFactory.getLogger(getClass());

	public Achievement getUserAchievments(String uid) {
		Achievement achievement = new Achievement();
		User user = userDao.findOne(uid);
		
		achievement.setDays(DateUtil.daysBetween(user.getCrDate(), Calendar.getInstance().getTime()) + 1);
		
		achievement.setCompletedMinutes(userWatchHistoryDao.aggregateRoutineDuration(uid));
		achievement.setCompletedWorkoutCount(userWatchHistoryDao
				.aggregateWorkoutWatchHistory(uid, HistoryEvent.SKIPPED.getCode(), HistoryDest.WORKOUT.getCode())
				.size());

		return achievement;
	}

	/**
	 * get default challenge set
	 * 
	 * @param uid
	 * @return
	 */
	public ChallengeSetView getDefaultChallengeSet(UserRef userRef) {
		ChallengeSet challengeSet = challengeSetDao.findOneByPrimary(true);
		ChallengeSetView view = initialChallengeSetView(challengeSet);

		UserState userState = userStateDao.findUserState(userRef.getId());
		String currentChallengeId = yogaCacheService.getDefaultChallengeId();;//yogaCacheService.getDefaultChallengeId();
		if (userState != null && userState.getCurrentChallengeId() != null
				&& userState.getCurrentChallengeId().length() > 0) {
			currentChallengeId = userState.getCurrentChallengeId();
		} else {
			createIfNoUserState(userRef.getId());
			userStateDao.updateCurrentState(userRef.getId(), currentChallengeId, null, null);
		}

		boolean needUnlocked = false;
		List<Boolean> completeList = new ArrayList<>();
		ChallengeView currentChallenge = null;
		for (ChallengeView c : view.getChallenges()) {
			UserWatchHistory history = userWatchHistoryDao.findUserHistory(userRef.getId(), c.getId(),
					HistoryDest.CHALLENGE.getCode());
			c.setStatus(history == null ? HistoryEvent.UNWATCH.getCode() : history.getEvent());

			c.setAvail(needUnlocked ? isAvail(completeList) : true);
			boolean completed = (history != null && history.getEvent() >= HistoryEvent.SKIPPED.getCode());
			if (c.isUnlocked() && !completed) {
				needUnlocked = true;
			}

			completeList.add(completed);

			// if (userState != null) {
			// // 只有当前的才能看
			// c.setAvail(c.isAvail() &&
			// c.getId().equals(userState.getCurrentChallengeId()));
			// }
			if (StringUtils.isEmpty(currentChallengeId)) {
				if (history != null) {
					currentChallenge = c;
				}
			} else {
				if (c.getId().equals(currentChallengeId)) {
					currentChallenge = c;
				}
			}
			if (!StringUtils.isEmpty(userRef.getRole()) && "0".equals(userRef.getRole())) {// role不为空且为admin，则为超级用户
				c.setAvail(true);
			}
			c.setWorkouts(null);

		}
		view.setCurrentChallengeId(currentChallenge == null ? null : currentChallenge.getId());
		return view;
	}

	private boolean isAvail(List<Boolean> completeList) {
		if (completeList == null) {
			return true;
		}
		boolean result = true;
		for (Boolean boolean1 : completeList) {
			result = result && boolean1;
		}
		return result;
	}

	private ChallengeSetView initialChallengeSetView(ChallengeSet challengeSet) {
		ChallengeSetView view = new ChallengeSetView();
		BeanUtils.copyProperties(challengeSet, view, "challenges");
		List<ChallengeView> challengeViews = new ArrayList<>();
		for (Challenge challenge : challengeSet.getChallenges()) {
			ChallengeView cView = initialChallengeView(yogaCacheService.getChallenge(challenge.getId()));
			cView.setUnlocked(challenge.isUnlocked());
			challengeViews.add(cView);
		}
		view.setChallenges(challengeViews);
		return view;
	}

	/**
	 * get user current challenge
	 * 
	 * @param uid
	 * @return
	 */
	public ChallengeView getCurrentChallenge(UserRef userRef) {
		UserState userState = userStateDao.findUserState(userRef.getId());
		if (userState != null && !StringUtils.isEmpty(userState.getCurrentChallengeId())) {
			return getChallenge(userRef, userState, userState.getCurrentChallengeId());
		} else {
			return getDefaultChallenge();
		}
	}

	private ChallengeView getDefaultChallenge() {
		ChallengeView view = initialChallengeView(yogaCacheService.getChallenge(yogaCacheService.getDefaultChallengeId()));
		if(view!=null){
			for (WorkoutView w : view.getWorkouts()) {
				w.setAvail(true);
				break;
			}
		}
		
		return view;
	}

	public ChallengeView chooseChallenge(UserRef userRef, String cid) {
		createIfNoUserState(userRef.getId());
		userStateDao.updateCurrentState(userRef.getId(), cid, null, null);
		return getChallenge(userRef, cid);
	}

	/**
	 * get challenge
	 * 
	 * @param uid
	 * @return
	 */
	public ChallengeView getChallenge(UserRef userRef, String cid) {
		UserState userState = userStateDao.findUserState(userRef.getId());
		if (userState == null) {
			userState = new UserState();
			userState.setUid(userRef.getId());
		}
		return getChallenge(userRef, userState, cid);
	}

	private ChallengeView getChallenge(UserRef userRef, UserState userState, String cid) {
		ChallengeView view = initialChallengeView(yogaCacheService.getChallenge(cid));

		int status = HistoryEvent.UNWATCH.getCode();
		int first = -1;
		int last = -1;
		WorkoutView lastWorkout = null;
		String currentRoutineId = null;

		for (WorkoutView w : view.getWorkouts()) {
			UserWatchHistory history = userWatchHistoryDao.findUserHistory(userState.getUid(), view.getId(), w.getId(),
					HistoryDest.WORKOUT.getCode());
			w.setStatus(history == null ? HistoryEvent.UNWATCH.getCode() : history.getEvent());

			boolean avail = false;
			if (first == -1) {
				// first workout avail
				avail = true;
			} else {
				avail = (last < HistoryEvent.SKIPPED.getCode() ? false : true);
			}
			w.setAvail(avail);
			if (avail) {
				lastWorkout = w;
				if (history != null) {
					status = history.getEvent();
					currentRoutineId = history.getRoutineId();
				}
			}

			if (first == -1) {
				first = status;
			}
			last = w.getStatus();
			if (!StringUtils.isEmpty(userRef.getRole()) && "0".equals(userRef.getRole())) {// role不为空且为admin，则为超级用户
				w.setAvail(true);
			}

		}

		// the status of last watched workout
		view.setStatus(last);

		// current workout in current challenge
		if (cid.equals(userState.getCurrentChallengeId())) {
			view.setCurrentWorkoutId(
					userState.getCurrentWorkoutId() == null ? lastWorkout.getId() : userState.getCurrentWorkoutId());
			view.setCurrentRoutineId(
					userState.getCurrentRoutineId() == null ? currentRoutineId : userState.getCurrentRoutineId());
			view.setSeconds(userState.getCurrentRoutineSeconds());
		} else {
			// current workout in every challenge
			view.setCurrentWorkoutId(lastWorkout.getId());
			view.setCurrentRoutineId(currentRoutineId);
		}
		return view;
	}

	private ChallengeView initialChallengeView(Challenge challenge) {
		if (challenge == null) {
			return null;
		}
		ChallengeView view = new ChallengeView();
		BeanUtils.copyProperties(challenge, view, "workouts");

		List<WorkoutView> workoutViews = new ArrayList<>();

		for (Workout workout : challenge.getWorkouts()) {
			workoutViews.add(initialWorkoutView(yogaCacheService.getWorkout(workout.getId())));
		}
		view.setWorkouts(workoutViews);

		return view;
	}

	/**
	 * get workout with user history
	 * 
	 * @param uid
	 * @param workoutId
	 * @return
	 */
	public WorkoutView getWorkout(UserRef userRef, String cid, String workoutId) {
		WorkoutView view = initialWorkoutView(yogaCacheService.getWorkout(workoutId));

		int status = HistoryEvent.UNWATCH.getCode();
		int seconds = 0;
		int first = -1;
		int last = -1;
		RoutineView lastRoutine = null;
		for (RoutineView r : view.getRoutines()) {
			UserWatchHistory history = userWatchHistoryDao.findUserHistory(userRef.getId(), cid, view.getId(),
					r.getId());
			r.setStatus(history == null ? HistoryEvent.UNWATCH.getCode() : history.getEvent());

			if (r.isDisplay()) {
				boolean avail = false;
				if (first == -1) {
					// first workout avail
					avail = true;
				} else {
					avail = (last < HistoryEvent.SKIPPED.getCode() ? false : true);
				}
				r.setAvail(avail);
				if (avail) {
					lastRoutine = r;
					if (history != null) {
						status = history.getEvent();
						if (history.getEvent() == HistoryEvent.STOP.getCode()) {
							seconds = history.getSeconds();
						}
					}
				}

				if (first == -1) {
					first = status;
				}
				last = status;

			} else {
				r.setAvail(false);
			}

			if (!StringUtils.isEmpty(userRef.getRole()) && "0".equals(userRef.getRole())) {// role不为空且为admin，则为超级用户
				r.setAvail(true);
			}

		}
		List<UserWorkout> userWorkoutList = userWorkoutDao.findSortByDate(userRef.getId(), workoutId);
		if (userWorkoutList != null && userWorkoutList.size() > 0) {
			view.setIsfollow(true);
		}

		// UserWatchHistory wh =
		// userWatchHistoryDao.findUserHistory(userRef.getId(), cid,
		// view.getId(),
		// HistoryDest.WORKOUT.getCode());

		// the status of last watched routine
		view.setStatus(last);
		view.setChallengeId(cid);
		view.setCurrentRoutineId(lastRoutine == null ? null : lastRoutine.getId());
		view.setAvail(lastRoutine == null ? false : lastRoutine.isAvail());
		view.setSeconds(seconds);
		return view;

	}

	private WorkoutView initialWorkoutView(Workout workout) {
		if (workout == null) {
			return null;
		}
		WorkoutView view = new WorkoutView();
		BeanUtils.copyProperties(workout, view, "routines");

		List<RoutineView> routineViews = new ArrayList<>();
		for (Routine r : workout.getRoutines()) {
			RoutineView routineView = new RoutineView();
			BeanUtils.copyProperties(yogaCacheService.getRoutine(r.getId()), routineView);
			routineViews.add(routineView);
		}
		if (StringUtils.isEmpty(view.getShareUrl())) {
			view.setShareUrl(UserWorkDef.SHARE_URL);
		}
		view.setRoutines(routineViews);
		return view;
	}

	public ActView skipWorkout(String uid, String cid, String wid) {

		Challenge challenge = yogaCacheService.getChallenge(cid);
		Workout workout = yogaCacheService.getWorkout(wid);

		String lastWorkoutId = challenge.getWorkouts().get(challenge.getWorkouts().size() - 1).getId();
		String lastRoutineIdOfChallenge = getLastRoutineIdOfWorkout(lastWorkoutId);
		String lastRoutineIdOfWorkout = getLastRoutineIdOfWorkout(initialWorkoutView(workout));

		logger.info("skip workout:{},{},{}", uid, cid, wid);
		logger.info("lastWorkoutId:{},lastRoutineIdOfChallenge:{},lastRoutineIdOfWorkout:{}", lastWorkoutId,
				lastRoutineIdOfChallenge, lastRoutineIdOfWorkout);

		Routine lastRoutine = null;
		Routine firstRoutine = null;
		List<UserWatchHistory> histories = new ArrayList<>();
		for (Routine routine : workout.getRoutines()) {
			if (routine.isDisplay()) {
				if (firstRoutine == null) {
					firstRoutine = routine;
				}
				lastRoutine = routine;
				UserWatchHistory history = new UserWatchHistory(cid, wid, routine.getId());
				history.setUid(uid);
				history.setEvent(HistoryEvent.SKIPPED.getCode());
				history.setDestType(
						(routine.getId().equalsIgnoreCase(lastRoutineIdOfChallenge) && wid.equals(lastWorkoutId))
								? HistoryDest.CHALLENGE.getCode()
								: routine.getId().equals(lastRoutineIdOfWorkout) ? HistoryDest.WORKOUT.getCode()
										: HistoryDest.ROUTINE.getCode());
				histories.add(history);
			}

		}
		if (lastRoutine != null) {
			createIfNoUserState(uid);
			userStateDao.updateCurrentState(uid, cid, wid, lastRoutine.getId());
		}
		if (!histories.isEmpty()) {
			if (userWatchHistoryDao.countSkipWorkout(histories.get(0)) < histories.size()) {
				userWatchHistoryDao.save(histories);
			}
		}
		return createActView(uid);
	}

	private void createIfNoUserState(String uid) {
		if (userStateDao.findUserState(uid) == null) {
			UserState userState = new UserState();
			userState.setUid(uid);
			userStateDao.save(userState);
		}
	}

	public ActView watchingRoutine(String uid, String cid, String wid, String rid, int type, int seconds) {

		Routine routine = yogaCacheService.getRoutine(rid);
		Challenge challenge = null;
		if (cid != null && cid.length() > 0) {
			challenge = yogaCacheService.getChallenge(cid);
		}
		Workout workout = yogaCacheService.getWorkout(wid);

		// only record for routine which show in app
		if (routine != null && routine.isDisplay()) {
			createIfNoUserState(uid);
			if (challenge != null) {
				if (type == HistoryEvent.STOP.getCode()) {
					// update current user state
					userStateDao.updateCurrentState(uid, cid, wid, rid, seconds);
				} else {
					// update current user state
					userStateDao.updateCurrentState(uid, cid, wid, rid);
				}
			}
			// 比较下第一个和最后一个的数据
			String lastWorkoutId = null;
			String lastRoutineIdOfChallenge = null;
			String lastRoutineIdOfWorkout = getLastRoutineIdOfWorkout(initialWorkoutView(workout));

			String firstWorkoutId = null;
			String firstRoutineIdOfChallenge = null;

			if (challenge != null) {
				lastWorkoutId = challenge.getWorkouts().get(challenge.getWorkouts().size() - 1).getId();
				if (lastWorkoutId != null) {
					lastRoutineIdOfChallenge = getLastRoutineIdOfWorkout(lastWorkoutId);
				}
				firstWorkoutId = challenge.getWorkouts().get(0).getId();
				if (firstWorkoutId != null) {
					firstRoutineIdOfChallenge = getFirstRoutineIdOfWorkout(firstWorkoutId);
				}
			}
			logger.info("watching routine:uid:{},cid:{},wid:{},rid:{}", uid, cid, wid, rid);
			logger.info("lastWorkoutId:{},lastRoutineIdOfChallenge:{},lastRoutineIdOfWorkout:{}", lastWorkoutId,
					lastRoutineIdOfChallenge, lastRoutineIdOfWorkout);

			UserWatchHistory history = new UserWatchHistory(cid, wid, rid);
			history.setUid(uid);
			history.setEvent(type);
			history.setSeconds(seconds);
			if (type == HistoryEvent.COMPLETE.getCode() || type == HistoryEvent.SKIPPED.getCode()) {
				history.setDuration(routine == null ? 0 : routine.getDuration());

				if (challenge != null) {
					history.setDestType(
							(routine.getId().equalsIgnoreCase(lastRoutineIdOfChallenge) && wid.equals(lastWorkoutId))
									? HistoryDest.CHALLENGE.getCode()
									: rid.equalsIgnoreCase(lastRoutineIdOfWorkout) ? HistoryDest.WORKOUT.getCode()
											: HistoryDest.ROUTINE.getCode());
				} else {
					history.setDestType(rid.equalsIgnoreCase(lastRoutineIdOfWorkout) ? HistoryDest.WORKOUT.getCode()
							: HistoryDest.ROUTINE.getCode());
				}

			} else if (type == HistoryEvent.START.getCode()) {
				if (challenge != null) {
					history.setDestType((rid.equalsIgnoreCase(firstRoutineIdOfChallenge) && wid.equals(firstWorkoutId))
							? HistoryDest.CHALLENGE.getCode()
							: rid.equalsIgnoreCase(getFirstRoutineIdOfWorkout(initialWorkoutView(workout)))
									? HistoryDest.WORKOUT.getCode() : HistoryDest.ROUTINE.getCode());
				} else {
					history.setDestType(rid.equalsIgnoreCase(getFirstRoutineIdOfWorkout(initialWorkoutView(workout)))
							? HistoryDest.WORKOUT.getCode() : HistoryDest.ROUTINE.getCode());
				}
			} else {
				history.setDestType(HistoryDest.ROUTINE.getCode());
			}

			if (!userWatchHistoryDao.exists(history)) {
				userWatchHistoryDao.save(history);
			}
			String shareUrl = workout.getShareUrl();
			if (StringUtils.isEmpty(shareUrl)) {
				shareUrl = UserWorkDef.SHARE_URL;
			}
			return createActView(uid, shareUrl);
		} else {
			return null;
		}

	}

	private ActView createActView(String uid) {
		return createActView(uid, null);
	}

	private ActView createActView(String uid, String shareUrl) {

		Milestone milestone = milestoneDao.findOne();

		if (milestone == null) {
			return null;
		}

		int countOfWorkouts = 0;
		List<UserWatchHistoryStat> historys = userWatchHistoryDao.aggregateWorkoutWatchHistory(uid,
				HistoryEvent.SKIPPED.getCode(), HistoryDest.WORKOUT.getCode());
		if (historys != null) {
			countOfWorkouts = historys.size();
		}
		int workoutCountNo = countOfWorkouts / milestone.getAchievementWorkoutNum();
		// &&(countOfWorkouts % milestone.getAchievementWorkoutNum())==0
		if (workoutCountNo > 0
				&& achievementRecordDao.findByNo(uid, DialogType.WORKOUT.getCode(), workoutCountNo) == null) {
			achievementRecordDao
					.save(new AchievementRecord(uid, DialogType.WORKOUT.getCode(), workoutCountNo, countOfWorkouts));
			//
			logger.info("milestone WORKOUT:" + countOfWorkouts);

			if (StringUtils.isEmpty(shareUrl)) {
				return new ActView(DialogType.WORKOUT.getCode(), milestone.getAchievementWorkoutContent());
			} else {
				return new ActView(DialogType.WORKOUT.getCode(), milestone.getAchievementWorkoutContent(),
						"I've done " + countOfWorkouts
								+ " yoga classes on the Fitflow app. I loved it. And it's free. Check it out.",
						shareUrl);
			}
		}

		int totalDuration = userWatchHistoryDao.aggregateRoutineDuration(uid);
		int durationNo = totalDuration / milestone.getAchievementMinutes();
		logger.info("durationNo:" + durationNo + " workoutCountNo:" + workoutCountNo);
		if (durationNo > 0 && achievementRecordDao.findByNo(uid, DialogType.DURATION.getCode(), durationNo) == null) {
			logger.info("milestone minute:" + totalDuration);

			achievementRecordDao
					.save(new AchievementRecord(uid, DialogType.DURATION.getCode(), durationNo, totalDuration));
			// show minutes dialog
			if (StringUtils.isEmpty(shareUrl)) {
				return new ActView(DialogType.DURATION.getCode(), milestone.getAchievementMinutesContent());
			} else {
				return new ActView(DialogType.DURATION.getCode(), milestone.getAchievementMinutesContent(),
						"I've done " + totalDuration
								+ " minutes of yoga classes on the Fitflow app. I loved it. And it's free. Check it out.",
						shareUrl);
			}
		}

		return null;

	}

	// private String getFirstRoutineIdOfChallenge(String cid) {
	// Challenge challenge = challengeDao.findOne(cid);
	// return getFirstRoutineIdOfChallenge(challenge);
	// }
	//
	// private String getFirstRoutineIdOfWorkout(String wid) {
	// Workout workout = workoutDao.findOne(wid);
	// return getFirstRoutineIdOfWorkout(workout);
	// }

	private String getFirstRoutineIdOfWorkout(String workoutId) {
		return getFirstRoutineIdOfWorkout(initialWorkoutView(yogaCacheService.getWorkout(workoutId)));
	}

	private String getFirstRoutineIdOfWorkout(WorkoutView workout) {
		if (workout.getRoutines() != null && !workout.getRoutines().isEmpty()) {
			Routine firstRoutine = null;
			for (Routine routine : workout.getRoutines()) {
				if (routine.isDisplay()) {
					if (firstRoutine == null) {
						firstRoutine = routine;
						break;
					}
				}
			}
			return firstRoutine == null ? null : firstRoutine.getId();
		} else {
			return null;
		}
	}

	private String getLastRoutineIdOfWorkout(String workoutId) {
		return getLastRoutineIdOfWorkout(initialWorkoutView(yogaCacheService.getWorkout(workoutId)));
	}

	private String getLastRoutineIdOfWorkout(WorkoutView workout) {

		if (workout.getRoutines() != null && !workout.getRoutines().isEmpty()) {
			Routine lastRoutine = null;
			for (Routine routine : workout.getRoutines()) {
				if (routine.isDisplay()) {
					lastRoutine = routine;
				}
			}
			return lastRoutine == null ? null : lastRoutine.getId();
		} else {
			return null;
		}
	}

	public UserWorkoutStat getUserWorkoutStat(String uid) {
		UserWorkoutStat stat = new UserWorkoutStat();
		stat.setWorkoutCount(userWatchHistoryDao.countCompleteWorkout(uid));
		stat.setSeconds(userWatchHistoryDao.sumWorkoutSeconds(uid));
		return stat;
	}

	public List<Workout> getUserWorkoutList(String uid) {
		if (uid == null || uid.length() <= 0) {
			return null;
		}

		return userWorkoutDao.findWorkoutSortByDate(uid);
	}

	public List<UserWorkout> getUserAllWorkoutList(String uid) {
		if (uid == null || uid.length() <= 0) {
			return null;
		}
		return userWorkoutDao.findSortByDate(uid, null, true);
	}

	public List<Workout> defaultUserWorkout(String uid) {

		UserState userState = userStateDao.findUserState(uid);
		int index = 0;
		if (userState != null) {
			String challengeId = userState.getCurrentChallengeId();
			if (challengeId != null && challengeId.length() > 0) {
				ArrayList<String> defaultChallengeIdList = UserWorkDef.topicChallengeList;
				if (defaultChallengeIdList.contains(challengeId)) {
					index = defaultChallengeIdList.indexOf(challengeId);
				}
			}
		}

		final List<String> defaultWorkoutIdList = UserWorkDef.singlesIdList.get(index);
		List<Workout> workoutList = new ArrayList<>();
		String iterableItem =null;
		Workout workout=null;
		for (int i=0;i<defaultWorkoutIdList.size();i++) {
			iterableItem=defaultWorkoutIdList.get(i);
			workout=yogaCacheService.getWorkout(iterableItem);
			if(workout!=null){
				workoutList.add(workout);
			}
		}
		
		// 异步同步到userworkout
		asyncInsertDefaultUserWorkout(defaultWorkoutIdList, uid);
		return workoutList;
	}
	public List<Workout> defaultUserWorkoutFromDatabase(String uid) {
		
		UserState userState = userStateDao.findUserState(uid);
		List<TopicSingles> topicSinglesList=null;
		if(userState!=null){
			String challengeId=userState.getCurrentChallengeId();
			if(StringUtils.isEmpty(challengeId)==false){
				Topic topic=topicDao.findOneByChallengeId(challengeId);
				if(topic!=null){
					topicSinglesList=topicSinglesDao.findList(topic.getId());
				}
			}
		}
		if(topicSinglesList==null){
			topicSinglesList=topicSinglesDao.findList(yogaCacheService.getDefaultTopicId());
		}
		final List<String> defaultWorkoutIdList = new ArrayList<>();
		List<Workout> workoutList = new ArrayList<>();
		TopicSingles topicSingles =null;
		if(topicSinglesList!=null){
			for (int i=0;i<topicSinglesList.size();i++) {
				topicSingles=topicSinglesList.get(i);
				workoutList.add(yogaCacheService.getWorkout(topicSingles.getSinglesId()));
				defaultWorkoutIdList.add(topicSingles.getSinglesId());
			}
		}
		// 异步同步到userworkout
		asyncInsertDefaultUserWorkout(defaultWorkoutIdList, uid);
		return workoutList;
	}

	@Autowired
	Executor executor;

	private void asyncInsertDefaultUserWorkout(final List<String> workoutIdList, final String uid) {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Workout workout = null;
				UserWorkout userWorkout = null;
				for (String workoutId : workoutIdList) {
					workout = workoutDao.findOne(workoutId);
					if (workout != null) {
						userWorkout = new UserWorkout();
						userWorkout.setUid(uid);
						userWorkout.setWorkoutId(workoutId);
						userWorkout.setWorkout(workout);
						userWorkoutDao.save(userWorkout);
					}
				}
			}
		});
	}

	public boolean deleteUserWorkout(String uid, String workoutId) {

		userWorkoutDao.updateDeleteStatus(uid, workoutId);
		return true;
	}

	public boolean addWorkout(String uid, String workoutId) {
		Workout workout = workoutDao.findOne(workoutId);
		if (workout == null) {
			return false;
		}
		UserWorkout saved = userWorkoutDao.findOne(uid, workoutId);
		if (saved == null) {
			UserWorkout userWorkout = new UserWorkout();
			userWorkout.setUid(uid);
			userWorkout.setWorkoutId(workoutId);
			userWorkout.setWorkout(workout);
			userWorkout.setFrom(UserWorkout.USER);
			userWorkoutDao.save(userWorkout);

		} else {

			userWorkoutDao.updateDeleteStatus(uid, workoutId, UserWorkout.USER, false);
		}
		return true;
	}

	public List<SinglesWorkoutView> singleWorkoutList(String uid) {
		List<Workout> workoutList = workoutDao.findsginleList();

		return singleWorkoutList(workoutList);
	}

	private List<SinglesWorkoutView> singleWorkoutList(List<Workout> workoutList) {
		// List<UserWorkout>
		// userWorkoutList=userWorkoutDao.findSortByDate(uid);//暂时屏蔽列表页状态
		List<SinglesWorkoutView> resultWorkoutList = new ArrayList<>();
		SinglesWorkoutView singlesWorkout = null;
		// UserWorkout userWorkout=null;
		for (Workout workout : workoutList) {
			singlesWorkout = new SinglesWorkoutView();
			BeanUtils.copyProperties(workout, singlesWorkout);
			// for (int i = 0; i < userWorkoutList.size(); i++) {
			// userWorkout=userWorkoutList.get(i);
			// if(workout.getId().equals(userWorkout.getWorkoutId())){
			// singlesWorkout.setIsfollow(true);
			// }
			// }
			if (StringUtils.isEmpty(singlesWorkout.getShareUrl())) {
				singlesWorkout.setShareUrl(UserWorkDef.SHARE_URL);
			}
			resultWorkoutList.add(singlesWorkout);
		}
		return resultWorkoutList;
	}

	

}
