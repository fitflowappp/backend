package com.magpie.user.service;

import java.util.List;

import com.magpie.yoga.model.AppUpdate;
import com.magpie.yoga.model.AppUpdateSetting;
import com.magpie.yoga.model.AppVersion;

public interface AppVersionService {
	public void update(AppVersion appVersion);
	public AppUpdate getAppUpdate(AppVersion appVersion);
	public List<AppVersion> getAppVersion(int system);
	public AppUpdateSetting getUpdateSetting(int system);
	public AppUpdateSetting updateSetting(AppUpdateSetting system);
}
