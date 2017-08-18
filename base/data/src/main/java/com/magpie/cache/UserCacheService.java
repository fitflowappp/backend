package com.magpie.cache;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.magpie.user.model.UserRef;

@Service
public class UserCacheService {

	private final String LOGIN_KEY_PREFIX = getClass().getName() + ":login:";//
	private final String USER_KEY_PREFIX = getClass().getName() + ":user:";//
	private final String ACTIVATE_KEY_PREFIX = getClass().getName() + ":activate:";// activate
	private final String FILE_KEY_PREFIX_STRING = getClass().getName() + ":file:";
	private final String USER_NAME_KEY = getClass().getName() + ":visitor:";
	private final String USER_SESSIONKEY_PREFIX = getClass().getName() + ":session:";
	private final String USER_INDAYVARIFY_PREFIX = getClass().getName() + ":indayvarify:";

	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valueOperations;
	@Resource(name = "redisTemplate")
	private SetOperations<String, String> setOperations;
	@Autowired
	private StringRedisTemplate redisTemplate;

	/**
	 * 根据用户Id取得用户信息 Redis key:"user:xxxxxx"
	 * 
	 * @param id
	 * @return
	 */
	public UserRef getById(String uid) {
		String key = USER_KEY_PREFIX + uid;
		return JSON.parseObject(valueOperations.get(key), UserRef.class);
	}


	/**
	 * 从Redis中取得登陆用户信息 Redis key:"login:xxxxxx"
	 * 
	 * @param sessionId
	 * @return
	 */
	public UserRef getBySessionId(String sessionId) {
		String key = LOGIN_KEY_PREFIX + sessionId;
		String idKey = valueOperations.get(key);
		if (idKey != null) {
			return JSON.parseObject(valueOperations.get(key), UserRef.class);
		} else {
			return null;
		}
	}

	public void removeById(String uid) {
		String key = USER_KEY_PREFIX + uid;
		valueOperations.set(key, null);
	}

	/**
	 * 清除所有用户认证信息
	 * 
	 * @param id
	 *            用户id
	 */
	public void removeSessionById(String uid) {
		String sessionKey = USER_SESSIONKEY_PREFIX + uid;
		String loginKey = valueOperations.get(sessionKey);

		valueOperations.getOperations().delete(sessionKey);
		if (!StringUtils.isEmpty(loginKey)) {
			valueOperations.getOperations().delete(loginKey);
		}
	}

	/**
	 * 保存用户登陆信息到Redis ,Redis key: "login:xxxxx"
	 * 
	 * @param sessionId
	 * @param user
	 */
	public void saveBySessionId(String sessionId, UserRef user) {
		// "bwm:user:login:sessionIdxxxxxxx":"bwm:user:uidxxxxxxxx"
		String key = LOGIN_KEY_PREFIX + sessionId;
		String idKey = USER_KEY_PREFIX + user.getId();
		valueOperations.set(key, idKey);
		redisTemplate.expire(key, 30, TimeUnit.DAYS);

		// "bwm:user:session:uidxxxxxxxx":"bwm:user:login:sessionIdxxxxxxx"
		String sessionKey = USER_SESSIONKEY_PREFIX + user.getId();
		valueOperations.set(sessionKey, key);
		redisTemplate.expire(sessionKey, 30, TimeUnit.DAYS);
		this.saveUser(user);
	}

	/**
	 * 保存用户登陆信息到Redis ,Redis key: "login:xxxxx"
	 * 
	 * @param sessionId
	 * @param user
	 */
	public void saveNotExpiredBySessionId(String sessionId, UserRef user) {
		// "bwm:user:login:sessionIdxxxxxxx":"bwm:user:uidxxxxxxxx"
		String key = LOGIN_KEY_PREFIX + sessionId;
		String idKey = USER_KEY_PREFIX + user.getId();
		valueOperations.set(key, idKey);

		// "bwm:user:session:uidxxxxxxxx":"bwm:user:login:sessionIdxxxxxxx"
		String sessionKey = USER_SESSIONKEY_PREFIX + user.getId();
		valueOperations.set(sessionKey, key);
		this.saveUser(user);
	}

