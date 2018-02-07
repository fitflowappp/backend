package com.magpie.yoga.service;

import java.util.List;

import com.magpie.yoga.model.UserWatchHistory;

public interface WatchHistoryService {
	/**
	 * 获取用户观看记录
	* @Title: findOne  
	* @Description: 获取用户观看记录，如果cid为null，不是忽略cid，
	* 而是就是要求cid为null，如果是null，主要是正对singles查询   
	* @return UserWatchHistory    返回类型  
	* @throws
	 */
	public UserWatchHistory findOne(String userId,String cid,String workoutId);
	/**
	 * 获取用户完成的workout列表
	* @Title: completeWorkoutList  
	* @Description: 如果cid为null，不是忽略cid，
	* 而是就是要求cid为null，如果是null，主要是正对singles查询
	* @return List<UserWatchHistory>    返回类型  
	* @throws
	 */
	public List<UserWatchHistory> completeWorkoutList(String userId,String cid,String workoutId);
}
