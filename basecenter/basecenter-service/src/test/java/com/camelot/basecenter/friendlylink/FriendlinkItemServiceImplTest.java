package com.camelot.basecenter.friendlylink;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.camelot.basecenter.dto.FriendlylinkItemDTO;
import com.camelot.basecenter.service.FriendlylinkItemService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;


public class FriendlinkItemServiceImplTest {
	
	FriendlylinkItemService friendlylinkItemService; 
	 ApplicationContext ctx;
		
		@Before
		public void setUp() throws Exception {
			ctx = new ClassPathXmlApplicationContext("test.xml");
			friendlylinkItemService = (FriendlylinkItemService) ctx.getBean("friendlylinkItemService");
		}
	@Test
	public void testSaveFriendlylinkItem() {
		FriendlylinkItemDTO friendlylinkItemDTO = new FriendlylinkItemDTO();
		friendlylinkItemDTO.setLinkName("baidu");
		friendlylinkItemDTO.setLinkUrl("www.baidu.com");
		ExecuteResult<FriendlylinkItemDTO> result = this.friendlylinkItemService.saveFriendlylinkItem(friendlylinkItemDTO);
		Assert.assertEquals(result.isSuccess(), true);
	}
	@Test
	public void queryFriendlylinkItemTest(){
		FriendlylinkItemDTO friendlylinkItemDTO = new FriendlylinkItemDTO();
		//friendlylinkItemDTO.setLinkName("baidu");
		DataGrid<FriendlylinkItemDTO> questyFriendlylinkItemDTO = friendlylinkItemService.questyFriendlylinkItem(friendlylinkItemDTO, new Pager<FriendlylinkItemDTO>(1, 20));
		System.out.println(JSON.toJSONString(questyFriendlylinkItemDTO));
	}
	
	@Test
	public void getUnSelectedItemsTest(){
		//FriendlylinkItemDTO friendlylinkItemDTO = new FriendlylinkItemDTO();
		//friendlylinkItemDTO.setLinkName("baidu");
		DataGrid<FriendlylinkItemDTO> questyFriendlylinkItemDTO = friendlylinkItemService.getUnSelectedItems(1L, new Pager<FriendlylinkItemDTO>(1, 20));
		System.out.println(JSON.toJSONString(questyFriendlylinkItemDTO));
	}
	
	
	@Test
	public void editFriendlylinkItemTest(){
		FriendlylinkItemDTO friendlylinkItemDTO = new FriendlylinkItemDTO();
		friendlylinkItemDTO.setLinkName("科印");
		DataGrid<FriendlylinkItemDTO> questyFriendlylinkItemDTO = friendlylinkItemService.questyFriendlylinkItem(friendlylinkItemDTO, new Pager<FriendlylinkItemDTO>(1, 20));
		FriendlylinkItemDTO friendlylinkItemDTO2 = questyFriendlylinkItemDTO.getRows().get(0);
		friendlylinkItemDTO2.setLinkName("alibaba");
		friendlylinkItemService.editFriendlylinkItem(friendlylinkItemDTO2);
	}
	
	@Test
	public void deleteFriendlylinkItemTest(){
		FriendlylinkItemDTO friendlylinkItemDTO = new FriendlylinkItemDTO();
		friendlylinkItemDTO.setLinkName("baidu");
		DataGrid<FriendlylinkItemDTO> questyFriendlylinkItemDTO = friendlylinkItemService.questyFriendlylinkItem(friendlylinkItemDTO, new Pager<FriendlylinkItemDTO>(1, 20));
		FriendlylinkItemDTO friendlylinkItemDTO2 = questyFriendlylinkItemDTO.getRows().get(0);
		friendlylinkItemService.deleteFriendlylinkItem(friendlylinkItemDTO2);
	}
	
	@Test
	public void getUnselectFriendlylinkItemTest(){
		DataGrid<FriendlylinkItemDTO> unSelectedItems = friendlylinkItemService.getUnSelectedItems(1L, null);
		
		System.out.println(JSON.toJSONString(unSelectedItems.getRows()));
		
	}
	
}