	/**
	 * 保存用户基本信息到Redis，key:"user:xxxxxx"
	 * 
	 * @param user
	 */
	public void saveUser(UserRef user) {
		if (user == null || user.getId() == null) {
			return;
		}
		String key = USER_KEY_PREFIX + user.getId();

		valueOperations.set(key, JSON.toJSONString(user));
		redisTemplate.expire(key, 30, TimeUnit.DAYS);
	}

	/**
	 * 保存邮箱验证消息到Redis
	 */
	public void saveEmailCheckInfoByUserId(String emailSessionId, String userId) {
		String key = ACTIVATE_KEY_PREFIX + userId;
		valueOperations.set(key, emailSessionId);
		redisTemplate.expire(key, 2, TimeUnit.DAYS);
	}

	/**
	 * 
	 */
	public String getEmailCheckInfoByUserId(String id) {
		String key = ACTIVATE_KEY_PREFIX + id;
		return valueOperations.get(key);
	}

	/**
	 * 
	 */
	public void deleteEmailCheckInfoByUserId(String id) {
		String key = ACTIVATE_KEY_PREFIX + id;
		valueOperations.getOperations().delete(key);
	}

	public void saveToken(String token, String tokenKey, int time) {
		String key = FILE_KEY_PREFIX_STRING + tokenKey;
		valueOperations.set(key, token);
		redisTemplate.expire(key, time, TimeUnit.SECONDS);
	}

	// 保存没有时间限制的token
	public void saveLongTimeToken(String token, String tokenKey) {
		String key = FILE_KEY_PREFIX_STRING + tokenKey;
		valueOperations.set(key, token);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1);
		redisTemplate.expireAt(key, calendar.getTime());
	}

	public String getToken(String tokenKey) {
		String key = FILE_KEY_PREFIX_STRING + tokenKey;
		return valueOperations.get(key);
	}

	public void deleteToken(String tokenKey) {
		String key = FILE_KEY_PREFIX_STRING + tokenKey;
		valueOperations.getOperations().delete(key);
	}

	public void addVerifyCodeCount(String phone, int time) {
		String key = ACTIVATE_KEY_PREFIX + phone;
		if (StringUtils.isEmpty(valueOperations.get(key))) {
			valueOperations.increment(key, 1);
			redisTemplate.expire(key, time, TimeUnit.SECONDS);
		} else {
			valueOperations.increment(key, 1);
		}
	}

	public int getVerifyCodeCount(String phone) {
		String key = ACTIVATE_KEY_PREFIX + phone;
		String value = valueOperations.get(key);
		if (StringUtils.isEmpty(value)) {
			return 0;
		} else {
			return Integer.parseInt(value);
		}
	}

	// 存游客用户的用户名
	public void saveVisitorNameCode(String name) {
		String key = USER_NAME_KEY;
		valueOperations.set(key, name);
	}

	public long incVisitorNameCode() {
		String key = USER_NAME_KEY;
		if (valueOperations.get(key) == null) {
			return -1l;
		}
		return valueOperations.increment(key, 1);
	}

	/**
	 * 增加 当天验证次数
	 * 
	 */
	public void addInDayVarifyCount(String uid, String code) {
		String key = USER_INDAYVARIFY_PREFIX + code + uid;
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		valueOperations.increment(key, 1);
		redisTemplate.expireAt(key, calendar.getTime());
	}

	/**
	 * 获取当天之内的验证次数
	 * 
	 * @param uid
	 * @param code
	 * @return
	 */
	public int countInDayVarify(String uid, String code) {
		String key = USER_INDAYVARIFY_PREFIX + code + uid;
		String value = valueOperations.get(key);
		if (StringUtils.isEmpty(value)) {
			return 0;
		} else {
			return Integer.parseInt(value);
		}
	}

	/**
	 * 增加 当天发送短信的次数
	 * 
	 */
	public void addInDaySmsCount(String phone) {
		String key = USER_INDAYVARIFY_PREFIX + phone;
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		valueOperations.increment(key, 1);
		redisTemplate.expireAt(key, calendar.getTime());
	}

	/**
	 * 获取当天之内的发送短信次数
	 * 
	 * @param uid
	 * @param code
	 * @return
	 */
	public int countInDaySms(String phone) {
		String key = USER_INDAYVARIFY_PREFIX + phone;
		String value = valueOperations.get(key);
		if (StringUtils.isEmpty(value)) {
			return 0;
		} else {
			return Integer.parseInt(value);
		}
	}

}
