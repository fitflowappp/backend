package com.magpie.cache;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.magpie.share.UserRef;

/**
 * login prefix:login:sessionId uid<br>
 * session prefix:session:uid sessionId<br>
 * admin prefix:uid user<br>
 * 
 * 以前： <br>
 * login bwm:admin:login:sessionId bwm:admin:uid<br>
 * session bwm:admin:session:uid bwm:admin:sessionId<br>
 * admin bwm:admin:uid user
 * 
 * 
 * @author msdj0
 * 
 */
@Service
public class AdminCacheService {

	private final String LOGIN_KEY_PREFIX = getClass().getName() + ":login:";//
	private final String USER_KEY_PREFIX = getClass().getName() + ":user:";//
	private final String USER_SESSIONKEY_PREFIX = getClass().getName() + ":session:";//

	// private final String USER_COUNT_PREFIX = getClass().getName() + ":uc:";//
	// 新注册用户数
	// private final String RESERVE_USER_COUNT_PREFIX = getClass().getName() +
	// ":reserveuc:";// 新预约用户数
	// private final String ADMINS_PREFIX = getClass().getName() + ":ids";//
	// 管理员ids

	@Resource(name = "stringRedisTemplate")
	private ValueOperations<String, String> valueOperations;
	@Resource(name = "stringRedisTemplate")
	private ListOperations<String, String> listOperations;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 根据用户Id取得用户信息 Redis key:"user:xxxxxx"
	 * 
	 * @param id
	 * @return
	 */
	public UserRef getById(String uid) {
		String key = USER_KEY_PREFIX + uid;
		return getByUserKey(key);
	}

	private UserRef getByUserKey(String key) {
		return JSON.parseObject(valueOperations.get(key), UserRef.class);
	}

	// 从Redis中取得登陆用户信息 Redis key:"login:xxxxxx"
	public UserRef getBySessionId(String sessionId) {
		String key = LOGIN_KEY_PREFIX + sessionId;
		String idKey = valueOperations.get(key);
		if (idKey != null) {
			return getByUserKey(idKey);
		} else {
			return null;
		}
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
		stringRedisTemplate.expire(key, 30, TimeUnit.DAYS);

		// "bwm:user:session:uidxxxxxxxx":"bwm:user:login:sessionIdxxxxxxx"
		String sessionKey = USER_SESSIONKEY_PREFIX + user.getId();
		valueOperations.set(sessionKey, key);
		stringRedisTemplate.expire(sessionKey, 30, TimeUnit.DAYS);
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
		stringRedisTemplate.expire(key, 30, TimeUnit.DAYS);
	}

}
