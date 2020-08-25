
package com.thinkgem.jeesite.modules.sys.web;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.camelot.basecenter.dto.BaseSendMessageDTO;
import com.camelot.basecenter.dto.SmsConfigDTO;
import com.camelot.basecenter.service.BaseSendMessageService;
import com.camelot.basecenter.service.BaseSmsConfigService;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.enums.MessageContentTypeEnum;
import com.camelot.openplatform.common.enums.MessageTypeEnum;
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
@RequestMapping(value = "${adminPath}/sys/sms")
public class SmsController extends BaseController {
	
	@Autowired
	private BaseSmsConfigService baseSmsConfigService;

	@Resource
	private BaseSendMessageService baseSendMessageService;
	
	@RequiresPermissions("sys:sms:view")
	@RequestMapping("info")
	public String info(HttpServletRequest request, HttpServletResponse response, Model model) {
		SmsConfigDTO smsConfig = baseSmsConfigService.getSmsConfig();
		model.addAttribute("smsConfig", smsConfig);
		return "modules/sys/smsconfig";
	}
	@RequiresPermissions("sys:sms:view")
	@RequestMapping("modifySmsConfig")
	public String modifySmsConfig(SmsConfigDTO smsConfigDTO, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		ExecuteResult<String> result = baseSmsConfigService.modifySMSConfig(smsConfigDTO);
		//System.out.println("--------------"+JSON.toJSON(result));
		if(result.isSuccess()){
			model.addAttribute("message", "保存成功");
		}else{
			model.addAttribute("message", "保存失败，错误信息："+JSON.toJSON(result.getErrorMessages()));
		}
		SmsConfigDTO smsConfig = baseSmsConfigService.getSmsConfig();
		model.addAttribute("smsConfig", smsConfig);
		return "modules/sys/smsconfig";
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
	@RequestMapping("smsTest")
	public String smsTest(HttpServletRequest request, HttpServletResponse response, Model model) {
		return "modules/sys/smsconfigTest";
	}
	/**
	 * 
	 * <p>Discription:[短信发送，短信息发送接口使用服务商提供的 “不同内容群发，可自定义不同流水号，自定义不同扩展号” 方式发送]</p>
	 * Created on 2015年2月28日
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping("sendSms")
	public String sendSms(HttpServletRequest request, HttpServletResponse response, Model model) {
		//短信测试：接受手机号
		String receiverPhone = request.getParameter("receiverPhone");
		//短信测试：短信内容
		String sendContent = request.getParameter("sendContent");
        BaseSendMessageDTO baseSendMessageDTO = new BaseSendMessageDTO();
        baseSendMessageDTO.setAddress(receiverPhone);
        baseSendMessageDTO.setContent(sendContent);
        baseSendMessageDTO.setType(MessageTypeEnum.SMS.getId());
        baseSendMessageDTO.setContentType(MessageContentTypeEnum.NOTIC.getId());
        baseSendMessageService.sendMessageToMQ(baseSendMessageDTO);
        //baseSmsConfigService.sendMessage(SmsTypeEnum.REGISTER,receiverPhone,sendContent);
        //ExecuteResult<String> executeResult = baseSmsConfigService.sendMessage(SmsTypeEnum.REGISTER,receiverPhone,sendContent);
        //if(executeResult.isSuccess()){
            model.addAttribute("message", "发送成功");
        //}else{
        //    model.addAttribute("message", "发送失败");
        //}
		return "modules/sys/smsconfigTest";
	}
	
	
}
