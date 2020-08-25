package com.camelot.basecenter.util;

import org.apache.activemq.command.ActiveMQQueue;

import com.camelot.basecenter.activemq.MessagePublisherService;
import com.camelot.openplatform.common.enums.MessageContentTypeEnum;
import com.camelot.openplatform.common.enums.MessageTypeEnum;
import com.camelot.openplatform.common.enums.PlatformEnum;
import com.camelot.openplatform.util.SpringApplicationContextHolder;

/**
 * 消息生产者工厂
 * @author 王东晓
 *
 */
public class QueueDestinationFactory {

	/**
	 * 获取消息生产者实例
	 * @param platformId  null为舒适100，2为上海绿印中心
	 * @param type sms 为短信， mail 为邮件
	 * @author 王东晓
	 * @return MessagePublisherService消息生产者
	 */
	public static ActiveMQQueue getQueueDestinationIntance(Integer platformId,String type,Integer contentType){
		ActiveMQQueue queueDestination =null;
		//科印短信---验证码
		if (platformId == null && MessageTypeEnum.SMS.getId().equals(type)
				&& MessageContentTypeEnum.VERIFY.getId() == contentType) {
			queueDestination = SpringApplicationContextHolder.getBean("smsVerifyCodeDestination"); 
		}
		//科印短信 ---通知
		else if(platformId == null && MessageTypeEnum.SMS.getId().equals(type)
				&& MessageContentTypeEnum.NOTIC.getId() == contentType){
			queueDestination = SpringApplicationContextHolder.getBean("smsNoticQueueDestination"); 
		}
		//科印邮件 ---验证码
		else if(platformId == null && MessageTypeEnum.MAIL.getId().equals(type)
				&& MessageContentTypeEnum.VERIFY.getId() == contentType){
			queueDestination = SpringApplicationContextHolder.getBean("emailVerifyCodeDestination"); 
		}
		//科印邮件 ---通知
		else if(platformId == null && MessageTypeEnum.MAIL.getId().equals(type)
				&& MessageContentTypeEnum.NOTIC.getId() == contentType){
			queueDestination = SpringApplicationContextHolder.getBean("emailNoticQueueDestination"); 
		}
		//绿印短信---验证码
		else if (platformId == PlatformEnum.GREEN.getId() && MessageTypeEnum.SMS.getId().equals(type)
				&& MessageContentTypeEnum.VERIFY.getId() == contentType) {
			queueDestination = SpringApplicationContextHolder.getBean("smsVerifyCodeGreenDestination"); 
		}
		//绿印短信 ---通知
		else if(platformId == PlatformEnum.GREEN.getId() && MessageTypeEnum.SMS.getId().equals(type)
				&& MessageContentTypeEnum.NOTIC.getId() == contentType){
			queueDestination = SpringApplicationContextHolder.getBean("smsNoticGreenQueueDestination"); 
		}
		//绿印邮件 ---验证码
		else if(platformId == PlatformEnum.GREEN.getId() && MessageTypeEnum.MAIL.getId().equals(type)
				&& MessageContentTypeEnum.VERIFY.getId() == contentType){
			queueDestination = SpringApplicationContextHolder.getBean("emailVerifyCodeGreenDestination"); 
		}
		//绿印邮件 ---通知
		else if(platformId == PlatformEnum.GREEN.getId() && MessageTypeEnum.MAIL.getId().equals(type)
				&& MessageContentTypeEnum.NOTIC.getId() == contentType){
			queueDestination = SpringApplicationContextHolder.getBean("emailNoticGreenQueueDestination"); 
		}
		else{
			return null;
		}
		
		return queueDestination;
	}
}
