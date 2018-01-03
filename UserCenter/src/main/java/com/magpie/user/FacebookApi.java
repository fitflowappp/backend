package com.magpie.user;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.ImageType;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
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
import com.magpie.user.req.FindPassWordReq;
import com.magpie.user.req.ResetPasswordReq;
import com.magpie.user.req.SimpleRegUser;
import com.magpie.user.utils.EmailUtils;
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
		String[] fields = { "id", "name", "birthday", "email", "location", "link", "cover", "hometown", "gender",
				"first_name", "last_name" };

		FacebookTemplate template = new FacebookTemplate(facebookReq.getToken());
		User fbUser = template.fetchObject("me", User.class, fields);

		logger.info("get facebook user info: {}", JSON.toJSONString(fbUser));

		if (fbUser == null || StringUtils.isEmpty(fbUser.getId())) {
			return new BaseView<>(Result.FAILURE);
		}
		byte[] imgBytes = template.userOperations().getUserProfileImage(fbUser.getId(), ImageType.LARGE);
		if (StringUtils.isEmpty(userRef.getId())) {
			// 无session，重新注册/登录
			return new BaseView<>(userService.loginIfRegistered(fbUser, imgBytes));
		} else {
			return new BaseView<>(userService.register(userRef.getId(), fbUser, imgBytes));
		}

	}
	/**
	 * 功能：邮箱账号登陆<br>
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST,value = "/email/login")
	@ResponseBody
	@ApiOperation(value = "邮箱登陆")
	public BaseView<UserView> mailLogin(@RequestBody SimpleRegUser simpleRegUser,HttpServletRequest request) {
		return userService.login(simpleRegUser);
	}
	/**
	 * 功能：邮箱注册<br>
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST,value = "/email/register")
	@ResponseBody
	@ApiOperation(value = "邮箱注册")
	public BaseView mailRegister(@RequestBody SimpleRegUser simpleRegUser,@ActiveUser UserRef userRef) {
		return userService.registerByEmail(simpleRegUser,userRef.getId());
	}
	/**
	 * 功能：通过私钥重设密码<br>
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST,value = "/email/findPassword/reset")
	@ResponseBody
	@ApiOperation(value = "私钥重设密码")
	public BaseView mailResetPassword(@RequestBody ResetPasswordReq req, HttpServletRequest request) {
		String key=req.getKey();
		String password=req.getPassword();
		if(key==null||password==null||key.length()==0||password.length()==0){
			return new BaseView<>(new Result(Result.CODE_FAILURE, "key or password is no"));
		}
		return userService.resetPasswordByMail(key, password);
	}
	/**
	 * 功能：利用邮箱找回密码<br>
	 * 
	 */
	@RequestMapping(method = RequestMethod.POST,value = "/email/findPassWord")
	@ResponseBody
	@ApiOperation(value = "邮箱找回密码")
	public BaseView<Result> mailfindPassWord(@RequestBody FindPassWordReq findPassWordReq, HttpServletRequest request) {
		if(EmailUtils.validEmail(findPassWordReq.getEmail())==false){
			return new BaseView<>(Result.FAILURE);
		}
		return userService.sendFindPassWordEmail(findPassWordReq.getEmail());
	}
	
	

}
