package com.camelot.basecenter.activemq.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Service;

import com.camelot.basecenter.activemq.impl.QueuePublisherImpl;
import com.camelot.basecenter.service.ActiveMQFactoryService;
import com.camelot.basecenter.service.PublisherMessageService;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.openplatform.util.SpringApplicationContextHolder;

@Service("activeMQFactoryService")
public class ActiveMQFactoryServiceImpl implements ActiveMQFactoryService{
	
	@Resource
	private RedisDB redisDB;
	
	private static String mqKey = "mq_name_key";
	
	
	/**
	 * 获取队列生产者，如果不存在此队列则创建新的队列生产者。
	 * @param name 队列唯一标识
	 * @return
	 */
	public PublisherMessageService getMessagePublisher(String name,Class cla){
		PublisherMessageService publisherMessageService = null;
		//查询是否有此队列
		List<String> mqPublisherList = (List<String>)redisDB.getObject(mqKey);
		if(mqPublisherList == null){
			mqPublisherList = new ArrayList<String>();
		}
		if(!mqPublisherList.contains(name)){
			//新增mq
			JmsTemplate jmsTemplate = SpringApplicationContextHolder.getBean("jmsTemplate");
//			ActiveMQQueue activeMQQueue = new ActiveMQQueue(name);
//			messagePublisherService = new QueuePublisherImpl(jmsTemplate,activeMQQueue);
			//向spring容器中生成队列对象
			List<Object> valueList = new ArrayList<Object>();
			valueList.add(name);
			SpringApplicationContextHolder.registerBean(name+"ActiveMQQueue", ActiveMQQueue.class,valueList,null,null);
			valueList.clear();
			//获取activeMQQueue 实例
			ActiveMQQueue activeMQQueue = SpringApplicationContextHolder.getBean(name+"ActiveMQQueue");
			valueList.add(jmsTemplate);
			valueList.add(activeMQQueue);
			SpringApplicationContextHolder.registerBean("messagePublisherService_"+name, QueuePublisherImpl.class,valueList,null,null);
			mqPublisherList.add(name);
			redisDB.addObject(mqKey, mqPublisherList);
			//注册消费者
			registerConsumer(name,cla);
		}
		publisherMessageService = SpringApplicationContextHolder.getBean("messagePublisherService_"+name);
		return publisherMessageService;
	}
	
	/**
	 * 注册消费者
	 */
	private void registerConsumer(String queName,Class cla){
		//注册消费者实例
		SpringApplicationContextHolder.registerBean(cla.getName(),cla,null,null,null);
		//构建消费者适配器
		List<Object> valueList = new ArrayList<Object>();
		valueList.add(SpringApplicationContextHolder.getBean(cla.getName()));
		SpringApplicationContextHolder.registerBean(queName+"ListenerAdapter", MessageListenerAdapter.class,valueList,null,null);
		//构建消息监听容器
		Map<String,Object> valueParamsMap = new HashMap<String,Object>();
		valueParamsMap.put("concurrentConsumers", 15);
		valueParamsMap.put("maxConcurrentConsumers", 100);
		valueParamsMap.put("connectionFactory", SpringApplicationContextHolder.getBean("connectionFactory"));
		valueParamsMap.put("destination", SpringApplicationContextHolder.getBean(queName+"ActiveMQQueue"));
		valueParamsMap.put("messageListener", SpringApplicationContextHolder.getBean(queName+"ListenerAdapter"));
		SpringApplicationContextHolder.registerBean(queName+"ListenerAdapter", DefaultMessageListenerContainer.class,null,valueParamsMap,null);
		DefaultMessageListenerContainer dmc = SpringApplicationContextHolder.getBean(queName+"ListenerAdapter");
		//启动消费者
		dmc.start();
	}
}
