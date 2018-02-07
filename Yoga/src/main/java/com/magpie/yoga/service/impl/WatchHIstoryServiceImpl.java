package com.magpie.yoga.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.magpie.yoga.constant.HistoryDest;
import com.magpie.yoga.constant.HistoryEvent;
import com.magpie.yoga.dao.UserWatchHistoryDao;
import com.magpie.yoga.model.UserWatchHistory;
import com.magpie.yoga.service.WatchHistoryService;

@Service
public class WatchHIstoryServiceImpl implements WatchHistoryService {
	@Autowired
	UserWatchHistoryDao userWatchHistoryDao;
	@Override
	public UserWatchHistory findOne(String userId, String cid, String workoutId) {
		// TODO Auto-generated method stub
		return userWatchHistoryDao.findUserHistory(userId, cid, workoutId, HistoryDest.WORKOUT.getCode(),HistoryEvent.COMPLETE.getCode());
	}

	@Override
	public List<UserWatchHistory> completeWorkoutList(String userId, String cid, String workoutId) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(userId)){
			return null;
		}
		return userWatchHistoryDao.findUserHistoryList(userId, cid, workoutId, HistoryDest.WORKOUT.getCode(), HistoryEvent.COMPLETE.getCode());
	}

}
