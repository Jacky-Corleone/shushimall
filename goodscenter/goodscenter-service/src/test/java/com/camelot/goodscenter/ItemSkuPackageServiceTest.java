package com.camelot.goodscenter;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemSkuPackageDTO;
import com.camelot.goodscenter.service.ItemSkuPackageService;
import com.camelot.openplatform.common.ExecuteResult;

public class ItemSkuPackageServiceTest {
	private ApplicationContext ctx;
	private ItemSkuPackageService itemSkuPackageService;

	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		itemSkuPackageService = ctx.getBean(ItemSkuPackageService.class);
	}
	
	@Test
	public void testAdd() {
		itemSkuPackageService.add(1000000009l, "1000000002,1000000012,1000000022".split(","));
	}
	
	@Test
	public void testDelete() {
		ItemSkuPackageDTO dto = new ItemSkuPackageDTO();
//		dto.setPackageItemId(1000000005l);
//		dto.setPackageSkuId(1000000009l);
//		dto.setSubItemId(1000000007l);
		dto.setSubSkuId(1000000022l);
		itemSkuPackageService.delete(dto);
	}
	
	@Test
	public void testGetPackages() {
		ItemSkuPackageDTO dto = new ItemSkuPackageDTO();
//		dto.setPackageItemId(1000000005l);
//		dto.setPackageSkuId(1000000009l);
//		dto.setSubItemId(1000000007l);
		dto.setSubSkuId(1000000022l);
		itemSkuPackageService.getPackages(dto);
	}
	
	@Test
	public void testGetPackageSkus() {
		Long packageItemId = 1000000258L;
		Long packageSkuId = 1000000327L;
		String areaCode = "1201";
		Integer qty = 1;
		ExecuteResult<List<ItemDTO>> result = itemSkuPackageService.getPackageSkus(packageItemId, packageSkuId, areaCode, qty, 1, null);
		Assert.assertEquals(result.isSuccess(), true);
	}
}
