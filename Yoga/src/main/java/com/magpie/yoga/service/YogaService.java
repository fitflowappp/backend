package com.magpie.yoga.service;

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
			return null;
		}
	}

	/**
	 * get challenge
	 * 
	 * @param uid
	 * @return
	 */
	public ChallengeView getChallenge(String uid, String cid) {
		UserState userState = userStateDao.findUserState(uid);
		return getChallenge(userState, cid);
	}

	private ChallengeView getChallenge(UserState userState, String cid) {
		ChallengeView view = initialChallengeView(challengeDao.findOne(cid));
		if (userState != null) {
			view.setCurrentWorkoutId(userState.getCurrentWorkoutId());
		}
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
	public WorkoutView getWorkout(String uid, String workoutId) {
		WorkoutView view = initialWorkoutView(workoutDao.findOne(workoutId));

		UserState userState = userStateDao.findUserState(uid);
		if (userState != null) {
			view.setCurrentRoutineId(userState.getCurrentRoutineId());
			view.setSeconds(userState.getCurrentRoutineSeconds());
		}

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
		if (challenge.getWorkouts() != null && !challenge.getWorkouts().isEmpty()
				&& challenge.getWorkouts().get(0).getRoutines() != null
				&& !challenge.getWorkouts().get(0).getRoutines().isEmpty()) {
			return challenge.getWorkouts().get(0).getRoutines().get(0).getId();
		} else {
			return null;
		}
	}

	private String getFirstRoutineIdOfWorkout(Workout workout) {
		if (workout.getRoutines() != null && !workout.getRoutines().isEmpty()) {
			return workout.getRoutines().get(0).getId();
		} else {
			return null;
		}
	}

	private String getLastRoutineIdOfWorkout(Workout workout) {
		if (workout.getRoutines() != null && !workout.getRoutines().isEmpty()) {
			int size = workout.getRoutines().size();
			return workout.getRoutines().get(size - 1).getId();
		} else {
			return null;
		}
	}

	private String getLastRoutineIdOfChallenge(Challenge challenge) {
		if (challenge.getWorkouts() != null && !challenge.getWorkouts().isEmpty()
				&& challenge.getWorkouts().get(0).getRoutines() != null
				&& !challenge.getWorkouts().get(0).getRoutines().isEmpty()) {
			int size = challenge.getWorkouts().get(0).getRoutines().size();
			return challenge.getWorkouts().get(0).getRoutines().get(size - 1).getId();
		} else {
			return null;
		}
	}

}
