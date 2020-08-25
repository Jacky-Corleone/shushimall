package com.camelot.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camelot.example.controller.WeChatMsgProcess;
import com.camelot.example.controller.WeChatProcess;
import com.camelot.mall.orderWx.SendWeiXinMessage;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.util.SpringApplicationContextHolder;
import com.camelot.usercenter.dto.UserWxDTO;
import com.camelot.usercenter.service.UserWxExportService;

public class SendWeiXinMessageUtil {

	private static WeChatProcess weChatProcess = new WeChatMsgProcess();
	
	private static UserWxExportService userWxExportService;

	private static final Logger LOGGER = LoggerFactory.getLogger(SendWeiXinMessageUtil.class);

	static{
		userWxExportService = SpringApplicationContextHolder.getBean("userWxExportService");
	}
	
	/**
	 * 发送微信模板消息
	 */
	@SuppressWarnings("unchecked")
	public static void sendWeiXinMessage(String uid,SendWeiXinMessage sendWeiXinMessage,HttpServletRequest request,HttpServletResponse response){
		
		UserWxDTO userWxDTO = new UserWxDTO();
		userWxDTO.setUid(Long.valueOf(uid));
		ExecuteResult<UserWxDTO> executeResult = userWxExportService.getUserInfoByOpenId(userWxDTO);
		if(null!=executeResult){
			UserWxDTO user = executeResult.getResult();
			if(null!=user){
				sendWeiXinMessage.setOpenId(user.getWxopenid());
			}
		}
		
		Map<String, String> map = new BeanMap(sendWeiXinMessage);
		if(null!=map && map.size()>0 && null!=map.get("openId") && null!=map.get("modeId")){
			try{
				weChatProcess.SendInformation(map, request, response);
			}catch (Exception e){
				LOGGER.info("发送微信消息失败",e);
			}
		}
	}

}
