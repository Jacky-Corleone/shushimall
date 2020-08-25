package com.camelot.activeMQ.service;

import com.camelot.basecenter.dto.BaseSendMessageDTO;
import com.camelot.basecenter.dto.VerifyCodeMessageDTO;

/**
 * Description: activeMQ消息消费者接口
 *  
 * @author 王东晓
 * 
 * create on 2015-06-22
 * 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 *
 */
public interface ConsumerListenerService {
	/**
	 * 异步接受文本消息
	 */
	public void handleMessage(String message);
	/**
	 * 异步接收通知对象消息
	 */
	public void handleMessage(BaseSendMessageDTO message);
	/**
	 * 异步接收验证码对象消息
	 * @param messageDTO
	 */
	public void handleMessage(VerifyCodeMessageDTO messageDTO);
}
