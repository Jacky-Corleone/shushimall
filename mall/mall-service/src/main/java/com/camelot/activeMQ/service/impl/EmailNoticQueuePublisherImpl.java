package com.camelot.activeMQ.service.impl;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.camelot.activeMQ.service.MessagePublisherService;
import com.camelot.basecenter.dto.BaseSendMessageDTO;

/**
 * Description: activeMQ消息生产者
 *  
 * @author 王东晓
 * 
 * create on 2015-06-22
 * 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 *
 */


@Service("emailNoticQueuePublisher")
public class EmailNoticQueuePublisherImpl implements MessagePublisherService{
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private JmsTemplate jmsTemplate;
	@Resource
	private ActiveMQQueue emailNoticQueueDestination;
	
	/**
	 * 发送对象消息
	 */
	@Override
	public boolean sendMessage(Object obj) {
		final BaseSendMessageDTO messageDTO = (BaseSendMessageDTO) obj;
		try{
			this.jmsTemplate.send(emailNoticQueueDestination, new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					// TODO Auto-generated method stub
					return session.createObjectMessage(messageDTO);
				}
			});
		}catch(JmsException e){
			logger.error("向activeMQ中间件服务器发送对象消息失败:"+e.getMessage());
			//向消息中间件发送消息失败，
			return false;
		}
		logger.error("向activeMQ中间件服务器发送对象消息成功！");
		return true;
	}
	
}
