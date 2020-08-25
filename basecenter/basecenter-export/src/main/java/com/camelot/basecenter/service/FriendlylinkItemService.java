package com.camelot.basecenter.service;

import com.camelot.basecenter.dto.FriendlylinkItemDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

public interface FriendlylinkItemService {
	
	/**
	 * 添加友情链接
	 * @param baseSendMessageDTO
	 * @return
	 */
	public ExecuteResult<FriendlylinkItemDTO> saveFriendlylinkItem(FriendlylinkItemDTO friendlylinkItemDTO);
	
	
	public ExecuteResult<FriendlylinkItemDTO> findFriendlylinkItemById(Long id);
	
	/**
	 * 查询友情链接
	 * @param baseSendMessageDTO
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataGrid<FriendlylinkItemDTO> questyFriendlylinkItem(FriendlylinkItemDTO friendlylinkItemDTO,Pager pager);
	
	/**
	 * 编辑友情链接
	 * @param baseSendMessageDTO
	 * @return
	 */
	public ExecuteResult<FriendlylinkItemDTO> editFriendlylinkItem(FriendlylinkItemDTO friendlylinkItemDTO);
	
	
	/**
	 * 删除消息
	 * @param baseSendMessageDTO
	 * @return
	 */
	public ExecuteResult<String> deleteFriendlylinkItem(FriendlylinkItemDTO friendlylinkItemDTO);
	
	
	
	/**
	 * 删除消息
	 * @param baseSendMessageDTO
	 * @return
	 */
	public ExecuteResult<String> deleteFriendlylinkItemById(Long id);
	
	
	/**
	 * 获取页面伟添加的友情链接
	 * @param baseSendMessageDTO
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataGrid<FriendlylinkItemDTO> getUnSelectedItems(Long pageId,Pager pager);
	
	
	/**
	 * 获取页面伟添加的友情链接
	 * @param baseSendMessageDTO
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataGrid<FriendlylinkItemDTO> getSelectedItems(Long pageId,Pager pager);
}
