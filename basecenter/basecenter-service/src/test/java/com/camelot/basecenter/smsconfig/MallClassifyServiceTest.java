package com.camelot.basecenter.smsconfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;










import com.camelot.basecenter.dto.MallClassifyDTO;
import com.camelot.basecenter.service.MallClassifyService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年1月26日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class MallClassifyServiceTest {
    ApplicationContext ctx = null;
	
	MallClassifyService mallClassifyService=null;
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		
		mallClassifyService = (MallClassifyService) ctx.getBean("mallClassifyService");
	}
	
	//文档分类列表查询
	 @Test
	    public void testQueryMallCassifyList() throws ParseException
	    {
		    Pager page=new Pager();
	    	page.setPage(1);
	    	page.setRows(10);
	    	MallClassifyDTO mallClassifyDTO=new MallClassifyDTO();
	    	//mallClassifyDTO.setType(2);
	    	//SimpleDateFormat df = new SimpleDateFormat("yyyy-dd-mm");
	    	//mallClassifyDTO.setStartTime("2015-01-26 00:00:00");
	    	//mallClassifyDTO.setEndTime("2015-01-30 00:00:00");
	    	DataGrid<MallClassifyDTO> dg=mallClassifyService.queryMallCassifyList(mallClassifyDTO,page);
	    	List<MallClassifyDTO> list = dg.getRows();
	    }
	 
	 // 文档分类添加
	 @Test
	 public void testAddMallCassify()
	 {
		 MallClassifyDTO mld=new MallClassifyDTO();
		 mld.setType(2);
		 mld.setTitle("分配标题");
		mallClassifyService.addMallCassify(mld);
	    
	 }
	 
	 //文档分类修改
	 @Test
	 public void testModifyInfoById()
	 {
		 MallClassifyDTO mld=new MallClassifyDTO();
		 mld.setId(4);
		 mld.setType(3);
		 mld.setTitle("标题内容");
		 ExecuteResult<String> str= mallClassifyService.modifyInfoById(mld);
		 System.out.println(str.getResult()+"//////");
	 }
	 
	 //文档分类下架
	 @Test
	 public void testModifyStatusById()
	 {
		 mallClassifyService.modifyStatusById(1,1);
	 }
}
