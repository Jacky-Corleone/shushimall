package com.camelot.shop.test;

import java.util.Date;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopFreightTemplateDTO;
import com.camelot.storecenter.service.ShopFreightTemplateService;

public class ShopFreightTemplateServiceImplTest extends TestCase {
	
	private final static Logger logger = LoggerFactory.getLogger(ShopFreightTemplateServiceImplTest.class);
    ApplicationContext ctx = null;
    ShopFreightTemplateService shopFreightTemplateService = null;

    public void setUp() throws Exception {
        ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
        shopFreightTemplateService = (ShopFreightTemplateService) ctx.getBean("shopFreightTemplateService");
    }

    @Test
    public void testAddShopFreightTemplateDTO() throws Exception {
        ShopFreightTemplateDTO test=new ShopFreightTemplateDTO();
        test.setTemplateName("辅导辅导方法");
        test.setValuationWay(2);
        test.setShopId(3L);
        test.setSellerId(4L);
        test.setAddressDetails("详细地址");
        test.setCityId(5L);
        test.setCreateTime(new Date());
        test.setUpdateTime(new Date());
        test.setCountyId(6L);
        test.setPostageFree(7);
        test.setProvinceId(8L);
        test.setDeliveryTime("4小时");
        test.setDeliveryType("1,2,3");
        test.setDelState("1");
        ExecuteResult<ShopFreightTemplateDTO> result=shopFreightTemplateService.addShopFreightTemplate(test);
        Assert.assertNotNull(result.getResult());
    }

    @Test
    public void testDeleteShopFreightTemplateDTO() throws Exception{
        Assert.assertNotNull(shopFreightTemplateService.deleteShopFreightTemplateById(40L));
    }

    @Test
    public void testUpdateShopFreightTemplateDTO() throws Exception{
        ShopFreightTemplateDTO test=new ShopFreightTemplateDTO();
        test.setTemplateName("辅导辅导方法11111111111");
        test.setId(37L);
        Assert.assertNotNull(shopFreightTemplateService.update(test));
    }

    @Test
    public void testGetShopFreightTemplateDTO() throws Exception{
        ShopFreightTemplateDTO shopFreightTemplateDTO=shopFreightTemplateService.queryShopFreightTemplateById(2L);
        Assert.assertNotNull(shopFreightTemplateDTO);
    }

    @Test
    public void testQueryShopFreightTemplate() throws Exception{
        ShopFreightTemplateDTO test=new ShopFreightTemplateDTO();
        test.setTemplateName("辅导辅导方法");
        test.setValuationWay(1);
        test.setShopId(1L);
        test.setSellerId(4L);
        test.setAddressDetails("详细地址");
        test.setCreateTime(new Date());
        test.setUpdateTime(new Date());
        test.setPostageFree(7);
        Pager pager=new Pager();
        ExecuteResult<DataGrid<ShopFreightTemplateDTO>> shopFreightTemplateById=shopFreightTemplateService.queryShopFreightTemplateList(test,pager);
        Assert.assertNotNull(shopFreightTemplateById);
    }

    /**
     * 
     * <p>Description: [测试模版复制]</p>
     * Created on 2015年11月13日
     * @throws Exception
     * @author:[宋文斌]
     */
	@Test
	public void testCopy() throws Exception {
		ExecuteResult<ShopFreightTemplateDTO> result = shopFreightTemplateService.copy(405L);
		logger.info(JSONObject.toJSONString(result));
	}

}