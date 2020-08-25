package com.camelot.openplatform.dao.interceptor.mybatis;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

public class ReflectUtil {
	private static Logger logger = Logger.getLogger(ReflectUtil.class);
	/**
	 * 利用反射获取指定对象里面的指定属性
	 * 
	 * @param obj 目标对象
	 * @param fieldName 目标属性
	 * @return 目标字段
	 */
	public static Object getFieldValue(Object obj, String fieldName) {
		Object result = null;
		Field field = ReflectUtil.getField(obj, fieldName);
		if (field != null) {
			field.setAccessible(true);
			try {
				result = field.get(obj);
			} catch (Exception e) {
				logger.error(e.getMessage(), e.getCause());
			}
		}
		return result;
	}

	/**
	 * 利用反射获取指定对象里面的指定属性
	 * 
	 * @param obj 目标对象
	 * @param fieldName 目标属性
	 * @return 目标字段
	 */
	private static Field getField(Object obj, String fieldName) {
		Field field = null;
		for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				field = clazz.getDeclaredField(fieldName);
				break;
			} catch (Exception e) {
				logger.error(e.getMessage(), e.getCause());
			}
		}
		return field;
	}

	/**
	 * 利用反射设置指定对象的指定属性为指定的值
	 * 
	 * @param obj 目标对象
	 * @param fieldName 目标属性
	 * @param fieldValue 目标值
	 */
	public static void setFieldValue(Object obj, String fieldName,
			String fieldValue) {
		Field field = ReflectUtil.getField(obj, fieldName);
		if (field != null) {
			try {
				field.setAccessible(true);
				field.set(obj, fieldValue);
			} catch (Exception e) {
				logger.error(e.getMessage(), e.getCause());
			}
		}
	}
}
