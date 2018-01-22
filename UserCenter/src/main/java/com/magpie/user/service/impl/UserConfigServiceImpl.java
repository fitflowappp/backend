package com.magpie.user.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.magpie.user.dao.UserBackgroundMusicDao;
import com.magpie.user.model.UserBackgroundMusic;
import com.magpie.user.service.UserConfigService;


@Service
public class UserConfigServiceImpl implements UserConfigService  {
	@Autowired
	UserBackgroundMusicDao userBackgroundMusicDao;
	
	@Override
	public UserBackgroundMusic updateBackgroundMusic(UserBackgroundMusic userBackgroundMusic){
		if(StringUtils.isEmpty(userBackgroundMusic.getUserId())){
			return null;
		}
		UserBackgroundMusic saved=userBackgroundMusicDao.findOneByUserId(userBackgroundMusic.getUserId());
		if(saved==null){
			return userBackgroundMusicDao.save(userBackgroundMusic);
			
		}else{
			BeanUtils.copyProperties(userBackgroundMusic, saved, "id");
			userBackgroundMusicDao.update(saved);
			return saved;
		}
	}
	@Override
	public List<UserBackgroundMusic> findList(List<String> userIdList) {
		// TODO Auto-generated method stub
		return userBackgroundMusicDao.findList(userIdList);
	}
	@Override
	public UserBackgroundMusic getBackgroundMusic(String userId) {
		// TODO Auto-generated method stub
		if(StringUtils.isEmpty(userId)){
			return null;
		}
		return userBackgroundMusicDao.findOneByUserId(userId);
		
	}
	
	
}
