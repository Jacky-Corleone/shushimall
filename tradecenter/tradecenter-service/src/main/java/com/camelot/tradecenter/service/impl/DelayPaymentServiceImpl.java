package com.camelot.tradecenter.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.camelot.basecenter.dto.BaseSendMessageDTO;
import com.camelot.basecenter.dto.WebSiteMessageDTO;
import com.camelot.basecenter.service.BaseSendMessageService;
import com.camelot.basecenter.service.BaseWebSiteMessageService;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.enums.MessageContentTypeEnum;
import com.camelot.openplatform.common.enums.MessageTypeEnum;
import com.camelot.openplatform.common.enums.PlatformEnum;
import com.camelot.openplatform.common.util.MessageTemplateFileUtil;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;

/**
 * 延期支付短信、邮件、站内信处理
 * 
 * @author Admin
 * 
 */
@Service("delayPaymentServiceImpl")
public class DelayPaymentServiceImpl {
	private Logger log = Logger.getLogger(this.getClass());

	@Resource
	private UserExportService userExportService;
	@Resource
	private BaseWebSiteMessageService baseWebSiteMessageService;
	@Resource
	private BaseSendMessageService baseSendMessageService;

	/**
	 * 依据扣款成功与否 发送短信、邮件、站内信
	 * 
	 * @param item
	 * @param orderId
	 *            订单号
	 * @param flag
	 *            延期支付扣款成功与否
	 */
	public void sendMessage(TradeOrdersDTO item, boolean flag) {
		List<String> sellerIdList = new ArrayList<String>(2);
		sellerIdList.add(String.valueOf(item.getSellerId()));
		sellerIdList.add(String.valueOf(item.getBuyerId()));
		// 获取卖家用户的基本信息:电话和邮箱
		ExecuteResult<List<UserDTO>> userResult = userExportService
				.findUserListByUserIds(sellerIdList);
		if (!userResult.isSuccess() && userResult.getResult().size() == 0) {
			log.error("延期支付时发送通知消息时获取用户信息失败。。。");
			return;
		}
		// flag=true 扣款成功 /false 扣款失败
		// 发送短信、邮件、站内信
		this.sendMessage(userResult.getResult().get(0),item, flag);

	}

	/**
	 * 发送短信、邮件、站内信
	 * @param userDTO
	 * @param content
	 */
	private void sendMessage(UserDTO userDTO,TradeOrdersDTO item ,boolean flag ) {
		
		MessageTemplateFileUtil messageTemplateFileUtil = MessageTemplateFileUtil.getInstance();
		String mailTitle = messageTemplateFileUtil.getValue("mail_title");
		String mailTop = "";
		String mailBottom = "";
		if(item.getPlatformId() == null){
			mailTop = messageTemplateFileUtil.getValue("mail_content_top");
			mailBottom = messageTemplateFileUtil.getValue("mail_content_bottom");
		}else if(item.getPlatformId() == PlatformEnum.GREEN.getId()){
			mailTop = messageTemplateFileUtil.getValue("mail_content_top");
			mailBottom = messageTemplateFileUtil.getValue("mail_content_bottom");
		}
		
		
		
		String wscontent = "";
		String mailcontent = "";
		String smscontent = "";
		
		if(flag && userDTO.getUsertype() == 2){//买家
			wscontent = messageTemplateFileUtil.getValue("ws_delay_pay_success_b").replaceAll("orderId", item.getOrderId()+"");
			mailcontent = messageTemplateFileUtil.getValue("mail_delay_pay_success_b").replaceAll("orderId", item.getOrderId()+"");
			smscontent = messageTemplateFileUtil.getValue("sms_delay_pay_success_b").replaceAll("orderId", item.getOrderId()+"");
		}else if(flag && userDTO.getUsertype() == 3){//卖家
			wscontent = messageTemplateFileUtil.getValue("ws_delay_pay_success_s").replaceAll("orderId", item.getOrderId()+"");
			mailcontent = messageTemplateFileUtil.getValue("mail_delay_pay_success_s").replaceAll("orderId", item.getOrderId()+"");
			smscontent = messageTemplateFileUtil.getValue("sms_delay_pay_success_s").replaceAll("orderId", item.getOrderId()+"");
		}else if(flag == false && userDTO.getUsertype() == 2){
			wscontent = messageTemplateFileUtil.getValue("ws_delay_pay_fail_b").replaceAll("orderId", item.getOrderId()+"");
			mailcontent = messageTemplateFileUtil.getValue("mail_delay_pay_fail_b").replaceAll("orderId", item.getOrderId()+"");
			smscontent = messageTemplateFileUtil.getValue("sms_delay_pay_fail_b").replaceAll("orderId", item.getOrderId()+"");
		}else if(flag == false && userDTO.getUsertype() == 3){
			wscontent = messageTemplateFileUtil.getValue("ws_delay_pay_fail_s").replaceAll("orderId", item.getOrderId()+"");
			mailcontent = messageTemplateFileUtil.getValue("mail_delay_pay_fail_s").replaceAll("orderId", item.getOrderId()+"");
			smscontent = messageTemplateFileUtil.getValue("sms_delay_pay_fail_s").replaceAll("orderId", item.getOrderId()+"");
		}
		final BaseSendMessageDTO smsMessageDTO = new BaseSendMessageDTO();
		smsMessageDTO.setAddress(userDTO.getUmobile());
		smsMessageDTO.setContent(smscontent);
		smsMessageDTO.setType(MessageTypeEnum.SMS.getId());
		smsMessageDTO.setPlatformId(item.getPlatformId());
		smsMessageDTO.setContentType(MessageContentTypeEnum.NOTIC.getId());
		//扣款成功的发送通知内容
		// 发送短信通知
		ExecuteResult<String> result = baseSendMessageService.sendMessageToMQ(smsMessageDTO);
		final BaseSendMessageDTO emailMessageDTO = new BaseSendMessageDTO();
		emailMessageDTO.setAddress(userDTO.getUserEmail());
		emailMessageDTO.setTitle(mailTitle);
		emailMessageDTO.setContent(mailTop+mailcontent+mailBottom);
		emailMessageDTO.setType(MessageTypeEnum.MAIL.getId());
		emailMessageDTO.setPlatformId(item.getPlatformId());
		emailMessageDTO.setContentType(MessageContentTypeEnum.NOTIC.getId());

		// 发送邮件通知
		ExecuteResult<String> resultmail = baseSendMessageService.sendMessageToMQ(emailMessageDTO);
		// 发送站内信
		WebSiteMessageDTO webSiteMessageDTO = new WebSiteMessageDTO();
		webSiteMessageDTO.setWmRead(1);
		webSiteMessageDTO.setWmCreated(new Date());
		webSiteMessageDTO.setModified(new Date());
		webSiteMessageDTO.setType(1);
		webSiteMessageDTO.setWmToUserid(userDTO.getUid());
		webSiteMessageDTO.setWmToUsername(userDTO.getUname());
		webSiteMessageDTO.setPlatformId(item.getPlatformId());
		// 信息内容
		webSiteMessageDTO.setWmContext(wscontent);
		baseWebSiteMessageService.addWebMessage(webSiteMessageDTO);
	}
}
