/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.interceptor;

import com.camelot.openplatform.util.SysProperties;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.Log;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.LogService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 系统拦截器
 * @author ThinkGem
 * @version 2013-6-6
 */
public class LogInterceptor extends BaseService implements HandlerInterceptor {

	private static LogService logService = SpringContextHolder.getBean(LogService.class);

    private List<String> uriList;

    public List<String> getUriList() {
        return uriList;
    }

    public void setUriList(List<String> uriList) {
        this.uriList = uriList;
    }

    @Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, 
			Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, 
			ModelAndView modelAndView) throws Exception {
		if(modelAndView!=null) {
			String viewName = modelAndView.getViewName();
			UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent")); 
			if(viewName.startsWith("modules/") && DeviceType.MOBILE.equals(userAgent.getOperatingSystem().getDeviceType())){
				modelAndView.setViewName(viewName.replaceFirst("modules", "mobile"));
			}
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
			Object handler, Exception ex) throws Exception {
//		request.getPathInfo();
		String requestRri = request.getRequestURI();
		String uriPrefix = request.getContextPath() + SysProperties.getAdminPath();
//        logger.debug("||||{}", requestRri);
//        logger.debug("||||{}", uriPrefix);
//        HandlerMethod h = (HandlerMethod)handler;
//        logger.debug("name:||||{}|||{}", h.getMethod().getName(), h.getBeanType().getName());
        boolean flag = false;
        if (uriList!=null&&uriList.size()>0){
            for (String uri:uriList){
//                logger.debug("set uri:{}",uri);
                if(StringUtils.endsWith(requestRri, uri)){
                    flag = true;
                    break;
                }
            }
        }

        if ((StringUtils.startsWith(requestRri, uriPrefix) && flag) || ex!=null){
		
			User user = UserUtils.getUser();
			if (user!=null && user.getId()!=null){
				
				StringBuilder params = new StringBuilder();
				int index = 0;
				for (Object param : request.getParameterMap().keySet()){ 
					params.append((index++ == 0 ? "" : "&") + param + "=");
					params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase((String)param, "password")
							? "" : request.getParameter((String)param), 100));
				}
				
				Log log = new Log();
				log.setType(ex == null ? Log.TYPE_ACCESS : Log.TYPE_EXCEPTION);
				log.setCreateBy(user);
				log.setCreateDate(new Date());
				log.setRemoteAddr(StringUtils.getRemoteAddr(request));
				log.setUserAgent(request.getHeader("user-agent"));
				log.setRequestUri(request.getRequestURI());
				log.setMethod(request.getMethod());
				log.setParams(params.toString());
				log.setException(ex != null ? ex.toString() : "");
				logService.save(log);
				
				logger.info("save log {type: {}, loginName: {}, uri: {}}, ", log.getType(), user.getLoginName(), log.getRequestUri());
				
			}
		}
		
//		logger.debug("最大内存: {}, 已分配内存: {}, 已分配内存中的剩余空间: {}, 最大可用内存: {}", 
//				Runtime.getRuntime().maxMemory(), Runtime.getRuntime().totalMemory(), Runtime.getRuntime().freeMemory(), 
//				Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory()); 
		
	}

}
