package com.camelot.basecenter.malldocument;


import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.basecenter.dto.MallDocumentDTO;
import com.camelot.basecenter.dto.MallTypeDTO;
import com.camelot.basecenter.service.MallDocumentService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
/**
 * 帮助文档单元测试
 * @author 周立明
 *
 */
public class MallDocumentServiceImplTest {
	private MallDocumentService mallDocumentService;
    ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		mallDocumentService = (MallDocumentService) ctx.getBean("mallDocumentService");
	}
	//帮助文档列表查询测试类
	@Test
	public void testQueryMallDocumentList(){
		Pager pager = new Pager();
		MallDocumentDTO mallDocumentDTO =new MallDocumentDTO();
//		mallDocumentDTO.setMallClassifyId(1);
		mallDocumentDTO.setMallTitle("网上支");
//		mallDocumentDTO.setMallSortNum(2);
//		mallDocumentDTO.setMallStatus(1);
//		mallDocumentDTO.setMallId(1);
		DataGrid<MallDocumentDTO> dto = mallDocumentService.queryMallDocumentList(mallDocumentDTO, pager);
		System.out.println(dto.getTotal());
	}
	//帮助文档添加功能测试类
	@Test
	public void testAddMallDocument(){
		MallDocumentDTO mallDocumentDTO =new MallDocumentDTO();
		mallDocumentDTO.setMallSortNum(1);
		mallDocumentDTO.setMallStatus(1);
		mallDocumentDTO.setMallClassifyId(3);
		mallDocumentDTO.setMallTitle("网上支付");
		mallDocumentService.addMallDocument(mallDocumentDTO);
	}
	//帮助文档修改功能测试类
	@Test
	public void testModifyInfoById(){
		MallDocumentDTO mallDocumentDTO =new MallDocumentDTO();
		mallDocumentDTO.setMallClassifyId(2);
		mallDocumentDTO.setMallTitle("网上支付");
		mallDocumentDTO.setMallId(6);
		mallDocumentService.modifyInfoById(mallDocumentDTO);
	}
	//帮助文档上下架状态修改测试类
	@Test
	public void testModifyStatusById(){
		MallDocumentDTO mallDocumentDTO =new MallDocumentDTO();
		mallDocumentDTO.setMallStatus(2);
		mallDocumentDTO.setMallId(6);
		mallDocumentService.modifyInfoById(mallDocumentDTO);
	}
	//帮助文档获取通过id获取详情测试类
	@Test
	public void testGetMallDocumentById(){
		Long id = 1L;
		mallDocumentService.getMallDocumentById(id);
	}
	
	//帮助文档获取通过id获取详情测试类
	@Test
	public void testGetMallDocumentByType(){
		List<MallTypeDTO> list = mallDocumentService.queryMallDocumentListByType("3");
		System.out.println("===="+list.size()+"====");
	}
}
