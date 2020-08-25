package com.camelot.tradecenter;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.tradecenter.dto.TradeOrderItemsDiscountDTO;
import com.camelot.tradecenter.service.TradeOrderItemsDiscountExportService;

public class TradeOrderItemsDiscountExportServiceImplTest {
	
	private TradeOrderItemsDiscountExportService tradeOrderItemsDiscountExportService = null;
    ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		tradeOrderItemsDiscountExportService = (TradeOrderItemsDiscountExportService) ctx.getBean("tradeOrderItemsDiscountExportService");
	}
	
	@Test
	public void add(){
		TradeOrderItemsDiscountDTO t = new TradeOrderItemsDiscountDTO();
		t.setOrderItemsId(1L);//trade_order_items表的ID
		t.setOrderId("1"); //订单ID
		t.setSkuId(1L); //skuID
		t.setMarkdownId(1L);//满减活动ID
		t.setMarkdownType(1);//满减活动类型；1：平台活动，2：店铺活动
		t.setMarkdownDiscount(new BigDecimal(100));//满减活动优惠金额
		t.setFullReductionId(1L);//直降活动ID
		t.setFullReductionType(1);//直降活动类型；1：平台直降，2：店铺直降
		t.setFullReductionDiscount(new BigDecimal(100));//直降活动优惠金额
		t.setCouponId("1");//优惠券编号
		t.setCouponType(2);//优惠券类型；1：平台优惠券，2：店铺优惠券
		t.setCouponDiscount(new BigDecimal(100));//优惠券优惠金额
		t.setIntegral(2);//商品使用积分
		t.setIntegralDiscount(new BigDecimal(100));//商品使用积分兑换的金额
		t.setIntegralType(11);//积分类型；1：平台积分，2：店铺积分
		this.tradeOrderItemsDiscountExportService.createOrderItemsDiscount(t);
	}
	
	@Test
	public void query(){
		TradeOrderItemsDiscountDTO t = new TradeOrderItemsDiscountDTO();
		t.setOrderItemsId(1L);//trade_order_items表的ID
		t.setOrderId("1"); //订单ID
		t.setSkuId(1L); //skuID
		t.setMarkdownId(1L);//满减活动ID
		t.setMarkdownType(1);//满减活动类型；1：平台活动，2：店铺活动
		t.setMarkdownDiscount(new BigDecimal(100));//满减活动优惠金额
		t.setFullReductionId(1L);//直降活动ID
		t.setFullReductionType(1);//直降活动类型；1：平台直降，2：店铺直降
		t.setFullReductionDiscount(new BigDecimal(100));//直降活动优惠金额
		t.setCouponId("1");//优惠券编号
		t.setCouponType(2);//优惠券类型；1：平台优惠券，2：店铺优惠券
		t.setCouponDiscount(new BigDecimal(100));//优惠券优惠金额
		t.setIntegral(2);//商品使用积分
		t.setIntegralDiscount(new BigDecimal(100));//商品使用积分兑换的金额
		t.setIntegralType(11);//积分类型；1：平台积分，2：店铺积分
//		this.tradeOrderItemsDiscountExportService.queryOrderItemsDiscountById(100);
		this.tradeOrderItemsDiscountExportService.queryOrderItemsDiscountByCondition(t, null);
	}
}
