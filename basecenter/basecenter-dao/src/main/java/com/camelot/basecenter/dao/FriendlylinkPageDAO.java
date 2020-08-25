package com.camelot.basecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.basecenter.dto.FriendlylinkPageDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface FriendlylinkPageDAO extends BaseDAO<FriendlylinkPageDTO>{
	
	@SuppressWarnings("rawtypes")
	public List<FriendlylinkPageDTO> getAllDetails(@Param("entity")FriendlylinkPageDTO friendlylinkPageDTO, @Param("page") Pager page);
	
	public int addFriendlylinkPage(FriendlylinkPageDTO friendlylinkPageDTO);
	
	public FriendlylinkPageDTO selectByPrimaryKey(Long id);
	
	public List<FriendlylinkPageDTO> getSelectedDetails(@Param("entity")FriendlylinkPageDTO friendlylinkPageDTO, @SuppressWarnings("rawtypes") @Param("page") Pager page);
	
	public int deleteByPrimaryKey(Long id);
	
	public int updateByPrimaryKeySelective(FriendlylinkPageDTO friendlylinkPageDTO);
	
	@SuppressWarnings("rawtypes")
	public List<FriendlylinkPageDTO> getFriendlylinkPages (@Param("entity")FriendlylinkPageDTO friendlylinkPageDTO, @Param("page") Pager page);
}
