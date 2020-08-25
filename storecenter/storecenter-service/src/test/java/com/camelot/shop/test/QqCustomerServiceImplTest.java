package com.camelot.shop.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.QqCustomerDTO;
import com.camelot.storecenter.service.QqCustomerService;
/**
 * 
 * <p>Description: [QQ客服接口测试类]</p>
 * Created on 2016年2月2日
 * @author  <a href="mailto: liweilong@camelotchina.com">李伟龙</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public class QqCustomerServiceImplTest {
	
	ApplicationContext ctx = null;
	QqCustomerService qqCustomerService = null;
	
	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		qqCustomerService = (QqCustomerService) ctx.getBean("qqCustomerService");
	}
	
	/**
	 * 
	 * <p>Discription:[测试新增QQ客服]</p>
	 * Created on 2016年2月2日
	 * @author:[李伟龙]
	 */
//	@Test
//	public void  queryAddQqCustomer(){
//		QqCustomerDTO qqCustomerDTO = new QqCustomerDTO();
//		qqCustomerDTO.setCustomerQqNumber("2765175246");
//		qqCustomerDTO.setCustomerSortNumber(1);
//		qqCustomerDTO.setCustomerType(2);
//		qqCustomerDTO.setDeletedFlag(3);
//		qqCustomerDTO.setIsDefault(4);
//		qqCustomerDTO.setLastOperatorId(2898l);
//		qqCustomerDTO.setLastUpdateDate(new Date());
//		qqCustomerDTO.setShopId(29893890l);
//		qqCustomerDTO.setShopName("name");
//		qqCustomerDTO.setUserId(45545l);
//		qqCustomerService.addQqCustomer(qqCustomerDTO);
//	}
	
	/**
	 * 
	 * <p>Discription:[测试查询QQ客服]</p>
	 * Created on 2016年2月2日
	 * @author:[李伟龙]
	 */
//	@Test
//	public void  selectListByConditionAll(){
//		QqCustomerDTO qqCustomerDTO = new QqCustomerDTO();
//		qqCustomerDTO.setDeletedFlag(0);
//		qqCustomerDTO.setCustomerType(1);
//		qqCustomerDTO.setUserId(1000000008l);
//		ExecuteResult<DataGrid<QqCustomerDTO>> r = qqCustomerService.selectListByConditionAll(new Pager(), qqCustomerDTO);
//	}
	
	
	@Test
	public void  getQqCustomerByIds(){
		QqCustomerDTO qqCustomerDTO = new QqCustomerDTO();
		List<Long> list = new ArrayList<Long>();
		list.add(1000000008l);
		String s = qqCustomerService.getQqCustomerByIds(list, 1);
	}
	
	/**
	 * <p>Discription:[修改默认客服时将其他客服都设置成非默认客服]</p>
	 * Created on 2016年2月17日
	 * @author:[王宏伟]
	 */
//	@Test
//	public void  updateMRCustomer(){
//		QqCustomerDTO qqCustomerDTO = new QqCustomerDTO();
//		qqCustomerDTO.setCustomerType(1);
//		qqCustomerDTO.setShopId(Long.valueOf("2000000004"));
//		qqCustomerService.updateMRCustomer(qqCustomerDTO);
//	}
	
	
	/**
	 * 
	 * <p>Discription:[测试更新QQ客服]</p>
	 * Created on 2016年2月2日
	 * @author:[李伟龙]
	 */
//	@Test
//	public void  updateQqCustomer(){
//		QqCustomerDTO qqCustomerDTO = new QqCustomerDTO();
//		qqCustomerDTO.setCustomerQqNumber("2765175246");
//		qqCustomerDTO.setCustomerSortNumber(9);
//		qqCustomerDTO.setCustomerType(8);
//		qqCustomerDTO.setDeletedFlag(7);
//		qqCustomerDTO.setIsDefault(6);
//		qqCustomerDTO.setLastOperatorId(555l);
//		qqCustomerDTO.setLastUpdateDate(new Date());
//		qqCustomerDTO.setShopId(2666l);
//		qqCustomerDTO.setShopName("name111111");
//		qqCustomerDTO.setUserId(444l);
//		qqCustomerDTO.setId(1l);
//		qqCustomerService.updateQqCustomer(qqCustomerDTO);
//	}
}
