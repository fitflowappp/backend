package com.magpie.yoga.service;

import java.util.List;

import com.magpie.share.UserRef;
import com.magpie.yoga.model.UserWorkout;
import com.magpie.yoga.model.Workout;
import com.magpie.yoga.view.Achievement;
import com.magpie.yoga.view.ActView;
import com.magpie.yoga.view.ChallengeSetView;
import com.magpie.yoga.view.ChallengeView;
import com.magpie.yoga.view.SinglesWorkoutView;
import com.magpie.yoga.view.UserWorkoutStat;
import com.magpie.yoga.view.WorkoutView;

public interface YogaService {
	public Achievement getUserAchievments(String uid);

	/**
	 * get default challenge set
	 * 
	 * @param uid
	 * @return
	 */
	public ChallengeSetView getDefaultChallengeSet(UserRef userRef) ;


	
	/**
	 * get user current challenge
	 * 
	 * @param uid
	 * @return
	 */
	public ChallengeView getCurrentChallenge(UserRef userRef);

	

	public ChallengeView chooseChallenge(UserRef userRef, String cid) ;

	/**
	 * get challenge
	 * 
	 * @param uid
	 * @return
	 */
	public ChallengeView getChallenge(UserRef userRef, String cid);



	/**
	 * get workout with user history
	 * 
	 * @param uid
	 * @param workoutId
	 * @return
	 */
	public WorkoutView getWorkout(UserRef userRef, String cid, String workoutId) ;

	

	public ActView skipWorkout(String uid, String cid, String wid) ;

	

	public ActView watchingRoutine(String uid, String cid, String wid, String rid, int type, int seconds) ;

	
	
	public UserWorkoutStat getUserWorkoutStat(String uid);
	public List<Workout> getUserWorkoutList(String uid);
	public List<UserWorkout> getUserAllWorkoutList(String uid);
	public List<Workout> defaultUserWorkout(String uid);
	
	public boolean deleteUserWorkout(String uid,String workoutId);
	public boolean addWorkout(String uid,String workoutId);
	public List<SinglesWorkoutView> singleWorkoutList(String uid);
	
}
