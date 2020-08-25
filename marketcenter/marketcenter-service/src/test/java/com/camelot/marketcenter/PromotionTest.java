package com.camelot.marketcenter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.maketcenter.dto.PromotionFullReduction;
import com.camelot.maketcenter.dto.PromotionInDTO;
import com.camelot.maketcenter.dto.PromotionInfo;
import com.camelot.maketcenter.dto.PromotionInfoDTO;
import com.camelot.maketcenter.dto.PromotionMarkdown;
import com.camelot.maketcenter.dto.PromotionMdDTO;
import com.camelot.maketcenter.dto.PromotionOutDTO;
import com.camelot.maketcenter.dto.indto.PromotionFullReducitonInDTO;
import com.camelot.maketcenter.dto.indto.PromotionMarkdownInDTO;
import com.camelot.maketcenter.service.PromotionService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enums.PromotionTimeStatusEnum;


public class PromotionTest {
    private PromotionService promotionService;
    ApplicationContext ctx;
	
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("test.xml");
		promotionService = (PromotionService) ctx.getBean("promotionService");
	}
	@Test
	public void getPromotionTest (){
/*		String json='{\"activityName\":\"12331Test\",\"createTimeend\":1426521600000,\"createTimestr\":1425225600000,\"onlineState\":2,\"type\":2} ' ;
		*/
		PromotionInDTO proDTO=new PromotionInDTO();
		/*proDTO.setShopId(2000000093l);
		proDTO.setNum(3);*/
		proDTO.setIsEffective(String.valueOf(PromotionTimeStatusEnum.UNDERWAY.getStatus()));
		Pager page=new Pager();
		ExecuteResult<DataGrid<PromotionOutDTO>> res = promotionService.getPromotion(proDTO,page);
		System.out.println(JSON.toJSONString(res));
		Assert.assertEquals(true, res.isSuccess());
	}
	@Test
	public void modifyPromotionOnlineState(){
		ExecuteResult<String> result = promotionService.modifyPromotionOnlineState(2, 1l);
		System.out.println("result---------"+result.getResult());
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void addPromotionFullReducitonTest(){
		PromotionFullReducitonInDTO promotionFullReducitonInDTO=new PromotionFullReducitonInDTO();
		PromotionInfo promotionInfo=new PromotionInfo();
		promotionInfo.setSellerId(1l);
		promotionInfo.setShopId(1l);
		promotionInfo.setActivityName("满减1");
		promotionInfo.setType(2);
		
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
		promotionInfo.setStartTime(c.getTime());
		c.add(Calendar.DAY_OF_MONTH, 1);
		promotionInfo.setEndTime(c.getTime());
		promotionInfo.setWords("满减给力给力");
		promotionInfo.setCreateUser("用户1");
		promotionInfo.setUserType(1);
		promotionInfo.setIsAllItem(1);
		promotionInfo.setMembershipLevel("1");
		promotionInfo.setPlatformId(null);
		
		List<PromotionFullReduction> list=new ArrayList<PromotionFullReduction>();
		PromotionFullReduction pfr=new PromotionFullReduction();
		pfr.setItemId(1l);
		//pfr.setSkuId(1l);
		pfr.setMeetPrice(BigDecimal.valueOf(200));
		pfr.setDiscountPrice(BigDecimal.valueOf(100));
		list.add(pfr);
//		PromotionFullReduction pfr2=new PromotionFullReduction();
//		pfr2.setItemId(2l);
//		//pfr2.setSkuId(1l);
//		pfr2.setMeetPrice(BigDecimal.valueOf(200));
//		pfr2.setDiscountPrice(BigDecimal.valueOf(100));
//		list.add(pfr2);
//		promotionFullReducitonInDTO.setPromotionFrList(list);
//		promotionFullReducitonInDTO.setPromotionInfo(promotionInfo);
		ExecuteResult<String> result = promotionService.addPromotionFullReduciton(promotionFullReducitonInDTO);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void editPromotionFullReducitonTest(){
		PromotionFullReducitonInDTO promotionFullReducitonInDTO=new PromotionFullReducitonInDTO();
		PromotionInfo promotionInfo=new PromotionInfo();
		promotionInfo.setSellerId(1l);
		promotionInfo.setId(98l);
		promotionInfo.setShopId(1l);
		promotionInfo.setActivityName("满减1");
		promotionInfo.setType(2);
		promotionInfo.setStartTime(new Date());
		promotionInfo.setEndTime(new Date());
		promotionInfo.setWords("满减给力给力");
		promotionInfo.setCreateUser("用户1");
		
		List<PromotionFullReduction> list=new ArrayList<PromotionFullReduction>();
		PromotionFullReduction pfr=new PromotionFullReduction();
		pfr.setItemId(1l);
		pfr.setSkuId(1l);
		pfr.setMeetPrice(BigDecimal.valueOf(200));
		pfr.setDiscountPrice(BigDecimal.valueOf(100));
		list.add(pfr);
		PromotionFullReduction pfr2=new PromotionFullReduction();
		pfr2.setItemId(3l);
		//pfr2.setSkuId(1l);
		pfr2.setMeetPrice(BigDecimal.valueOf(200));
		pfr2.setDiscountPrice(BigDecimal.valueOf(100));
		list.add(pfr2);
//		promotionFullReducitonInDTO.setPromotionFrList(list);
//		promotionFullReducitonInDTO.setPromotionInfo(promotionInfo);
		ExecuteResult<String> result = promotionService.editPromotionFullReduciton(promotionFullReducitonInDTO);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void addPromotionMarkdownTest() {
		PromotionMarkdownInDTO promotionMarkdownInDTO=new PromotionMarkdownInDTO();
		PromotionInfoDTO promotionInfo=new PromotionInfoDTO();
		promotionInfo.setSellerId(1l);
		promotionInfo.setShopId(1l);
		promotionInfo.setActivityName("直降1");
		promotionInfo.setType(1);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String start="2015-11-08 18:00:00";
		String end="2015-11-10 14:00:00";
		try {
			promotionInfo.setStartTime(sdf.parse(start));
			promotionInfo.setEndTime(sdf.parse(end));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		promotionInfo.setWords("直降给力给力");
		promotionInfo.setCreateUser("用户1");
		
		List<PromotionMdDTO> list=new ArrayList<PromotionMdDTO>();
		PromotionMdDTO pfr=new PromotionMdDTO();
		pfr.setItemId(1000000429l);
		//pfr.setSkuId(1l);
		pfr.setAreaId("1");
		pfr.setMinNum(2l);
		pfr.setMaxNum(10l);
		pfr.setSellPrice(BigDecimal.valueOf(100));
		pfr.setPromotionPrice(BigDecimal.valueOf(80));
		pfr.setDiscountPercent(new BigDecimal("0.51"));
		list.add(pfr);
		PromotionMdDTO pfr2=new PromotionMdDTO();
		pfr2.setItemId(1000000437l);
		//pfr2.setSkuId(1l);
		pfr2.setAreaId("1");
		pfr2.setMinNum(2l);
		pfr2.setMaxNum(10l);
		pfr2.setSellPrice(BigDecimal.valueOf(100));
		pfr2.setPromotionPrice(BigDecimal.valueOf(80));
		pfr2.setDiscountPercent(new BigDecimal("0.51"));
		list.add(pfr2);
		promotionMarkdownInDTO.setPromotionMdDTOList(list);
		promotionMarkdownInDTO.setPromotionInfoDTO(promotionInfo);
		ExecuteResult<String> result = promotionService.addPromotionMarkdown(promotionMarkdownInDTO);
		Assert.assertEquals(true, result.isSuccess());
	}
	
	@Test
	public void editPromotionMarkdownTest() {
		PromotionMarkdownInDTO promotionMarkdownInDTO=new PromotionMarkdownInDTO();
		PromotionInfoDTO promotionInfo=new PromotionInfoDTO();
		promotionInfo.setId(99L);
		promotionInfo.setSellerId(1l);
		promotionInfo.setShopId(1l);
		promotionInfo.setActivityName("直降1");
		promotionInfo.setType(1);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String start="2015-11-11 18:00:00";
		String end="2015-12-10 14:00:00";
		try {
			promotionInfo.setStartTime(sdf.parse(start));
			promotionInfo.setEndTime(sdf.parse(end));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		promotionInfo.setWords("直降给力给力");
		promotionInfo.setCreateUser("用户1");
		
		List<PromotionMdDTO> list=new ArrayList<PromotionMdDTO>();
		PromotionMdDTO pfr=new PromotionMdDTO();
		pfr.setItemId(1000000429l);
		//pfr.setSkuId(1l);】
		pfr.setAreaId("1");
		pfr.setMinNum(2l);
		pfr.setMaxNum(10l);
		pfr.setSellPrice(BigDecimal.valueOf(100));
		pfr.setPromotionPrice(BigDecimal.valueOf(80));
		pfr.setDiscountPercent(new BigDecimal("0.51"));
		list.add(pfr);
		PromotionMdDTO pfr2=new PromotionMdDTO();
		pfr2.setItemId(1000000457l);
		//pfr2.setSkuId(1l);
		pfr2.setAreaId("1");
		pfr2.setMinNum(2l);
		pfr2.setMaxNum(10l);
		pfr2.setSellPrice(BigDecimal.valueOf(100));
		pfr2.setPromotionPrice(BigDecimal.valueOf(80));
		pfr2.setDiscountPercent(new BigDecimal("0.51"));
		list.add(pfr2);
		promotionMarkdownInDTO.setPromotionMdDTOList(list);
		promotionMarkdownInDTO.setPromotionInfoDTO(promotionInfo);
		ExecuteResult<String> result = promotionService.editPromotionMarkdown(promotionMarkdownInDTO);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void deletePromotionTest(){
		ExecuteResult<String> result =promotionService.deletePromotion(4L);
		Assert.assertEquals(true, result.isSuccess());
	}
	@Test
	public void getMdPromotionConflictTest(){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date startTime = null;
		Date endTime = null;
		try {
			startTime = sf.parse("2015-12-01 10:10:10");
			endTime = sf.parse("2015-12-10 10:10:10");
		} catch (ParseException e) {
			//时间转换异常
			e.printStackTrace();
		}
		ExecuteResult<List<Long>> result = promotionService.getMdPromotionConflict(0l, startTime, endTime);
		Assert.assertEquals(true, result.isSuccess());
	}
	
}
