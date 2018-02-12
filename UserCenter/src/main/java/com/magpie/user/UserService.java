package com.magpie.user;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;

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
import com.magpie.user.dao.FindPassWordDao;
import com.magpie.user.dao.UserBackgroundMusicDao;
import com.magpie.user.dao.UserDao;
import com.magpie.user.model.FaceBookUser;
import com.magpie.user.model.FindPassWord;
import com.magpie.user.model.User;
import com.magpie.user.model.UserBackgroundMusic;
import com.magpie.user.req.SimpleRegUser;
import com.magpie.user.service.UserConfigService;
import com.magpie.user.utils.EmailUtils;
import com.magpie.user.view.AppUserView;
import com.magpie.user.view.FacebookUserView;
import com.magpie.user.view.UserView;
import com.magpie.yoga.constant.HistoryDest;
import com.magpie.yoga.constant.HistoryEvent;
import com.magpie.yoga.dao.ShareRecordDao;
import com.magpie.yoga.dao.UserConfigurationDao;
import com.magpie.yoga.dao.UserStateDao;
import com.magpie.yoga.dao.UserWatchHistoryDao;
import com.magpie.yoga.model.UserState;
import com.magpie.yoga.stat.UserWatchHistoryStat;

@Service
public class UserService {

	public static int MAIL_REGISTERED = 10008;
	public static int USER_ERROR_PASSWORD = MAIL_REGISTERED + 1;
	public static int MAIL_NO_REGISTER = USER_ERROR_PASSWORD + 1;

	@Autowired
	private UserDao userDao;

	@Autowired
	private FacebookDao FacebookDao;
	@Autowired
	private FindPassWordDao findPassWordDao;

	@Autowired
	private UserCacheService userCacheService;

	@Autowired
	private UserWatchHistoryDao userWatchHistoryDao;

	@Autowired
	private UserStateDao userStateDao;
	@Autowired
	private ShareRecordDao shareRecordDao;
	@Autowired
	private UserConfigurationDao userConfigurationDao;
	@Autowired
	UserBackgroundMusicDao userBackgroundMusicDao;
	
	
	public User findOne(String uid){
		if(uid!=null&&uid.length()>0){
			return userDao.findOne(uid);
		}
		return null;
	}

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

	public BaseView resetPasswordByMail(String key, String password) {
		FindPassWord findPassWord = findPassWordDao.findOne(key);
		if (findPassWord == null) {
			return new BaseView<>(new Result(Result.CODE_FAILURE,
					"The verification code is incorrect, please check your email again"));
		}
		if (findPassWord.getVaildDate().before(new Date())) {
			return new BaseView<>(new Result(Result.CODE_FAILURE,
					" The verification code has expired, please resend another email."));
		}

		userDao.updatePassword(findPassWord.getUserId(), password);
		User user = userDao.findOne(findPassWord.getUserId());
		BaseView baseView=new BaseView<>(getLoginUserView(user));
		baseView.setResult(new Result(Result.CODE_SUCCESS, "Your password has been successfully changed"));
		return baseView;

	}

	public BaseView registerByEmail(SimpleRegUser reguser,String userId) {
		User user = userDao.findOneByEmail(reguser.getEmail());
		if (user != null) {
			return new BaseView<>(new Result(MAIL_REGISTERED, "That email is already registered. Please sign in."));
		}
		if(userId!=null&&userId.length()>0){
			user=userDao.findOne(userId);
		}
		if(user==null)
			user = new User();
		user.setEmail(reguser.getEmail());
		user.setPassword(reguser.getPassword());
		user.setFrom(reguser.getFrom());
		user.setUnRegistered(false);
		user.setClient(reguser.getClient());

		user.setLastLoginDate(DateUtil.getCurrentDate());
		if (StringUtils.isEmpty(user.getHeaderImg())) {
			user.setHeaderImgUrl(generateRandomPicture());// 随机生成头像
		}
		user = userDao.save(user);
		//异步推送欢迎邮件 
		asyncSendRegisterWelcomeEmail(reguser.getEmail());
		return new BaseView<>(getLoginUserView(user));
	}

	public BaseView<UserView> login(SimpleRegUser simpleRegUser) {
		// 修改成可以使用万能密码登录
		User user = userDao.findOneByEmail(simpleRegUser.getEmail());
		if (user == null) {
			return new BaseView<>(new Result(MAIL_NO_REGISTER, "Email not recognised. Please try again or sign up."));
		}
		if (StringUtils.isEmpty(simpleRegUser.getPassword())) {
			return new BaseView<>(Result.FAILURE);
		} else if (simpleRegUser.getPassword().equals(user.getPassword()) == false) {
			return new BaseView<>(new Result(USER_ERROR_PASSWORD,
					"Your email & password do not match, please try again or reset your password."));
		}

		user.setLastLoginDate(Calendar.getInstance().getTime());
		userDao.save(user);
		

		return new BaseView<>(getLoginUserView(user));
	}

	public boolean changeChallenge(String userId, String challengeId) {
		if (userId == null || challengeId == null || userId.length() == 0 || challengeId.length() == 0) {
			return false;
		}
		userDao.updateChallenge(userId, challengeId);
		return true;
	}

	

