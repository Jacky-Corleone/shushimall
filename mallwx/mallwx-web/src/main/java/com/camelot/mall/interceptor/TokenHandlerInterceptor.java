package com.camelot.mall.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.camelot.mall.Constants;
import com.camelot.openplatform.dao.util.RedisDB;

/** 
 * <p>Description: Token设置拦截器</p>
 * 
 * 给页面设置Token参数，并存入redis数据库
 * 配合TokenValidInterceptor 使用
 * 
 * Created on 2015年3月27日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class TokenHandlerInterceptor implements HandlerInterceptor {
	private Logger LOG = Logger.getLogger(this.getClass());
	@Resource
	private RedisDB redisDB;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// 如果还是重复了那就认了吧 (/ □ \)，实在不想用 UUID (⊙０⊙)
		String token = System.currentTimeMillis() + "";
		LOG.debug("FORM TOKEN SET :" + token);
		this.redisDB.setAndExpire(token,"1", 60 * 30);
		request.setAttribute(Constants.TOKEN, token);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
