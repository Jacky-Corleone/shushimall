package com.camelot.mall.interceptor;

import java.io.IOException;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.camelot.CookieHelper;
import com.camelot.mall.Constants;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;

public class TokenInterceptor implements HandlerInterceptor {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TokenInterceptor.class);

	@Resource
	private UserExportService userExportService;

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		LOGGER.info("TokenInterceptor拦截的路径：{}", request.getRequestURI());
		String token = LoginToken.getLoginToken(request);
		if(token == null){
			LOGGER.debug("用户TOKEN为NULL，移除COOKIE中用户信息");
			CookieHelper.delCookie(request, response, Constants.USER_ID);
			CookieHelper.delCookie(request, response, Constants.USER_NAME);
			dispose(request, response);
			return false;
		}
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if (registerDTO == null) {
			LOGGER.debug("REDIS中用户信息为NULL，移除COOKIE中用户ID");
			CookieHelper.delCookie(request, response, Constants.USER_ID);
			CookieHelper.delCookie(request, response, Constants.USER_NAME);
			dispose(request, response);
			return false;
		}
		return true;
	}

	private void dispose(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if(isAjaxRequest(request)){
			response.setContentType("application/json; charset=utf-8");
			response.setStatus(600);
			response.getWriter().write("");
		} else {
			@SuppressWarnings("rawtypes")
			Enumeration enumeration = request.getParameterNames();
			String name;
			String[] values;
			StringBuffer url = new StringBuffer();
			url.append(request.getRequestURI());
			url.append("?");
			while (enumeration.hasMoreElements()) {
				name = (String) enumeration.nextElement();
				values = request.getParameterValues(name);
				for (int i = 0; i < values.length; i++) {
					url.append(name);
					url.append("=");
					url.append(values[i]);
					url.append("&");
				}
			}
			request.setAttribute("url", url.toString());
			request.getRequestDispatcher("/user/login").forward(request, response);
		}
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
