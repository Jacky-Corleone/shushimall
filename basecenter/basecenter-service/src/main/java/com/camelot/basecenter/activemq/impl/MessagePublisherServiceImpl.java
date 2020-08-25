package com.camelot.basecenter.activemq.impl;

import javax.annotation.Resource;
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

import com.camelot.basecenter.activemq.MessagePublisherService;
import com.camelot.basecenter.dto.BaseSendMessageDTO;
import com.camelot.openplatform.common.ExecuteResult;

@Service("messagePublisherServiceImpl")
public class MessagePublisherServiceImpl implements MessagePublisherService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	private JmsTemplate jmsTemplate;
	
	/**
	 * 发送对象消息
	 */
	@Override
	public ExecuteResult<String> sendMessage(ActiveMQQueue queueDestination ,final BaseSendMessageDTO messageDTO) {
		
		ExecuteResult<String> result = new ExecuteResult<String>();
		logger.info("收信人："+messageDTO.getAddress()+";发送内容："+messageDTO.getContent());
		try{
			this.jmsTemplate.send(queueDestination, new MessageCreator() {
				
				@Override
				public Message createMessage(Session session) throws JMSException {
					// TODO Auto-generated method stub
					return session.createObjectMessage(messageDTO);
				}
			});
		}catch(JmsException e){
			logger.error("向activeMQ中间件服务器发送对象消息失败:"+e.getMessage());
			result.addErrorMessage("向activeMQ中间件服务器发送对象消息失败:"+e.getMessage());
			//向消息中间件发送消息失败，
			return result;
		}
		logger.info("向activeMQ中间件服务器发送对象消息成功！");
		result.setResultMessage("向activeMQ中间件服务器发送对象消息成功！");
		return result;
	}
	
}
