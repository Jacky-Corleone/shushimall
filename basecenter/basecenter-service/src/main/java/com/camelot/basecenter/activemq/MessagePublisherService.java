package com.camelot.basecenter.activemq;

import org.apache.activemq.command.ActiveMQQueue;

import com.camelot.basecenter.dto.BaseSendMessageDTO;
import com.camelot.openplatform.common.ExecuteResult;

/**
 * Description: activeMQ消息生产者接口
 *  
 * @author 王东晓
 * 
 * create on 2015-06-22
 * 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 *
 */
public interface MessagePublisherService {
	/**
	 * 发送消息
	 * @param obj  消息对象
	 * @return 发送成功返回true,发送失败返回false
	 */
	public ExecuteResult<String> sendMessage(ActiveMQQueue queueDestination,final BaseSendMessageDTO messageDTO);
}
