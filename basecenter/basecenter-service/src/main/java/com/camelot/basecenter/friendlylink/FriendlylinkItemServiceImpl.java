package com.camelot.basecenter.friendlylink;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.basecenter.dao.FriendlylinkItemDAO;
import com.camelot.basecenter.dto.FriendlylinkItemDTO;
import com.camelot.basecenter.service.FriendlylinkItemService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * <p>Description: [友情链接实现类]</p>
 * Created on 2016年3月29日
 * @author  <a href="mailto: wufuhong@camelotchina.com">吴赋宏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */

@Service("friendlylinkItemService")
public class FriendlylinkItemServiceImpl implements FriendlylinkItemService{
	
	private static final Logger LOGGER=LoggerFactory
			.getLogger(FriendlylinkItemServiceImpl.class);
	
	@Resource
	private FriendlylinkItemDAO friendlylinkItemDAO;

	@Override
	public ExecuteResult<FriendlylinkItemDTO> saveFriendlylinkItem(
			FriendlylinkItemDTO friendlylinkItemDTO) {
		ExecuteResult<FriendlylinkItemDTO> result = new ExecuteResult<FriendlylinkItemDTO>();
		
		try {
			friendlylinkItemDAO.addFriendlylinkItem(friendlylinkItemDTO);
			result.setResult(friendlylinkItemDTO);
		} catch (Exception e) {
			LOGGER.error("调用方法【saveFriendlylinkItemDTO】出错,错误:" + e);
			result.addErrorMessage("执行方法【saveFriendlylinkItemDTO】报错:"+e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public DataGrid<FriendlylinkItemDTO> questyFriendlylinkItem(
			FriendlylinkItemDTO friendlylinkItemDTO, Pager pager) {
		DataGrid<FriendlylinkItemDTO> fIDDG = new DataGrid<FriendlylinkItemDTO>();
		try {
			List<FriendlylinkItemDTO> fIDList = this.friendlylinkItemDAO.questyBaseFriendlylinkItemList(friendlylinkItemDTO, pager);
			fIDDG.setRows(null);
			if(fIDList != null && fIDList.size() > 0){
				fIDDG.setRows(fIDList);
			}
			//查询总条数
			Long count = this.friendlylinkItemDAO.queryCount(friendlylinkItemDTO);
			fIDDG.setTotal(count);
		} catch (Exception e) {
			LOGGER.error("调用方法【questyFriendlylinkItemDTO】出错,错误:" + e);
			e.printStackTrace();
			//LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return fIDDG;
	}

	@Override
	public ExecuteResult<FriendlylinkItemDTO> editFriendlylinkItem(
			FriendlylinkItemDTO friendlylinkItemDTO) {
		ExecuteResult<FriendlylinkItemDTO> result = new ExecuteResult<FriendlylinkItemDTO>();
		try {
			
			friendlylinkItemDAO.updateByPrimaryKeySelective(friendlylinkItemDTO);
			result.setResult(friendlylinkItemDTO);
		} catch (Exception e) {
			LOGGER.error("调用方法【editFriendlylinkItemDTO】出错,错误:" + e);
			result.addErrorMessage("执行方法【editFriendlylinkItemDTO】报错:"+e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<String> deleteFriendlylinkItem(
			FriendlylinkItemDTO friendlylinkItemDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			friendlylinkItemDAO.delete(friendlylinkItemDTO);
		} catch (Exception e) {
			LOGGER.error("调用方法【deleteFriendlylinkItemDTO】出错,错误:" + e);
			result.addErrorMessage("执行方法【deleteFriendlylinkItemDTO】报错:"+e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public DataGrid<FriendlylinkItemDTO> getUnSelectedItems(Long pageId,
			Pager pager) {
		DataGrid<FriendlylinkItemDTO> fIDDG = new DataGrid<FriendlylinkItemDTO>();
		try {
			List<FriendlylinkItemDTO> fIDList = this.friendlylinkItemDAO.getUnSelectedItems(pageId, pager);
			fIDDG.setRows(null);
			if(fIDList != null && fIDList.size() > 0){
				fIDDG.setRows(fIDList);
			}
			//查询总条数
			//FriendlylinkItemDTO friendlylinkItemDTO = new FriendlylinkItemDTO();
			//Long count = this.friendlylinkItemDAO.queryCount(null);
			fIDDG.setTotal(fIDList == null?0L:fIDList.size());
		} catch (Exception e) {
			LOGGER.error("调用方法【getUnSelectedItems】出错,错误:" + e);
			e.printStackTrace();
			//LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return fIDDG;
	}

	@Override
	public ExecuteResult<FriendlylinkItemDTO> findFriendlylinkItemById(Long id) {
		ExecuteResult<FriendlylinkItemDTO> result = new ExecuteResult<FriendlylinkItemDTO>();
		try {
			FriendlylinkItemDTO friendlylinkItemDTO = friendlylinkItemDAO.selectByPrimaryKey(id);
			result.setResult(friendlylinkItemDTO);
		} catch (Exception e) {
			LOGGER.error("调用方法【findFriendlylinkItemById】出错,错误:" + e);
			result.addErrorMessage("执行方法【findFriendlylinkItemById】报错:"+e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<String> deleteFriendlylinkItemById(Long id) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			friendlylinkItemDAO.deleteByPrimaryKey(id);
		} catch (Exception e) {
			LOGGER.error("调用方法【deleteFriendlylinkItemById】出错,错误:" + e);
			result.addErrorMessage("执行方法【deleteFriendlylinkItemById】报错:"+e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public DataGrid<FriendlylinkItemDTO> getSelectedItems(Long pageId,
			Pager pager) {
		DataGrid<FriendlylinkItemDTO> fIDDG = new DataGrid<FriendlylinkItemDTO>();
		try {
			List<FriendlylinkItemDTO> fIDList = this.friendlylinkItemDAO.getSelectedItems(pageId, pager);
			fIDDG.setRows(null);
			if(fIDList != null && fIDList.size() > 0){
				fIDDG.setRows(fIDList);
			}
			//查询总条数
			//FriendlylinkItemDTO friendlylinkItemDTO = new FriendlylinkItemDTO();
			//Long count = this.friendlylinkItemDAO.queryCount(null);
			fIDDG.setTotal(fIDList == null?0L:fIDList.size());
		} catch (Exception e) {
			LOGGER.error("调用方法【getSelectedItems】出错,错误:" + e);
			e.printStackTrace();
			//LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return fIDDG;
	}

}
