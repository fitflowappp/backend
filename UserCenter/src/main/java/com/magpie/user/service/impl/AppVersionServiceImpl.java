package com.magpie.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.magpie.user.service.AppVersionService;
import com.magpie.yoga.dao.AppUpdateDao;
import com.magpie.yoga.dao.AppUpdateSettingDao;
import com.magpie.yoga.dao.AppVersionDao;
import com.magpie.yoga.model.AppUpdate;
import com.magpie.yoga.model.AppUpdateSetting;
import com.magpie.yoga.model.AppVersion;
@Service
public class AppVersionServiceImpl implements AppVersionService{
	@Autowired
	AppVersionDao appVersionDao;
	@Autowired
	AppUpdateDao appUpdateDao;
	@Autowired 
	AppUpdateSettingDao appUpdateSettingDao;
	@Async
	@Override
	public void update(AppVersion appVersion) {
		// TODO Auto-generated method stub
		if(appVersion==null){
			return;
		}
		AppVersion savedAppVersion=appVersionDao.findOne(appVersion.getSystem(), appVersion.getVersion(), appVersion.getBuild());
		if(savedAppVersion==null){
			appVersionDao.save(appVersion);
		}
	}
	@Override
	public AppUpdate getAppUpdate(AppVersion appVersion) {
		// TODO Auto-generated method stub
		if(appVersion==null){
			return null;
		}
		
		return appUpdateDao.findOne(appVersion.getBuild(), appVersion.getSystem());
	}
	@Override
	public List<AppVersion> getAppVersion(int system) {
		// TODO Auto-generated method stub
		
		return appVersionDao.findList(system);
	}
	@Override
	public AppUpdateSetting getUpdateSetting(int system) {
		// TODO Auto-generated method stub
		return appUpdateSettingDao.findOne(system);
	}
	@Override
	public AppUpdateSetting updateSetting(AppUpdateSetting appUpdateSetting) {
		// TODO Auto-generated method stub
		if(appUpdateSetting==null){
			return null;
		}
		AppUpdateSetting saved=appUpdateSettingDao.findOne(appUpdateSetting.getSystem());
		if(saved!=null&&StringUtils.isEmpty(saved.getId())==false){
			appUpdateSettingDao.delete(saved);
		}
		return appUpdateSettingDao.save(appUpdateSetting);

	}
	
}
