package com.magpie.yoga.app.api;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.query.PageQuery;
import com.magpie.base.view.BaseView;
import com.magpie.base.view.PageView;
import com.magpie.base.view.Result;
import com.magpie.session.ActiveUser;
import com.magpie.share.UserRef;
import com.magpie.yoga.def.UserWorkDef;
import com.magpie.yoga.model.UserSinglesLock;
import com.magpie.yoga.model.UserWatchHistory;
import com.magpie.yoga.model.Workout;
import com.magpie.yoga.service.WatchHistoryService;
import com.magpie.yoga.service.WorkoutService;
import com.magpie.yoga.view.SinglesWorkoutView;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/yoga/app/workout/single")
public class AppWorkoutApi {
	
	@Autowired
	private WorkoutService workoutService;
	@Autowired 
	private WatchHistoryService watchHistoryService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "single 列表")
	public PageView<SinglesWorkoutView> singleWorkout(@ActiveUser UserRef userRef,@ModelAttribute PageQuery pageQuery) {
		Page<Workout> singlesWorkoutPage=workoutService.singleWorkoutList(pageQuery,false);
		return createSinglesWorkoutWithUserLock(singlesWorkoutPage, userRef.getId());
	}
	private PageView<SinglesWorkoutView> createSinglesWorkoutWithUserLock(Page<Workout> page,String userId){
		PageView<SinglesWorkoutView> pageView = new PageView<>();
		logger.trace("start create PageView<SinglesWorkoutView> time:{}", System.currentTimeMillis());
		BeanUtils.copyProperties(page, pageView, "content");
		SinglesWorkoutView singlesWorkout=null;
		List<SinglesWorkoutView> singlesWorkoutViews=new ArrayList<>();
		List<UserSinglesLock> singlesLocks=workoutService.singleLockList(userId);
		int lockSize=singlesLocks==null?0:singlesLocks.size();
		
		UserSinglesLock userSinglesLock=null;
		List<UserWatchHistory> userWatchHistories=null;
		for (Workout workout : page.getContent()) {
			singlesWorkout = new SinglesWorkoutView();
			BeanUtils.copyProperties(workout, singlesWorkout);
			if(singlesWorkout.isSinglesLock()){
				 for (int i = 0; i < lockSize; i++) {
					 userSinglesLock=singlesLocks.get(i);
					 if(singlesWorkout.getId().equals(userSinglesLock.getSinglesId())){
						 singlesWorkout.setSinglesLock(false);
					 }
				 }
				logger.trace("start check user watch history time:{}", System.currentTimeMillis());

				 //如果完整观看过singles，自动解锁
				if(singlesWorkout.isSinglesLock()&&StringUtils.isEmpty(userId)==false){
					userWatchHistories=watchHistoryService.completeWorkoutList(userId, null, singlesWorkout.getId());
					if(userWatchHistories!=null&&userWatchHistories.size()>0){
						singlesWorkout.setSinglesLock(false);
					}
				}
				logger.trace("end check user watch history time:{}", System.currentTimeMillis());


			}
			if (StringUtils.isEmpty(singlesWorkout.getShareUrl())) {
				singlesWorkout.setShareUrl(UserWorkDef.SHARE_URL);
			}
			singlesWorkoutViews.add(singlesWorkout);
		}
		pageView.setContent(singlesWorkoutViews);
		logger.trace("end create PageView<SinglesWorkoutView> time:{}", System.currentTimeMillis());

		return pageView;
	}
	
	@RequestMapping(value = "/page/lock", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "未解锁single 列表")
	public PageView<SinglesWorkoutView> lockSingleWorkoutList(@ActiveUser UserRef userRef,@ModelAttribute PageQuery pageQuery) {
		Page<Workout> singlesWorkoutPage=workoutService.singleWorkoutList(pageQuery,true);
		PageView<SinglesWorkoutView> singlesWorkoutView=createSinglesWorkoutWithUserLock(singlesWorkoutPage, userRef.getId());
		//去除用户解锁的singles
		PageView<SinglesWorkoutView> pageView = new PageView<>();
		BeanUtils.copyProperties(singlesWorkoutView, pageView, "content");
		List<SinglesWorkoutView> singlesWorkoutViews=new ArrayList<>();
		for (SinglesWorkoutView workout : singlesWorkoutView.getContent()){
			if(workout.isSinglesLock()){
				singlesWorkoutViews.add(workout);
			}
		}
		pageView.setContent(singlesWorkoutViews);
		return pageView;
	}
	@RequestMapping(value = "/unlock/{singlesId}", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "解锁singles")
	public BaseView unlockSingleWorkout(@ActiveUser UserRef userRef,@PathVariable String singlesId) {
		if(workoutService.unLockSingle(userRef.getId(), singlesId)){
			return new BaseView(Result.SUCCESS);
		}
		
		return new BaseView(Result.FAILURE);
	}
}
