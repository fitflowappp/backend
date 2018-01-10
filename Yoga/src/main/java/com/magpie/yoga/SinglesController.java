package com.magpie.yoga;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.view.BaseView;
import com.magpie.yoga.model.ChallengeSet;
import com.magpie.yoga.model.Workout;
import com.magpie.yoga.service.WorkoutService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/manage/yoga/singles")
public class SinglesController {
	@Autowired
	WorkoutService  workoutService;
	
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get order list")
	public BaseView<List<Workout>> orderList() {
		List<Workout> workoutList=workoutService.allSinglesList();
		return new BaseView<List<Workout>>(workoutList);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get one singles")
	public BaseView<Workout> getChallengeSet(@PathVariable String id) {
		return new BaseView<Workout>(workoutService.find(id));
	}
	
	@RequestMapping(value="addSingles/{id}",method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "add one new singles")
	public BaseView<List<Workout>> addSingles(@PathVariable String id) {
		logger.debug("add singles", id);
		if(id!=null&&id.length()>0){
			workoutService.addSinglesWorkout(id);
		}
		List<Workout> workoutList=workoutService.allSinglesList();
		return new BaseView<List<Workout>>(workoutList);
	}

	

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value = "delete one singles")
	public BaseView<?> removeChallengeSet(@PathVariable String id) {
		workoutService.deleteSinglesWorkout(id);
		List<Workout> workoutList=workoutService.allSinglesList();
		return new BaseView<List<Workout>>(workoutList);
	}
	@RequestMapping(value = "/{id}/sort/{sort}", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "update  singles sort")
	public BaseView<?> removeChallengeSet(@PathVariable String id,@PathVariable int sort) {
		workoutService.updateSinglesWorkoutSort(id, sort);
		List<Workout> workoutList=workoutService.allSinglesList();
		return new BaseView<List<Workout>>(workoutList);
	}
}
