package com.camelot.mall.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.camelot.mall.Constants;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.dao.util.RedisDB;

/** 
 * <p>Description: Token 拦截防止重复提交</p>
 * 
 * 配合TokenHandlerInterceptor【Token设置拦截器】使用，
 * 
 * 判断传入Token是否存在，如果存在则返回true并清除Token；如果不存在，则属于重复提交返回false
 * 
 * Created on 2015年3月27日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class TokenValidHandler implements HandlerInterceptor {
	private Logger LOG = Logger.getLogger(this.getClass());
	@Resource
	private RedisDB redisDB;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String token = request.getParameter(Constants.TOKEN);
		String value = this.redisDB.get(token);
		LOG.debug("GET FORM TOKEN:" + token + ">" + value);
		if( value != null && !"".equals(value) ){
			this.redisDB.del(token);
			return true;
		}else{
			
			String requestType = request.getHeader("X-Requested-With");
		    if (requestType != null && requestType.equals("XMLHttpRequest")) {
		    	ExecuteResult<String> er = new ExecuteResult<String>();
		    	er.addErrorMessage("请不要频繁操作！");
//				Map<String , Object> data = new HashMap<String , Object>();  
//		        data.put("flag", 0);  
//		        data.put("msg", "请不要频繁操作！");
		        response.setCharacterEncoding("UTF-8");
		        response.getWriter().write(JSON.toJSONString(er));
		    } else {
		    	
		    	response.sendRedirect(request.getContextPath() + "/reSubmit");
		    }
	        return false;
		}
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
