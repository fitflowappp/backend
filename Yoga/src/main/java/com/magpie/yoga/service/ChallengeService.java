package com.magpie.yoga.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.magpie.yoga.dao.ChallengeDao;
import com.magpie.yoga.dao.UserStateDao;
import com.magpie.yoga.dao.UserWatchHistoryDao;
import com.magpie.yoga.dao.WorkoutDao;
import com.magpie.yoga.model.Challenge;
import com.magpie.yoga.model.UserState;
import com.magpie.yoga.model.UserWatchHistory;
import com.magpie.yoga.model.Workout;
import com.magpie.yoga.view.ChallengeView;
import com.magpie.yoga.view.WorkoutView;

@Service
public class ChallengeService {

	@Autowired
	private UserStateDao userStateDao;
	@Autowired
	private ChallengeDao challengeDao;
	@Autowired
	private WorkoutDao workoutDao;
	@Autowired
	private UserWatchHistoryDao userWatchHistoryDao;

	/**
	 * get user current challenge
	 * 
	 * @param uid
	 * @return
	 */
	public ChallengeView getUserCurrentChallenge(String uid) {
		UserState userState = userStateDao.findUserState(uid);
		if (userState != null && !StringUtils.isEmpty(userState.getCurrentChallengeId())) {
			ChallengeView view = initialChallengeView(challengeDao.findOne(userState.getCurrentChallengeId()));
			view.setCurrentWorkoutId(userState.getCurrentWorkoutId());
			return view;
		} else {
			return initialChallengeView(challengeDao.findRandomOne());
		}
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

		if (!StringUtils.isEmpty(workoutId)) {
			WorkoutView view = initialWorkoutView(workoutDao.findOne(workoutId));
			UserWatchHistory lastHist = userWatchHistoryDao.findLastHistory(uid, workoutId);
			view.setCurrentRoutineId(lastHist == null ? null : lastHist.getRoutineId());

			return view;
		} else {
			return null;
		}
	}

	private WorkoutView initialWorkoutView(Workout workout) {
		if (workout == null) {
			return null;
		}
		WorkoutView view = new WorkoutView();
		BeanUtils.copyProperties(workout, view);
		return view;
	}

}
