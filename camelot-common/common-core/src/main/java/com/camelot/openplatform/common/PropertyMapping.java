package com.camelot.openplatform.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  转换器注解
 * 
 * @Description -
 * @author - 
 * @createDate - 2015-1-22
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface PropertyMapping {
	// 转换属性
	String value();
}
