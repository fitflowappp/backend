package com.magpie.yoga.statistics.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.query.PageQuery;
import com.magpie.yoga.statistics.service.WorkoutStatisticsService;
import com.magpie.yoga.view.WorkoutStatisticsView;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/manage/yoga/statistiscs/workout")
public class WorkoutStatisticsController {
	@Autowired
	WorkoutStatisticsService workoutStatisticsService;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get all workouts")
	public List<WorkoutStatisticsView> getWorkoutStatistics(@ModelAttribute PageQuery pageQuery) {
		return workoutStatisticsService.getWorkoutList(pageQuery);
	}
}
