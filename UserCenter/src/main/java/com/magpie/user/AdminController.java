package com.magpie.user;

import javax.servlet.http.HttpServletRequest;

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
import com.magpie.base.view.PageView;
import com.magpie.user.constant.RoleType;
import com.magpie.user.dao.UserDao;
import com.magpie.user.req.AdminLoginReq;
import com.magpie.user.view.AdminView;
import com.magpie.user.view.FacebookUserView;

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
		return new BaseView<>();
	}

	@RequestMapping(value = "/user", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取所有用户（有分页）")
	public PageView<FacebookUserView> getUsers(@ModelAttribute PageQuery pageQuery) {
		return userService.getPageView(pageQuery);
	}

	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取用户详细信息")
	public FacebookUserView getUser(@PathVariable String id) {
		return userService.getFacebookUserView(id);
	}

}
