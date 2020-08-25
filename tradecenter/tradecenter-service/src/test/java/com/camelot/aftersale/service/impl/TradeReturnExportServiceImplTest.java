package com.camelot.aftersale.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDetailDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnInfoDto;
import com.camelot.aftersale.dto.returngoods.TradeReturnInfoQueryDto;
import com.camelot.aftersale.dto.returngoods.TradeReturnPicDTO;
import com.camelot.aftersale.service.TradeReturnExportService;
import com.camelot.aftersale.service.TradeReturnGoodsService;
import com.camelot.common.enums.TradeReturnStatusEnum;
import com.camelot.common.util.DateUtils;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;

public class TradeReturnExportServiceImplTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TradeReturnExportServiceImplTest.class);
	private ApplicationContext ctx;
	private TradeReturnExportService tradeReturnExportService;
	private TradeReturnGoodsService tradeReturnGoodsService;
	private TradeOrderExportService tradeOrderExportService;

	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		tradeReturnExportService = (TradeReturnExportService) ctx
				.getBean("tradeReturnExportService");
		tradeReturnGoodsService = (TradeReturnGoodsService) ctx
				.getBean("tradeReturnGoodsService");
		tradeOrderExportService = (TradeOrderExportService) ctx
				.getBean("tradeOrderExportService");
	}

	@Test
	public void testCreateTradeReturn() throws Exception {
		TradeReturnInfoDto insertDto = new TradeReturnInfoDto();
		TradeReturnGoodsDTO goodsDTO = new TradeReturnGoodsDTO();
		goodsDTO.setBuyerName("nnnnnnnnnnnnn");
		goodsDTO.setState(1);
		goodsDTO.setBuyId("222");
		goodsDTO.setSellerId("3333");
		goodsDTO.setOrderId("201510110273201");
		goodsDTO.setOrderPrice(new BigDecimal(22L));

		List<TradeReturnPicDTO> picDTOList = new ArrayList<TradeReturnPicDTO>();
		TradeReturnPicDTO returnPicDTO = new TradeReturnPicDTO();
		returnPicDTO.setPicUrl("tttttttttttttttttttttttttt");
		picDTOList.add(returnPicDTO);
		goodsDTO.setPicDTOList(picDTOList);

		insertDto.setTradeReturnDto(goodsDTO);

		List<TradeReturnGoodsDetailDTO> detailDTOList = new ArrayList<TradeReturnGoodsDetailDTO>();
		TradeReturnGoodsDetailDTO detailDTO = new TradeReturnGoodsDetailDTO();
		detailDTO.setGoodsId(111221L);
		// detailDTO.setSkuId(222L);
		// detailDTO.setSkuName("yyyyyy");
		// detailDTO.setPayPrice(new BigDecimal(222L));
		detailDTOList.add(detailDTO);

		insertDto.setTradeReturnGoodsDetailList(detailDTOList);

		ExecuteResult<TradeReturnGoodsDTO> res = tradeReturnExportService
				.createTradeReturn(insertDto, TradeReturnStatusEnum.AUTH);
		Assert.assertNotNull(res);
	}

	@Test
	public void testUpdateTradeReturnStatus() throws Exception {
		TradeReturnGoodsDTO dto = new TradeReturnGoodsDTO();
		List<TradeReturnPicDTO> list = new ArrayList<TradeReturnPicDTO>();
		TradeReturnPicDTO d = new TradeReturnPicDTO();
		// d.setPicUrl("sdfdsfdsds");
		// list.add(d);
		// dto.setPicDTOList(list);
		dto.setId(1688L);
		dto.setConfirmStatus("0");//待审核确认
		dto.setState(TradeReturnStatusEnum.PLATFORMTOREFUND.getCode());
		dto.setSellerId("1000000019");
		// dto.setState(6);

		ExecuteResult<TradeReturnGoodsDTO> res = tradeReturnExportService
				.updateTradeReturnStatus(dto,
						TradeReturnStatusEnum.PLATFORMTOREFUND);
		Assert.assertNotNull(res);
	}

	@Test
	public void testGetTradeReturnInfoDto() throws Exception {
		TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
		TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
		tradeReturnDto.setOrderId("201507100152202");
		// tradeReturnDto.setApplyDtBegin(DateUtils.parse("2015-06-01 00:00:00",
		// DateUtils.YYDDMMHHMMSS));
		// tradeReturnDto.setApplyDtEnd(DateUtils.parse("2015-06-02 00:00:00",
		// DateUtils.YYDDMMHHMMSS));

		queryDto.setTradeReturnDto(tradeReturnDto);
		DataGrid<TradeReturnGoodsDTO> res = tradeReturnExportService
				.getTradeReturnInfoDto(new Pager<TradeReturnGoodsDTO>(),
						queryDto);
		Assert.assertNotNull(res);
	}

	@Test
	public void testGetTradeReturnInfoByReturnGoodsId() throws Exception {

		ExecuteResult<TradeReturnInfoDto> res = tradeReturnExportService
				.getTradeReturnInfoByReturnGoodsId("26");
		Assert.assertNotNull(res);
	}

	@Test
	public void test_returnGoodsJob() throws Exception {
		TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
		tradeReturnDto.setState(3); // 3 退款申请达成,等待买家发货
		Date preDate = DateUtils.dayOffset(new Date(), -3);
		tradeReturnDto.setTimeNode(preDate);

		List<TradeReturnGoodsDTO> resData = tradeReturnGoodsService
				.searchByCondition(tradeReturnDto);
		if (resData != null && resData.size() > 0) {

			for (TradeReturnGoodsDTO item : resData) {
				item.setState(5); // 5 退款关闭
				item.setLastUpdDt(new Date());
				tradeReturnGoodsService.updateSelective(item);

			}
		}
	}

	@Test
	public void testGetTradeReturnDetail() throws Exception {
		TradeReturnGoodsDetailDTO dto = new TradeReturnGoodsDetailDTO();
		Long[] goodsIds = new Long[2];
		goodsIds[0] = 45L;
		goodsIds[1] = 46L;
		dto.setReturnGoodsIds(goodsIds);
		ExecuteResult<List<TradeReturnGoodsDetailDTO>> res = tradeReturnExportService
				.getTradeReturnGoodsDetaiByCondition(dto);
		Assert.assertNotNull(res);
	}

	@Test
	public void testRefundResult() {
		Map<String, String> params = new HashMap<String, String>();
		String payBank = "AP";
		params.put("sign","27d70e7ccf780c164df1e31412d8fa25");
		params.put("result_details","2015102021001004760034692652^0.03^SUCCESS");
		params.put("notify_time","2015-10-20 15:32:30");
		params.put("sign_type","MD5");
		params.put("notify_type","batch_refund_notify");
		params.put("notify_id","d93dfe41a62a7727731a3e244eaf9b5nk8");
		params.put("batch_no","2015102020120151020010474");
		params.put("success_num","1");
		ExecuteResult<Integer> er = tradeReturnExportService.refundResult(params,payBank);
		Assert.assertNotNull(er);
	}
	
	@Test
	public void testGetReturnNum() {
		Integer[] states = { TradeReturnStatusEnum.SUCCESS.getCode(),
				TradeReturnStatusEnum.REFUNDING.getCode(),
				TradeReturnStatusEnum.PLATFORMTOREFUND.getCode(),
				TradeReturnStatusEnum.PLATFORMDEALING.getCode(), };
		ExecuteResult<TradeOrdersDTO> orderRes = tradeOrderExportService.getOrderById("201512310103401");
		if (orderRes.isSuccess() && orderRes.getResult() != null) {
			TradeOrdersDTO orderItem = orderRes.getResult();
			ExecuteResult<Map<String, Object>> returnNumResult = tradeReturnExportService.getReturnNum(orderItem, states);
			LOGGER.info(JSON.toJSONString(returnNumResult));
		}
	}
}