	public String getUserIdBySessionId(String seesionId) {
		return null;
	}

	public BaseView<Result> sendFindPassWordEmail(String email) {
		User user = userDao.findOneByEmail(email);
		if (user == null) {
			return new BaseView<>(new Result(MAIL_NO_REGISTER, "Email not recognized. Please try again or sign up"));
		}

		// 产生私钥发送给客户端
		Random random = new Random();
		String randomKey = "" + (random.nextInt(9) + 1);
		for (int i = 0; i < 5; i++) {
			randomKey = randomKey + (random.nextInt(9));
		}
		// 保存私钥
		Date vaildDate = new Date();

		Calendar c = Calendar.getInstance();
		c.setTime(vaildDate);
		c.add(Calendar.MINUTE, 15);

		FindPassWord findPassWord = findPassWordDao.findOneByUserId(user.getId());
		if (findPassWord == null) {
			findPassWord = new FindPassWord();
			findPassWord.setPrivateKey(randomKey);
			findPassWord.setUserId(user.getId());

			findPassWord.setVaildDate(c.getTime());
			findPassWordDao.save(findPassWord);
		} else {
			findPassWord.setPrivateKey(randomKey);
			findPassWord.setVaildDate(c.getTime());

			findPassWordDao.update(user.getId(), randomKey, c.getTime());

		}
		// 发送邮件
		StringBuilder emailBody = new StringBuilder();

		emailBody.append("Dear Fitflow Member,<br/><br/>");
		emailBody.append(
				"You applied to reset your password through our app. If this was not you, please ignore this message.<br/><br/>");
		emailBody.append("Your verification code is: " + randomKey + ".<br/><br/>");
		emailBody.append(
				"Please enter this in the Reset Password page of the app to create a new password. This code is valid for 15 minutes.<br/><br/>");
		emailBody.append("Thank you for using Fitflow.<br/><br/>");
		emailBody.append("FItflow Team.<br/><br/>");
		emailBody.append(
				"P.S. This is an automated inbox which is not monitored. If you have any other questions, please contact help@fitflow.io.<br/><br/>");
		String messageBody = emailBody.toString();

		boolean result = EmailUtils.sendEmail(email, "reset_password@fitflow.io", randomKey + " is your Fitflow account recovery code",
				"Fitflow", messageBody,  "Yoga123!");
		if (result)
			return new BaseView<>(new Result(Result.CODE_SUCCESS, "Email sent, please check your inbox"));
		else {
			return new BaseView<>(Result.FAILURE);
		}
	}
	@Autowired
	Executor executor;
	private void asyncSendRegisterWelcomeEmail(final String email){
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				sendRegisterWelcomeEmail(email);
			}
		});
	}

	private void sendRegisterWelcomeEmail(String email) {
		
		StringBuilder emailBody = new StringBuilder();
		emailBody.append("Hi,<br/><br/>");
		emailBody.append("I'm Anya from Fitflow. I'm here to support you as you embark on your yoga journey. By signing up, you've taken your first step towards a happier, healthier life.<br/><br/>");
		emailBody.append(
				"- We have designed a number of 1-4 week challenges to help you achieve your goal, be it toning, relaxing, or relieving pain.<br/>");
		emailBody.append(
				"- Fitflow offers 60+ online yoga classes you can take anytime at home. Each class lasts for only 15-30 minutes.<br/>");
		emailBody.append("- All the yoga classes are free. If you like Fitflow, we would really appreciate if you could leave a positive review on the App Store.<br/><br/>");
		emailBody.append(
				"Reply to this email if you have any questions. I will get back to you asap.<br/><br/>");
		emailBody.append("Warm regards,<br/><br/>");
		emailBody.append("Anya<br/>");
		emailBody.append("Your Fitflow Coach<br/>");
		emailBody.append("International Yoga Alliance Certified lead teacher & a dedicated yoga practitioner for 14 years<br/><br/>");
		String messageBody = emailBody.toString();

		EmailUtils.sendEmail(email, "coach@fitflow.io", "Nice to meet you!","Anya from Fitflow", messageBody, "Yoga123!");
	}

	public UserView getLoginUserView(User user) {

		UserRef userRef = saveCacheBySessionId(user);
		UserView view = initialAppUserView(user);
		view.setSessionId(userRef.getSessionId());
		return view;
	}

	public UserView getUserView(String uid) {

		UserView view = initialUserView(userDao.findOne(uid));
		return view;
	}
	public AppUserView getAappUserView(String uid) {
		
		
		return initialAppUserView(userDao.findOne(uid));
	}

	public BaseView<UserView> registerAnonymous(User user) {

		user.setUnRegistered(true);// 匿名注册，未注册

		user.setLastLoginDate(DateUtil.getCurrentDate());
		userDao.save(user);

		return new BaseView<>(getLoginUserView(user));
	}

	public UserView register(String uid, org.springframework.social.facebook.api.User fbUser, byte[] imgBytes) {

		FaceBookUser faceBookUser = FacebookDao.findByFacebookUid(fbUser.getId());

		if (faceBookUser == null) {

			User exist = userDao.findOne(uid);
			exist.setUnRegistered(false);
			exist.setRegisterType("facebook");// facebook

			exist.setRegisterDate(DateUtil.getCurrentDate());
			exist.setLastLoginDate(DateUtil.getCurrentDate());
			userDao.save(exist);

			faceBookUser = new FaceBookUser();
			faceBookUser.setHeaderImgContent(imgBytes);
			saveFacebookUser(uid, fbUser, faceBookUser);
			//推送欢迎邮件
			asyncSendRegisterWelcomeEmail(fbUser.getEmail());
			return getLoginUserView(exist);
		} else {
			faceBookUser.setHeaderImgContent(imgBytes);
			saveFacebookUser(faceBookUser.getUid(), fbUser, faceBookUser);
			return getLoginUserView(userDao.findOne(faceBookUser.getUid()));
		}
	}

	public UserView loginIfRegistered(org.springframework.social.facebook.api.User fbUser, byte[] imgBytes) {

		FaceBookUser faceBookUser = FacebookDao.findByFacebookUid(fbUser.getId());

		if (faceBookUser == null) {
			// 生成user
			User user = new User();
			user.setUnRegistered(false);
			user.setRegisterType("facebook");// facebook
			user.setRegisterDate(DateUtil.getCurrentDate());
			userDao.save(user);

			faceBookUser = new FaceBookUser();
			faceBookUser.setHeaderImgContent(imgBytes);
			saveFacebookUser(user.getId(), fbUser, faceBookUser);
			//推送欢迎邮件
			asyncSendRegisterWelcomeEmail(fbUser.getEmail());

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

		exist.setRegisterDate(DateUtil.getCurrentDate());
		exist.setLastLoginDate(DateUtil.getCurrentDate());
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
	private AppUserView initialAppUserView(User user) {
		AppUserView userView = new AppUserView();
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
		UserBackgroundMusic userBackgroundMusic=userBackgroundMusicDao.findOneByUserId(user.getId());
		if(userBackgroundMusic!=null){
			userView.setMusicStatus(userBackgroundMusic.getStatus());
			userView.setMusicType(userBackgroundMusic.getMusicType());
			userView.setMusicVolume(userBackgroundMusic.getVolume());
		}else{
			userView.setMusicStatus(1);
			userView.setMusicType(0);
			userView.setMusicVolume(0.5f);
		}
		
		return userView;
	}

	public PageView<FacebookUserView> getPageView(PageQuery pageQuery) {
		Page<User> userPage = userDao.findPage(pageQuery);
		PageView<FacebookUserView> pageView = new PageView<>();

		BeanUtils.copyProperties(userPage, pageView, "content");

		List<FacebookUserView> facebookUserViews = new ArrayList<>();
		for (User user : userPage.getContent()) {
			FacebookUserView view = getFacebookUserView(user);
			facebookUserViews.add(view);
		}

		pageView.setContent(facebookUserViews);

		return pageView;
	}
	public Page<User> getUserPage(PageQuery pageQuery){
		return userDao.findPage(pageQuery);
	}

	public FacebookUserView getFacebookUserView(String uid) {
		return getFacebookUserView(userDao.findOne(uid));
	}

	public FacebookUserView getFacebookUserView(User user) {
		FacebookUserView view = new FacebookUserView();
		FaceBookUser fbUser = FacebookDao.findByUid(user.getId());
		if (fbUser != null) {
			BeanUtils.copyProperties(fbUser, view);
		}
		view.setUser(user);
		UserState userState = userStateDao.findUserState(user.getId());
		if (userState == null) {
			userState = new UserState();
		}
		int comChallengeNum = 0;
		int comWorkoutNum = 0;
		int routineDuration = 0;
		for (UserWatchHistoryStat stat : userWatchHistoryDao.aggregateCountPerUser(user.getId())) {
			if (stat.getDestType() == HistoryDest.CHALLENGE.getCode()
					&& stat.getEvent() == HistoryEvent.COMPLETE.getCode()) {
				comChallengeNum += stat.getCount();
			}
			if (stat.getDestType() == HistoryDest.WORKOUT.getCode()
					&& stat.getEvent() == HistoryEvent.COMPLETE.getCode()) {
				comWorkoutNum += stat.getCount();
			}

			if (stat.getEvent() == HistoryEvent.COMPLETE.getCode()
					&& stat.getDestType() == HistoryDest.ROUTINE.getCode()) {
				routineDuration += stat.getDuration();
			}
		}
		userState.setCompletedChallengeNum(comChallengeNum);
		userState.setCompletedWorkoutNum(comChallengeNum + comWorkoutNum);
		userState.setDuration(routineDuration);
		String email=user.getEmail();
		//补充facebook邮箱
		if((email==null||email.length()==0)&&fbUser!=null){
			email=fbUser.getEmail();
		}
		view.setEmail(email);
		view.setUserState(userState);
		view.setShareCount(shareRecordDao.count(user.getId()));

		view.setUserConfiguration(userConfigurationDao.findOneByUid(user.getId()));

		return view;
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
