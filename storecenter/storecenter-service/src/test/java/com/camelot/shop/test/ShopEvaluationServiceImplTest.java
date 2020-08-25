package com.camelot.shop.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopEvaluationDTO;
import com.camelot.storecenter.dto.ShopEvaluationQueryInDTO;
import com.camelot.storecenter.dto.ShopEvaluationTotalDTO;
import com.camelot.storecenter.service.ShopDomainExportService;
import com.camelot.storecenter.service.ShopEvaluationService;

public class ShopEvaluationServiceImplTest {
	private ShopEvaluationService shopEvaluationService;
	 ApplicationContext ctx= null;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		shopEvaluationService = (ShopEvaluationService) ctx.getBean("shopEvaluationService");
	}
	 
//	 
//	@Test
//	public void testAddShopEvaluation() {
//		ShopEvaluationDTO shopEvaluationDTO = new ShopEvaluationDTO();
//		shopEvaluationDTO.setUserId(698L);
//		shopEvaluationDTO.setByShopId(2000000212L);
//		shopEvaluationDTO.setShopDescriptionScope(5);
//		shopEvaluationDTO.setShopServiceScope(4);
//		shopEvaluationDTO.setShopArrivalScope(3);
//		shopEvaluationDTO.setResource("2");
//		shopEvaluationDTO.setOrderId(201504170049301L);
//		ExecuteResult<ShopEvaluationDTO> result = this.shopEvaluationService.addShopEvaluation(shopEvaluationDTO);
//		Assert.assertEquals(result.isSuccess(), true);
//	}

	@Test
	public void testQueryShopEvaluationTotal() {
		ShopEvaluationQueryInDTO shopEvaluationQueryInDTO = new ShopEvaluationQueryInDTO();
		shopEvaluationQueryInDTO.setByShopId(2000000245L);
		ExecuteResult<ShopEvaluationTotalDTO> result = shopEvaluationService.queryShopEvaluationTotal(shopEvaluationQueryInDTO);
		Assert.assertEquals(result.isSuccess(), true);
	}
	
	
//	@Test
//	public void testQueryShopEvaluationDTOList(){
//		ShopEvaluationQueryInDTO shopEvaluationQueryInDTO = new ShopEvaluationQueryInDTO();
//		shopEvaluationQueryInDTO.setOrderId(1L);
//		shopEvaluationQueryInDTO.setUserId(111L);
//		DataGrid<ShopEvaluationDTO> dg = shopEvaluationService.queryShopEvaluationDTOList(shopEvaluationQueryInDTO, null);
//		Assert.assertEquals(dg.getRows() != null, true);
//	}
}
