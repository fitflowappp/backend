package com.magpie.user.service;

import java.util.List;

import com.magpie.user.model.UserBackgroundMusic;

public interface UserConfigService {
	
	public UserBackgroundMusic updateBackgroundMusic(UserBackgroundMusic userBackgroundMusic);
	public List<UserBackgroundMusic> findList(List<String> userIdList);
	public UserBackgroundMusic getBackgroundMusic(String userId);
	
	
	
}
