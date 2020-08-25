package com.camelot.activeMQ.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.activeMQ.service.ConsumerListenerService;
import com.camelot.basecenter.dto.BaseSendMessageDTO;
import com.camelot.basecenter.dto.VerifyCodeMessageDTO;
import com.camelot.basecenter.service.BaseWebSiteMessageService;
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
@Service("emailVerifyCodeQueueConsumer")
public class EmailVerifyCodeQueueConsumerListenerImpl implements ConsumerListenerService{
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private BaseWebSiteMessageService baseWebSiteMessageService;

    @Resource
    private MessageService messageService;
    @Resource
    private RedisDB redisDB;
    
	@Override
	public void handleMessage(String message) {
		
	}
	@Override
	public void handleMessage(BaseSendMessageDTO message) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 异步接收对象消息
	 * 
	 *  验证码无论发送成功和失败都不再给用户发送，等待用户点击“再次发送”按钮，再次发送
	 */
	@Override
	public void handleMessage(VerifyCodeMessageDTO messageDTO) {
		logger.info("开始向用户"+messageDTO.getAddress()+"发送验证码");
		if(Constants.MAIL_TYPE.equals(messageDTO.getType())){
			String enumType = messageDTO.getEnumType();
			String key = messageDTO.getKey();
			String[] verificationCodes = messageDTO.getAddress();
			ExecuteResult<String> result = messageService.sendVerificationCode(enumType, key, verificationCodes, Integer.valueOf(Constants.MAIL_TYPE));
			//如果发送验证码失败,则需要删除redis中保存的key
			if(result != null&&!result.isSuccess()){
				redisDB.del(key);
			}
		}else{
			logger.info("邮箱验证码消息队列获取到一个不是邮箱验证码的消息，直接放弃--");
		}
	}
	
}
