package com.camelot.basecenter.friendlylink;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.basecenter.dao.FriendlylinkPageItemDAO;
import com.camelot.basecenter.dto.FriendlylinkPageItemDTO;
import com.camelot.basecenter.service.FriendlylinkPageItemService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * <p>Description: [友情链接页面中间映射实现类]</p>
 * Created on 2016年3月29日
 * @author  <a href="mailto: wufuhong@camelotchina.com">吴赋宏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */

@Service("friendlylinkPageItemService")
public class FriendlylinkPageItemServiceImpl implements FriendlylinkPageItemService{
	
	private static final Logger LOGGER=LoggerFactory
			.getLogger(FriendlylinkPageItemServiceImpl.class);
	
	@Resource
	private FriendlylinkPageItemDAO friendlylinkPageItemDAO;

	@Override
	public ExecuteResult<FriendlylinkPageItemDTO> saveFriendlylinkPageItem(
			FriendlylinkPageItemDTO friendlylinkPageItemDTO) {
		
		ExecuteResult<FriendlylinkPageItemDTO> result = new ExecuteResult<FriendlylinkPageItemDTO>();
		
		try {
			friendlylinkPageItemDAO.addFriendlylinkPage(friendlylinkPageItemDTO);
			result.setResult(friendlylinkPageItemDTO);
		} catch (Exception e) {
			LOGGER.error("调用方法【saveFriendlylinkPageItemDTO】出错,错误:" + e);
			result.addErrorMessage("执行方法【saveFriendlylinkPageItemDTO】报错:"+e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<List<FriendlylinkPageItemDTO>> saveFriendlylinkPageItemBatch(
			List<FriendlylinkPageItemDTO> friendlylinkPageItemDTOs) {
		ExecuteResult<List<FriendlylinkPageItemDTO>> result = new ExecuteResult<List<FriendlylinkPageItemDTO>>();
		try {
			friendlylinkPageItemDAO.addFriendlylinkPageItemBatch(friendlylinkPageItemDTOs);
			result.setResult(friendlylinkPageItemDTOs);
		} catch (Exception e) {
			LOGGER.error("调用方法【saveFriendlylinkPageItem】出错,错误:" + e);
			result.addErrorMessage("执行方法【saveFriendlylinkPageItem】报错:"+e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public DataGrid<FriendlylinkPageItemDTO> questyFriendlylinkPageItem(
			FriendlylinkPageItemDTO friendlylinkPageItemDTO, Pager pager) {
		//DataGrid<FriendlylinkPageItemDTO> fPIDDG = new DataGrid<FriendlylinkPageItemDTO>();
		return null;
	}

	@Override
	public ExecuteResult<FriendlylinkPageItemDTO> editFriendlylinkPageItem(
			FriendlylinkPageItemDTO friendlylinkPageItemDTO) {
		ExecuteResult<FriendlylinkPageItemDTO> result = new ExecuteResult<FriendlylinkPageItemDTO>();
		try {
			friendlylinkPageItemDAO.update(friendlylinkPageItemDTO);
			result.setResult(friendlylinkPageItemDTO);
		} catch (Exception e) {
			LOGGER.error("调用方法【editFriendlylinkItem】出错,错误:" + e);
			result.addErrorMessage("执行方法【editFriendlylinkItem】报错:"+e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<String> deleteFriendlylinkPageItem(
			FriendlylinkPageItemDTO friendlylinkPageItemDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			friendlylinkPageItemDAO.deleteByDTO(friendlylinkPageItemDTO);
		} catch (Exception e) {
			LOGGER.error("调用方法【deleteFriendlylinkPageItem】出错,错误:" + e);
			result.addErrorMessage("执行方法【deleteFriendlylinkPageItem】报错:"+e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<String> deleteFriendlylinkPageItemByPageId(Long pageId) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			friendlylinkPageItemDAO.deleteByPageId(pageId);
		} catch (Exception e) {
			LOGGER.error("调用方法【deleteFriendlylinkPageItemByPageId】出错,错误:" + e);
			result.addErrorMessage("执行方法【deleteFriendlylinkPageItemByPageId】报错:"+e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<String> deleteFriendlylinkPageItemBatch(
			List<FriendlylinkPageItemDTO> friendlylinkPageItemDTOs) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			friendlylinkPageItemDAO.deleteBatch(friendlylinkPageItemDTOs);
		} catch (Exception e) {
			LOGGER.error("调用方法【deleteFriendlylinkPageItemDTOBatch】出错,错误:" + e);
			result.addErrorMessage("执行方法【deleteFriendlylinkPageItemDTOBatch】报错:"+e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}


}
