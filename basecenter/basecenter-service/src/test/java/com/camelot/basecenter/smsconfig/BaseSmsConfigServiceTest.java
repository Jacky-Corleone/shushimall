package com.camelot.basecenter.smsconfig;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.basecenter.dto.EmailTypeEnum;
import com.camelot.basecenter.dto.SmsConfigDTO;
import com.camelot.basecenter.dto.SmsTypeEnum;
import com.camelot.basecenter.service.BaseSmsConfigService;
import com.camelot.openplatform.common.ExecuteResult;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年1月23日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class BaseSmsConfigServiceTest {
	ApplicationContext ctx = null;
	BaseSmsConfigService baseSmsConfigService = null; 
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		baseSmsConfigService = (BaseSmsConfigService) ctx.getBean("baseSmsConfigService");
	}
 
	//@Test
	public void testGetSmsConfig() {
		SmsConfigDTO dto = baseSmsConfigService.getSmsConfig();
		System.out.println("------"+dto.getSendAddress()+"-------");
	}

    @Test
    public void testSendSms(){
        
    	//ExecuteResult<String> sendMessage = baseSmsConfigService.sendSms("15712885821","尊敬的用户，您的订单2015052700962已经于2015年05月28日16时49分完成付款，科印公司会尽快安排为您发货，请您耐心等待。");
//    	ExecuteResult<String> sendMessage = baseSmsConfigService.sendSms("18611555674","尊敬的用户，您的订单2015052700962已经于2015年05月28日16时49分完成付款，科印公司会尽快安排为您发货，请您耐心等待。");
    	
    	//System.out.println("发送结果："+sendMessage.getErrorMessages());
    }
    //@Test
    public void testMongateGetDeliver(){
    	baseSmsConfigService.mongateGetDeliver(2);
    }
	@Test
	public void testModifySMSConfig(){
		SmsConfigDTO smsConfigDTO = new SmsConfigDTO();
		smsConfigDTO.setId(1L);
		smsConfigDTO.setMsgAccount("J02216");
		smsConfigDTO.setMsgHost("http://218.204.222.12:9003");
		smsConfigDTO.setMsgPassword("127862");
		smsConfigDTO.setMsgPszsubport("33");
		smsConfigDTO.setMsgSoapaddress("1234");
		smsConfigDTO.setMsgUrl("/MWGate/wmgw.asmx/");
		/*smsConfigDTO.setEmailType((short) 1);
		smsConfigDTO.setLoginEmail("zl@126.com");*/
		ExecuteResult<String> result = baseSmsConfigService.modifySMSConfig(smsConfigDTO);
		System.out.println(result.getResult());
	}
	
	@Test
	public void testSendEmail(){
		//baseSmsConfigService.sendEmail("zhoulenihao@126.com", "舒适100邮箱验证（系统邮件，请勿回复）", "123456zl");
	}
	@Test
	public void testSendEmailByType(){
		//注册
//		EmailTypeEnum emailTypeEnum = EmailTypeEnum.EMAIL_REGISTER;
		//修改绑定邮箱
//		EmailTypeEnum emailTypeEnum = EmailTypeEnum.EMAIL_UP_BIND_EMAIL;
		//修改绑定手机号
		EmailTypeEnum emailTypeEnum = EmailTypeEnum.EMAIL_UP_BIND_PHONE;
		//修改登录密码
//		EmailTypeEnum emailTypeEnum = EmailTypeEnum.EMAIL_UP_LOGIN_PWD;
		//设置支付密码,暂时没有模版信息
//		EmailTypeEnum emailTypeEnum = EmailTypeEnum.EMAIL_SET_PAY_PWD;
		//修改支付密码，暂时没有模版信息
//		EmailTypeEnum emailTypeEnum = EmailTypeEnum.EMAIL_UP_PAY_PWD;
		/*ExecuteResult<String> result =baseSmsConfigService.sendEmailByType(emailTypeEnum, "wangdongxiao@camelotchina.com", "舒适100邮箱验证（系统邮件，请勿回复）", "111111");
		if(result.isSuccess()){
			System.out.println("发送成功");
		}else{
			System.out.println("发送失败");
		}*/
	}
}
