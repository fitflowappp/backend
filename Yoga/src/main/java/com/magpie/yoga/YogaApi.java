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
import com.magpie.yoga.dao.ShareRecordDao;
import com.magpie.yoga.model.ShareRecord;
import com.magpie.yoga.service.YogaService;
import com.magpie.yoga.view.Achievement;
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

	@Autowired
	private ShareRecordDao shareRecordDao;

	@RequestMapping(value = "/share", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "share yoga to facebook")
	public void share(@ActiveUser UserRef userRef) {

		ShareRecord shareRecord = new ShareRecord();
		shareRecord.setUid(userRef.getId());
		shareRecordDao.save(shareRecord);
	}

	@RequestMapping(value = "/my/achievements", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get my achievements")
	public BaseView<Achievement> getMyAchievements(@ActiveUser UserRef userRef) {
		return new BaseView<Achievement>(yogaService.getUserAchievments(userRef.getId()));
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get primary challenge set of homepage")
	public BaseView<ChallengeSetView> getChallengeSet(@ActiveUser UserRef userRef) {
		return new BaseView<ChallengeSetView>(yogaService.getDefaultChallengeSet(userRef));
	}

	@RequestMapping(value = "/challenge/mine", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get my previous challenge")
	public BaseView<ChallengeView> getMyChallenge(@ApiIgnore @ActiveUser UserRef userRef) {
		return new BaseView<ChallengeView>(yogaService.getCurrentChallenge(userRef));
	}

	@RequestMapping(value = "/challenge/{cid}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get challenge")
	public BaseView<ChallengeView> getChallenge(@ApiIgnore @ActiveUser UserRef userRef, @PathVariable String cid) {
		return new BaseView<ChallengeView>(yogaService.getChallenge(userRef, cid));
	}

	@RequestMapping(value = "/challenge/{cid}/change", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "unlock challenge")
	public BaseView<ChallengeView> changeChallenge(@ApiIgnore @ActiveUser UserRef userRef, @PathVariable String cid) {
		return new BaseView<ChallengeView>(yogaService.chooseChallenge(userRef, cid));
	}

	@RequestMapping(value = "/challenge/{cid}/workout/{wid}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "get workout")
	public BaseView<WorkoutView> getWorkout(@ApiIgnore @ActiveUser UserRef userRef, @PathVariable String cid,
			@PathVariable String wid) {
		WorkoutView workoutView = yogaService.getWorkout(userRef, cid, wid);
		return new BaseView<WorkoutView>(workoutView);
	}

	@RequestMapping(value = "/challenge/{cid}/workout/{wid}/routine/{rid}/start", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "start watching routine")
	public BaseView<ActView> startWatching(@ApiIgnore @ActiveUser UserRef userRef, @PathVariable String cid,
			@PathVariable String wid, @PathVariable String rid) {

		return new BaseView<>(
				yogaService.watchingRoutine(userRef.getId(), cid, wid, rid, HistoryEvent.START.getCode(), 0));
	}

	@RequestMapping(value = "/challenge/{cid}/workout/{wid}/routine/{rid}/skip", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "skip watching routine")
	public BaseView<ActView> skipWatching(@ApiIgnore @ActiveUser UserRef userRef, @PathVariable String cid,
			@PathVariable String wid, @PathVariable String rid) {
		return new BaseView<>(
				yogaService.watchingRoutine(userRef.getId(), cid, wid, rid, HistoryEvent.SKIPPED.getCode(), 0));
	}

	@RequestMapping(value = "/challenge/{cid}/workout/{wid}/skip", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "skip workout")
	public BaseView<ActView> skipWatching(@ApiIgnore @ActiveUser UserRef userRef, @PathVariable String cid,
			@PathVariable String wid) {
		return new BaseView<>(yogaService.skipWorkout(userRef.getId(), cid, wid));
	}

	@RequestMapping(value = "/challenge/{cid}/workout/{wid}/routine/{rid}/stop/{seconds}", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "skip watching routine")
	public BaseView<ActView> skipWatching(@ApiIgnore @ActiveUser UserRef userRef, @PathVariable String cid,
			@PathVariable String wid, @PathVariable String rid, @PathVariable int seconds) {
		return new BaseView<>(
				yogaService.watchingRoutine(userRef.getId(), cid, wid, rid, HistoryEvent.STOP.getCode(), seconds));
	}

	@RequestMapping(value = "/challenge/{cid}/workout/{wid}/routine/{rid}/complete", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "complete watching routine")
	public BaseView<ActView> completeWatching(@ApiIgnore @ActiveUser UserRef userRef, @PathVariable String cid,
			@PathVariable String wid, @PathVariable String rid) {
		return new BaseView<>(
				yogaService.watchingRoutine(userRef.getId(), cid, wid, rid, HistoryEvent.COMPLETE.getCode(), 0));
	}

}
