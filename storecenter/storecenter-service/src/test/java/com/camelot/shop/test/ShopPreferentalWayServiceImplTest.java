package com.camelot.shop.test;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopPreferentialWayDTO;
import com.camelot.storecenter.service.ShopPreferentialWayService;
import com.camelot.storecenter.service.ShopPreferentialWayService;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ShopPreferentalWayServiceImplTest extends TestCase {
    ApplicationContext ctx = null;
    ShopPreferentialWayService shopPreferentialWayService = null;

    public void setUp() throws Exception {
        ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
        shopPreferentialWayService = (ShopPreferentialWayService) ctx.getBean("shopPreferentialWayService");
    }

    @Test
    public void testAddShopPreferentialWayDTO() throws Exception {
        ShopPreferentialWayDTO test=new ShopPreferentialWayDTO();
        test.setShopId(3L);
        test.setSellerId(4L);
        test.setTemplateId(5L);
        test.setDeliveryType(6);
        test.setFull(new BigDecimal(10L));
        test.setReduce(new BigDecimal(11L));
        test.setStrategy(12);
        test.setCreateTime(new Date());
        test.setUpdateTime(new Date());
        Assert.assertNotNull(shopPreferentialWayService.addShopPreferentialWay(test));
    }

    @Test
    public void testDeleteShopPreferentialWayDTO() throws Exception{
        ShopPreferentialWayDTO test=new ShopPreferentialWayDTO();
        test.setId(10L);
        Assert.assertNotNull(shopPreferentialWayService.deleteShopPreferentialWay(test));
    }

    @Test
    public void testUpdateShopPreferentialWayDTO() throws Exception{
        ShopPreferentialWayDTO test=new ShopPreferentialWayDTO();
        test.setId(2L);
        test.setShopId(31L);
        test.setSellerId(41L);
        test.setTemplateId(51L);
        test.setDeliveryType(61);
        test.setFull(new BigDecimal(101L));
        test.setReduce(new BigDecimal(111L));
        test.setStrategy(121);
        test.setCreateTime(new Date());
        test.setUpdateTime(new Date());
        test.setDelState("2");
        Assert.assertNotNull(shopPreferentialWayService.updateShopPreferentialWay(test));
    }

    @Test
    public void testGetShopPreferentialWayDTO() throws Exception{
        ShopPreferentialWayDTO test=new ShopPreferentialWayDTO();
        test.setId(1L);
        ExecuteResult<List<ShopPreferentialWayDTO>> ShopPreferentialWayDTO=shopPreferentialWayService.queryShopPreferentialWay(test);
        Assert.assertNotNull(ShopPreferentialWayDTO);
    }



}