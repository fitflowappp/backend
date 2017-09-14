package com.magpie.yoga;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.view.BaseView;
import com.magpie.base.view.Result;
import com.magpie.yoga.dao.WorkoutDao;
import com.magpie.yoga.model.Workout;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/manage/yoga/workout")
public class WorkoutController {

	@Autowired
	private WorkoutDao workoutDao;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get all workouts", response = Workout.class, responseContainer = "List")
	public List<Workout> getWorkout() {
		return workoutDao.findAll();
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "add one new Workout")
	public BaseView<Workout> addWorkout(@RequestBody Workout Workout) {
		workoutDao.save(Workout);
		return new BaseView<Workout>(Workout);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "edit one Workout")
	public BaseView<Workout> editWorkout(@PathVariable String id, @RequestBody Workout Workout) {
		workoutDao.save(Workout);
		return new BaseView<Workout>(Workout);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value = "delete one Workout")
	public BaseView<?> removeWorkout(@PathVariable String id) {
		workoutDao.delete(id);
		return new BaseView<Workout>(Result.SUCCESS);
	}

}
