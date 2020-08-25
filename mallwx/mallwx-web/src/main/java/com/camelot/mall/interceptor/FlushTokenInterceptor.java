package com.camelot.mall.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.camelot.CookieHelper;
import com.camelot.mall.Constants;
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
		String logname = CookieHelper.getCookieVal(request, Constants.USER_TOKEN);
		String token = LoginToken.getLoginToken(request);


		
/*		String uri = request.getPathInfo();
		if( uri.indexOf("/productController/categoryItems") != -1 ){
			request.setAttribute("module", "pmarket");
		}else if( uri.indexOf("/brandMarket") != -1 ){
			request.setAttribute("module", "bmarket");
		}else if("/".equals(uri)){
			request.setAttribute("module", "index");
		}*/
		
		
		if(StringUtils.isNotBlank(logname)){
			LOGGER.debug(request.getRequestURI()+"刷新USER_TOKEN："+token+"的REDIS信息");
			RegisterDTO registerDTO = userExportService.getUserByRedis(token);
			if (registerDTO != null){
				LOGGER.debug(request.getRequestURI()+"开始重新写入token："+token);
				redisDB.addObject(token, registerDTO,60 * 60 * 24 * 7);
			}else{
				LOGGER.debug(request.getRequestURI()+"没有根据token找到用户信息："+token);
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
	/**
	 * <p>Discription:判断是否为Ajax请求</p>
	 * Created on 2015年3月20日
	 * @param request
	 * @return 是true, 否false
	 * @author:胡恒心
	 */
	public boolean isAjaxRequest(HttpServletRequest request) {
		String requestType = request.getHeader("X-Requested-With");
		if (requestType != null && requestType.equals("XMLHttpRequest")) {
			return true;
		} else {
			return false;
		}
	}
}
