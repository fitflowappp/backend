package com.magpie.user.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.view.BaseView;
import com.magpie.session.ActiveUser;
import com.magpie.share.UserRef;
import com.magpie.user.model.UserBackgroundMusic;
import com.magpie.user.service.UserConfigService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/user/")
public class UserConfigApi {
	@Autowired
	UserConfigService userConfigService;
	
	@RequestMapping(value="/background/music",method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "更新背景音乐配置")
	public BaseView<UserBackgroundMusic> updateBackgroundMusic(@ActiveUser UserRef userRef,
			@RequestBody UserBackgroundMusic userBackgroundMusic) {
		userBackgroundMusic.setUserId(userRef.getId());
		return new BaseView<>(userConfigService.updateBackgroundMusic(userBackgroundMusic));
	}
	@RequestMapping(value="/background/music/get",method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "获取背景音乐配置")
	public BaseView<UserBackgroundMusic> getMusic(@ActiveUser UserRef userRef) {
		return new BaseView<>(userConfigService.getBackgroundMusic(userRef.getId()));
	}
	
	
	
}