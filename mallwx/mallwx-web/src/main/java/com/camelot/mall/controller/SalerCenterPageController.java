package com.camelot.mall.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;

@Controller
public class SalerCenterPageController {
	@Resource
	private UserExportService userExportService;
	/**
	 * 跳转到主页
	 * @author 王帅 创建时间：2015-5-20 下午1:08:15
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("salerCenter")
	public String goSalerCenterPage(Model model, HttpServletRequest request,
			HttpServletResponse response){
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if (registerDTO == null) {
			return "redirect:/";
		}
		if (registerDTO.getUserType() != 3) {
			return "redirect:/";
		}
		UserDTO user = userExportService.queryUserById(registerDTO.getUid());
		if(null!=user){
			model.addAttribute("user", user);
		}
		return "/salerCenter/salerCenterPage";
	}
	
}
