package com.camelot.basecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.basecenter.dto.FriendlylinkItemDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
@SuppressWarnings("rawtypes")
public interface FriendlylinkItemDAO extends BaseDAO<FriendlylinkItemDTO>{
	
	public void addFriendlylinkItem(FriendlylinkItemDTO friendlylinkItemDTO);
	
	public List<FriendlylinkItemDTO> questyBaseFriendlylinkItemList(@Param("entity") FriendlylinkItemDTO friendlylinkItemDTO,@Param("page") Pager page);
	
	public List<FriendlylinkItemDTO> getUnSelectedItems(@Param("pageId")Long pageId,@Param("page") Pager page);
	
	public List<FriendlylinkItemDTO> getSelectedItems(@Param("pageId")Long pageId,@Param("page") Pager page);
	
	public int updateByPrimaryKeySelective(FriendlylinkItemDTO friendlylinkItemDTO);
	
	public int delete(@Param("entity") FriendlylinkItemDTO friendlylinkItemDTO);
	
	public int deleteByPrimaryKey(Long id);
	
	
	public FriendlylinkItemDTO findByLinkUrl(String linkUrl);
	
	
	public FriendlylinkItemDTO selectByPrimaryKey(Long id);
	
}
