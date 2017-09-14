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
import com.magpie.yoga.dao.RoutineDao;
import com.magpie.yoga.model.Routine;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/manage/yoga/routine")
public class RoutineController {

	@Autowired
	private RoutineDao routineDao;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get all routines", response = Routine.class, responseContainer = "List")
	public List<Routine> getRoutine() {
		return routineDao.findAll();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "add one new routine")
	public BaseView<Routine> addRoutine(@RequestBody Routine routine) {
		routineDao.save(routine);
		return new BaseView<Routine>(routine);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "edit one routine")
	public BaseView<Routine> editRoutine(@PathVariable String id, @RequestBody Routine routine) {
		routineDao.save(routine);
		return new BaseView<Routine>(routine);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value = "delete one routine")
	public BaseView<?> removeRoutine(@PathVariable String id) {
		routineDao.delete(id);
		return new BaseView<Routine>(Result.SUCCESS);
	}

}
