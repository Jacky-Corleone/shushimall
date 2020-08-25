package com.camelot.basecenter.service;

import java.util.List;

import com.camelot.basecenter.dto.FriendlylinkPageItemDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

public interface FriendlylinkPageItemService {
	
	
	/**
	 * 添加友情链接也页面的映射中间表数据
	 * 
	 * @param friendlylinkPageItemDTO
	 * @return
	 */
	public ExecuteResult<FriendlylinkPageItemDTO> saveFriendlylinkPageItem(FriendlylinkPageItemDTO friendlylinkPageItemDTO);
	
	/**
	 *批量 添加友情链接也页面的映射中间表数据
	 * 
	 * @param friendlylinkPageItemDTOs
	 * @return
	 */
	public ExecuteResult<List<FriendlylinkPageItemDTO>> saveFriendlylinkPageItemBatch(List<FriendlylinkPageItemDTO> friendlylinkPageItemDTOs);
	
	/**
	 * 查询友情链接也页面的映射中间表数据
	 * @param friendlylinkPageItemDTO page
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataGrid<FriendlylinkPageItemDTO> questyFriendlylinkPageItem(FriendlylinkPageItemDTO friendlylinkPageItemDTO,Pager pager);
	
	/**
	 * 编辑友情链接也页面的映射中间表数据
	 * @param friendlylinkPageItemDTO
	 * @return
	 */
	public ExecuteResult<FriendlylinkPageItemDTO> editFriendlylinkPageItem(FriendlylinkPageItemDTO friendlylinkPageItemDTO);
	
	
	/**
	 * 删除友情链接也页面的映射中间表数据
	 * @param friendlylinkPageItemDTO
	 * @return
	 */
	public ExecuteResult<String> deleteFriendlylinkPageItem(FriendlylinkPageItemDTO friendlylinkPageItemDTO);
	
	
	/**
	 * 删除友情链接也页面的映射中间表数据
	 * @param friendlylinkPageItemDTO
	 * @return
	 */
	public ExecuteResult<String> deleteFriendlylinkPageItemByPageId(Long pageId);
	
	

	/**
	 * 批量删除友情链接也页面的映射中间表数据
	 * @param List<FriendlylinkItemDTO>
	 * @return
	 */
	public ExecuteResult<String> deleteFriendlylinkPageItemBatch(List<FriendlylinkPageItemDTO> friendlylinkPageItemDTOs);
	
}
