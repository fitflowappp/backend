package com.magpie.user;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.magpie.base.view.BaseView;
import com.magpie.base.view.Result;
import com.magpie.session.ActiveUser;
import com.magpie.share.UserRef;
import com.magpie.user.constant.RoleType;
import com.magpie.user.model.User;
import com.magpie.user.req.FacebookReq;
import com.magpie.user.req.SimpleRegUser;
import com.magpie.user.view.UserView;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/user")
public class UserApi {
	@Autowired
	private UserService userService;
	@Autowired
	private Facebook facebook;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = "/login/anonymous", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "匿名登录")
	public BaseView<UserView> anonymousLogin(@ModelAttribute SimpleRegUser simpleRegUser) {
		User user = new User();
		BeanUtils.copyProperties(simpleRegUser, user);
		if ("admin".equals(user.getFrom())) {
			user.setRole(RoleType.ADMIN.getCode());
		}
		return userService.registerAnonymous(user);
	}

	/**
	 * 功能：用户登录<br>
	 * 
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "用户登录")
	public BaseView<UserView> login(@RequestBody SimpleRegUser simpleRegUser, HttpServletRequest request) {
		// RequestParam requestParam = BaseUtil.getRequestParam(request);
		// ClientCat client =
		// versionInfoService.guessClientType(requestParam.getUserAgent());
		// if (client == ClientCat.ANDROID || client == ClientCat.IPHONE) {
		// simpleRegUser.setClient(client.getName());
		// }
		return userService.login(simpleRegUser);
	}

	/**
	 * 功能：用户登录<br>
	 * 
	 */
	@RequestMapping(value = "/facebook/login", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "用户facebook登录")
	public BaseView<UserView> login(@RequestBody FacebookReq facebookReq, @ActiveUser UserRef userRef,
			HttpServletRequest request) {
		org.springframework.social.facebook.api.User fbUser = facebook.userOperations().getUserProfile();
		logger.info("get facebook user info: {}", JSON.toJSONString(fbUser));

		if (fbUser == null || StringUtils.isEmpty(fbUser.getId())) {
			return new BaseView<>(Result.FAILURE);
		}
		if (StringUtils.isEmpty(userRef.getId())) {
			// 无session，重新注册/登录
			return new BaseView<>(userService.loginIfRegistered(fbUser));
		} else {
			return new BaseView<>(userService.register(userRef.getId(), fbUser));
		}

	}

}
