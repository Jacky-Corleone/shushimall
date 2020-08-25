package com.camelot.goodscenter;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.Base;
import com.camelot.goodscenter.dto.ItemEvaluationDTO;
import com.camelot.goodscenter.dto.ItemEvaluationQueryInDTO;
import com.camelot.goodscenter.dto.ItemEvaluationQueryOutDTO;
import com.camelot.goodscenter.dto.ItemEvaluationReplyDTO;
import com.camelot.goodscenter.dto.ItemEvaluationShowDTO;
import com.camelot.goodscenter.service.ItemAttributeExportService;
import com.camelot.goodscenter.service.ItemEvaluationService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

public class ItemEvaluationServiceImplTest extends Base {
	ApplicationContext ctx = null;
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		this.itemEvaluationService = (ItemEvaluationService)ctx.getBean("itemEvaluationService");
		this.itemExportService = (ItemExportService)ctx.getBean("itemExportService");
	}
	
	@Test
	public void testAddItemEvaluation() {
		ItemEvaluationDTO itemEvaluationDTO = new ItemEvaluationDTO();
		itemEvaluationDTO.setUserId(1L);
		itemEvaluationDTO.setUserShopId(2L);
		itemEvaluationDTO.setByUserId(3L);
		itemEvaluationDTO.setByShopId(4L);
		itemEvaluationDTO.setOrderId("5");
		itemEvaluationDTO.setSkuId(6L);
		itemEvaluationDTO.setItemId(7L);
		itemEvaluationDTO.setSkuScope(2);
		itemEvaluationDTO.setContent("一般般");
		itemEvaluationDTO.setType("1");
		ExecuteResult<ItemEvaluationDTO> result = this.itemEvaluationService.addItemEvaluation(itemEvaluationDTO);
		Assert.assertEquals(result.isSuccess(), true);
	}

	@Test
	public void testAddItemEvaluationReply() {
		ItemEvaluationReplyDTO itemEvaluationReplyDTO = new ItemEvaluationReplyDTO();
		itemEvaluationReplyDTO.setEvaluationId(1L);
		itemEvaluationReplyDTO.setContent("这是为什么呢?");
		ExecuteResult<ItemEvaluationReplyDTO> result = this.itemEvaluationService.addItemEvaluationReply(itemEvaluationReplyDTO);
		Assert.assertEquals(result.isSuccess(), true);
	}

	@Test
	public void testQueryItemEvaluationList() {
		ItemEvaluationQueryInDTO itemEvaluationQueryInDTO = new ItemEvaluationQueryInDTO();
//		itemEvaluationQueryInDTO.setResource("2");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
//			Date beginDate = sdf.parse("2015-04-14");
//			Date endDate = sdf.parse("2015-04-14");
//			itemEvaluationQueryInDTO.setBeginTime(beginDate);
//			itemEvaluationQueryInDTO.setEndTime(endDate);
//			itemEvaluationQueryInDTO.setScopeLevel("2");
//			
//			itemEvaluationQueryInDTO.setReply("1");
			itemEvaluationQueryInDTO.setByUserId(1000000289L);
			itemEvaluationQueryInDTO.setContentEmpty("1");
			itemEvaluationQueryInDTO.setReply("0");
			itemEvaluationQueryInDTO.setType("1");
			
			DataGrid<ItemEvaluationQueryOutDTO> dg = this.itemEvaluationService.queryItemEvaluationList(itemEvaluationQueryInDTO,null);
			for(int i = 0 ; i < dg.getTotal() ; i++){
				System.out.println(dg.getRows().get(i).getOrderId());
			}
			Assert.assertEquals(dg != null, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
//	@Test
//	public void testQueryItemEvaluationTotal() {
//		ItemEvaluationQueryInDTO itemEvaluationQueryInDTO = new ItemEvaluationQueryInDTO();
//		itemEvaluationQueryInDTO.setByShopId(4L);
//		ExecuteResult<ItemEvaluationTotalDTO> result = itemEvaluationService.queryItemEvaluationTotal(itemEvaluationQueryInDTO);
//		ItemEvaluationTotalDTO itemEvaluationTotalDTO = result.getResult();
//		Set<Integer> key = itemEvaluationTotalDTO.getScopeAvgDetails().keySet();
//		for (Iterator it = key.iterator(); it.hasNext();) {
//            Integer s = (Integer) it.next();
//            ItemEvaluationTotalDetailDTO detail = itemEvaluationTotalDTO.getScopeAvgDetails().get(s);
//            System.out.println("级别:" + s + "数量：" + detail.getCount() + "百分比：" + detail.getPercent());
//        }
//		Assert.assertEquals(result.isSuccess(), true);
//	}
//	@Test
//	public void testQueryItemEvaluationById() {
//		fail("Not yet implemented");
//	}
//
	@Test
	public void testQueryItemEvaluationReplyList() {
		ItemEvaluationReplyDTO itemEvaluationReplyDTO=new ItemEvaluationReplyDTO();
		itemEvaluationReplyDTO.setEvaluationId(1l);
		//itemEvaluationReplyDTO.setId(54l);
		DataGrid<ItemEvaluationReplyDTO> dg=itemEvaluationService.queryItemEvaluationReplyList(itemEvaluationReplyDTO, null);
		for(int i = 0 ; i < dg.getTotal() ; i++){
			System.out.println(dg.getRows().get(i).getContent());
		}
		Assert.assertEquals(dg != null, true);
		
		
	}
	
	@Test
	public void testAddItemEvaluations() {

		List<ItemEvaluationDTO> itemEvaluationDTOList = new ArrayList<ItemEvaluationDTO>();
		ItemEvaluationDTO itemEvaluationDTO = new ItemEvaluationDTO();
		itemEvaluationDTO.setUserId(1L);
		itemEvaluationDTO.setUserShopId(2L);
		itemEvaluationDTO.setByUserId(3L);
		itemEvaluationDTO.setByShopId(4L);
		itemEvaluationDTO.setOrderId("22222444");
		itemEvaluationDTO.setSkuId(6L);
		itemEvaluationDTO.setItemId(7L);
		itemEvaluationDTO.setSkuScope(2);
		itemEvaluationDTO.setContent("一般般");
		itemEvaluationDTO.setType("1");
		itemEvaluationDTO.setResource("2");
		itemEvaluationDTOList.add(itemEvaluationDTO);
		
		ItemEvaluationDTO itemEvaluationDTO2 = new ItemEvaluationDTO();
		itemEvaluationDTO2.setUserId(2L);
		itemEvaluationDTO2.setUserShopId(2L);
		itemEvaluationDTO2.setByUserId(3L);
		itemEvaluationDTO2.setByShopId(4L);
		itemEvaluationDTO2.setOrderId("3333333355");
		itemEvaluationDTO2.setSkuId(6L);
		itemEvaluationDTO2.setItemId(7L);
		itemEvaluationDTO2.setSkuScope(2);
		itemEvaluationDTO2.setContent("一般般");
		itemEvaluationDTO2.setType("1");
		itemEvaluationDTO2.setResource("2");
		itemEvaluationDTOList.add(itemEvaluationDTO2);
		
		ExecuteResult<List<ItemEvaluationDTO>> resultList = this.itemEvaluationService.addItemEvaluationsReturnList(itemEvaluationDTOList);
		Assert.assertEquals(resultList.isSuccess(), true);
	
		ExecuteResult<ItemEvaluationDTO> result = this.itemEvaluationService.addItemEvaluations(itemEvaluationDTOList);
		Assert.assertEquals(result.isSuccess(), true);
	}

	/**
	 * 
	 * <p>Discription:[//新增晒单图片]</p>
	 * Created on 2015年12月11日
	 * @author:[李伟龙]
	 */
	@Test
	public void testaddItemEvaluationShow() {
		ItemEvaluationShowDTO itemEvaluationShowDTO = new ItemEvaluationShowDTO();
		itemEvaluationShowDTO.setEvaluationId(1l);
		itemEvaluationShowDTO.setImgUrl("122");
		itemEvaluationShowDTO.setIsDelete(0);
		itemEvaluationShowDTO.setSkuId(829899l);
		itemEvaluationService.addItemEvaluationShow(itemEvaluationShowDTO);
		
	}
	
	/**
	 * 
	 * <p>Discription:[//查询晒单图片]</p>
	 * Created on 2015年12月11日
	 * @author:[李伟龙]
	 */
	@Test
	public void testqueryItemEvaluationShowList() {
		ItemEvaluationShowDTO itemEvaluationShowDTO = new ItemEvaluationShowDTO();
		itemEvaluationService.queryItemEvaluationShowList(itemEvaluationShowDTO, new Pager());
		
	}
}
