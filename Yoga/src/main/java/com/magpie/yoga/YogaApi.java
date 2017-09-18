package com.magpie.yoga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.view.BaseView;
import com.magpie.session.ActiveUser;
import com.magpie.share.UserRef;
import com.magpie.yoga.constant.HistoryEvent;
import com.magpie.yoga.service.YogaService;
import com.magpie.yoga.view.ActView;
import com.magpie.yoga.view.ChallengeSetView;
import com.magpie.yoga.view.ChallengeView;
import com.magpie.yoga.view.WorkoutView;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value = "/yoga")
public class YogaApi {

	@Autowired
	private YogaService yogaService;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get primary challenge set of homepage", response = BaseView.class)
	public BaseView<ChallengeSetView> getChallengeSet(@ActiveUser UserRef userRef) {
		return new BaseView<ChallengeSetView>(yogaService.getDefaultChallengeSet(userRef.getId()));
	}

	@RequestMapping(value = "/challenge/mine", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get my previous challenge", response = BaseView.class)
	public BaseView<ChallengeView> getMyChallenge(@ApiIgnore @ActiveUser UserRef userRef) {
		return new BaseView<ChallengeView>(yogaService.getCurrentChallenge(userRef.getId()));
	}

	@RequestMapping(value = "/challenge/{cid}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get challenge", response = BaseView.class)
	public BaseView<ChallengeView> getChallenge(@ApiIgnore @ActiveUser UserRef userRef, @PathVariable String cid) {
		return new BaseView<ChallengeView>(yogaService.getChallenge(userRef.getId(), cid));
	}

	@RequestMapping(value = "/challenge/{cid}/workout/{wid}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get workout", response = BaseView.class)
	public BaseView<WorkoutView> getWorkout(@ApiIgnore @ActiveUser UserRef userRef, @PathVariable String cid,
			@PathVariable String wid) {
		WorkoutView workoutView = yogaService.getWorkout(userRef.getId(), wid);
		if (workoutView != null) {
			workoutView.setChallengeId(cid);
		}
		return new BaseView<WorkoutView>(workoutView);
	}

	@RequestMapping(value = "/challenge/{cid}/workout/{wid}/routine/{rid}/start", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "start watching routine", response = BaseView.class)
	public BaseView<ActView> startWatching(@ApiIgnore @ActiveUser UserRef userRef, @PathVariable String cid,
			@PathVariable String wid, @PathVariable String rid) {

		return new BaseView<>(
				yogaService.watchingRoutine(userRef.getId(), cid, wid, rid, HistoryEvent.START.getCode(), 0));
	}

	@RequestMapping(value = "/challenge/{cid}/workout/{wid}/routine/{rid}/skip", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "skip watching routine", response = BaseView.class)
	public BaseView<ActView> skipWatching(@ApiIgnore @ActiveUser UserRef userRef, @PathVariable String cid,
			@PathVariable String wid, @PathVariable String rid) {
		return new BaseView<>(
				yogaService.watchingRoutine(userRef.getId(), cid, wid, rid, HistoryEvent.SKIPPED.getCode(), 0));
	}

	@RequestMapping(value = "/challenge/{cid}/workout/{wid}/routine/{rid}/stop/{seconds}", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "skip watching routine", response = BaseView.class)
	public BaseView<ActView> skipWatching(@ApiIgnore @ActiveUser UserRef userRef, @PathVariable String cid,
			@PathVariable String wid, @PathVariable String rid, @PathVariable int seconds) {
		return new BaseView<>(
				yogaService.watchingRoutine(userRef.getId(), cid, wid, rid, HistoryEvent.SKIPPED.getCode(), seconds));
	}

	@RequestMapping(value = "/challenge/{cid}/workout/{wid}/routine/{rid}/complete", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "complete watching routine", response = BaseView.class)
	public BaseView<ActView> completeWatching(@ApiIgnore @ActiveUser UserRef userRef, @PathVariable String cid,
			@PathVariable String wid, @PathVariable String rid) {
		return new BaseView<>(
				yogaService.watchingRoutine(userRef.getId(), cid, wid, rid, HistoryEvent.COMPLETE.getCode(), 0));
	}

}
