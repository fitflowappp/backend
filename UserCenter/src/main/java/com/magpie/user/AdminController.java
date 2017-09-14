package com.magpie.user;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.view.BaseView;
import com.magpie.user.req.AdminLoginReq;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/manage/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

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
	public BaseView<?> login(@RequestBody AdminLoginReq admin, HttpServletRequest request) {
		return adminService.login(admin.getName(), admin.getPassword());
	}

}
