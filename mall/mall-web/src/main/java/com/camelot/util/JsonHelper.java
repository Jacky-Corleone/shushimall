package com.camelot.util;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月9日
 * @author  <a href="mailto: zhoule@camelotchina.com">周乐</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class JsonHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonHelper.class);
	
	public static void success(HttpServletResponse response){
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write("{\"result\":\"success\"}");
		} catch (IOException e) {
			LOGGER.error("output json data error:"+e.getMessage());
		}
	}
	
	public static void failure(HttpServletResponse response){
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write("{\"result\":\"failure\"}");
		} catch (IOException e) {
			LOGGER.error("output json data error:"+e.getMessage());
		}
	}
	
	/**将对象输出成json串*/
	public static void objectToJson(HttpServletResponse response, Object obj){
		PrintWriter out = null;
		try {
			out = response.getWriter();
			String result = JSON.toJSONString(obj);
			out.write(result);
		} catch (IOException e) {
			LOGGER.error("output json data error:"+e.getMessage());
		}
	}
}
