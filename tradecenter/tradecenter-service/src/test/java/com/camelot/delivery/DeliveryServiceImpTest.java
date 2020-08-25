package com.camelot.delivery;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.delivery.dto.DeliveryDTO;
import com.camelot.delivery.dto.DeliveryReturnMessage;
import com.camelot.delivery.dto.SendBodyDTO;
import com.camelot.delivery.service.DeliveryService;

public class DeliveryServiceImpTest {
	
	private static final Logger LOG = LoggerFactory.getLogger(DeliveryServiceImp.class);

	ApplicationContext ctx = null;
	DeliveryService deliveryService = null; 
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		deliveryService = (DeliveryService) ctx.getBean("deliveryService");
	}
	
	@Test
	public void testSaveDeliverStatus() {
		
		SendBodyDTO sendBody = new SendBodyDTO();
		DeliveryDTO delivery = new DeliveryDTO();
		
		delivery.setOrderId("201601130158201");
		delivery.setOrderItemId(5698L);
		delivery.setItemId(5698L);
		delivery.setDeliveryCompanyCode("yunda");
		delivery.setDeliveryNumber("3100916948849");// 762847362889
		delivery.setIsSametemplate("no");
		delivery.setDeliveryRemark("测试");
		delivery.setStatus(0);
		delivery.setMessage("测试");
		delivery.setCreateTime(new Date());
		
		sendBody.setCompany("yunda");
		sendBody.setNumber("3100916948849");
		
		String code = deliveryService.addDeliverInfo(sendBody, delivery);
		LOG.info("code：{}", code);
	}
	
	@Test
	public void testUpdateDeliverInfo() {
		
		SendBodyDTO sendBody = new SendBodyDTO();
		sendBody.setCompany("yunda");
		sendBody.setNumber("3100916948851");
		
		DeliveryDTO delivery = new DeliveryDTO();
		delivery.setDeliveryCompanyCode("yunda");
		delivery.setDeliveryNumber("3100916948851");
		delivery.setDeliveryRemark("是飒飒飒飒飒飒啊啊啊啊啊啊啊啊啊啊");
		delivery.setIsSametemplate("no");
		delivery.setItemId(1000000142L);
		delivery.setOrderId("201601130158301");
		delivery.setOrderItemId(5699L);
		delivery.setShopFreightTemplateId(877L);
		
		String code = deliveryService.updateDeliverInfo(sendBody, delivery);
		LOG.info("code：{}", code);
	}

	
	@Test
	 public void getShopFreightTemplate(){
		Long itemId= 1000000490l;
		
   }
	@Test 
	public void testupdateDeliverMessage(){
		deliveryService.updateDeliverMessage("{\"status\":\"polling\",\"billstatus\":\"got\",\"message\":\"寄件\",\"lastResult\":{\"message\":\"ok\",\"nu\":\"3100916948852\",\"ischeck\":\"0\",\"com\":\"yunda\",\"status\":\"200\",\"data\":[{\"time\":\"2016-01-11 04:37:39\",\"context\":\"在江苏苏州分拨中心进行装车扫描，即将发往：新疆乌鲁木齐分拨中心\",\"ftime\":\"2016-01-11 04:37:39\",\"areaCode\":\"320500000000\",\"areaName\":\"江苏,苏州市\",\"status\":\"在途\"},{\"time\":\"2016-01-11 04:33:04\",\"context\":\"在分拨中心江苏苏州分拨中心进行卸车扫描\",\"ftime\":\"2016-01-11 04:33:04\",\"areaCode\":\"320500000000\",\"areaName\":\"江苏,苏州市\",\"status\":\"在途\"},{\"time\":\"2016-01-10 19:34:37\",\"context\":\"在浙江杭州分拨中心进行装车扫描，即将发往：江苏苏州分拨中心\",\"ftime\":\"2016-01-10 19:34:37\",\"areaCode\":\"330100000000\",\"areaName\":\"浙江,杭州市\",\"status\":\"在途\"},{\"time\":\"2016-01-10 02:30:49\",\"context\":\"在分拨中心浙江杭州分拨中心进行称重扫描\",\"ftime\":\"2016-01-10 02:30:49\",\"areaCode\":\"330100000000\",\"areaName\":\"浙江,杭州市\",\"status\":\"在途\"},{\"time\":\"2016-01-09 20:37:52\",\"context\":\"在安徽黄山公司进行到件扫描\",\"ftime\":\"2016-01-09 20:37:52\",\"areaCode\":\"341000000000\",\"areaName\":\"安徽,黄山市\",\"status\":\"在途\"},{\"time\":\"2016-01-09 20:06:54\",\"context\":\"在安徽黄山公司进行下级地点扫描，将发往：新疆乌鲁木齐分拨中心\",\"ftime\":\"2016-01-09 20:06:54\",\"areaCode\":\"341000000000\",\"areaName\":\"安徽,黄山市\",\"status\":\"在途\"},{\"time\":\"2016-01-09 18:52:16\",\"context\":\"在安徽黄山公司进行到件扫描\",\"ftime\":\"2016-01-09 18:52:16\",\"areaCode\":\"341000000000\",\"areaName\":\"安徽,黄山市\",\"status\":\"在途\"}],\"state\":\"0\",\"condition\":\"00\"}}");
	}

}
