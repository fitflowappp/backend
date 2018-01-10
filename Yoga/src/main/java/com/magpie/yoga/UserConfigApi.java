package com.magpie.yoga;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.magpie.base.view.BaseView;
import com.magpie.session.ActiveUser;
import com.magpie.share.UserRef;
import com.magpie.yoga.dao.UserConfigurationDao;
import com.magpie.yoga.model.UserConfiguration;

@RestController
@RequestMapping(value = "/yoga/config")
public class UserConfigApi {

	@Autowired
	private UserConfigurationDao userConfigurationDao;

	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	public BaseView<UserConfiguration> setConfig(@ActiveUser UserRef userRef,
			@RequestBody UserConfiguration userConfiguration) {
		userConfiguration.setUid(userRef.getId());
		UserConfiguration repository = userConfigurationDao.findOneByUid(userRef.getId());
		if (repository == null) {
			repository = new UserConfiguration();
		}
		BeanUtils.copyProperties(userConfiguration, repository, "id");

		userConfigurationDao.save(repository);
		return new BaseView<>(repository);
	}
	@RequestMapping(value="/notification",method = RequestMethod.PUT)
	@ResponseBody
	public BaseView<UserConfiguration> updateNotification(@ActiveUser UserRef userRef,
			@RequestBody UserConfiguration userConfiguration) {
		UserConfiguration repository = userConfigurationDao.findOneByUid(userRef.getId());
		if (repository == null) {
			repository = new UserConfiguration();
		}
		repository.setNotification(userConfiguration.isNotification());
		repository.setUid(userRef.getId());
		
		userConfigurationDao.save(repository);
		return new BaseView<>(repository);
	}
	@RequestMapping(value="/remider",method = RequestMethod.PUT)
	@ResponseBody
	public BaseView<UserConfiguration> updateRemider(@ActiveUser UserRef userRef,
			@RequestBody UserConfiguration userConfiguration) {
		userConfiguration.setUid(userRef.getId());

		UserConfiguration repository = userConfigurationDao.findOneByUid(userRef.getId());
		if (repository == null) {
			repository = new UserConfiguration();
		}
		BeanUtils.copyProperties(userConfiguration, repository, "id","notification");

		
		userConfigurationDao.save(repository);
		return new BaseView<>(repository);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public BaseView<UserConfiguration> getConfig(@ActiveUser UserRef userRef) {

		UserConfiguration repository = userConfigurationDao.findOneByUid(userRef.getId());
		if (repository == null) {
			repository = new UserConfiguration();
		}
		return new BaseView<>(repository);
	}

}
