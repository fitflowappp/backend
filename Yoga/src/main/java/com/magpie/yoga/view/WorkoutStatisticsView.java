package com.magpie.yoga.view;

import com.magpie.yoga.model.Workout;

public class WorkoutStatisticsView extends Workout {
	private int userFavCount;//用户正在收藏的个数
	private int userFavedCount;//用户收藏过的个数，包括已经删除
	public int getUserFavCount() {
		return userFavCount;
	}
	public void setUserFavCount(int userFavCount) {
		this.userFavCount = userFavCount;
	}
	public int getUserFavedCount() {
		return userFavedCount;
	}
	public void setUserFavedCount(int userFavedCount) {
		this.userFavedCount = userFavedCount;
	}
	
}
