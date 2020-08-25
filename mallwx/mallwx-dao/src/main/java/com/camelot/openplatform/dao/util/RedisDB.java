package com.camelot.openplatform.dao.util;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
/**
 * redis工具类，现在只有简单的set，get，del方法
 * @author jianping
 *
 */
@Component("redisDB")
public class RedisDB {
	@Resource
	private JedisPool jedisPool;
	
	private RedisDB() {
		//jedisPool= SpringApplicationContextHolder.getBean("jedisPool");
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(String key) {
		Jedis jedis = getJedis();
		boolean btn = jedis.exists(key);
		release(jedis);
		return btn;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) {
		Jedis jedis = getJedis();
		String value = jedis.get(key);
		release(jedis);
		return value;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		Jedis jedis = getJedis();
		jedis.set(key, value);
		release(jedis);
	}
	
	/**
	 * 添加对象
	 * @param key
	 * @param object
	 */
	public void addObject(String key,Object object){
		Jedis jedis = getJedis();
		jedis.set(key.getBytes(), SerializeUtil.serialize(object));
		release(jedis);
	}
	
	/**
	 * 带生命周期的对象
	 * @param key
	 * @param object
	 */
	public void addObject(String key,Object object,int seconds){
		Jedis jedis = getJedis();
		Pipeline p = jedis.pipelined();
		p.set(key.getBytes(), SerializeUtil.serialize(object));
		p.expire(key, seconds);
		p.sync();
		release(jedis);
	}
	
	/**
	 * 获取对象
	 * @param key
	 * @param object
	 */
	public Object getObject(String key){
		Jedis jedis = getJedis();
		byte[] dataBytes = jedis.get(key.getBytes());
		if(dataBytes==null){
			return null;
		}
		Object object = SerializeUtil.unserialize(dataBytes);
		release(jedis);
		return object;
	}
	
	/**
	 * 
	 * @param key
	 */
	public void del(String key) {
		Jedis jedis = getJedis();
		jedis.del(key);
		release(jedis);
	}

	/**
	 * 添加值，并且设置过期时间
	 * @param key
	 * @param value
	 * @param seconds
	 */
	public void setAndExpire(String key, String value, int seconds) {
		Jedis jedis = getJedis();
		Pipeline p = jedis.pipelined();
		p.set(key, value);
		p.expire(key, seconds);
		
		p.sync();
		release(jedis);
		
	}
	
	public void flushDB(){
		Jedis jedis = getJedis();
		jedis.flushAll();
		release(jedis);
	}
	/**
	 * 在redis消息队列队尾插入数据
	 * @param key
	 * @param value
	 */
	public void tailPush(String key,Object object){
		
		Jedis jedis = getJedis();
		jedis.rpush(key.getBytes(),  SerializeUtil.serialize(object));
		release(jedis);
	}
	/**
	 * 在redis消息队列对头插入数据
	 * @param key
	 * @param value
	 */
	public void headPush(String key,Object object){
		Jedis jedis = getJedis();
		jedis.lpush(key.getBytes(), SerializeUtil.serialize(object));
		release(jedis);
	}
	/**
	 * 在redis消息队列队尾删除数据
	 * @param key
	 */
	public Object tailPop(String key){
		Jedis jedis = getJedis();
		byte[] result = jedis.rpop(key.getBytes());
		release(jedis);
		if(StringUtils.isEmpty(result)){
			return null;
		}
		return SerializeUtil.unserialize(result);
	}
	/**
	 * 在redis消息队列队头删除数据
	 * @param key
	 */
	public Object headPop(String key){
		Jedis jedis = getJedis();
		byte[] result = jedis.lpop(key.getBytes());
		release(jedis);
		if(StringUtils.isEmpty(result)){
			return null;
		}
		return SerializeUtil.unserialize(result);
	}
	/**
	 * 存入redis的hash
	 * @param key hashID
	 * @param field  字段值
 	 * @param value  
	 */
	public void setHash(String key ,String field ,String value){
		Jedis jedis = getJedis();
		jedis.hset(key, field, value);
		release(jedis);
	}
	/**
	 * 根据key和字段值获取内容值
	 * @param key
	 * @param field
	 * @return value  内容
	 */
	public String getHash(String key ,String field){
		Jedis jedis = getJedis();
		String value = jedis.hget(key, field);
		release(jedis);
		return value;
	}
	/**
	 * 设置key的过期时间，endTime格式：yyyy-MM-dd hh:mm:ss
	 * @param key
	 * @param endTime
	 */
	public void setExpire(String key,Date endTime){
		Jedis jedis = getJedis();
		long seconds =endTime.getTime()-new Date().getTime();
		jedis.expire(key, (int) (seconds/1000));
		release(jedis);
	}
//	public void hmget() {
//		Jedis jedis = getJedis();
//		jedis.hm
//	}
	
	/**
	 * 释放redis客户端
	 * @param jedis
	 */
	private void release(Jedis jedis) {
		jedisPool.returnResource(jedis);
	}
	
	/**
	 * 得到一个jedis客户端
	 * @return
	 */
	private Jedis getJedis() {
		Jedis jedis = jedisPool.getResource();
		return jedis;
	}
	
}
