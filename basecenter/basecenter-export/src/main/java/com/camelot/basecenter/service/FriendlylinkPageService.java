package com.camelot.basecenter.service;

import java.util.List;

import com.camelot.basecenter.dto.FriendlylinkPageDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

public interface FriendlylinkPageService {
	
	/**
	 * 添加友情链接页面
	 * @param FriendlylinkPageDTO
	 * @return
	 */
	public ExecuteResult<FriendlylinkPageDTO> saveFriendlylinkPage(FriendlylinkPageDTO friendlylinkPageDTO);
	
	public ExecuteResult<FriendlylinkPageDTO> findFriendlylinkPageById(Long id);
	
	/**
	 * 查询友情链接页面
	 * @param FriendlylinkPageDTO
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataGrid<FriendlylinkPageDTO> questyFriendlylinkPageDetails(FriendlylinkPageDTO friendlylinkPageDTO,Pager pager);
	/**
	 * 查询友情链接页面
	 * @param FriendlylinkPageDTO
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataGrid<FriendlylinkPageDTO> getFriendlylinkPages(FriendlylinkPageDTO friendlylinkPageDTO,Pager pager);
	
	/**
	 * 编辑友情链接页面
	 * @param FriendlylinkPageDTO
	 * @return
	 */
	public ExecuteResult<FriendlylinkPageDTO> editFriendlylinkPage(FriendlylinkPageDTO friendlylinkPageDTO);
	
	
	/**
	 * 删除消息
	 * @param FriendlylinkPageDTO
	 * @return
	 */
	public ExecuteResult<String> deleteFriendlylinkPage(FriendlylinkPageDTO friendlylinkPageDTO);
	
	
	/**
	 * 删除消息
	 * @param baseSendMessageDTO
	 * @return
	 */
	public ExecuteResult<String> deleteFriendlylinkItemById(Long id);
	/**
	 * 为页面添加链接
	 * @param FriendlylinkPageDTO
	 * @return
	 */
	public ExecuteResult<String> addFriendlyLinks(Long pageId, List<Long> itemIds);
	

}
