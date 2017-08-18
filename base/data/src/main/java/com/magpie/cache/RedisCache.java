package com.magpie.cache;

import java.util.concurrent.TimeUnit;

public interface RedisCache<T> {

	public T save(int key, T obj);

	public T save(String key, T obj);

	public T save(int key, T obj, long timeout, TimeUnit timeUnit);

	public T save(String key, T obj, long timeout, TimeUnit timeUnit);

	public void delete(int key);

	public void delete(String key);

	public T get(int key);

	public T get(String key);

	public boolean exist(int key);

	public boolean exist(String key);

	public String joinKey(String key);

	public String joinKey(int key);

	public String getPrefix();
}
