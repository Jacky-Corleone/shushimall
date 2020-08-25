
package com.thinkgem.jeesite.modules.sys.web;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.camelot.basecenter.dto.BaseSendMessageDTO;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.camelot.basecenter.dto.SmsConfigDTO;
import com.camelot.basecenter.service.BaseSendMessageService;
import com.camelot.basecenter.service.BaseSmsConfigService;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.util.SysProperties;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 
 * <p>Description: [短信设置]</p>
 * Created on 2015年2月27日
 * @author  <a href="mailto: maguilei59@camelotchina.com">马桂雷</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/email")
public class EmailController extends BaseController {
	
	@Autowired
	private BaseSmsConfigService baseSmsConfigService;

	@Resource
	private BaseSendMessageService baseSendMessageService;
	
	@RequestMapping("info")
	public String info(HttpServletRequest request, HttpServletResponse response, Model model) {
		SmsConfigDTO emailConfig = baseSmsConfigService.getSmsConfig();
		model.addAttribute("emailConfig", emailConfig);
		return "modules/sys/emailconfig";
	}
	@RequestMapping("modifyEmailConfig")
	public String modifySmsConfig(SmsConfigDTO smsConfigDTO, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		ExecuteResult<String> result = baseSmsConfigService.modifySMSConfig(smsConfigDTO);
		//System.out.println("--------------"+JSON.toJSON(result));
		boolean bl = result.getErrorMessages() == null ? true : result.getErrorMessages().isEmpty();
		if(bl){
			model.addAttribute("message", "保存成功");
		}else{
			model.addAttribute("message", "保存失败，错误信息："+JSON.toJSON(result.getErrorMessages()));
		}
		SmsConfigDTO emailConfig = baseSmsConfigService.getSmsConfig();
		model.addAttribute("emailConfig", emailConfig);
		return "modules/sys/emailconfig";
	}
	/**
	 * 
	 * <p>Discription:[短信发送测试]</p>
	 * Created on 2015年2月27日
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping("emailTest")
	public String smsTest(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "modules/sys/emailconfigTest";
	}
	@RequestMapping("sendEmail")
	public String sendSms(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String receiverAccount = request.getParameter("receiverAccount");
		String content = request.getParameter("content");
		if(StringUtils.isNotEmpty(receiverAccount) && StringUtils.isNotEmpty(content)){
			//baseSmsConfigService.sendEmail(receiverAccount, "", content);
            BaseSendMessageDTO baseSendMessageDTO = new BaseSendMessageDTO();
            baseSendMessageDTO.setAddress(receiverAccount);
            baseSendMessageDTO.setContent(content);
            baseSendMessageDTO.setType("1");
            baseSendMessageDTO.setContentType(2);
            baseSendMessageService.sendMessageToMQ(baseSendMessageDTO);
			model.addAttribute("message", "发送成功");
		}else{
			model.addAttribute("message", "发送失败：收件人和发送内容不能为空.");
		}
		return "modules/sys/emailconfigTest";
	}
	
	
}
