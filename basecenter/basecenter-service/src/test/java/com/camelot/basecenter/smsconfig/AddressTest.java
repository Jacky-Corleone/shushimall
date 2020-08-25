package com.camelot.basecenter.smsconfig;




import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.domain.AddressInfo;
import com.camelot.basecenter.dto.AddressInfoDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.basecenter.service.AddressInfoService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年1月26日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class AddressTest {
    
	ApplicationContext ctx = null;
	
	AddressBaseService addressBaseService=null;
	
	AddressInfoService addressInfoService=null;
	
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		addressBaseService = (AddressBaseService) ctx.getBean("addressBaseService");
		addressInfoService = (AddressInfoService) ctx.getBean("addressInfoService");
		
	}
	//省市区县服务列表查询
    @Test
	public void testQueryAddressBase()
	{
    	List<AddressBase> list=addressBaseService.queryAddressBase("shandong");
    	
      	System.out.println("查询的条数："+list.size());	
	}
    //收货地址列表查询
    @Test
    public void testQueryAddressinfo()
    {
    	DataGrid<AddressInfoDTO> dg=addressInfoService.queryAddressinfo(37);
    	
    	System.out.println(dg.getTotal()+"........"+dg.getRows().size());
    }
    //收货地址详情查询
    @Test
    public void QueryAddressinfoByIdTest(){
    	ExecuteResult<AddressInfoDTO> er=addressInfoService.queryAddressinfoById(9);
    	System.out.println(er.getResultMessage()+er.getResult().getProvicecode());
    }
    //收货地址添加
    @Test
    public void testAddAddressInfo()
    {
    	AddressInfoDTO aid=new AddressInfoDTO();
    	aid.setBuyerid(3L);
    	aid.setProvicecode("山东省");
    	aid.setCitycode("vv");
    	aid.setCountrycode("df");
    	aid.setFulladdress("dg");
    	aid.setIsdefault(1);
    	aid.setContactperson("xx");
    	aid.setContactphone("1123");
    	aid.setPostalcode("3434");
    	
    	ExecuteResult<String> str=addressInfoService.addAddressInfo(aid);
    	System.out.println(str.getResult()+"....");
    }
  //收货地址修改
    @Test
    public void testupdate1()
    {
    	AddressInfoDTO aid=new AddressInfoDTO();
    	aid.setProvicecode("山东省");
    	aid.setCitycode("青岛市");
    	aid.setIsdefault(2);
    	aid.setId(10L);
    	ExecuteResult<String> er=addressInfoService.modifyAddressInfo(aid);
    	System.out.println(er.getResultMessage());
    }
  //收货地址删除
    @Test
    public void testdelete()
    {
    	addressInfoService.removeAddresBase(8L);
    }
  //默认收货地址修改
    @Test
    public void testupdate2()
    {
    	ExecuteResult<String> er=addressInfoService.modifyDefaultAddress(9L,37);
    	System.out.println(er.getResultMessage());
    }
 
}
