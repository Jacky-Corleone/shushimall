package com.camelot.openplatform.common.json;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2016年3月16日
 * @author  <a href="mailto: liuxiangping86@camelotchina.com">刘香平</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部 
 */
public class JSONUtils {
	
	/**
	 * <p>Discription:[把JSON文本parse为JSONObject或者JSONArray]</p>
	 * Created on 2016年3月18日
	 * @param text JSON文本
	 * @return
	 * @author:[刘香平]
	 */
	public static Object parse(String text){		
		return JSON.parse(text);
	}

	/**
	 * <p>Discription:[把JSON文本parse为JavaBean]</p>
	 * Created on 2016年3月18日
	 * @param text JSON文本
	 * @param clazz JavaBean 类
	 * @return
	 * @author:[刘香平]
	 */
	public static final <T> T parseObject(String text, Class<T> clazz) {
        return (T) JSON.parseObject(text, clazz);
    }

	/**
	 * <p>Discription:[把JSON文本parse为MAP类型对象]</p>
	 * Created on 2016年3月18日
	 * @param text JSON文本
	 * @return Map类型实体
	 * @author:[刘香平]
	 */
	@SuppressWarnings("unchecked")
    public static <V extends Object> Map<String, V> parseMap(String text) {
        Map<String, V> map = JSON.parseObject(text, Map.class);
        return map;
    }
	
	/**
	 * <p>Discription:[把JSON文本parse成JSONArray]</p>
	 * Created on 2016年3月18日
	 * @param text JSON文本
	 * @return JSONArray
	 * @author:[刘香平]
	 */
	public static JSONArray parseArray(String text){
		return JSON.parseArray(text);
	}

	/**
	 * <p>Discription:[把JSON文本parse成JavaBean集合 ]</p>
	 * Created on 2016年3月18日
	 * @param text JSON文本
	 * @param clazz JavaBean 类
	 * @return JavaBean List 集合
	 * @author:[刘香平]
	 */
	public static <T> List<T> parseArray(String text, Class<T> clazz) {
        return JSON.parseArray(text, clazz);
    }
	
	/**
	 * <p>Discription:[将JavaBean序列化为JSON文本 ]</p>
	 * Created on 2016年3月18日
	 * @param object JavaBean实体
	 * @return
	 * @author:[刘香平]
	 */
	public static String toJSONString(Object object){
		return JSON.toJSONString(object);
	}
	
	/**
	 * <p>Discription:[实体Bean转换成Map对象]</p>
	 * Created on 2016年3月25日
	 * @param object 实体Bean
	 * @return 实体转换的Map对象
	 * @author:[刘香平]
	 */
	@SuppressWarnings("unchecked")
	public static  <V extends Object> Map<String, V> toJSONMap(Object object){
		String text = JSON.toJSONString(object);
		return JSON.parseObject(text, Map.class);
	}
	
	/**
	 * <p>Discription:[将JavaBean序列化为带格式的JSON文本]</p>
	 * Created on 2016年3月18日
	 * @param object JavaBean实体
	 * @param prettyFormat 是否开启格式化
	 * @return JSON文本
	 * @author:[刘香平]
	 */
	public static String toJSONString(Object object, boolean prettyFormat){
		return JSON.toJSONString(object, prettyFormat);
	}
	
	/**
	 * <p>Discription:[将JavaBean转换为JSONObject或者JSONArray]</p>
	 * Created on 2016年3月18日
	 * @param object JavaBean实体
	 * @return JSONObject或者JSONArray
	 * @author:[刘香平]
	 */
	public static Object toJSON(Object object){
		return JSON.toJSON(object);
	}
	
	/**
	 * <p>Discription:[把JSON文本parse成JSONObject]</p>
	 * Created on 2016年3月18日
	 * @param text JSON文本
	 * @return JSONObject
	 * @author:[刘香平]
	 */
	public static JSONObject parseObject(String text){
		return JSON.parseObject(text);
	}    
    
    /**
     * <p>Discription:[JSON中日期类型的数据处理]</p>
     * Created on 2016年3月18日
     * @param obj 日期对象
     * @param dateFormat 转换的格式
     * @return JSON 文本
     * @author:[刘香平]
     */
    public static String toJSONStringWithDateFormat(Object obj, String dateFormat){
    	return JSON.toJSONStringWithDateFormat(obj,dateFormat,SerializerFeature.WriteDateUseDateFormat);
    }
}
