package com.magpie.user.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.query.PageQuery;
import com.magpie.base.utils.CsvUtils;
import com.magpie.base.view.BaseView;
import com.magpie.base.view.PageView;
import com.magpie.cache.UserCacheService;
import com.magpie.share.UserRef;
import com.magpie.statistics.user.view.UserStatisticsView;
import com.magpie.user.AdminService;
import com.magpie.user.UserService;
import com.magpie.user.constant.RoleType;
import com.magpie.user.dao.UserDao;
import com.magpie.user.model.User;
import com.magpie.user.model.UserBackgroundMusic;
import com.magpie.user.req.AdminLoginReq;
import com.magpie.user.service.UserConfigService;
import com.magpie.user.view.AdminView;
import com.magpie.user.view.FacebookUserView;
import com.magpie.yoga.model.UserConfiguration;
import com.magpie.yoga.model.UserState;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/manage/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserCacheService userCacheService;
	@Autowired
	private UserConfigService UserConfigService;

	@RequestMapping(method = RequestMethod.GET, value = "/csv")
	@ResponseBody
	@ApiOperation(value = "export csv")
	public void exportCsv(HttpServletResponse response) {

		StringBuffer sb = new StringBuffer();
		sb.append("ID").append(",").append("Facebook ID").append(",").append("Email Address").append(",")
				.append("Timestamp of App First Opened").append(",").append("Registered?").append(",")
				.append("Timestamp of Facebook Registration submitted").append(",")
				.append("Timestamp of Registration Completed").append(",").append("Current challenge ID").append(",")
				.append("Cumulative duration of videos watched").append(",").append("Number of completed Workouts")
				.append(",").append("Scheduling in-app notification on?").append(",")
				.append("Scheduling calendar reminder on?").append(",").append("Cumulative number of social shares")
				.append(",").append("Music on")
				.append(",").append("Music track")
				.append(",").append("\r\n");

		PageQuery pageQuery = new PageQuery();
		pageQuery.setSize(500000);
		PageView<FacebookUserView> pageView = userService.getPageView(pageQuery);
		User du = new User();
		UserState dus = new UserState();
		UserConfiguration duc = new UserConfiguration();
		for (FacebookUserView fu : pageView.getContent()) {
			User u = fu.getUser() == null ? du : fu.getUser();
			UserState us = fu.getUserState() == null ? dus : fu.getUserState();
			UserConfiguration uc = fu.getUserConfiguration() == null ? duc : fu.getUserConfiguration();
			UserBackgroundMusic ubm=UserConfigService.getBackgroundMusic(u.getId());
			sb.append(u.getId()).append(",").append(fu.getFacebookUid()).append(",").append(fu.getEmail()).append(",")
					.append(u.getCrDate()).append(",").append(u.isUnRegistered() ? "no" : "yes").append(",")
					.append(u.getFacebookRegistrationSumbmittedDate()).append(",").append(u.getRegisterDate())
					.append(",").append(us.getCurrentChallengeId()).append(",").append(us.getDuration()).append(",")
					.append(us.getCompletedWorkoutNum()).append(",").append(uc.isNotification() ? "on" : "off")
					.append(",").append(uc.isRemider() ? "on" : "off").append(",").append(fu.getShareCount())
					.append(",");
			if(ubm!=null){
				sb.append(ubm.getStatus()==0?"off":"on").append(",").append(""+(ubm.getMusicType()+1));
			}else{
				sb.append("").append(",").append("");

			}
			sb.append("\r\n");
		}
		CsvUtils.download("user.csv", sb.toString(), response);
	}

	/**
	 * 管理员登录
	 * 
	 * @param admin
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "用户登录", response = BaseView.class)
	public BaseView<AdminView> login(@RequestBody AdminLoginReq admin, HttpServletRequest request) {
		return adminService.login(admin.getName(), admin.getPassword());
	}

	/**
	 * 升级为测试管理用户
	 * 
	 * @return
	 */
	@RequestMapping(value = "/user/{uid}/super", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "升级为测试管理用户", response = BaseView.class)
	public BaseView<?> upgradeUser(@PathVariable String uid) {
		userDao.updateUserRole(uid, RoleType.ADMIN.getCode());
		UserRef userRef = userCacheService.getById(uid);
		userRef.setRole(RoleType.ADMIN.getCode());
		userCacheService.saveUser(userRef);
		return new BaseView<>();
	}

	/**
	 * 取消测试管理用户
	 * 
	 * @return
	 */
	@RequestMapping(value = "/user/{uid}/super", method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value = "取消测试管理用户", response = BaseView.class)
	public BaseView<?> cancelUpgradeUser(@PathVariable String uid) {
		userDao.updateUserRole(uid, RoleType.CUSTOMER.getCode());
		UserRef userRef = userCacheService.getById(uid);
		userRef.setRole(RoleType.CUSTOMER.getCode());
		userCacheService.saveUser(userRef);
		return new BaseView<>();
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取所有用户（有分页）")
	public PageView<UserStatisticsView> getUsersStatis(@ModelAttribute PageQuery pageQuery) {
		Page<User> userPage=userService.getUserPage(pageQuery);
		PageView<UserStatisticsView> pageView = new PageView<>();

		BeanUtils.copyProperties(userPage, pageView, "content");
		List<String> list=new ArrayList<>();
		for (User user : userPage.getContent()) {
			list.add(user.getId());
		}
		List<UserBackgroundMusic> userBackgroundMusics=UserConfigService.findList(list);
		List<UserStatisticsView> facebookUserViews = new ArrayList<>();
		UserStatisticsView userStatisticsView=null;
		int size=userBackgroundMusics.size();
		UserBackgroundMusic item=null;
		for (User user : userPage.getContent()) {
			userStatisticsView=new UserStatisticsView();
			BeanUtils.copyProperties(userService.getFacebookUserView(user),userStatisticsView);
			userStatisticsView.setMusicStatus(1);
			userStatisticsView.setMusicType(0);
			facebookUserViews.add(userStatisticsView);
			for(int i=0;i<size;i++){
				item=userBackgroundMusics.get(i);
				if(item.getUserId()!=null&&
						item.getUserId().equals(user.getId())){
					userStatisticsView.setMusicStatus(item.getStatus());
					userStatisticsView.setMusicType(item.getMusicType());
				}
			}
		}
		pageView.setContent(facebookUserViews);
		return pageView;
	}
	
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取用户详细信息")
	public FacebookUserView getUser(@PathVariable String id) {
		return userService.getFacebookUserView(id);
	}

}
