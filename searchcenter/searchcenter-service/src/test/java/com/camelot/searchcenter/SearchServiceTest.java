package com.camelot.searchcenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.searchcenter.dto.SearchItemSkuInDTO;
import com.camelot.searchcenter.dto.SearchItemSkuOutDTO;
import com.camelot.searchcenter.dto.SearchShopDTO;
import com.camelot.searchcenter.service.SearchExportService;

/**
 * 
 * <p>Description: [搜索测试]</p>
 * Created on 2015-3-6
 * @author  wangcs
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class SearchServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(SearchServiceTest.class);

	ApplicationContext ctx = null;
	private SearchExportService searchService = null;

	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		searchService = (SearchExportService) ctx.getBean("searchExportService");
	}

	@Test
	public void testSearchItem() {
		SearchItemSkuInDTO inDTO = new SearchItemSkuInDTO();
		inDTO.setKeyword("商品");
		inDTO.setOrderSort(7);
		inDTO.setBuyerId(712L);
		SearchItemSkuOutDTO result = searchService.searchItemSku(inDTO);
		logger.info("结果：" + JSON.toJSONString(result.getItemSkus().getRows()));
		Assert.assertEquals(true, true);
	}

	@Test
	public void testSearchShop() {
		DataGrid<SearchShopDTO> result = this.searchService.searchShop("F123", null, 1, 1000000626l,"0");
		Assert.assertEquals(true, true);
	}

	@Test
	public void testSearchShopByPlatformId() {
		DataGrid<SearchShopDTO> result = this.searchService.searchShopByPlatformId(2, "关键字", null, 1, 1000000626l,"0");
		// logger.info(JSON.toJSONString(result));
	}
}
