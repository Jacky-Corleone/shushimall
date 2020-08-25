
package com.thinkgem.jeesite.modules.sys.web;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.openplatform.common.Constants;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.logo.dto.LogoDTO;
import com.camelot.sellercenter.logo.service.LogoExportService;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 *
 * <p>Description: [logo设置]</p>
 * Created on 2015年2月26日
 * @author  <a href="mailto: maguilei59@camelotchina.com">马桂雷</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/logo")
public class LogoController extends BaseController {

	@Autowired
	private LogoExportService logoExportService;

	@Resource
	private RedisDB redisDB;

	@RequiresPermissions("sys:user:view")
	@RequestMapping("info")
	public String info(HttpServletRequest request, HttpServletResponse response, Model model) {
		String platformId = request.getParameter("platformId");
		ExecuteResult<LogoDTO> executeResult = logoExportService.getMallLogoByPlatformId(StringUtils.isBlank(platformId) ? null : Integer.parseInt(platformId));
		LogoDTO logoDTO = executeResult.getResult();
		model.addAttribute("logoDTO", logoDTO);
		model.addAttribute("platformId", platformId);
		return "modules/sys/logo";
	}

	@RequiresPermissions("sys:user:view")
	@ResponseBody
	@RequestMapping("update")
	public ExecuteResult<String> update(LogoDTO logoDTO, HttpServletRequest request, HttpServletResponse response, Model model) {
        String url = logoDTO.getPicUrl();
        url = url.replaceAll(SysProperties.getProperty("ngIp"),"");
        logoDTO.setPicUrl(url);
		ExecuteResult<String> executeResult = logoExportService.modifyMallLogoByPlatformId(logoDTO.getLogoName(),
				logoDTO.getPicUrl(), logoDTO.getPlatformId());
		if (logoDTO.getPlatformId() == null || logoDTO.getPlatformId() != Constants.PLATFORM_ID) {// 科印
			redisDB.addObject(Constants.KEY_LOGO_REDIS, logoDTO);
		} else {// 绿印
			redisDB.addObject(Constants.KEY_GREEN_LOGO_REDIS, logoDTO);
		}
		return executeResult;
	}

}
