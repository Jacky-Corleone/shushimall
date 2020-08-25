package com.camelot.maketcenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.maketcenter.dao.PromotionMarkdownDAO;
import com.camelot.maketcenter.dto.PromotionMarkdown;
import com.camelot.maketcenter.service.PromotionMdExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
@Service("promotionMdExportService")
public class PromotionMarkdownServiceImpl implements PromotionMdExportService{
	private final static Logger logger = LoggerFactory.getLogger(PromotionMarkdownServiceImpl.class);
	@Resource
	private PromotionMarkdownDAO promotionMarkdownDAO;
	
	@Override
	public ExecuteResult<DataGrid<PromotionMarkdown>> queryPromotionMdList(PromotionMarkdown promotionMarkdown,
			Pager page) {
		ExecuteResult<DataGrid<PromotionMarkdown>> result=new ExecuteResult<DataGrid<PromotionMarkdown>>();
		try {
			DataGrid<PromotionMarkdown> dataGrid=new DataGrid<PromotionMarkdown>();
			List<PromotionMarkdown> list = promotionMarkdownDAO.queryList(promotionMarkdown, page);
			Long count = promotionMarkdownDAO.queryCount(promotionMarkdown);
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * 删除限时折扣
	 * @param promotionInfoId
	 * @return
	 */
	public ExecuteResult<String> deleteMdByPromotionId(Long promotionInfoId){
		ExecuteResult<String> result=new ExecuteResult<String>();
		try {
			Integer num=promotionMarkdownDAO.deleteByPromotionId(promotionInfoId);
			if(num>0){
				result.setResultMessage("success");
			}else{
				result.setResultMessage("error");
			}
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * 删除限时折扣
	 * @param promotionMarkdown
	 * @return
	 */
	public ExecuteResult<String> addMdByPromotionId(PromotionMarkdown promotionMarkdown){
		ExecuteResult<String> result=new ExecuteResult<String>();
		try {
			promotionMarkdownDAO.add(promotionMarkdown);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * <p>Discription:[查看当前店铺的下架操作的商品是否所有商品存在促销活动]</p>
	 * @param shopId 店铺ID
	 * @param itemIds 商品ids
	 * @return
	 * @autor: WHW
	 */
	public boolean queryActivityCheck(Long shopId,Long[] itemIds){
		if(promotionMarkdownDAO.queryActivityCheck(shopId,itemIds)>0){
			return true;
		}
		return false;
	}

}
