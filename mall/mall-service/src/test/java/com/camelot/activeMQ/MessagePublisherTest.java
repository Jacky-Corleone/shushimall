package com.camelot.activeMQ;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.activeMQ.service.impl.EmailNoticQueuePublisherImpl;
import com.camelot.basecenter.dto.BaseSendMessageDTO;

public class MessagePublisherTest {
	
	ApplicationContext ctx = null;
	
	private EmailNoticQueuePublisherImpl messagePublisher = null;

	@Before
	public void setUp() throws Exception {
		
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		messagePublisher = (EmailNoticQueuePublisherImpl) ctx.getBean("messagePublisher");
		
	}

	@Test
	public void testSendMessage() {
		
		for(int i = 1 ; i < 101; i++){
			BaseSendMessageDTO baseSendMessageDTO = new BaseSendMessageDTO();
			if(i == 0){
			baseSendMessageDTO.setId(11111L);
			baseSendMessageDTO.setAddress("389971223@qq.com");
			baseSendMessageDTO.setContent("尊敬的用户，您已经成功修改绑定电话为18501252392");
			baseSendMessageDTO.setType("2");
			
			
		}else if(i==1){
			baseSendMessageDTO.setId(11112L);
			baseSendMessageDTO.setAddress("wdx0806@sina.com");
			baseSendMessageDTO.setContent("尊敬的用户，您正在注册舒适100，验证码为234567（切勿将验证码泄漏他人），请您在30分钟内填写验证码并完成注册");
			baseSendMessageDTO.setType("2");
		}else if(i==2){
			baseSendMessageDTO.setId(11113L);
			baseSendMessageDTO.setAddress("wangdongxiao@camelotchina.com");
			baseSendMessageDTO.setContent("尊敬的用户，订单已提交成功，订单号1111，订单信息以“我的订单”页面显示为准");
			baseSendMessageDTO.setType("2");
		}/*else if(i==3){
			baseSendMessageDTO.setId(11114L);
			baseSendMessageDTO.setAddress("18501252392");
			baseSendMessageDTO.setContent("尊敬的用户，您的订单111已经于2015年05月28日 10时13分22秒成功取消。");
			baseSendMessageDTO.setType("1");
		}else if(i==4){
			baseSendMessageDTO.setId(11115L);
			baseSendMessageDTO.setAddress("18501252392");
			baseSendMessageDTO.setContent("尊敬的用户，用户wdx的订单已提交取消，订单号1111，订单详情请在我的订单中进行查看。");
			baseSendMessageDTO.setType("1");
		}else if(i==5){
			baseSendMessageDTO.setId(11116L);
			baseSendMessageDTO.setAddress("18501252392");
			baseSendMessageDTO.setContent("尊敬的用户，您的订单1111已经于2015年05月28日 10时13分22秒完成付款，xx尽快安排为您发货，请您耐心等待。");
			baseSendMessageDTO.setType("1");
		}else if(i==6){
			baseSendMessageDTO.setId(11117L);
			baseSendMessageDTO.setAddress("18501252392");
			baseSendMessageDTO.setContent("尊敬的用户，订单1111已经于2015年05月28日 10时13分22秒成功完成付款，印刷家提醒您及时查看并尽快完成发货。");
			baseSendMessageDTO.setType("1");
		}else if(i==7){
			baseSendMessageDTO.setId(11118L);
			baseSendMessageDTO.setAddress("18501252392");
			baseSendMessageDTO.setContent("尊敬的用户您好，您购买的xx商品已经于2015年05月28日 10时13分22秒从xxx发出，请您耐心等待。");
			baseSendMessageDTO.setType("1");
		}else if(i==8){
			baseSendMessageDTO.setId(11119L);
			baseSendMessageDTO.setAddress("18501252392");
			baseSendMessageDTO.setContent("尊敬的用户，您的订单1111退款/退货申请已经于2015年05月28日 10时13分22秒收到结果，请您及时查看。");
			baseSendMessageDTO.setType("1");
		}else if(i==9){
			baseSendMessageDTO.setId(111110L);
			baseSendMessageDTO.setAddress("18501252392");
			baseSendMessageDTO.setContent("尊敬的用户，您收到一条退款/退货申请，请在退款/退货处理中进行查看。");
			baseSendMessageDTO.setType("1");
		}else if(i==10){
			baseSendMessageDTO.setId(111111L);
			baseSendMessageDTO.setAddress("18501252392");
			baseSendMessageDTO.setContent("尊敬的用户，卖家用户wdx已经于2015年05月28日 10时13分22秒提交订单xxx平台仲裁，请您及时查看。");
			baseSendMessageDTO.setType("1");
		}else if(i==11){
			baseSendMessageDTO.setId(111112L);
			baseSendMessageDTO.setAddress("18501252392");
			baseSendMessageDTO.setContent("尊敬的用户，买家用户wdx已经于2015年05月28日 10时13分22秒提交订单xxx平台仲裁，印刷家提醒您及时查看。");
			baseSendMessageDTO.setType("1");
		}else if(i==12){
			baseSendMessageDTO.setId(111113L);
			baseSendMessageDTO.setAddress("18501252392");
			baseSendMessageDTO.setContent("尊敬的用户，订单1111已经于wdx收到仲裁结果，您可以随时在“我的投诉”中查看仲裁结果。");
			baseSendMessageDTO.setType("1");
		}*/else{
			continue;
		}
			messagePublisher.sendMessage(baseSendMessageDTO);
			
			try {
				Thread.sleep(100000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
