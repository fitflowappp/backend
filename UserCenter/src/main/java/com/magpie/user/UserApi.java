package com.magpie.user;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.user.model.User;
import com.magpie.user.req.SimpleRegUser;

import io.swagger.annotations.ApiOperation;

import com.magpie.base.view.BaseView;

@RestController
@RequestMapping(value = "/user")
public class UserApi {
	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "用户注册")
	public BaseView<?> register(@RequestBody SimpleRegUser simpleRegUser) {
		User user = new User();
		BeanUtils.copyProperties(simpleRegUser, user);
		user.setPassword(DigestUtils.md5Hex(simpleRegUser.getPassword()));
		return userService.registerAnonymous(user);
	}

	/**
	 * 功能：用户登录<br>
	 * 
	 * @param user
	 *            phone:必需 <br>
	 *            password:必需
	 * @param request
	 * @return UserView
	 * @time 20160225
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "用户登录")
	public BaseView<?> login(@RequestBody SimpleRegUser simpleRegUser, HttpServletRequest request) {
		// RequestParam requestParam = BaseUtil.getRequestParam(request);
		// ClientCat client =
		// versionInfoService.guessClientType(requestParam.getUserAgent());
		// if (client == ClientCat.ANDROID || client == ClientCat.IPHONE) {
		// simpleRegUser.setClient(client.getName());
		// }
		return userService.login(simpleRegUser);
	}

}
