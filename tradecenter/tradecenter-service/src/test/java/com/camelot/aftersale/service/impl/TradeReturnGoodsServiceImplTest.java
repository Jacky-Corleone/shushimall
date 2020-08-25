package com.camelot.aftersale.service.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.aftersale.service.TradeReturnGoodsService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

public class TradeReturnGoodsServiceImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TradeReturnGoodsServiceImplTest.class);
    private ApplicationContext ctx;
    private TradeReturnGoodsService tradeReturnGoodsService;
    @Before
    public void setUp() throws Exception {
        ctx = new ClassPathXmlApplicationContext("test.xml");
        tradeReturnGoodsService = (TradeReturnGoodsService) ctx.getBean("tradeReturnGoodsService");
    }

    @Ignore
    public void testCreateTradeReturnGoodsDTO() throws Exception {
        TradeReturnGoodsDTO t=new TradeReturnGoodsDTO();
        t.setBuyerAddress("testmmm");
        t.setBuyerName("liuqingshan");
        t.setDeletedFlag("0");
        ExecuteResult<TradeReturnGoodsDTO> res= tradeReturnGoodsService.createTradeReturnGoodsDTO(t);
        LOGGER.info("操作结果{}", JSON.toJSON(res));
        Assert.assertNotNull(res);

    }

    @Ignore
    public void testUpdateTradeReturnGoodsDTO() throws Exception {
        TradeReturnGoodsDTO t=new TradeReturnGoodsDTO();
        t.setBuyerAddress("testmmmYYYYY");
        t.setBuyerName("liuqingshanYYYYY");
        t.setDeletedFlag("0");
        t.setId(1L);
        ExecuteResult<TradeReturnGoodsDTO> res= tradeReturnGoodsService.updateSelective(t);
        LOGGER.info("操作结果{}", JSON.toJSON(res));
        Assert.assertNotNull(res);
    }
//
//    @Test
//    public void testDeleteById() throws Exception {
//
//    }
//
//    @Test
//    public void testDeleteAll() throws Exception {
//
//    }
//
    @Test
    public void testSearchTradeReturnGoodsDTOs() throws Exception {
        TradeReturnGoodsDTO t=new TradeReturnGoodsDTO();
        t.setBuyerAddress("testmmmYYYYY");


        DataGrid<TradeReturnGoodsDTO> res= tradeReturnGoodsService.searchTradeReturnGoodsDTOs(new Pager<TradeReturnGoodsDTO>(), t);
        LOGGER.info("操作结果{}", JSON.toJSON(res));
        Assert.assertNotNull(res);

    }

    @Test
    public void testSearchByOrderIdAndState() throws Exception {
    	Integer[] i = {6,7,8,9};
    	tradeReturnGoodsService.searchByOrderIdAndState("201510120275901", i);
    }
//
//    @Test
//    public void testUpdateSelective() throws Exception {
//
//    }
//
//    @Test
//    public void testUpdateSelectiveWithDateTimeCheck() throws Exception {
//
//    }
//
//    @Test
//    public void testSearchByCondition() throws Exception {
//
//    }
//
//    @Test
//    public void testUpdateSelectiveByIdList() throws Exception {
//
//    }
//
//    @Test
//    public void testUpdateAllByIdList() throws Exception {
//
//    }
//
//    @Test
//    public void testDefunctById() throws Exception {
//
//    }
//
//    @Test
//    public void testDefunctByIdList() throws Exception {
//
//    }
}