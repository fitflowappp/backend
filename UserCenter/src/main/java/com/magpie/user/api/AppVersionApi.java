package com.magpie.user.api;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.view.BaseView;
import com.magpie.base.view.Result;
import com.magpie.session.ActiveUser;
import com.magpie.share.UserRef;
import com.magpie.user.service.AppVersionService;
import com.magpie.user.view.AppUpdateSettingView;
import com.magpie.yoga.model.AppUpdate;
import com.magpie.yoga.model.AppUpdateSetting;
import com.magpie.yoga.model.AppVersion;

import io.swagger.annotations.ApiOperation;

@RestController
@RefreshScope
@RequestMapping(value = "/user/version")
public class AppVersionApi {
	public static int NO_APP_UPDATE = 20008;

	@Autowired
	AppVersionService appVersionService;
	
	@Value("${app_no_update}")
	String app_no_update_desc;

	@Value("${normal_app_download_url}")
	String normal_app_download_url;
	
	@Value("${force_app_download_url}")
	String force_app_download_url;
	
	
	
	@RequestMapping(value="/update",method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "版本更新")
	public BaseView versionUpdate(@ActiveUser UserRef userRef,@RequestBody AppVersion appVersion) {
		appVersionService.update(appVersion);
		AppUpdateSetting appupdateSettting=appVersionService.getUpdateSetting(appVersion.getSystem());
		if(appupdateSettting!=null){
			AppUpdateSettingView appUpdateSettingView=new AppUpdateSettingView();
			if(appupdateSettting.getForce()!=null&&(appVersion.getBuild()<=appupdateSettting.getForce().getBuild())){
				BeanUtils.copyProperties(appupdateSettting.getForce(), appUpdateSettingView);
				appUpdateSettingView.setType(AppUpdateSetting.FORCE_UPDATE);
			}else if(appupdateSettting.getUpdate()!=null&&(appVersion.getBuild()<=appupdateSettting.getUpdate().getBuild())){
				BeanUtils.copyProperties(appupdateSettting.getUpdate(), appUpdateSettingView);
			}
			if(appUpdateSettingView.getBuild()>0){
				if(appUpdateSettingView.getType()==AppUpdateSettingView.FORCE){
					appUpdateSettingView.setDownloadUrl(force_app_download_url);
				}else{
					appUpdateSettingView.setDownloadUrl(normal_app_download_url);
				}
				return new BaseView<>(appUpdateSettingView);
			}
		}
		return new BaseView<>(new Result(NO_APP_UPDATE, app_no_update_desc));
	}
	
	@RequestMapping(value="/{system}",method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "用户版本列表")
	public List<AppVersion> appVersionList(@ActiveUser UserRef userRef, @PathVariable int system) {
		return appVersionService.getAppVersion(system);
	}
	@RequestMapping(value="/backgroud/{system}",method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "获取更新配置内容")
	public HashMap<String,Object> getUpdateSetting(@ActiveUser UserRef userRef, @PathVariable int system) {
		HashMap<String,Object> hashMap=new HashMap<>();
		hashMap.put("versions", appVersionService.getAppVersion(system));
		hashMap.put("setting", appVersionService.getUpdateSetting(system));
		return hashMap;
	}
	@RequestMapping(value="/backgroud/set",method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation(value = "更新更新配置内容")
	public BaseView addUpdateSetting(@RequestBody AppUpdateSetting appUpdateSetting) {
		
		if(appUpdateSetting.getForce()!=null&&appUpdateSetting.getForce().getBuild()<=0){
			appUpdateSetting.setForce(null);
		}
		if(appUpdateSetting.getUpdate()!=null&&appUpdateSetting.getUpdate().getBuild()<=0){
			appUpdateSetting.setUpdate(null);
		}
		if(appUpdateSetting.getUpdate()!=null&&appUpdateSetting.getForce()!=null&&appUpdateSetting.getForce().getBuild()>=appUpdateSetting.getUpdate().getBuild()){
			return new BaseView<>(Result.FAILURE);
		}
		
		return new BaseView(appVersionService.updateSetting(appUpdateSetting));
	}
	

}
