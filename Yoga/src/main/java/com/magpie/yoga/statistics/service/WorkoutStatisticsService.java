package com.magpie.yoga.statistics.service;

import java.util.List;

import com.magpie.base.query.PageQuery;
import com.magpie.yoga.view.WorkoutStatisticsView;

public interface WorkoutStatisticsService {
	 public List<WorkoutStatisticsView> getWorkoutList(PageQuery pageQuery);
}
