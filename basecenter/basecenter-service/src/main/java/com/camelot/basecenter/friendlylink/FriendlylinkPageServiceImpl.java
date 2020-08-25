package com.camelot.basecenter.friendlylink;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.camelot.basecenter.dao.FriendlylinkPageDAO;
import com.camelot.basecenter.dao.FriendlylinkPageItemDAO;
import com.camelot.basecenter.dto.FriendlylinkPageDTO;
import com.camelot.basecenter.dto.FriendlylinkPageItemDTO;
import com.camelot.basecenter.service.FriendlylinkPageService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * <p>Description: [友情链接页面实现类]</p>
 * Created on 2016年3月29日
 * @author  <a href="mailto: wufuhong@camelotchina.com">吴赋宏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */

@Service("friendlylinkPageService")
public class FriendlylinkPageServiceImpl implements FriendlylinkPageService{
	
	private static final Logger LOGGER=LoggerFactory
			.getLogger(FriendlylinkPageServiceImpl.class);
	
	@Resource
	private FriendlylinkPageDAO friendlylinkPageDAO;
	
	@Resource
	private FriendlylinkPageItemDAO friendlylinkPageItemDAO;

	@Override
	public ExecuteResult<FriendlylinkPageDTO> saveFriendlylinkPage(
			FriendlylinkPageDTO friendlylinkPageDTO) {
		ExecuteResult<FriendlylinkPageDTO> result = new ExecuteResult<FriendlylinkPageDTO>();
		
		try {
			friendlylinkPageDAO.addFriendlylinkPage(friendlylinkPageDTO);
			result.setResult(friendlylinkPageDTO);
		} catch (Exception e) {
			LOGGER.error("调用方法【saveFriendlylinkPageDTO】出错,错误:" + e);
			result.addErrorMessage("执行方法【saveFriendlylinkPageDTO】报错:"+e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public DataGrid<FriendlylinkPageDTO> questyFriendlylinkPageDetails(
			FriendlylinkPageDTO FriendlylinkPageDTO, Pager pager) {
		DataGrid<FriendlylinkPageDTO> fPDDG = new DataGrid<FriendlylinkPageDTO>();
		try {
			List<FriendlylinkPageDTO> fIDList = this.friendlylinkPageDAO.getAllDetails(FriendlylinkPageDTO, pager);
			fPDDG.setRows(null);
			if(fIDList != null && fIDList.size() > 0){
				fPDDG.setRows(fIDList);
			}
			//查询总条数
			Long count = this.friendlylinkPageDAO.queryCount(FriendlylinkPageDTO);
			fPDDG.setTotal(count);
		} catch (Exception e) {
			LOGGER.error("调用方法【questyFriendlylinkPageDTO】出错,错误:" + e);
			throw new RuntimeException(e);
		}
		return fPDDG;
	}

	@Override
	public ExecuteResult<FriendlylinkPageDTO> editFriendlylinkPage(
			FriendlylinkPageDTO friendlylinkPageDTO) {
		ExecuteResult<FriendlylinkPageDTO> result = new ExecuteResult<FriendlylinkPageDTO>();
		try {
			friendlylinkPageDAO.updateByPrimaryKeySelective(friendlylinkPageDTO);
			result.setResult(friendlylinkPageDTO);
		} catch (Exception e) {
			LOGGER.error("调用方法【editFriendlylinkPageDTO】出错,错误:" + e);
			result.addErrorMessage("执行方法【editFriendlylinkPageDTO】报错:"+e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	
	@Transactional
	@Override
	public ExecuteResult<String> deleteFriendlylinkPage(
			FriendlylinkPageDTO friendlylinkPageDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			friendlylinkPageDAO.delete(friendlylinkPageDTO);
			// 删除中间表数据
			friendlylinkPageItemDAO.deleteByPageId(friendlylinkPageDTO.getId());
		} catch (Exception e) {
			LOGGER.error("调用方法【deleteFriendlylinkPageDTO】出错,错误:" + e);
			result.addErrorMessage("执行方法【deleteFriendlylinkPageDTO】报错:"+e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<String> addFriendlyLinks(Long pageId,
			List<Long> itemIds) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		
		try {
			List<FriendlylinkPageItemDTO> friendlylinkPageItemList = new ArrayList<FriendlylinkPageItemDTO>();
			for (Long itemId : itemIds) {
				FriendlylinkPageItemDTO friendlylinkPageItemDTO = new FriendlylinkPageItemDTO();
				
				friendlylinkPageItemDTO.setItemId(itemId);
				friendlylinkPageItemDTO.setPageId(pageId);
				friendlylinkPageItemList.add(friendlylinkPageItemDTO);
			}
			friendlylinkPageItemDAO.addFriendlylinkPageItemBatch(friendlylinkPageItemList);
		} catch (Exception e) {
			LOGGER.error("调用方法【addFriendlyLinks】出错,错误:" + e);
			result.addErrorMessage("执行方法【addFriendlyLinks】报错:"+e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return result;
	}

	@Override
	public ExecuteResult<FriendlylinkPageDTO> findFriendlylinkPageById(Long id) {
		ExecuteResult<FriendlylinkPageDTO> result = new ExecuteResult<FriendlylinkPageDTO>();
		try {
			FriendlylinkPageDTO friendlylinkItemDTO = friendlylinkPageDAO.selectByPrimaryKey(id);
			result.setResult(friendlylinkItemDTO);
		} catch (Exception e) {
			LOGGER.error("调用方法【findFriendlylinkPageById】出错,错误:" + e);
			result.addErrorMessage("执行方法【findFriendlylinkPageById】报错:"+e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<String> deleteFriendlylinkItemById(Long id) {
		 {
			ExecuteResult<String> result = new ExecuteResult<String>();
			try {
				friendlylinkPageDAO.deleteByPrimaryKey(id);
				
			} catch (Exception e) {
				LOGGER.error("调用方法【deleteFriendlylinkItemById】出错,错误:" + e);
				result.addErrorMessage("执行方法【deleteFriendlylinkItemById】报错:"+e.getMessage());
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			return result;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public DataGrid<FriendlylinkPageDTO> getFriendlylinkPages(
			FriendlylinkPageDTO friendlylinkPageDTO, Pager pager) {
		DataGrid<FriendlylinkPageDTO> fPDDG = new DataGrid<FriendlylinkPageDTO>();
		try {
			List<FriendlylinkPageDTO> fIDList = this.friendlylinkPageDAO.getFriendlylinkPages(friendlylinkPageDTO, pager);;
			fPDDG.setRows(null);
			if(fIDList != null && fIDList.size() > 0){
				fPDDG.setRows(fIDList);
			}
			//查询总条数
			Long count = this.friendlylinkPageDAO.queryCount(friendlylinkPageDTO);
			fPDDG.setTotal(count);
		} catch (Exception e) {
			LOGGER.error("调用方法【getFriendlylinkPages】出错,错误:" + e);
			throw new RuntimeException(e);
		}
		return fPDDG;
	}
}
