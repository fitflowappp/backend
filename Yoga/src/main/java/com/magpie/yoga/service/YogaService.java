package com.magpie.yoga.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.magpie.cache.yoga.YogaCacheService;
import com.magpie.share.UserRef;
import com.magpie.yoga.constant.DialogType;
import com.magpie.yoga.constant.HistoryDest;
import com.magpie.yoga.constant.HistoryEvent;
import com.magpie.yoga.dao.AchievementRecordDao;
import com.magpie.yoga.dao.ChallengeSetDao;
import com.magpie.yoga.dao.MilestoneDao;
import com.magpie.yoga.dao.UserStateDao;
import com.magpie.yoga.dao.UserWatchHistoryDao;
import com.magpie.yoga.model.AchievementRecord;
import com.magpie.yoga.model.Challenge;
import com.magpie.yoga.model.ChallengeSet;
import com.magpie.yoga.model.Milestone;
import com.magpie.yoga.model.Routine;
import com.magpie.yoga.model.UserState;
import com.magpie.yoga.model.UserWatchHistory;
import com.magpie.yoga.model.Workout;
import com.magpie.yoga.stat.UserWatchHistoryStat;
import com.magpie.yoga.view.Achievement;
import com.magpie.yoga.view.ActView;
import com.magpie.yoga.view.ChallengeSetView;
import com.magpie.yoga.view.ChallengeView;
import com.magpie.yoga.view.RoutineView;
import com.magpie.yoga.view.WorkoutView;

@Service
public class YogaService {

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

	private Logger logger = LoggerFactory.getLogger(getClass());

	public Achievement getUserAchievments(String uid) {
		Achievement achievement = new Achievement();
		for (UserWatchHistoryStat stat : userWatchHistoryDao.aggregateUserWatchHistory(uid,
				HistoryEvent.COMPLETE.getCode())) {
			if (stat.getDestType() == HistoryDest.CHALLENGE.getCode()) {
				achievement.setCompletedChallengeCount(stat.getCount());
			} else if (stat.getDestType() == HistoryDest.WORKOUT.getCode()) {
				achievement.setCompletedWorkoutCount(stat.getCount());
			}
		}
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
		String currentChallengeId = challengeSet.getChallenges().get(0).getId();
		if (userState != null) {
			currentChallengeId = userState.getCurrentChallengeId();
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
			if (!StringUtils.isEmpty(userRef.getRole())) {// role不为空，则为超级用户
				c.setAvail(true);
			}

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
		ChallengeSet challengeSet = challengeSetDao.findOneByPrimary(true);
		ChallengeView view = initialChallengeView(
				yogaCacheService.getChallenge(challengeSet.getChallenges().get(0).getId()));

		for (WorkoutView w : view.getWorkouts()) {
			w.setAvail(true);
			break;
		}
		return view;
	}

	public ChallengeView chooseChallenge(UserRef userRef, String cid) {
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
			if (!StringUtils.isEmpty(userRef.getRole())) {// role不为空，则为超级用户
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

			if (!StringUtils.isEmpty(userRef.getRole())) {// role不为空，则为超级用户
				r.setAvail(true);
			}

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
		userWatchHistoryDao.save(histories);
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
		Challenge challenge = yogaCacheService.getChallenge(cid);
		Workout workout = yogaCacheService.getWorkout(wid);

		// only record for routine which show in app
		if (routine != null && routine.isDisplay()) {
			createIfNoUserState(uid);
			if (type == HistoryEvent.STOP.getCode()) {
				// update current user state
				userStateDao.updateCurrentState(uid, cid, wid, rid, seconds);
			} else {
				// update current user state
				userStateDao.updateCurrentState(uid, cid, wid, rid);
			}

			String lastWorkoutId = challenge.getWorkouts().get(challenge.getWorkouts().size() - 1).getId();
			String lastRoutineIdOfChallenge = getLastRoutineIdOfWorkout(lastWorkoutId);
			String lastRoutineIdOfWorkout = getLastRoutineIdOfWorkout(initialWorkoutView(workout));

			String firstWorkoutId = challenge.getWorkouts().get(0).getId();
			String firstRoutineIdOfChallenge = getFirstRoutineIdOfWorkout(firstWorkoutId);

			logger.info("watching routine:uid:{},cid:{},wid:{},rid:{}", uid, cid, wid, rid);
			logger.info("lastWorkoutId:{},lastRoutineIdOfChallenge:{},lastRoutineIdOfWorkout:{}", lastWorkoutId,
					lastRoutineIdOfChallenge, lastRoutineIdOfWorkout);

			UserWatchHistory history = new UserWatchHistory(cid, wid, rid);
			history.setUid(uid);
			history.setEvent(type);
			history.setSeconds(seconds);
			if (type == HistoryEvent.COMPLETE.getCode() || type == HistoryEvent.SKIPPED.getCode()) {
				history.setDuration(routine == null ? 0 : routine.getDuration());
				history.setDestType(
						(routine.getId().equalsIgnoreCase(lastRoutineIdOfChallenge) && wid.equals(lastWorkoutId))
								? HistoryDest.CHALLENGE.getCode()
								: rid.equalsIgnoreCase(lastRoutineIdOfWorkout) ? HistoryDest.WORKOUT.getCode()
										: HistoryDest.ROUTINE.getCode());
			} else if (type == HistoryEvent.START.getCode()) {
				history.setDestType((rid.equalsIgnoreCase(firstRoutineIdOfChallenge) && wid.equals(firstWorkoutId))
						? HistoryDest.CHALLENGE.getCode()
						: rid.equalsIgnoreCase(getFirstRoutineIdOfWorkout(initialWorkoutView(workout)))
								? HistoryDest.WORKOUT.getCode()
								: HistoryDest.ROUTINE.getCode());
			} else {
				history.setDestType(HistoryDest.ROUTINE.getCode());
			}

			userWatchHistoryDao.save(history);
			return createActView(uid);
		} else {
			return null;
		}

	}

	private ActView createActView(String uid) {

		UserState userState = userStateDao.findUserState(uid);
		// dialog has been shown
		if (userState.isSendAchieveDurationDialog() && userState.isSendAchieveWorkoutDialog()) {
			return null;
		}

		Milestone milestone = milestoneDao.findOne();

		if (milestone == null) {
			return null;
		}

		int totalDuration = 0;
		int countOfWorkouts = 0;
		for (UserWatchHistoryStat stat : userWatchHistoryDao.aggregateUserWatchHistory(uid,
				HistoryEvent.SKIPPED.getCode())) {
			if (HistoryDest.WORKOUT.getCode() == stat.getDestType()
					|| HistoryDest.CHALLENGE.getCode() == stat.getDestType()) {
				countOfWorkouts += stat.getCount();
				totalDuration += stat.getDuration();
			}
		}

		if (!userState.isSendAchieveDurationDialog() && totalDuration >= milestone.getAchievementMinutes()) {

			achievementRecordDao.save(new AchievementRecord(uid, DialogType.DURATION.getCode()));
			userStateDao.updateAchieveDuration(uid, true);
			// show minutes dialog
			return new ActView(DialogType.DURATION.getCode(), milestone.getAchievementMinutesContent());
		}

		if (!userState.isSendAchieveWorkoutDialog() && countOfWorkouts >= milestone.getAchievementWorkoutNum()) {
			achievementRecordDao.save(new AchievementRecord(uid, DialogType.WORKOUT.getCode()));
			userStateDao.updateAchieveWorkout(uid, true);
			return new ActView(DialogType.WORKOUT.getCode(), milestone.getAchievementWorkoutContent());
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

}
