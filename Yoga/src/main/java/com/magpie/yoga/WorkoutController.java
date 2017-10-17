package com.magpie.yoga;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.query.PageQuery;
import com.magpie.base.view.BaseView;
import com.magpie.base.view.Result;
import com.magpie.cache.yoga.YogaCacheService;
import com.magpie.yoga.dao.WorkoutDao;
import com.magpie.yoga.model.Workout;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/manage/yoga/workout")
public class WorkoutController {

	@Autowired
	private WorkoutDao workoutDao;
	@Autowired
	private YogaCacheService yogaCacheService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get all workouts", response = Workout.class, responseContainer = "List")
	public List<Workout> getWorkout(@ModelAttribute PageQuery pageQuery) {
		return workoutDao.findBySort(pageQuery);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "add one new Workout")
	public BaseView<Workout> addWorkout(@RequestBody Workout workout) {
		workoutDao.save(workout);
		yogaCacheService.setWorkout(workout);
		return new BaseView<Workout>(workout);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get one Workout")
	public BaseView<Workout> getWorkout(@PathVariable String id) {
		return new BaseView<Workout>(workoutDao.findOne(id));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "edit one Workout")
	public BaseView<Workout> editWorkout(@PathVariable String id, @RequestBody Workout workout) {
		workout.setId(id);
		workoutDao.save(workout);
		yogaCacheService.setWorkout(workout);
		return new BaseView<Workout>(workout);
	}

	@RequestMapping(value = "/{id}/copy/{title}", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "edit one Workout")
	public BaseView<Workout> copyWorkout(@PathVariable String id, @PathVariable String title) {
		Workout copyWorkout = workoutDao.findOne(id);
		copyWorkout.setId(null);
		copyWorkout.setTitle(title);
		workoutDao.save(copyWorkout);
		yogaCacheService.setWorkout(copyWorkout);
		return new BaseView<Workout>(copyWorkout);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value = "delete one Workout")
	public BaseView<?> removeWorkout(@PathVariable String id) {
		workoutDao.delete(id);
		yogaCacheService.delWorkout(id);
		return new BaseView<Workout>(Result.SUCCESS);
	}

}
