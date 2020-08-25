package com.camelot.basecenter.friendlylink;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.basecenter.dto.FriendlylinkPageItemDTO;
import com.camelot.basecenter.service.FriendlylinkPageItemService;
import com.camelot.openplatform.common.ExecuteResult;


public class FriendlinkPageItemServiceImplTest {
	
	FriendlylinkPageItemService friendlylinkPageItemService; 
	 ApplicationContext ctx;
		
		@Before
		public void setUp() throws Exception {
			ctx = new ClassPathXmlApplicationContext("test.xml");
			friendlylinkPageItemService = (FriendlylinkPageItemService) ctx.getBean("friendlylinkPageItemService");
		}
	@Test
	public void testSaveFriendlylinkItem() {
		FriendlylinkPageItemDTO friendlylinkPageItemDTO = new FriendlylinkPageItemDTO();
		friendlylinkPageItemDTO.setItemId(3L);
		friendlylinkPageItemDTO.setPageId(2L);
		ExecuteResult<FriendlylinkPageItemDTO> result = friendlylinkPageItemService.saveFriendlylinkPageItem(friendlylinkPageItemDTO);
		Assert.assertEquals(result.isSuccess(), true);
	}
	
	@Test
	public void testSaveFriendlylinkItemBatch() {
		ArrayList<FriendlylinkPageItemDTO> arrayList = new ArrayList<FriendlylinkPageItemDTO>();
		for (Long i = 10L; i < 20; i++) {
			FriendlylinkPageItemDTO friendlylinkPageItemDTO = new FriendlylinkPageItemDTO();
			friendlylinkPageItemDTO.setItemId(i);
			friendlylinkPageItemDTO.setPageId(i);
			arrayList.add(friendlylinkPageItemDTO);
		}
		
		ExecuteResult<List<FriendlylinkPageItemDTO>> saveFriendlylinkPageItemBatch = friendlylinkPageItemService.saveFriendlylinkPageItemBatch(arrayList);
		
		Assert.assertEquals(saveFriendlylinkPageItemBatch.isSuccess(), true);
	}
	
	
	@Test
	public void testDeleteFriendlylinkItem() {
		 FriendlylinkPageItemDTO friendlylinkPageItemDTO = new FriendlylinkPageItemDTO();
		 friendlylinkPageItemDTO.setItemId(68L);
		 friendlylinkPageItemDTO.setPageId(50L);
		 ExecuteResult<String> deleteFriendlylinkPageItem = friendlylinkPageItemService.deleteFriendlylinkPageItem(friendlylinkPageItemDTO);
		
		Assert.assertEquals(deleteFriendlylinkPageItem.isSuccess(), true);
	}
	
}
