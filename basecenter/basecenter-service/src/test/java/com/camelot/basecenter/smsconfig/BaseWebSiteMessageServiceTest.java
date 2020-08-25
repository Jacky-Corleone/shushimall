package com.camelot.basecenter.smsconfig;

import java.sql.Date;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.basecenter.dto.WebSiteMessageDTO;
import com.camelot.basecenter.service.BaseWebSiteMessageService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年1月23日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class BaseWebSiteMessageServiceTest {
	ApplicationContext ctx = null;
	BaseWebSiteMessageService baseWebSiteMessageService = null; 
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		baseWebSiteMessageService = (BaseWebSiteMessageService) ctx.getBean("baseWebSiteMessageService");
	}
 
	@Test
	public void testGetSmsConfig() {
		
		/**
		 * 查询站内信列表
		 */
		/*Pager page = new Pager();
		page.setPage(1);
		page.setRows(10);
		WebSiteMessageDTO webSiteMessageDTO = new WebSiteMessageDTO();
		//webSiteMessageDTO.setType(1);
		DataGrid<WebSiteMessageDTO> data = baseWebSiteMessageService.queryWebSiteMessageList(webSiteMessageDTO, page);
		System.out.println("------------"+data.getTotal()+"--------------------"+data.getRows().get(0).getId());
		*/
	/*	*//**
		 * 测试邮箱发送验证码
		 */
		/*String y = "UPBINDEMAIL";
		String[] u = {"18501252392"};
		int s = 2;
		ExecuteResult<String> e = new ExecuteResult<String>();
		e = baseWebSiteMessageService.sendVerificationCode(y,"11",u, s);
		System.out.println(e.getResult());*/
	}
	@Test
	public void textgetWebSiteMessageInfo(){
		WebSiteMessageDTO wsm=new WebSiteMessageDTO();
		wsm.setId(3l);
		ExecuteResult<WebSiteMessageDTO> ss = baseWebSiteMessageService.getWebSiteMessageInfo(wsm);
		System.out.println(ss.getResult());
	}
	@Test
	public void textQueryWebSiteMessageWithOutDel(){
		WebSiteMessageDTO wsm=new WebSiteMessageDTO();
		wsm.setWmCreated(Date.valueOf("2015-05-15"));
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(10000);
		DataGrid<WebSiteMessageDTO> ss = baseWebSiteMessageService.queryWebSiteMessageList(wsm, page);
		System.out.println(ss.getTotal()+"**************************");
	}
	@Test
	public void textModifyWebSiteMessage(){
		String[] ids = {"1"};
		ExecuteResult<String> result = baseWebSiteMessageService.modifyWebSiteMessage(ids, "2");
		System.out.println(result.getResult());
	}
	@Test
	public void textaddWebMessage(){
		WebSiteMessageDTO wsm=new WebSiteMessageDTO();
		wsm.setWmContext("【platformName】尊敬的用户，由于您未在7日内处理订单：201509090222801的退款/退货申请，系统已默认同意买家的退款/退货申请，如有疑问请联系platformName。");
		ExecuteResult<String> result = baseWebSiteMessageService.addWebMessage(wsm);
		System.out.println(result.getResult());
	}
}
