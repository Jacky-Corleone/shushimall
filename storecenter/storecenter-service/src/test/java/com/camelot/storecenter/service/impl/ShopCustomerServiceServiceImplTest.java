package com.camelot.storecenter.service.impl;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopAndCustomerDTO;
import com.camelot.storecenter.dto.ShopAndCustomerQueryDTO;
import com.camelot.storecenter.dto.ShopCustomerServiceDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopAndCustomerService;
import com.camelot.storecenter.service.ShopCustomerServiceService;

public class ShopCustomerServiceServiceImplTest extends TestCase {
    ApplicationContext ctx = null;
    ShopCustomerServiceService shopCustomerServiceService = null;
    ShopAndCustomerService shopAndCustomerService=null;

    public void setUp() throws Exception {
        ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
        shopCustomerServiceService = (ShopCustomerServiceService) ctx.getBean("shopCustomerServiceService");
        shopAndCustomerService=(ShopAndCustomerService) ctx.getBean("shopAndCustomerService");
    }

    public void testCreateShopCustomerServiceDTO() throws Exception {
        ShopCustomerServiceDTO test=new ShopCustomerServiceDTO();
        test.setShopId(2222L);
        test.setStationId("sdfdsfsdfsdd");
        ExecuteResult<ShopCustomerServiceDTO> res= shopCustomerServiceService.createShopCustomerServiceDTO(test);
        Assert.assertNotNull(res);
    }

    public void testUpdateShopCustomerServiceDTO() throws Exception {
        ShopCustomerServiceDTO test=new ShopCustomerServiceDTO();
        test.setShopId(2222222L);
        test.setId(1L);
        ExecuteResult<ShopCustomerServiceDTO> res= shopCustomerServiceService.updateSelective(test);
        Assert.assertNotNull(res);
    }

    public void testDeleteById() throws Exception {

    }

    public void testDeleteAll() throws Exception {

    }

    public void testSearchShopCustomerServiceDTOs() throws Exception {
        ShopCustomerServiceDTO test=new ShopCustomerServiceDTO();
        test.setShopId(2222222L);
        Pager<ShopCustomerServiceDTO> pager=new Pager<ShopCustomerServiceDTO>();
        DataGrid<ShopCustomerServiceDTO> res= shopCustomerServiceService.searchShopCustomerServiceDTOs(pager,test);
        Assert.assertNotNull(res);
    }

    public void testGetShopCustomerServiceDTOById() throws Exception {

    }

    public void testUpdateSelective() throws Exception {

    }

    public void testUpdateSelectiveWithDateTimeCheck() throws Exception {

    }

    public void testSearchByCondition() throws Exception {
        ShopCustomerServiceDTO test=new ShopCustomerServiceDTO();
        test.setShopId(2222222L);
        Pager<ShopCustomerServiceDTO> pager=new Pager<ShopCustomerServiceDTO>();
        List<ShopCustomerServiceDTO> res= shopCustomerServiceService.searchByCondition(test);
        Assert.assertNotNull(res);
    }

    public void testUpdateSelectiveByIdList() throws Exception {

    }

    public void testUpdateAllByIdList() throws Exception {

    }

    public void testDefunctById() throws Exception {

    }

    public void testDefunctByIdList() throws Exception {

    }
    @Test
    public void testQuery(){
        Pager<ShopDTO> pager=new Pager<ShopDTO>();
        ShopAndCustomerQueryDTO shopAndCustomerQueryDTO=new ShopAndCustomerQueryDTO();
        DataGrid<ShopAndCustomerDTO> res =shopAndCustomerService.searchShopAndCustomer(pager,shopAndCustomerQueryDTO);
        Assert.assertNotNull(res);
    }
    @Test
    public void testSearchShopCustomer(){
        Pager<ShopCustomerServiceDTO> pager=new Pager<ShopCustomerServiceDTO>();
        ShopAndCustomerQueryDTO shopAndCustomerQueryDTO=new ShopAndCustomerQueryDTO();
       // shopAndCustomerQueryDTO.setStationId("fa");
       // shopAndCustomerQueryDTO.setShopName("32");
        DataGrid<ShopCustomerServiceDTO> res =shopAndCustomerService.searchShopCustomer(pager, shopAndCustomerQueryDTO);
        Assert.assertNotNull(res);
    }
}