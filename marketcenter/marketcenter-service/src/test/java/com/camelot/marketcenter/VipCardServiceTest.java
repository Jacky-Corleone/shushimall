package com.camelot.marketcenter;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.maketcenter.dto.VipCardDTO;
import com.camelot.maketcenter.service.PromotionService;
import com.camelot.maketcenter.service.VipCardService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

public class VipCardServiceTest {
	private VipCardService vipCardService;
	ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		vipCardService = (VipCardService) ctx.getBean("vipCardService");
	}
	
	@Test
	public void testQueryVipCardList() {
		VipCardDTO vipCardDTO = new VipCardDTO();
		ExecuteResult<DataGrid<VipCardDTO>> result = vipCardService.queryVipCardList(vipCardDTO, null);
		DataGrid<VipCardDTO> dg = result.getResult();
		List<VipCardDTO> vipCardDTOList = dg.getRows();
		System.out.println(vipCardDTOList.size());
	}

	@Test
	public void testQueryVipCard() {
		VipCardDTO vipCardDTO = new VipCardDTO();
		vipCardDTO.setVip_id(123);
		ExecuteResult<VipCardDTO> result = vipCardService.queryVipCard(vipCardDTO);
		vipCardDTO = result.getResult();
		System.out.println(vipCardDTO.getBuyer_id());
	}
	
	@Test
	public void testQueryCountVipCard() {
		ExecuteResult<Long> result = vipCardService.queryCountVipCard(null);
		System.out.println(result.getResult());
	}
	
	@Test
	public void testUpdateVip() {
		VipCardDTO vipCardDTO = new VipCardDTO();
		vipCardDTO.setId(1l);
		vipCardDTO.setResidual_amount(new BigDecimal(3));
		ExecuteResult<VipCardDTO> result = vipCardService.updateVipCard(vipCardDTO);
		System.out.println(result.getResult());
	}

}
