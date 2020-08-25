package com.camelot.basecenter.dao;


import java.util.List;

import com.camelot.basecenter.dto.FriendlylinkPageItemDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface FriendlylinkPageItemDAO extends BaseDAO<FriendlylinkPageItemDTO>{
	
	public int addFriendlylinkPageItemBatch(List<FriendlylinkPageItemDTO> friendlylinkPageItems);
	
	public int  addFriendlylinkPage(FriendlylinkPageItemDTO friendlylinkPageItems);
	
	public void  deleteBatch(List<FriendlylinkPageItemDTO> friendlylinkPageItems);
	
	public void  deleteByPageId(long pageId);
	
	public void deleteByDTO(FriendlylinkPageItemDTO friendlylinkPageItems);
}
