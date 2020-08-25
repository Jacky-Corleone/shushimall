package com.camelot.activeMQ.service.impl;

import javax.annotation.Resource;

import com.camelot.mall.service.MessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.activeMQ.service.ConsumerListenerService;
import com.camelot.activeMQ.service.MessagePublisherService;
import com.camelot.basecenter.dto.BaseSendMessageDTO;
import com.camelot.basecenter.dto.VerifyCodeMessageDTO;
import com.camelot.basecenter.service.BaseSendMessageService;
import com.camelot.mall.Constants;
import com.camelot.mall.util.MessageTemplateFileUtil;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.util.SysProperties;
/**
 * Description: activeMQ消息消费者实现
 * 
 * @author 王东晓
 * 
 * create on 2015-06-22
 * 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 *
 */
@Service("emailNoticQueueConsumer")
public class EmailNoticQueueConsumerListenerImpl implements ConsumerListenerService{
	private Logger logger = LoggerFactory.getLogger(getClass());
	private String isUsedDB = MessageTemplateFileUtil.getInstance().getValue("use_db");
	@Resource
	private MessageService baseSmsConfigClientService;
	@Resource
	private BaseSendMessageService baseSendMessageService;
	@Resource
	private MessagePublisherService emailNoticQueuePublisher;
	/**
	 * 异步接受文本消息
	 */
	@Override
	public void handleMessage(String message) {

	}
	/**
	 * 异步接收对象消息
	 */
	@Override
	public void handleMessage(BaseSendMessageDTO message) {
		if("true".equals(SysProperties.getProperty("sms.pressure_test"))){
			logger.info("当前正在进行压力测试，不真正发送邮件通知");
			return ;
		}
		BaseSendMessageDTO baseSendMessage = (BaseSendMessageDTO) message;
		logger.info("开始给"+baseSendMessage.getAddress()+"发送消息!!");
		if("".equals(baseSendMessage.getAddress())){
			logger.error("没有邮箱地址，无法发送");
			return ;
		}
		//如果使用数据库作为备份，发送时需要将正在发送的消息存放在redis中备份
		if(Constants.DB_YES.equals(isUsedDB)){
		//	redisDB.set(key, value);
		}
		//发送短信或邮件
		ExecuteResult<String> result = null;
		try{
			result = new ExecuteResult<String>();
			if(Constants.MAIL_TYPE.equals(baseSendMessage.getType())){
				result = baseSmsConfigClientService.sendEmail(baseSendMessage.getAddress(), baseSendMessage.getTitle(), baseSendMessage.getContent());
			}else{
				logger.info("邮件通知消息队列获取到一个不是邮件通知的消息，直接放弃--");
				return ;
			}
		}catch(Exception e){
			logger.error("activeMQ调用dubbo发送邮件过程中出现异常："+e);
			this.rePushToQuene(baseSendMessage);
			return ;
		}
		//发送成功，修改数据库状态
		//发送失败，重新放入消息队列(queue)中
		
		if(result.isSuccess()){
			//修改数据库状态
			logger.info("activeMQ向用户"+baseSendMessage.getAddress()+"发送消息成功");
			//如果配置文件中配置需要将消息信息存入数据库,那么在消息发送后，需要修改数据库中消息的状态
			if(Constants.DB_YES.equals(isUsedDB)){
				Integer num = baseSendMessage.getSendNum()==null?1:baseSendMessage.getSendNum()+1;
				baseSendMessage.setSendNum(num);
				boolean editSuccess = false;
				int editNum = 1 ; 
				logger.info("检测到需要使用数据库，开始修改数据库状态");
				//如果修改数据库时发生异常，则需要再次修改数据库,最多修改5次
				do{
					logger.info("第"+editNum+"次修改消息状态");
					ExecuteResult<BaseSendMessageDTO> editResult =  baseSendMessageService.editBaseSendMessage(baseSendMessage);
					editSuccess = editResult.isSuccess();
					editNum++;
				}while(!editSuccess&editNum<=5);
			}
			logger.info("给"+baseSendMessage.getAddress()+"发送消息的线程结束！！！");
			
		}else{
			this.rePushToQuene(baseSendMessage);
		}
		
	}
	
	/**
	 * 消息发送失败，重新将信息放到消息队列中
	 * @return
	 */
	private boolean rePushToQuene(BaseSendMessageDTO baseSendMessage){
		
		
		int sendNum = (baseSendMessage.getSendNum()==null?1:baseSendMessage.getSendNum())+1;
		//发送5次未成功则不再发送
		if(sendNum>=6){
			logger.info("activeMQ向用户5次发送消息失败，不再发送");
			return false;
		}
		//重新放入消息队列中
		logger.info("activeMQ向用户发送消息失败，重新放入消息队列，重新发送");
		baseSendMessage.setSendNum(sendNum);
		boolean isSuccess =  emailNoticQueuePublisher.sendMessage(baseSendMessage);
		logger.info("activeMQ已重新第"+sendNum+"次向用户发送消息");
		//如果配置中配置了数据库，那么需要更新数据库消息状态
		/**
		 * 
		 * 需要处理的问题------当更新数据库的时候发生异常，后该怎么处理 ？？？？？？
		 * 
		 */
		if(Constants.DB_YES.equals(isUsedDB)){
			Integer num = baseSendMessage.getSendNum()==null?1:baseSendMessage.getSendNum()+1;
			baseSendMessage.setSendNum(num);
			boolean editSuccess = false;
			int editNum = 1 ; 
			do{
				logger.info("第"+editNum+"次修改消息发送的次数");
				ExecuteResult<BaseSendMessageDTO> editResult =  baseSendMessageService.editBaseSendMessage(baseSendMessage);
				editSuccess = editResult.isSuccess();
				editNum++;
			}while(!editSuccess&&editNum<=5);
			
			
		}
		return isSuccess;
	}
	@Override
	public void handleMessage(VerifyCodeMessageDTO messageDTO) {
		// TODO Auto-generated method stub
		
	}
}
