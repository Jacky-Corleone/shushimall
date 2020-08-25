package com.camelot.basecenter.friendlylink;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.basecenter.dto.FriendlylinkItemDTO;
import com.camelot.basecenter.dto.FriendlylinkPageDTO;
import com.camelot.basecenter.service.FriendlylinkPageService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;


public class FriendlinkPageServiceImplTest {
	
	FriendlylinkPageService friendlylinkPageService; 
	 ApplicationContext ctx;
		
		@Before
		public void setUp() throws Exception {
			ctx = new ClassPathXmlApplicationContext("test.xml");
			friendlylinkPageService = (FriendlylinkPageService) ctx.getBean("friendlylinkPageService");
		}
	@Test
	public void testSaveFriendlylinkItem() {
		FriendlylinkPageDTO friendlylinkPageDTO = new FriendlylinkPageDTO();
		friendlylinkPageDTO.setPageName("首页");
		friendlylinkPageDTO.setPageUrl("www.shushi100.com");
		ExecuteResult<FriendlylinkPageDTO> result = friendlylinkPageService.saveFriendlylinkPage(friendlylinkPageDTO);
		Assert.assertEquals(result.isSuccess(), true);
	}
	@Test
	public void queryFriendlylinkItemTest(){
		FriendlylinkPageDTO friendlylinkPageDTO = new FriendlylinkPageDTO();
		//friendlylinkPageDTO.setPageName("首页");
		DataGrid<FriendlylinkPageDTO> friendlylinkPageDTOGD = friendlylinkPageService.questyFriendlylinkPageDetails(friendlylinkPageDTO, new Pager<FriendlylinkItemDTO>(1, 20));
		System.out.println(JSON.toJSONString(friendlylinkPageDTOGD.getRows()));
	}
	
	@Test
	public void addFriendlyLinksTest(){
		Long pageId =50L;
		ArrayList<Long> itemIds = new ArrayList<Long>();
		
		for (Long i = 60L; i < 70; i++) {
			itemIds.add(i);
		}
		friendlylinkPageService.addFriendlyLinks(pageId, itemIds);
	}
	
	/*@Test
	public void deleteFriendlylinkItemTest(){
		FriendlylinkItemDTO friendlylinkItemDTO = new FriendlylinkItemDTO();
		friendlylinkItemDTO.setLinkName("baidu");
		DataGrid<FriendlylinkItemDTO> questyFriendlylinkItemDTO = friendlylinkItemService.questyFriendlylinkItemDTO(friendlylinkItemDTO, new Pager<FriendlylinkItemDTO>(1, 20));
		FriendlylinkItemDTO friendlylinkItemDTO2 = questyFriendlylinkItemDTO.getRows().get(0);
		friendlylinkItemService.deleteFriendlylinkItemDTO(friendlylinkItemDTO2);
	}*/
	
}
