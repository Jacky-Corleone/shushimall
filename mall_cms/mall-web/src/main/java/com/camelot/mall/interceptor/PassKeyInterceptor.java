package com.camelot.mall.interceptor;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.encrypt.EncrypUtil;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.PassKeyUrlUtil;
import com.camelot.util.WebUtil;

/**
 * 
 * <p>Description: [订单号拦截]</p>
 * 防止用户随意输入订单号
 * 例如：用户查看订单详情时随意输入订单号，利用加密算法判断orderId和解密后的passKey是否对应，不正确则跳转到商城首页。
 * username作为加密的key，orderId作为加密的content
 * 
 * Created on 2015年10月20日
 * @author <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class PassKeyInterceptor implements HandlerInterceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(PassKeyInterceptor.class);

	@Resource
	private UserExportService userExportService;

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		LOGGER.info("PassKeyInterceptor拦截的路径：{}", request.getRequestURI());
		String requestUri = request.getRequestURI().replace("/mall-web", "");
		Long uid = WebUtil.getInstance().getUserId(request);
		String value = PassKeyUrlUtil.passKeyUrlMap.get(requestUri);
		if(StringUtils.isEmpty(value)){
			dispose(request, response);
			return false;
		}
		String passId = "";
		String[] values = value.split(";");
		for(int i = 0 ; i < values.length ; i++){
			String paras = values[i];
			
			String[] para = paras.split(",");
			if(StringUtils.isEmpty(passId)){
				for(int j = 0 ; j < para.length ;j++ ){
					String passIdTemp = request.getParameter(para[j]);
					if(StringUtils.isEmpty(passIdTemp)){
						passId="";
						break;
					}
					passId =passId + passIdTemp;
				}
			}
			
		}
//		String[] values = value.split(",");
//		
//		for(int i = 0 ; i < values.length ; i++){
//			passId = passId + request.getParameter(values[i]);
//		}
		if(StringUtils.isBlank(passId) ){
			return true;
		}
		String passKey = request.getParameter("passKey");
		if (uid == null || StringUtils.isBlank(passKey)) {
			dispose(request, response);
			return false;
		} else {
			// 解密passKey并比对订单号
//			ExecuteResult<String> passKeyEr = EncrypUtil.DecryptStrByAES(uid+"", passKey);
			ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(uid+"", passId);
			LOGGER.info("加密后："+passKeyEr.getResult()+"；key"+uid+";"+value+"："+passId);
			if (passKeyEr.isSuccess()) {
				if(!passKey.equals(passKeyEr.getResult())){
					dispose(request, response);
					return false;
				}
			} else {
				dispose(request, response);
				return false;
			}
		}
		return true;
	}

	private void dispose(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (isAjaxRequest(request)) {
			response.setContentType("application/json; charset=utf-8");
			response.setStatus(600);
			response.getWriter().write("");
		} else {
			String contextPath = request.getContextPath();
			response.sendRedirect(contextPath);
		}
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	/**
	 * <p>
	 * Discription:判断是否为Ajax请求
	 * </p>
	 * Created on 2015年3月20日
	 * 
	 * @param request
	 * @return 是true, 否false
	 * @author:胡恒心
	 */
	public boolean isAjaxRequest(HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		String uri = request.getRequestURI();
		String requestType = request.getHeader("X-Requested-With");
		if (requestType != null && requestType.equals("XMLHttpRequest")) {
			return true;
		} else {
			return false;
		}
	}
}
