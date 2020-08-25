package com.camelot.activeMQ.service;

import java.util.Random;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.camelot.basecenter.dto.BaseSendMessageDTO;

public class activeMQUtil {
	private static final String url = "tcp://10.6.14.15:61616";  
    private static final String QUEUE_NAME = "emailNoticQueue";  
   
    public void sendMessage() throws JMSException {  
       // JMS 客户端到JMSProvider 的连接  
       Connection connection = null;  
       try {  
           // 连接工厂，JMS 用它创建连接  
           // 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar  
           ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);  
           connection = (Connection)connectionFactory.createConnection();  
           // 启动连接  
           connection.start();  
           //Session：发送或接收消息的线程  
           // 获取session  
           Session session = (Session) connection.createSession(false,  
                  Session.AUTO_ACKNOWLEDGE);  
           // 消息的目的地，消息发送到那个队列  
           Destination destination = session.createQueue(QUEUE_NAME);  
           //MessageProducer：消息发送者（生产者）  
           // 创建消息发送者  
           MessageProducer producer =session.createProducer(destination);  
           // 设置是否持久化  
           //DeliveryMode.NON_PERSISTENT：不持久化  
           //DeliveryMode.PERSISTENT：持久化  
           producer.setDeliveryMode(DeliveryMode.PERSISTENT);  
            
           String msg = "";  
           int i = 0;  
        do {  
            msg = "第"+i + "次发送的消息："+new Random();
	            BaseSendMessageDTO baseSendMessageDTO = new BaseSendMessageDTO();
				if(i == 0){
					baseSendMessageDTO.setId(11111L);
					baseSendMessageDTO.setAddress("389971223@qq.com");
					baseSendMessageDTO.setContent("尊敬的用户，您的订单2015052700962已经于2015年05月28日16时49分完成付款，科印公司会尽快安排为您发货，请您耐心等待。");
					baseSendMessageDTO.setType("2");
					
					ObjectMessage message = session.createObjectMessage(baseSendMessageDTO);  
	                // 发送消息到目的地方  
	                producer.send(message);  
	                System.out.println("发送消息：" +msg);  
				}else if(i == 1){
					baseSendMessageDTO.setId(11112L);
					baseSendMessageDTO.setAddress("wangdongxiao@camelotchina.com");
					baseSendMessageDTO.setContent("尊敬的用户，您的订单2015052700962已经于2015年05月28日16时49分完成付款，科印公司会尽快安排为您发货，请您耐心等待。");
					baseSendMessageDTO.setType("2");
					
					ObjectMessage message = session.createObjectMessage(baseSendMessageDTO);  
	                // 发送消息到目的地方  
	                producer.send(message);  
	                System.out.println("发送消息：" +msg);  
				}else if(i == 2){
					baseSendMessageDTO.setId(11113L);
					baseSendMessageDTO.setAddress("wdx0806@sina.com");
					baseSendMessageDTO.setContent("尊敬的用户，您的订单2015052700962已经于2015年05月28日16时49分完成付款，科印公司会尽快安排为您发货，请您耐心等待。");
					baseSendMessageDTO.setType("2");
					
					ObjectMessage message = session.createObjectMessage(baseSendMessageDTO);  
	                // 发送消息到目的地方  
	                producer.send(message);  
	                System.out.println("发送消息：" +msg);  
				}
               
                i++;  
        } while (i<10);  
       } catch (Exception e) {  
           e.printStackTrace();  
       }  
    }  
	public static void main(String[] args) {
		activeMQUtil sndMsg = new activeMQUtil();  
	       try {  
	           sndMsg.sendMessage();  
	       } catch (Exception ex) {  
	           System.out.println(ex.toString());  
	       } 
	}

}
