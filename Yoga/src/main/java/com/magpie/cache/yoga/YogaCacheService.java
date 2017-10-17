package com.magpie.cache.yoga;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.magpie.yoga.dao.ChallengeDao;
import com.magpie.yoga.dao.RoutineDao;
import com.magpie.yoga.dao.WorkoutDao;
import com.magpie.yoga.model.Challenge;
import com.magpie.yoga.model.Routine;
import com.magpie.yoga.model.Workout;

@Service
public class YogaCacheService {

	private final String YOGA_CHALLENGE_KEY_PREFIX = getClass().getName() + ":CHALLENGE:";//
	private final String YOGA_WORKOUT_KEY_PREFIX = getClass().getName() + ":WORKOUT:";//
	private final String YOGA_ROUTINE_KEY_PREFIX = getClass().getName() + ":ROUTINE:";//

	@Resource(name = "stringRedisTemplate")
	private ValueOperations<String, String> valueOperations;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private ChallengeDao challengeDao;
	@Autowired
	private WorkoutDao workoutDao;
	@Autowired
	private RoutineDao routineDao;

	@PostConstruct
	public void initalYogaCache() {
		logger.info("====================initial yoga cache=================");
		for (Challenge challenge : challengeDao.findAll()) {
			this.setChallenge(challenge);
		}

		for (Workout workout : workoutDao.findAll()) {
			this.setWorkout(workout);
		}

		for (Routine routine : routineDao.findAll()) {
			this.setRoutine(routine);
		}
	}

	public Challenge getChallenge(String id) {
		String key = YOGA_CHALLENGE_KEY_PREFIX + id;
		String value = valueOperations.get(key);
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		return JSON.parseObject(value, Challenge.class);
	}

	public Workout getWorkout(String id) {
		String key = YOGA_WORKOUT_KEY_PREFIX + id;
		String value = valueOperations.get(key);
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		return JSON.parseObject(value, Workout.class);
	}

	public Routine getRoutine(String id) {
		String key = YOGA_ROUTINE_KEY_PREFIX + id;
		String value = valueOperations.get(key);
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		return JSON.parseObject(value, Routine.class);
	}

	public void setChallenge(Challenge challenge) {
		String key = YOGA_CHALLENGE_KEY_PREFIX + challenge.getId();
		valueOperations.set(key, JSON.toJSONString(challenge));
	}

	public void setWorkout(Workout workout) {
		String key = YOGA_WORKOUT_KEY_PREFIX + workout.getId();
		valueOperations.set(key, JSON.toJSONString(workout));
	}

	public void setRoutine(Routine routine) {
		String key = YOGA_ROUTINE_KEY_PREFIX + routine.getId();
		valueOperations.set(key, JSON.toJSONString(routine));
	}

	public void delChallenge(String id) {
		String key = YOGA_CHALLENGE_KEY_PREFIX + id;
		stringRedisTemplate.delete(key);
	}

	public void delWorkout(String id) {
		String key = YOGA_WORKOUT_KEY_PREFIX + id;
		stringRedisTemplate.delete(key);
	}

	public void delRoutine(String id) {
		String key = YOGA_ROUTINE_KEY_PREFIX + id;
		stringRedisTemplate.delete(key);
	}
}
