package com.camelot.tradecenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.activity.dto.ActivityRecordDTO;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.dto.combin.TradeOrderCreateDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;

/**
 * 平台类目列表测试类
 * @author 周立明
 *
 */
public class TradeOrderExportServiceImplAllTest{
	
	private static final Logger logger = LoggerFactory.getLogger(TradeOrderExportServiceImplAllTest.class);
	
	private TradeOrderExportService tradeOrderExportService = null;
    ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		tradeOrderExportService = (TradeOrderExportService) ctx.getBean("tradeOrderExportService");
	}
	//平台类目添加方法测试类
	@Test
	public void createOrderTest(){
		TradeOrdersDTO parent = new TradeOrdersDTO();
		parent.setAfterService(1);
		parent.setBuyerId(1000000002L);
		parent.setCityId(1L);
		parent.setCountyId(1L);
		parent.setDetailAddress("测试详细地址");
		parent.setEmail("1333@163.com");
		parent.setFreight(new BigDecimal(100L));
		parent.setFullAddress("测试全地址");
		parent.setInvoice(2);
		parent.setInvoiceTitle("测试发票抬头");
		parent.setInvoiceId(23L);
		parent.setLogisticsCompany("测试物流公司");
		parent.setLogisticsNo("123123123");
		parent.setMemo("备注");
		parent.setMobile("18566666666");
		parent.setName("测试用户名");
		parent.setParentOrderId("1");
		parent.setPaymentPrice(new BigDecimal("1123"));
		parent.setPaymentMethod(1);
		parent.setPaymentType(1);
		parent.setPhone("0532-88888888");
		parent.setProvinceId(1L);
		parent.setSellerId(1L);
		parent.setShipmentType(1);
		parent.setShopId(1L);
		parent.setTotalDiscount(new BigDecimal(199));
		parent.setTotalPrice(new BigDecimal(188000));
		parent.setOrderType(1);
		parent.setContractNo("88888888");
		parent.setIntegral(1);
		
		List<TradeOrdersDTO> subOrders = new ArrayList<TradeOrdersDTO>();
		for (int i = 0; i < 3; i++) {
			TradeOrdersDTO order = new TradeOrdersDTO();
			order.setAfterService(1);
			order.setBuyerId(1000000002L);
			order.setCityId(1L);
			order.setCountyId(1L);
			order.setDetailAddress("测试详细地址");
			order.setEmail("1333@163.com");
			order.setFreight(new BigDecimal(100L));
			order.setFullAddress("测试全地址");
			order.setInvoice(2);
			order.setInvoiceTitle("测试发票抬头");
			order.setInvoiceId(23L);
			order.setLogisticsCompany("测试物流公司");
			order.setLogisticsNo("123123123");
			order.setMemo("备注");
			order.setMobile("18566666666");
			order.setName("测试用户名");
			order.setParentOrderId("1");
			order.setPaymentPrice(new BigDecimal("1123"));
			order.setPaymentMethod(4);
			order.setPaymentType(1);
			order.setPhone("0532-88888888");
			order.setProvinceId(1L);
			order.setSellerId(1L);
			order.setShipmentType(1);
			order.setShopId(1L);
			order.setTotalDiscount(new BigDecimal(199));
			order.setTotalPrice(new BigDecimal(188000));
			order.setContractNo("88888888");
			order.setOrderType(1);
			order.setIntegral(1);
			
			List<TradeOrderItemsDTO> items = new ArrayList<TradeOrderItemsDTO>();
			TradeOrderItemsDTO item = new TradeOrderItemsDTO();
			item.setAreaId(1L);
			item.setItemId(1L);
			item.setNum(10);
			item.setPrimitivePrice(new BigDecimal(1000));
			item.setPromotionDiscount(new BigDecimal(90));
			item.setPromotionId(1L);
			item.setPromotionType(1);
			item.setSkuId(1L);
			item.setCid(1L);
			item.setSkuName("测试SKU1");
			item.setPayPrice(new BigDecimal(910));
			item.setPayPriceTotal(new BigDecimal(910));
			item.setShopFreightTemplateId(1L);
			item.setDeliveryType(2);
			items.add(item);
			item = new TradeOrderItemsDTO();
			item.setAreaId(1L);
			item.setItemId(1L);
			item.setNum(10);
			item.setPrimitivePrice(new BigDecimal(1000));
			item.setPromotionDiscount(new BigDecimal(90));
			item.setPromotionId(1L);
			item.setPromotionType(1);
			item.setSkuId(1L);
			item.setCid(1L);
			item.setSkuName("测试SKU1");
			item.setPayPrice(new BigDecimal(910));
			item.setPayPriceTotal(new BigDecimal(910));
			item.setShopFreightTemplateId(1L);
			item.setDeliveryType(2);
			item.setIntegral(1);
			items.add(item);
			order.setItems(items);
			subOrders.add(order);
		}
		
		TradeOrderCreateDTO dto = new TradeOrderCreateDTO();
		dto.setParentOrder(parent);
		dto.setSubOrders(subOrders);
		ExecuteResult<TradeOrderCreateDTO> result = this.tradeOrderExportService.createOrder(dto);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	/**
	 * 
	 * <p>Discription:[创建订单测试]</p>
	 * Created on 2015年12月15日
	 * @author:[宋文斌]
	 */
	@Test
	public void testCreateOrder(){
		TradeOrdersDTO parentOrder = new TradeOrdersDTO();
		parentOrder.setAuditorId(1000000046L);
		parentOrder.setBuyerId(1000000046L);
		parentOrder.setCityId(1306L);
		parentOrder.setContractNo("");
		parentOrder.setDetailAddress("柯莱特");
		parentOrder.setFreight(BigDecimal.ZERO);
		parentOrder.setFullAddress("河北省 保定市 新市区 柯莱特");
		parentOrder.setInvoice(1);
		parentOrder.setInvoiceTitle("");
		parentOrder.setMemo("");
		parentOrder.setMobile("13501133340");
		parentOrder.setName("宋文斌");
		parentOrder.setOrderType(2);
		parentOrder.setPaid(1);
		parentOrder.setPaymentMethod(2);
		parentOrder.setPaymentPrice(BigDecimal.valueOf(100.00));
		parentOrder.setPhone("");
		parentOrder.setPromoCode("");
		parentOrder.setShipmentType(1);
		parentOrder.setTotalPrice(BigDecimal.valueOf(100.00));
		
		List<TradeOrdersDTO> subOrders = new ArrayList<TradeOrdersDTO>();
		TradeOrdersDTO subOrder = new TradeOrdersDTO();
		subOrder.setAuditorId(1000000046L);
		subOrder.setBuyerId(1000000046L);
		subOrder.setCityId(1306L);
		subOrder.setContractNo("");
		subOrder.setDetailAddress("柯莱特");
		subOrder.setFreight(BigDecimal.valueOf(50.00));
		subOrder.setFullAddress("河北省 保定市 新市区 柯莱特");
		subOrder.setInvoice(1);
		subOrder.setInvoiceTitle("");
		subOrder.setMemo("");
		subOrder.setMobile("13501133340");
		subOrder.setName("宋文斌");
		subOrder.setOrderType(2);
		subOrder.setPaid(1);
		subOrder.setPaymentMethod(2);
		subOrder.setPaymentPrice(BigDecimal.valueOf(70.00));
		subOrder.setPhone("");
		subOrder.setPromoCode("");
		subOrder.setSellerId(1000000008L);
		subOrder.setShipmentType(1);
		subOrder.setShopId(2000000004L);
		subOrder.setTotalDiscount(BigDecimal.valueOf(30));
		subOrder.setTotalPrice(BigDecimal.valueOf(100.00));
		
		ActivityRecordDTO recordDTO = new ActivityRecordDTO();
		recordDTO.setDiscountAmount(BigDecimal.valueOf(30));
		recordDTO.setPromotionId("320");
		recordDTO.setShopId(2000000004L);
		recordDTO.setType(6);
		
		subOrder.getActivityRecordDTOs().add(recordDTO);
		
		List<TradeOrderItemsDTO> items = new ArrayList<TradeOrderItemsDTO>();
		TradeOrderItemsDTO itemsDTO = new TradeOrderItemsDTO();
		itemsDTO.setAreaId(13L);
		itemsDTO.setCid(444L);
		itemsDTO.setDeliveryType(1);
		itemsDTO.setItemId(1000000095L);
		itemsDTO.setNum(1);
		itemsDTO.setPayPrice(BigDecimal.valueOf(100));
		itemsDTO.setPayPriceTotal(BigDecimal.valueOf(70));
		itemsDTO.setPrimitivePrice(BigDecimal.valueOf(100));
		itemsDTO.setPromotionDiscount(BigDecimal.valueOf(30));
		itemsDTO.setPromotionId(320L);
		itemsDTO.setPromotionType(2);
		itemsDTO.setShopFreightTemplateId(829L);
		itemsDTO.setSkuId(1000000120L);
		
		items.add(itemsDTO);
		
		subOrder.setItems(items);
		subOrders.add(subOrder);
		
		TradeOrderCreateDTO createDTO = new TradeOrderCreateDTO();
		createDTO.setParentOrder(parentOrder);
		createDTO.setSubOrders(subOrders);
		tradeOrderExportService.createOrder(createDTO);
	}
	
//	@Test
//	public void queryOrderTest() throws ParseException{
//
//		Pager<TradeOrdersQueryInDTO> pager = new Pager<TradeOrdersQueryInDTO>();
//		TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
//		inDTO.setBuyerId(1L);
//		inDTO.setCancelFlag(1);
//		inDTO.setCreateEnd(new Date());
//		inDTO.setCreateStart(new Date());
//		inDTO.setEvaluate(1);
//		inDTO.setItemName("123");
//		inDTO.setLocked(1);
//		inDTO.setOrderId("201511040353001");
//		inDTO.setSellerId(1L);
//		inDTO.setShopId(1L);
//		inDTO.setShopName("店铺名称");
//		inDTO.setState(TradeOrdersStateEnum.PAYING.getCode());
//		inDTO.setBuyerId(1L);
//		List<Long> ids = new ArrayList<Long>();
//		ids.add(1L);
//		inDTO.setBuyerIdList(ids);
//		inDTO.setSellerIdList(ids);
//		inDTO.setSellerEvaluate(1);
//		inDTO.setShopIdList(ids);
//		List<Integer> statList = new ArrayList<Integer>();
//		inDTO.setStateList(statList);
//		inDTO.setUpdateEnd(new Date());
//		inDTO.setUpdateStart(new Date());
//		inDTO.setDeleted(1);
//		inDTO.setPaymentTimeEnd(new Date());
//		inDTO.setPaymentTimeStart(new Date());
//		inDTO.setPaymentType(1);
//		ExecuteResult<DataGrid<TradeOrdersDTO>>  result = this.tradeOrderExportService.queryOrders(inDTO, pager);
//		logger.info(JSON.toJSONString(result));
//		Assert.assertEquals(true, result.isSuccess());
//		
//	}
	

	
	
//	@Test
//	public void queryOrderQtyTest() throws ParseException{
//		TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
//		inDTO.setBuyerId(1L);
//		inDTO.setCancelFlag(1);
//		inDTO.setCreateEnd(new Date());
//		inDTO.setCreateStart(new Date());
//		inDTO.setEvaluate(1);
//		inDTO.setItemName("123");
//		inDTO.setLocked(1);
//		inDTO.setOrderId(201503120004101L);
//		inDTO.setSellerId(1L);
//		inDTO.setShopId(1L);
//		inDTO.setShopName("店铺名称");
//		inDTO.setState(TradeOrdersStateEnum.PAYING.getCode());
//		ExecuteResult<Long>  result = this.tradeOrderExportService.queryOrderQty(inDTO);
//		logger.info(JSON.toJSONString(result));
//		Assert.assertEquals(true, result.isSuccess());
//	}
//	
//	@Test
//	public void testModifyOrderStatus(){
//		ExecuteResult<String> result = this.tradeOrderExportService.modifyOrderStatus("201505040071901",TradeOrdersStateEnum.EVALUATING.getCode());
//		Assert.assertEquals(true, result.isSuccess());
//	}
//	
//	@Test
//	public void testDeleteOrderById(){
//		ExecuteResult<String> result = this.tradeOrderExportService.deleteOrderById("201505050072901");
//		Assert.assertEquals(true, result.isSuccess());
//	}
//	
//	@Test
//	public void testGetOrderById(){
//		ExecuteResult<TradeOrdersDTO> result = this.tradeOrderExportService.getOrderById("201503200015301");
//		Assert.assertEquals(true, result.isSuccess());
//	}
	
//	@Test
//	public void testModifyOrderPayStatus(){
//		TradeOrdersDTO inDTO = new TradeOrdersDTO();
//		inDTO.setOrderId("201505130045801");
//		inDTO.setPaymentType(1);
//		inDTO.setState(TradeOrdersStateEnum.DELIVERING.getCode());
//		ExecuteResult<String> result = this.tradeOrderExportService.modifyOrderPayStatus(inDTO);
//		Assert.assertEquals(true, result.isSuccess());
//	}
	
//	@Test
//	public void testModifyOrderPrice(){
//		TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
//		inDTO.setOrderId("201508270204401");
//		TradeOrdersDTO inOrder =  this.tradeOrderExportService.queryOrders(inDTO, null).getResult().getRows().get(0);
//		inOrder.setFreight(new BigDecimal("800"));
//		TradeOrderItemsDTO item = inOrder.getItems().get(0);
//		item.setPrimitivePrice(new BigDecimal("2000"));
//		item.setPromotionDiscount(new BigDecimal("400"));
//		inOrder.getItems().clear();
//		inOrder.getItems().add(item);
//		ExecuteResult<String>  result = this.tradeOrderExportService.modifyOrderPrice(inOrder);
//		Assert.assertEquals(true, result.isSuccess());
//	}
//	
//	/**
//	 * 
//	 * <p>Description: [模拟订单商品改价]</p>
//	 * Created on 2015年11月19日
//	 * @author:[宋文斌]
//	 */
//	@Test
//	public void testModifyOrderPrice2(){
//		TradeOrdersDTO inDTO = new TradeOrdersDTO();
//		inDTO.setSellerId(1000001229L);
//		inDTO.setOrderId("201511190399401");
//		List<TradeOrderItemsDTO>  items = new ArrayList<TradeOrderItemsDTO>();
//		TradeOrderItemsDTO toid = new TradeOrderItemsDTO();
//		toid.setOrderId("201511190399401");
//		toid.setItemId(1000000568L);
//		toid.setSkuId(1000001038L);
//		toid.setPromotionDiscount(new BigDecimal(100));
//		items.add(toid);
//		inDTO.setItems(items);
//		ExecuteResult<String>  result = this.tradeOrderExportService.modifyOrderPrice(inDTO);
//		Assert.assertEquals(true, result.isSuccess());
//	}
	
//	@Test
//	public void delayDeliveryTest(){
//		
//		 ExecuteResult<String> result = tradeOrderExportService.delayDelivery("201504140046001");
//		 Assert.assertEquals(true, result.isSuccess());
//	}
//	@Test
//	public void confirmOrderAutoTest(){
//		tradeOrderExportService.confirmOrderAuto();
//		
//	}
//	@Test
//	public void testDeleteTradeOrders(){
//		ExecuteResult<String> result = this.tradeOrderExportService.deleteTradeOrders("201505050072901");
//		Assert.assertEquals(true, result.isSuccess());
//	}
//	
//	@Test
//	public void testModifyEvaluationStatus(){
//		TradeOrdersDTO inDTO = new TradeOrdersDTO();
//		inDTO.setOrderId("201504160047401");
//		inDTO.setEvaluate(2);
//		inDTO.setSellerEvaluate(2);
//		ExecuteResult<String> result = this.tradeOrderExportService.modifyEvaluationStatus(inDTO);
//		Assert.assertEquals(true, result.isSuccess());
//	}
//	
	@Test
	public void countNum(){
		ExecuteResult<List<TradeOrderItemsDTO>> result = this.tradeOrderExportService.countNumber("XY201512111154453721");
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void countCost(){

		ExecuteResult<List<TradeOrderItemsDTO>> result = this.tradeOrderExportService.countCost("XY201512111154453721");
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void testModifyOrderPrice(){
		TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
		inDTO.setOrderId("201512100033001");
		TradeOrdersDTO inOrder =  this.tradeOrderExportService.queryOrders(inDTO, null).getResult().getRows().get(0);
		inOrder.setFreight(new BigDecimal("0"));
		TradeOrderItemsDTO item = inOrder.getItems().get(0);
		item.setPrimitivePrice(new BigDecimal("4"));
		item.setPromotionDiscount(new BigDecimal("1"));
		inOrder.getItems().clear();
		inOrder.getItems().add(item);
		ExecuteResult<String>  result = this.tradeOrderExportService.modifyOrderPrice(inOrder);
		Assert.assertEquals(true, result.isSuccess());
	}
	
//	@Test
//	public void testModifyLogisticsInfo(){
//		TradeOrdersDTO inDTO = new TradeOrdersDTO();
//		inDTO.setOrderId("201504300070301");
//		inDTO.setLogisticsCompany("测试物流信息录入");
//		ExecuteResult<String> result = this.tradeOrderExportService.modifyLogisticsInfo(inDTO);
//		Assert.assertEquals(true, result.isSuccess());
//	}
//
//    @Test
//    public void testUpdateTradeOrdersDTOSelective(){
//        TradeOrdersDTO orderDTO = new TradeOrdersDTO();
//        orderDTO.setOrderId("2015062301134");
//        orderDTO.setDeliverTime(new Date());
//        this.tradeOrderExportService.updateTradeOrdersDTOSelective(orderDTO);
//    }
}
