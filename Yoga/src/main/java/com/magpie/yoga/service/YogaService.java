package com.magpie.yoga.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.magpie.yoga.constant.DialogType;
import com.magpie.yoga.constant.HistoryDest;
import com.magpie.yoga.constant.HistoryEvent;
import com.magpie.yoga.dao.ChallengeDao;
import com.magpie.yoga.dao.ChallengeSetDao;
import com.magpie.yoga.dao.MilestoneDao;
import com.magpie.yoga.dao.RoutineDao;
import com.magpie.yoga.dao.UserStateDao;
import com.magpie.yoga.dao.UserWatchHistoryDao;
import com.magpie.yoga.dao.WorkoutDao;
import com.magpie.yoga.model.Challenge;
import com.magpie.yoga.model.ChallengeSet;
import com.magpie.yoga.model.Milestone;
import com.magpie.yoga.model.Routine;
import com.magpie.yoga.model.UserState;
import com.magpie.yoga.model.UserWatchHistory;
import com.magpie.yoga.model.Workout;
import com.magpie.yoga.stat.UserWatchHistoryStat;
import com.magpie.yoga.view.ActView;
import com.magpie.yoga.view.ChallengeSetView;
import com.magpie.yoga.view.ChallengeView;
import com.magpie.yoga.view.WorkoutView;

@Service
public class YogaService {

	@Autowired
	private UserStateDao userStateDao;
	@Autowired
	private ChallengeSetDao challengeSetDao;
	@Autowired
	private ChallengeDao challengeDao;
	@Autowired
	private WorkoutDao workoutDao;
	@Autowired
	private RoutineDao routineDao;
	@Autowired
	private UserWatchHistoryDao userWatchHistoryDao;
	@Autowired
	private MilestoneDao milestoneDao;

	/**
	 * get default challenge set
	 * 
	 * @param uid
	 * @return
	 */
	public ChallengeSetView getDefaultChallengeSet(String uid) {
		ChallengeSet challengeSet = challengeSetDao.findOneByPrimary(true);
		ChallengeSetView view = new ChallengeSetView();
		BeanUtils.copyProperties(challengeSet, view);
		UserState userState = userStateDao.findUserState(uid);
		if (userState != null) {
			view.setCurrentChallengeId(userState.getCurrentChallengeId());
		}
		List<Integer> statuses = new ArrayList<>();
		List<Boolean> avails = new ArrayList<>();

		boolean unlocked = false;
		boolean needUnlocked = false;
		for (Challenge c : view.getChallenges()) {
			UserWatchHistory history = userWatchHistoryDao.findUserHistory(uid, c.getId(),
					HistoryDest.CHALLENGE.getCode());
			statuses.add(history == null ? HistoryEvent.UNWATCH.getCode() : history.getEvent());
			if (c.isUnlocked()) {
				unlocked = (history != null && history.getEvent() >= HistoryEvent.SKIPPED.getCode());
				needUnlocked = true;
			}
			avails.add(needUnlocked ? unlocked ? true : false : true);
		}

		view.setStatuses(statuses);
		view.setAvails(avails);
		return view;
	}

	/**
	 * get user current challenge
	 * 
	 * @param uid
	 * @return
	 */
	public ChallengeView getCurrentChallenge(String uid) {
		UserState userState = userStateDao.findUserState(uid);
		if (userState != null && !StringUtils.isEmpty(userState.getCurrentChallengeId())) {
			return getChallenge(userState, userState.getCurrentChallengeId());
		} else {
			return getDefaultChallenge();
		}
	}

	private ChallengeView getDefaultChallenge() {
		ChallengeView view = initialChallengeView(challengeDao.findRandomOne());

		List<Integer> statuses = new ArrayList<>();
		List<Boolean> avails = new ArrayList<>();
		int status = HistoryEvent.UNWATCH.getCode();
		int index = 0;
		for (@SuppressWarnings("unused")
		Workout w : view.getWorkouts()) {
			statuses.add(HistoryEvent.UNWATCH.getCode());
			avails.add(index == 0);
			index++;
		}
		// the status of last watched workout
		view.setStatus(status);
		// status list of workouts
		view.setStatuses(statuses);
		return view;
	}

