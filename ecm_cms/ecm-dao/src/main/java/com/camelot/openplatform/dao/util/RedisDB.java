package com.camelot.openplatform.dao.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
/**
 * <p>Description: [redis工具]</p>
 * Created on 2015-03-26
 * @author  <a href="mailto: zhouzhijun@camelotchina.com">周志军</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Component("redisDB")
public class RedisDB {
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	
	private RedisDB() {
		//jedisPool= SpringApplicationContextHolder.getBean("jedisPool");
	}
	
	/**
     * <p>Discription:[根据key查询是否在redis中存在]</p>
     * Created on 2015-03-26
     * @param key
     * @return
     * @author:[周志军]
     */
	public boolean exists(String key){
		return redisTemplate.hasKey(key);
	}
	
	/**
     * <p>Discription:[根据key查询redis中的value]</p>
     * Created on 2015-03-26
     * @param key
	 * @return key对应的value
     * @author:[周志军]
     */
	public String get(String key){
		String value = (String) redisTemplate.opsForValue().get(key);
		return value;
	}
	
	/**
     * <p>Discription:[根据key将value存入Redis]</p>
     * Created on 2015-03-26
     * @param key
     * @param value
     * @author:[周志军]
     */
	public void set(String key, Object value){
		redisTemplate.opsForValue().set(key, value);
	}
	
	/**
     * <p>Discription:[添加对象]</p>
     * Created on 2015-03-26
     * @param key
     * @param object
     * @author:[周志军]
     */
	public void addObject(String key,Object object){
		redisTemplate.opsForValue().set(key, object);
	}
	
	/**
     * <p>Discription:[带生命周期的对象]</p>
     * Created on 2015-03-26
     * @param key
     * @param object
     * @author:[周志军]
     */
	public void addObject(String key,Object object,int seconds){
		redisTemplate.opsForValue().set(key, object, seconds, TimeUnit.SECONDS);
	}
	
	/**
     * <p>Discription:[获取对象]</p>
     * Created on 2015-03-26
     * @param key
     * @return object
     * @author:[周志军]
     */
	public Object getObject(String key){
		return redisTemplate.opsForValue().get(key);
	}
	
	/**
     * <p>Discription:[删除对象]</p>
     * Created on 2015-03-26
     * @param key
     * @author:[周志军]
     */
	public void del(String key){
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}
	
	/**
     * <p>Discription:[根据key和字段删除value]</p>
     * Created on 2015-03-26
     * @param key
	 * @param field
     * @author:[周志军]
     */
	public void delHash(String key ,String... field){
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
     * <p>Discription:[添加值，并且设置过期时间]</p>
     * Created on 2015-03-26
     * @param key
	 * @param value
	 * @param seconds
     * @author:[周志军]
     */
	public void setAndExpire(String key, Object value, int seconds){
		redisTemplate.opsForValue().set(key, value);
		redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
	}
	/**
     * <p>Discription:[立即写入]</p>
     * Created on 2015-03-26
     * @author:[周志军]
     */
	public void flushDB(){
		redisTemplate.getConnectionFactory().getConnection().flushDb();
	}
	/**
     * <p>Discription:[在redis消息队列队尾插入数据]</p>
     * Created on 2015-03-26
     * @param key
	 * @param value
     * @author:[周志军]
     */
	public void tailPush(String key,Object object){
		redisTemplate.opsForList().rightPush(key, object);
	}
	/**
     * <p>Discription:[在redis消息队列对头插入数据]</p>
     * Created on 2015-03-26
     * @param key
	 * @param value
     * @author:[周志军]
     */
	public void headPush(String key,Object object){
		redisTemplate.opsForList().leftPush(key, object);
	}
	/**
     * <p>Discription:[在redis消息队列队尾删除数据]</p>
     * Created on 2015-03-26
     * @param key
     * @return object
     * @author:[周志军]
     */
	public Object tailPop(String key){
		return redisTemplate.opsForList().rightPop(key);
	}
	/**
     * <p>Discription:[在redis消息队列队头删除数据]</p>
     * Created on 2015-03-26
     * @param key
     * @return object
     * @author:[周志军]
     */
	public Object headPop(String key){
		return redisTemplate.opsForList().leftPop(key);
	}
	/**
     * <p>Discription:[存入redis的hash]</p>
     * Created on 2015-03-26
     * @param key hashID
	 * @param field  字段值
 	 * @param value 
     * @author:[周志军]
     */
	public void setHash(String key ,String field ,String value){
		redisTemplate.opsForHash().put(key, field, value);
	}
	/**
	 * <p>Discription:[存入redis的hash并设置过期时间]</p>
	 * Created on 2015-03-26
	 * @param key hashID
	 * @param field  字段值
	 * @param value 
	 * @param seconds 过期时间
	 * @author:[周志军]
	 */
	public void setHash(String key, String field, String value, int seconds) {
		redisTemplate.opsForHash().put(key, field, value);
		redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
	}
	/**
     * <p>Discription:[根据key和字段值获取内容值]</p>
     * Created on 2015-03-26
     * @param key
	 * @param field
	 * @return value  内容
     * @author:[周志军]
     */
	public String getHash(String key ,String field){
		return (String)redisTemplate.opsForHash().get(key, field);
	}
	/**
	 * <p>Discription:[根据key查询hash中的所有结果集]</p>
	 * Created on 2015-03-26
	 * @param key
	 * @param field
	 * @author:[周志军]
	 */
	public Map<String, String> getHashByKey(String key) {
		Map<String, String> result = new HashMap<String, String>();
		Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
		for (Entry<Object, Object> entry : map.entrySet()) {
			result.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
		}
		return result;
	}
	/**
     * <p>Discription:[设置key的过期时间，endTime格式：yyyy-MM-dd hh:mm:ss]</p>
     * Created on 2015-03-26
     * @param key
	 * @param endTime
     * @author:[周志军]
     */
	public void setExpire(String key,Date endTime){
		long seconds = endTime.getTime() - new Date().getTime();
		redisTemplate.expire(key, (int) (seconds / 1000), TimeUnit.SECONDS);
	}

}
