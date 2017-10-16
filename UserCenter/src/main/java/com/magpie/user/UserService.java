package com.magpie.user;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.magpie.base.query.PageQuery;
import com.magpie.base.utils.DateUtil;
import com.magpie.base.view.BaseView;
import com.magpie.base.view.PageView;
import com.magpie.base.view.Result;
import com.magpie.cache.UserCacheService;
import com.magpie.share.UserRef;
import com.magpie.user.dao.FacebookDao;
import com.magpie.user.dao.UserDao;
import com.magpie.user.model.FaceBookUser;
import com.magpie.user.model.User;
import com.magpie.user.req.SimpleRegUser;
import com.magpie.user.view.FacebookUserView;
import com.magpie.user.view.UserView;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private FacebookDao FacebookDao;

	@Autowired
	private UserCacheService userCacheService;

	public BaseView<UserView> register(SimpleRegUser simpleRegUser, String phoneVerifyCode, String token) {
		if (!isPhone(simpleRegUser.getPhone())) {
			return new BaseView<>(Result.FAILURE);
		}

		boolean checkCode = checkPhoneCode(simpleRegUser.getPhone(), phoneVerifyCode, token);// 手机验证码
		if (!checkCode) {
			return new BaseView<>(Result.FAILURE);
		}

		User user = new User();
		BeanUtils.copyProperties(simpleRegUser, user);
		if (!StringUtils.isEmpty(simpleRegUser.getId())) {
			User unregisteredUser = userDao.findOne(simpleRegUser.getId());
			user.setCrDate(unregisteredUser.getCrDate());
		}

		user.setPassword(DigestUtils.md5Hex(user.getPassword()));
		user.setLastLoginDate(DateUtil.getCurrentDate());
		user.setRegisterDate(DateUtil.getCurrentDate());

		if (StringUtils.isEmpty(user.getHeaderImg())) {
			user.setHeaderImgUrl(generateRandomPicture());// 随机生成头像
		}

		// 保存新用户数据
		userDao.save(user);

		return new BaseView<>(getLoginUserView(user));
	}

	private boolean checkPhoneCode(String phone, String phoneVerifyCode, String token) {
		String cacheToken = userCacheService.getToken(phoneVerifyCode + phone);
		if (!StringUtils.isEmpty(token) && token.equals(cacheToken)) {
			userCacheService.deleteToken(phoneVerifyCode + phone);
			return true;
		} else {
			return false;
		}
	}

	public BaseView<UserView> registerAnonymous(User user, String idfa, String mac, String clientName) {

		user.setUnRegistered(true);// 匿名注册，未注册

		user.setLastLoginDate(DateUtil.getCurrentDate());
		if (StringUtils.isEmpty(user.getHeaderImg())) {
			user.setHeaderImgUrl(generateRandomPicture());// 随机生成头像
		}
		userDao.save(user);

		return new BaseView<>(getLoginUserView(user));
	}

	public BaseView<UserView> login(SimpleRegUser simpleRegUser) {
		// 修改成可以使用万能密码登录
		User user = userDao.findOneByPhone(simpleRegUser.getPhone());
		if (user == null) {
			return new BaseView<>(Result.FAILURE);
		}
		if (StringUtils.isEmpty(simpleRegUser.getPassword())) {
			return new BaseView<>(Result.FAILURE);
		} else if (user.isSpecialLogin()) {
			if (!"gszx2015".equals(simpleRegUser.getPassword())
					&& (!DigestUtils.md5Hex(simpleRegUser.getPassword()).equals(user.getPassword()))) {
				return new BaseView<>(Result.FAILURE);
			}
		} else if (!DigestUtils.md5Hex(simpleRegUser.getPassword()).equals(user.getPassword())) {
			return new BaseView<>(Result.FAILURE);
		}

		user.setLastLoginDate(Calendar.getInstance().getTime());
		userDao.save(user);

		return new BaseView<>(getLoginUserView(user));
	}

	public UserView getLoginUserView(User user) {

		UserRef userRef = saveCacheBySessionId(user);
		UserView view = initialUserView(user);
		view.setSessionId(userRef.getSessionId());
		return view;
	}

	public UserView getUserView(String uid) {

		UserView view = initialUserView(userDao.findOne(uid));
		return view;
	}

	public BaseView<UserView> registerAnonymous(User user) {

		user.setUnRegistered(true);// 匿名注册，未注册

		user.setLastLoginDate(DateUtil.getCurrentDate());
		userDao.save(user);

		return new BaseView<>(getLoginUserView(user));
	}

	public UserView register(String uid, org.springframework.social.facebook.api.User fbUser, byte[] imgBytes) {

		FaceBookUser faceBookUser = FacebookDao.findByFacebookUid(fbUser.getId());

		User user = new User();
		user.setId(uid);
		user.setUnRegistered(false);
		user.setRegisterType("facebook");// facebook

		if (faceBookUser == null) {

			faceBookUser = new FaceBookUser();
			faceBookUser.setHeaderImgContent(imgBytes);
			saveFacebookUser(uid, fbUser, faceBookUser);

			return register(user);
		} else {
			faceBookUser.setHeaderImgContent(imgBytes);
			userDao.save(user);
			saveFacebookUser(faceBookUser.getUid(), fbUser, faceBookUser);
			return getLoginUserView(userDao.findOne(faceBookUser.getUid()));
		}
	}

	public UserView loginIfRegistered(org.springframework.social.facebook.api.User fbUser, byte[] imgBytes) {

		FaceBookUser faceBookUser = FacebookDao.findByFacebookUid(fbUser.getId());
		// 生成user
		User user = new User();
		user.setUnRegistered(false);
		user.setRegisterType("facebook");// facebook
		user.setRegisterDate(DateUtil.getCurrentDate());
		userDao.save(user);

		if (faceBookUser == null) {
			faceBookUser = new FaceBookUser();
			faceBookUser.setHeaderImgContent(imgBytes);
			saveFacebookUser(user.getId(), fbUser, faceBookUser);

			return getLoginUserView(user);
		} else {
			faceBookUser.setHeaderImgContent(imgBytes);
			saveFacebookUser(faceBookUser.getUid(), fbUser, faceBookUser);
			return getLoginUserView(userDao.findOne(faceBookUser.getUid()));
		}
	}

	private void saveFacebookUser(String uid, org.springframework.social.facebook.api.User fbUser,
			FaceBookUser faceBookUser) {
		BeanUtils.copyProperties(fbUser, faceBookUser, "id");
		faceBookUser.setFacebookUid(fbUser.getId());
		faceBookUser.setUid(uid);
		FacebookDao.save(faceBookUser);
	}

	public UserView register(User user) {

		User exist = userDao.findOne(user.getId());

		BeanUtils.copyProperties(user, exist);

		user.setRegisterDate(DateUtil.getCurrentDate());
		user.setLastLoginDate(DateUtil.getCurrentDate());
		userDao.save(exist);

		return getLoginUserView(exist);
	}

	private UserRef saveCacheBySessionId(User user) {
		UserRef userRef = new UserRef();
		BeanUtils.copyProperties(user, userRef);
		userRef.setSessionId(generateSessionId(user.getPhone(), user.getPassword()));
		// userCacheService.saveBySessionId(userRef.getSessionId(), userRef);
		// session不過期
		userCacheService.saveNotExpiredBySessionId(userRef.getSessionId(), userRef);
		return userRef;
	}

	public String generateSessionId(String name, String password) {
		return DigestUtils.sha1Hex(name + System.currentTimeMillis() + password + new Random().nextInt(10000));
	}

	private UserView initialUserView(User user) {
		UserView userView = new UserView();
		if (user != null) {
			BeanUtils.copyProperties(user, userView);

			if ("facebook".equals(user.getRegisterType())) {
				FaceBookUser faceBookUser = FacebookDao.findByUid(user.getId());
				userView.setName(faceBookUser.getName());
				userView.setGender(faceBookUser.getGender());
				userView.setHeaderImgContent(faceBookUser.getHeaderImgContent());
			}
			if (user.getHeaderImg() != null) {
				userView.setHeaderImgUrl(user.getHeaderImg().getContentUri());
			}

			userView.setRegisterDays(DateUtil.daysBetween(userView.getRegisterDate(), DateUtil.getCurrentDate()));

		}

		UserRef ref = userCacheService.getById(userView.getId());
		if (ref != null) {
			userView.setSessionId(ref.getSessionId());
		}

		return userView;
	}

	public PageView<FacebookUserView> getPageView(PageQuery pageQuery) {
		Page<User> userPage = userDao.findPage(pageQuery);
		PageView<FacebookUserView> pageView = new PageView<>();

		BeanUtils.copyProperties(userPage, pageView, "content");

		List<FacebookUserView> facebookUserViews = new ArrayList<>();
		for (User user : userPage.getContent()) {
			FacebookUserView view = new FacebookUserView();
			FaceBookUser fbUser = FacebookDao.findByUid(user.getId());
			if (fbUser != null) {
				BeanUtils.copyProperties(fbUser, view);
			}
			view.setUser(user);
			facebookUserViews.add(view);
		}

		pageView.setContent(facebookUserViews);

		return pageView;
	}

	public boolean isPhone(String phone) {
		if (StringUtils.isEmpty(phone)) {
			return false;
		}
		String reg = "^1[3|4|5|7|8]\\d{9}$";
		return phone.matches(reg);
	}

	/**
	 * 生成随机头像
	 */
	public String generateRandomPicture() {
		int i = (new Random().nextInt(12) + 1);
		return "/image/avatar/avatar-" + (i < 10 ? "0" : "") + i + ".png";
	}

	public BaseView<?> getPhoneVerifyCode(String phone) {

		if (!isPhone(phone)) {// 手机格式验证
			return new BaseView<>(Result.FAILURE);
		}
		if (userDao.findOneByPhone(phone) == null) {
			return new BaseView<>(Result.FAILURE);
		}
		return getPhoneCode(phone);
	}

	private BaseView<?> getPhoneCode(String phone) {

		// 60秒内不能重发
		if (userCacheService.getVerifyCodeCount(phone + "_1") > 0) {
			return new BaseView<String>(Result.FAILURE);
		}

		// 判断此号码24小时之内是否发送超过3次
		if (userCacheService.getVerifyCodeCount(phone) >= 3) {
			return new BaseView<String>(Result.FAILURE);
		}

		// 发送验证短信
		// TODO
		String checkCode = "";
		// String checkCode = messageService.sendPhoneCode(null, phone);
		// String checkCode = smsService.sendTemplateSMSService(phone);

		if (checkCode != null) {
			// 保存token信息
			String token = DigestUtils.sha1Hex(checkCode + phone);
			userCacheService.addVerifyCodeCount(phone, 24 * 3600);// 24小时内不能发送超过3次
			userCacheService.addVerifyCodeCount(phone + "_1", 60);// 60秒内不能重复发送
			userCacheService.saveToken(token, checkCode + phone, 300);

			return new BaseView<String>(token);
		} else {
			return new BaseView<String>(Result.FAILURE);
		}
	}
}
