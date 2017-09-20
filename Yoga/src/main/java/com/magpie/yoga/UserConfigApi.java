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
			repository = userConfiguration;
		} else {
			BeanUtils.copyProperties(userConfiguration, repository);
		}
		userConfigurationDao.save(repository);
		return new BaseView<>(repository);
	}

}