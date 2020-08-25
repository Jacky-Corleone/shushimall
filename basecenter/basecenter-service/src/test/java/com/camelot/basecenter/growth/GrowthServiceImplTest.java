package com.camelot.basecenter.growth;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.basecenter.dto.GrowthDTO;
import com.camelot.basecenter.service.GrowthService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;

public class GrowthServiceImplTest {
	private GrowthService growthService;
    ApplicationContext ctx;
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		growthService = (GrowthService) ctx.getBean("growthService");
	}
	
	
	    //帮助文档添加功能测试类
		@Test
		public void testAddGrowth(){
		    GrowthDTO growthDTO =new GrowthDTO();
		    growthDTO.setGrowthValue(new BigDecimal(20));
		    growthDTO.setType(4);
		    growthDTO.setUserLevel(1);
		    growthService.addGrowth(growthDTO);
		}
		
		//修改成长值
		@Test
		public void modifyGrowth(){
		    GrowthDTO growthDTO =new GrowthDTO();
		    growthDTO.setId((long)29);
		    growthDTO.setGrowthValue(new BigDecimal(30));
		    growthDTO.setType(4);
		    growthService.updateGrowth(growthDTO);
		}
		
		//查询成长值
		@Test
		public void queryGrowthList(){
		    GrowthDTO growthDTO =new GrowthDTO();
		    Pager page = new Pager<GrowthDTO>();
		    DataGrid<GrowthDTO> result = growthService.queryGrowthList(page, growthDTO);
		    System.out.println(result.getRows().get(0).getType());
		}
		
		//根据实体查询
		@Test
		public void queryGrowth(){
		    GrowthDTO growthDTO =new GrowthDTO();
		    growthDTO.setType(1);
		    Pager page = new Pager<GrowthDTO>();
		    GrowthDTO result = growthService.getGrowthDTO(growthDTO);
		    System.out.println(result.getId());
		}
		

}
