package com.magpie.getui.getui.cache;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class MsgClientCacheService {

	private String MSG_CLIENT_KEY_PREFIX = getClass().getName() + "getui:client:";// 个推数据

	@Resource(name = "redisTemplate")
	private HashOperations<String, String, String> hashOperations;
	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valueOperations;
	@Resource(name = "redisTemplate")
	private SetOperations<String, String> setOperations;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 根据 Redis key取得MsgClient数据
	 * 
	 * @param uid
	 * @return
	 */
	public MsgClient getByUid(String uid) {
		String key = MSG_CLIENT_KEY_PREFIX + uid;
		MsgClient client = new MsgClient();
		// cid
		client.setCid(hashOperations.get(key, "cid"));

		client.setUid(hashOperations.get(key, "uid"));
		client.setType(hashOperations.get(key, "type"));

		return client;
	}

	/**
	 * 保存MsgClient数据
	 * 
	 * @param client
	 */
	public void saveMsgClient(MsgClient client) {
		if (client == null) {
			return;
		}
		String key = MSG_CLIENT_KEY_PREFIX + client.getUid();

		HashMap<String, String> map = new HashMap<>();
		map.put("cid", client.getCid() == null ? "" : client.getCid());
		map.put("uid", client.getUid() == null ? "" : client.getUid());
		// 客户端类型
		map.put("type", client.getType() == null ? "" : client.getType());

		hashOperations.putAll(key, map);
	}

}