	/**
	 * get challenge
	 * 
	 * @param uid
	 * @return
	 */
	public ChallengeView getChallenge(String uid, String cid) {
		UserState userState = userStateDao.findUserState(uid);
		if (userState == null) {
			userState = new UserState();
			userState.setUid(uid);
		}
		return getChallenge(userState, cid);
	}

	private ChallengeView getChallenge(UserState userState, String cid) {
		ChallengeView view = initialChallengeView(challengeDao.findOne(cid));
		if (userState != null) {
			if (cid.equals(userState.getCurrentChallengeId())) {
				view.setCurrentWorkoutId(userState.getCurrentWorkoutId());
			}
		}

		List<Integer> statuses = new ArrayList<>();
		List<Boolean> avails = new ArrayList<>();
		int status = HistoryEvent.UNWATCH.getCode();
		int first = -1;
		int last = -1;
		for (Workout w : view.getWorkouts()) {
			UserWatchHistory history = userWatchHistoryDao.findUserHistory(userState.getUid(), view.getId(), w.getId(),
					HistoryDest.WORKOUT.getCode());
			statuses.add(history == null ? HistoryEvent.UNWATCH.getCode() : history.getEvent());
			if (history != null) {
				status = history.getEvent();
			}

			if (first == -1) {
				// first workout avail
				avails.add(true);
			} else {
				avails.add(last < HistoryEvent.SKIPPED.getCode() ? false : true);
			}

			if (first == -1) {
				first = status;
			}
			last = status;
		}

		view.setAvails(avails);
		// the status of last watched workout
		view.setStatus(status);
		// status list of workouts
		view.setStatuses(statuses);
		return view;
	}

	private ChallengeView initialChallengeView(Challenge challenge) {
		if (challenge == null) {
			return null;
		}
		ChallengeView view = new ChallengeView();
		BeanUtils.copyProperties(challenge, view);
		return view;
	}

	/**
	 * get workout with user history
	 * 
	 * @param uid
	 * @param workoutId
	 * @return
	 */
	public WorkoutView getWorkout(String uid, String cid, String workoutId) {
		WorkoutView view = initialWorkoutView(workoutDao.findOne(workoutId));

		UserState userState = userStateDao.findUserState(uid);
		if (userState != null) {
			if (cid.equals(userState.getCurrentChallengeId()) && workoutId.equals(userState.getCurrentWorkoutId())) {
				view.setCurrentRoutineId(userState.getCurrentRoutineId());
				view.setSeconds(userState.getCurrentRoutineSeconds());
			}
		}

		List<Integer> statuses = new ArrayList<>();
		List<Boolean> avails = new ArrayList<>();
		int status = HistoryEvent.UNWATCH.getCode();
		int seconds = 0;
		int first = -1;
		int last = -1;
		for (Routine r : view.getRoutines()) {
			UserWatchHistory history = userWatchHistoryDao.findUserHistory(uid, cid, view.getId(), r.getId());
			statuses.add(history == null ? HistoryEvent.UNWATCH.getCode() : history.getEvent());
			if (history != null) {
				status = history.getEvent();
				if (history.getEvent() == HistoryEvent.STOP.getCode()) {
					seconds = history.getSeconds();
				}
			}
			if (r.isDisplay()) {
				if (first == -1) {
					// first workout avail
					avails.add(true);
				} else {
					avails.add(last < HistoryEvent.SKIPPED.getCode() ? false : true);
				}

				if (first == -1) {
					first = status;
				}
				last = status;
			} else {
				avails.add(false);
			}
		}

		view.setSeconds(view.getSeconds() > 0 ? view.getSeconds() : seconds);
		// the status of last watched routine
		view.setStatus(status);
		// status list of routines
		view.setStatuses(statuses);
		view.setChallengeId(cid);
		return view;

	}

	private WorkoutView initialWorkoutView(Workout workout) {
		if (workout == null) {
			return null;
		}
		WorkoutView view = new WorkoutView();
		BeanUtils.copyProperties(workout, view);
		return view;
	}

