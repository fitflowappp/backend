package com.magpie.yoga.app.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.view.BaseView;
import com.magpie.base.view.Result;
import com.magpie.session.ActiveUser;
import com.magpie.share.UserRef;
import com.magpie.yoga.constant.HistoryEvent;
import com.magpie.yoga.dao.ShareRecordDao;
import com.magpie.yoga.def.UserWorkDef;
import com.magpie.yoga.model.ShareRecord;
import com.magpie.yoga.model.UserWatchHistory;
import com.magpie.yoga.model.UserWorkout;
import com.magpie.yoga.model.Workout;
import com.magpie.yoga.service.WatchHistoryService;
import com.magpie.yoga.service.WorkoutService;
import com.magpie.yoga.service.YogaService;
import com.magpie.yoga.view.Achievement;
import com.magpie.yoga.view.ActView;
import com.magpie.yoga.view.ChallengeSetView;
import com.magpie.yoga.view.ChallengeView;
import com.magpie.yoga.view.SinglesWorkoutView;
import com.magpie.yoga.view.UserWorkoutStat;
import com.magpie.yoga.view.WorkoutView;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RefreshScope
@RestController
@RequestMapping(value = "/yoga")
public class YogaApi {

	@Autowired
	private YogaService yogaService;

	@Autowired
	private ShareRecordDao shareRecordDao;
	@Autowired
	private WorkoutService workoutService;
	@Autowired
	private WatchHistoryService watchHistoryService;
	
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = "/share", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "share yoga to facebook")
	public void share(@ActiveUser UserRef userRef) {

		ShareRecord shareRecord = new ShareRecord();
		shareRecord.setUid(userRef.getId());
		shareRecordDao.save(shareRecord);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/challenge/getTopical")
	@ResponseBody
	@ApiOperation(value = "get all topical")
	public List<Map<String, Object>> getTopical() {

		List<Map<String, Object>> topicals = new ArrayList<>();
		HashMap<String, Object> item = null;
		ArrayList<String> titles = UserWorkDef.topicTileList;
		ArrayList<String> challenges = UserWorkDef.topicChallengeList;
		for (int i = 0; ((i < titles.size()) && i < challenges.size()); i++) {
			item = new HashMap<>();
			item.put("title", titles.get(i));
			item.put("challengeId", challenges.get(i));
			topicals.add(item);
		}

		return topicals;
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
		if (workoutView != null) {
			if ((StringUtils.isEmpty(workoutView.getShareUrl()))) {
				workoutView.setShareUrl(UserWorkDef.SHARE_URL);
			}
			if (workoutView.isSinglesLock()) {
				logger.debug("try to unlock in user status");
				if (workoutService.unlockStatus(userRef.getId(), workoutView.getId())) {
					logger.debug("unlock in user status");

					workoutView.setSinglesLock(false);// 用户已经解锁，屏蔽不需要解锁
				}
				
				//如果完整观看过singles，自动解锁
				if(workoutView.isSinglesLock()){
					List<UserWatchHistory> userWatchHistories=watchHistoryService.completeWorkoutList(userRef.getId(), null, workoutView.getId());
					if(userWatchHistories!=null&&userWatchHistories.size()>0){
						workoutView.setSinglesLock(false);
					}
				}
				
			}
		}
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

	@RequestMapping(method = RequestMethod.POST, value = "/challenge/workout/status")
	@ResponseBody
	@ApiOperation(value = "用户自身使用统计")
	public BaseView<UserWorkoutStat> userWorkoutData(@ActiveUser UserRef userRef) {

		return new BaseView<>(yogaService.getUserWorkoutStat(userRef.getId()));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/challenge/myworkout/list")
	@ResponseBody
	@ApiOperation(value = "获取用户所有workout")
	public BaseView<List<Workout>> userWorkoutlist(@ActiveUser UserRef userRef) {
		List<Workout> workoutsList = yogaService.getUserWorkoutList(userRef.getId());
		if (workoutsList == null || workoutsList.size() <= 0) {
			List<UserWorkout> allUserWorkouts = yogaService.getUserAllWorkoutList(userRef.getId());
			if (allUserWorkouts == null || allUserWorkouts.size() <= 0) {// 检测是否添加过，用户删除了singles
				workoutsList = yogaService.defaultUserWorkout(userRef.getId());
			}
		}
		for (Workout workout : workoutsList) {
			if (StringUtils.isEmpty(workout.getShareUrl())) {
				workout.setShareUrl(UserWorkDef.SHARE_URL);
			}
		}
		return new BaseView<>(workoutsList);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/challenge/myworkout/{wid}/delete")
	@ResponseBody
	@ApiOperation(value = "删除用户workout")
	public BaseView<Result> deleteUserWorkout(@ActiveUser UserRef userRef, @PathVariable String wid) {

		if (yogaService.deleteUserWorkout(userRef.getId(), wid)) {
			return new BaseView<>(Result.SUCCESS);
		}
		return new BaseView<>(Result.FAILURE);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/challenge/myworkout/add/{wid}")
	@ResponseBody
	@ApiOperation(value = "用户选择workout")
	public BaseView<Result> addUserWorkout(@ActiveUser UserRef userRef, @PathVariable String wid) {
		if (yogaService.addWorkout(userRef.getId(), wid)) {
			return new BaseView(new Result(Result.CODE_SUCCESS, "You have added this single to your homepage."));
		}
		return new BaseView<>(Result.FAILURE);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/challenge/single/workout")
	@ResponseBody
	@ApiOperation(value = "single workout list")
	public BaseView<List<SinglesWorkoutView>> singleWorkout(@ActiveUser UserRef userRef) {
		return new BaseView<>(yogaService.singleWorkoutList(userRef.getId()));
	}

}
