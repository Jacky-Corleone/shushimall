package com.camelot.mall.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;

public class FlushTokenInterceptor implements HandlerInterceptor {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TokenInterceptor.class);

	@Resource
	private UserExportService userExportService;
	
	@Resource
	private RedisDB redisDB;
	
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String token = LoginToken.getLoginToken(request);
		LOGGER.debug("刷新USER_TOKEN："+token+"的REDIS信息");
		
		
		String uri = request.getPathInfo();
		if( uri.indexOf("/productController/categoryItems") != -1 ){
			request.setAttribute("module", "pmarket");
		}else if( uri.indexOf("/brandMarket") != -1 ){
			request.setAttribute("module", "bmarket");
		}else if("/".equals(uri)){
			request.setAttribute("module", "index");
		}
		
		if(StringUtils.isNotBlank(token)){
			RegisterDTO registerDTO = userExportService.getUserByRedis(token);
			if(registerDTO != null){
                redisDB.addObject(token, registerDTO, 60 * 30);
            }
		}
		
		return true;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
	
}
