package com.magpie.user;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.magpie.base.view.BaseView;
import com.magpie.base.view.Result;
import com.magpie.session.ActiveUser;
import com.magpie.share.UserRef;
import com.magpie.user.req.FacebookReq;
import com.magpie.user.view.UserView;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/user")
public class FacebookApi {

	@Autowired
	private UserService userService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private Facebook facebook;
	private ConnectionRepository connectionRepository;

	public FacebookApi(Facebook facebook, ConnectionRepository connectionRepository) {
		this.facebook = facebook;
		this.connectionRepository = connectionRepository;
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
		String[] fields = { "id", "name", "birthday", "email", "location", "cover", "hometown", "gender", "first_name",
				"last_name" };
		User fbUser = facebook.fetchObject("me", User.class, fields);
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
