package com.camelot.activeMQ.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.activeMQ.service.ConsumerListenerService;
import com.camelot.basecenter.dto.BaseSendMessageDTO;
import com.camelot.basecenter.dto.VerifyCodeMessageDTO;
import com.camelot.mall.Constants;
import com.camelot.mall.service.MessageService;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.dao.util.RedisDB;

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
@Service("smsVerifyCodeQueueConsumer")
public class SmsVerifyCodeQueueConsumerListenerImpl implements ConsumerListenerService{
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private MessageService messageService;
	@Resource
	private RedisDB redisDB;
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
		
	}
	@Override
	public void handleMessage(VerifyCodeMessageDTO messageDTO) {
		// TODO Auto-generated method stub
		logger.info("开始向"+messageDTO.getAddress()+"发送验证码");
		if(Constants.SMS_TYPE.equals(messageDTO.getType())){
			String enumType = messageDTO.getEnumType();
			String key = messageDTO.getKey();
			String[] verificationCodes = messageDTO.getAddress();
			ExecuteResult<String> result = messageService.sendVerificationCode(enumType, key, verificationCodes, Integer.valueOf(Constants.SMS_TYPE));
			//如果发送验证码失败,则需要删除redis中保存的key
			if(result != null&&!result.isSuccess()){
				redisDB.del(key);
			}
		}else{
			logger.info("短信验证码消息队列获取到一个不是短信验证码的消息，直接放弃--");
		}
	}
}
