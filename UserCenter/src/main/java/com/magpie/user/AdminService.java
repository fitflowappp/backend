package com.magpie.user;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.magpie.base.utils.Codec;
import com.magpie.base.view.BaseView;
import com.magpie.base.view.Result;
import com.magpie.cache.AdminCacheService;
import com.magpie.share.UserRef;
import com.magpie.user.dao.AdminDao;
import com.magpie.user.model.Admin;
import com.magpie.user.view.AdminView;

@Service
public class AdminService {

	@Autowired
	private AdminDao adminDao;
	@Autowired
	private AdminCacheService adminCacheService;

	public BaseView<AdminView> login(String name, String password) {
		Admin exist = adminDao.findByNameAndPasswd(name, DigestUtils.md5Hex(password));
		if (exist == null) {
			return new BaseView<>(Result.FAILURE);
		} else {
			return new BaseView<>(getLoginUserView(exist));
		}
	}

	private AdminView getLoginUserView(Admin admin) {

		UserRef userRef = new UserRef();
		BeanUtils.copyProperties(admin, userRef);
		userRef.setSessionId(Codec.generateSessionId(admin.getName(), admin.getPassword()));
		adminCacheService.saveBySessionId(userRef.getSessionId(), userRef);

		AdminView adminView = new AdminView();
		BeanUtils.copyProperties(admin, adminView);
		adminView.setSessionId(userRef.getSessionId());
		return adminView;
	}

}
