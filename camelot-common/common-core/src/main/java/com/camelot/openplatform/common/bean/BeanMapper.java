package com.camelot.openplatform.common.bean;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.dozer.DozerBeanMapper;

import com.camelot.openplatform.common.json.JSONUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/** 
 * <p>Description: [实现深度转换Bean<->Bean的Mapper实现]</p>
 * Created on 2016年3月25日
 * @author  <a href="mailto: liuxiangping86@camelotchina.com">刘香平</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部 
 */
public class BeanMapper {

	/**
	 * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
	 */
	private static DozerBeanMapper dozer = new DozerBeanMapper();
	
	/**
	 * <p>Discription:[复制已有实体Bean实例生成指定Class的实例]</p>
	 * Created on 2016年3月25日
	 * @param source 复制源实体Bean
	 * @param destinationClass 需要生成的实体Bean类
	 * @return 生成的实体Bean实例
	 * @author:[刘香平]
	 */
	public static <T> T map(Object source, Class<T> destinationClass) {
		return dozer.map(source, destinationClass);
	}

	/**
	 * <p>Discription:[复制已有实体Bean实例集合生成指定Class的实例集合]</p>
	 * Created on 2016年3月25日
	 * @param sourceList 复制源实体Bean集合
	 * @param destinationClass 需要生成的实体Bean类集合
	 * @return 生成的实体Bean实例集合
	 * @author:[刘香平]
	 */
	@SuppressWarnings("rawtypes")
	public static <T> List<T> mapList(Collection sourceList, Class<T> destinationClass) {
		List<T> destinationList = Lists.newArrayList();
		for (Object sourceObject : sourceList) {
			T destinationObject = dozer.map(sourceObject, destinationClass);
			destinationList.add(destinationObject);
		}
		return destinationList;
	}

	/**
	 * <p>Discription:[将对象A的值拷贝到对象B中]</p>
	 * Created on 2016年3月25日
	 * @param source 源对象
	 * @param destinationObject 指定对象
	 * @author:[刘香平]
	 */
	public static void copy(Object source, Object destinationObject) {
		dozer.map(source, destinationObject);
	}
	
	/**
	 * <p>Discription:[将对象A的值拷贝到对象B中,且将A对象中的多处的变量以Map对象的形式返回]</p>
	 * Created on 2016年3月25日
	 * @param source 源对象
	 * @param destinationObject 指定对象
	 * @return 差异变量Map对象
	 * @author:[刘香平]
	 */
	public static Map<String, Object> copyAndGetDiff(Object source, Object destinationObject) {
		BeanMapper.copy(source, destinationObject);
		Map<String, Object> sourceMap = JSONUtils.toJSONMap(source);
		Map<String, Object> destinationMap = JSONUtils.toJSONMap(destinationObject);
		Map<String, Object> differenceMap = BeanMapper.toDifferenceMap(sourceMap, destinationMap);
		return differenceMap;
	}
	
	/**
	 * <p>Discription:[获取sourceMap中比destinationMap多出的变量]</p>
	 * Created on 2016年3月25日
	 * @param sourceMap 源对象
	 * @param destinationMap 指定比较对象
	 * @return 差异变量Map对象
	 * @author:[刘香平]
	 */
	public static Map<String, Object> toDifferenceMap(Map<String, Object> sourceMap, Map<String, Object> destinationMap){
		Map<String, Object> differenceMap = Maps.newHashMap();
		for (String key : sourceMap.keySet()) { 
			if(!destinationMap.containsKey(key)){
				differenceMap.put(key, sourceMap.get(key));
			}
		}
		return differenceMap;
	}
}