	public ActView skipWorkout(String uid, String cid, String wid) {

		Challenge challenge = challengeDao.findOne(cid);
		Workout workout = workoutDao.findOne(wid);

		String lastRoutineIdOfChallenge = getLastRoutineIdOfChallenge(challenge);

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
						routine.getId().equalsIgnoreCase(lastRoutineIdOfChallenge) ? HistoryDest.CHALLENGE.getCode()
								: HistoryDest.ROUTINE.getCode());
				histories.add(history);
			}

		}
		if (lastRoutine != null) {
			userStateDao.updateCurrentState(uid, cid, wid, lastRoutine.getId());
		}
		userWatchHistoryDao.save(histories);
		return createActView(uid);
	}

	public ActView watchingRoutine(String uid, String cid, String wid, String rid, int type, int seconds) {

		Routine routine = routineDao.findOne(rid);
		Challenge challenge = challengeDao.findOne(cid);
		Workout workout = workoutDao.findOne(wid);
		// only record for routine which show in app
		if (routine != null && routine.isDisplay()) {
			if (userStateDao.findUserState(uid) == null) {
				UserState userState = new UserState();
				userState.setUid(uid);
				userStateDao.save(userState);
			}
			if (type == HistoryEvent.STOP.getCode()) {
				// update current user state
				userStateDao.updateCurrentState(uid, cid, wid, rid, seconds);
			} else {
				// update current user state
				userStateDao.updateCurrentState(uid, cid, wid, rid);
			}
			UserWatchHistory history = new UserWatchHistory(cid, wid, rid);
			history.setUid(uid);
			history.setEvent(type);
			history.setSeconds(seconds);
			if (type == HistoryEvent.COMPLETE.getCode()) {
				history.setDuration(routine == null ? 0 : routine.getDuration());
				history.setDestType(
						rid.equalsIgnoreCase(getLastRoutineIdOfChallenge(challenge)) ? HistoryDest.CHALLENGE.getCode()
								: rid.equalsIgnoreCase(getLastRoutineIdOfWorkout(workout))
										? HistoryDest.WORKOUT.getCode()
										: HistoryDest.ROUTINE.getCode());
			}

			if (type == HistoryEvent.START.getCode()) {
				history.setDestType(
						rid.equalsIgnoreCase(getFirstRoutineIdOfChallenge(challenge)) ? HistoryDest.CHALLENGE.getCode()
								: rid.equalsIgnoreCase(getFirstRoutineIdOfWorkout(workout))
										? HistoryDest.WORKOUT.getCode()
										: HistoryDest.ROUTINE.getCode());
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

		int totalDuration = 0;
		int countOfWorkouts = 0;
		for (UserWatchHistoryStat stat : userWatchHistoryDao.aggregateUserWatchHistory(uid,
				HistoryEvent.COMPLETE.getCode())) {
			totalDuration += stat.getDuration();
			if (HistoryDest.WORKOUT.getCode() == stat.getDestType()
					|| HistoryDest.CHALLENGE.getCode() == stat.getDestType()) {
				countOfWorkouts += stat.getCount();
			}
		}

		if (!userState.isSendAchieveDurationDialog() && totalDuration >= milestone.getAchievementMinutes()) {
			// show minutes dialog
			return new ActView(DialogType.DURATION.getCode(), milestone.getAchievementMinutesContent());
		}

		if (!userState.isSendAchieveWorkoutDialog() && countOfWorkouts >= milestone.getAchievementWorkoutNum()) {
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

	private String getFirstRoutineIdOfChallenge(Challenge challenge) {
		if (challenge.getWorkouts() != null && !challenge.getWorkouts().isEmpty()) {
			return getFirstRoutineIdOfWorkout(challenge.getWorkouts().get(0));
		} else {
			return null;
		}
	}

	private String getFirstRoutineIdOfWorkout(Workout workout) {
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

	private String getLastRoutineIdOfWorkout(Workout workout) {
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

	private String getLastRoutineIdOfChallenge(Challenge challenge) {
		if (challenge.getWorkouts() != null && !challenge.getWorkouts().isEmpty()) {
			return getLastRoutineIdOfWorkout(challenge.getWorkouts().get(0));
		} else {
			return null;
		}
	}

}
